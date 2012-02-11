package com.cisco.ettx.admin.gui.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUtils;

import org.apache.struts.action.*;
import org.apache.struts.action.ActionForm;
import com.cisco.ettx.admin.gui.web.beans.LeaseHistoryListFormBean;

import java.util.*;
import java.io.*;
import java.util.*;
import java.text.*;
import org.apache.log4j.Logger;
import com.cisco.ettx.admin.gui.web.datatypes.*;
import com.cisco.ettx.admin.common.util.*;
import com.cisco.ettx.admin.gui.web.util.ETTXUtil;
import com.cisco.nm.xms.rfw.RFParameter;
import com.cisco.nm.xms.rfw.RFRequestHandler;
import com.cisco.nm.uii.reports.SessionFiles;
import com.cisco.nm.uii.taglib.framework.Constants;

import java.text.ParseException;
import java.text.ParsePosition;

public class ReportLeaseHistoryListAction
	extends Action {
	public static final String DEFAULT_FORWARD = "leaseHistoryRecords";
	public static final String DETAILS_SUBSCRIBER_ACTION = "Subscriber";
	public static final String DETAILS_SUBSCRIBER_RESULT_FORWARD = "viewSubscriberRecord";
	public static final String ERROR_FORWARD = "closeWindowAndNotification";

	private static final String DefaultTemporaryDirectory = "/tmp";
	private static final String ApplicationTempDirectoryName = "cisco";
	private static final String SubscriberReportFileName = "2000.xml";
	private static final String FormatFileName = "format.xml";
	private static final String RecordListFileName = "RecordList.dat";
	private static final String TempDirectoryPropertyName = "java.io.tmpdir";
	private static final Logger logger = Logger.getLogger(ReportLeaseHistoryListAction.class);

	private static String StartDateColumnHeader = null;
	private static String EndDateColumnHeader = null;
	private static String StatusColumnHeader = null;
	private static String IPAddressColumnHeader = null;
	private static String MACAddressColumnHeader = null;
	private static String DevicePortColumnHeader = null;
	private static String SubscriberNameColumnHeader = null;
	private static String LoginIDColumnHeader = null;

	static {
		StartDateColumnHeader = ETTXUtil.localizeMessage("queryLeaseStartDateColumnHeader");
		EndDateColumnHeader = ETTXUtil.localizeMessage("queryLeaseEndDateColumnHeader");
		StatusColumnHeader = ETTXUtil.localizeMessage("queryLeaseStatusColumnHeader");
		IPAddressColumnHeader = ETTXUtil.localizeMessage("queryLeaseIPAddressColumnHeader");
		MACAddressColumnHeader = ETTXUtil.localizeMessage("queryLeaseMACAddressColumnHeader");
		DevicePortColumnHeader = ETTXUtil.localizeMessage("queryLeaseDevicePortColumnHeader");
		SubscriberNameColumnHeader = ETTXUtil.localizeMessage("queryLeaseSubscriberNameColumnHeader");
		LoginIDColumnHeader = ETTXUtil.localizeMessage("queryLeaseLoginIDColumnHeader");

	}

	protected String getDefaultForward() {
		return DEFAULT_FORWARD;
	}

	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String systemTempDir;
		String reportId;
		String tempFileDirectory;
		String leaseHistoryReportFileName;
		String reportFormatFileName;
		String recordListFileName;
		File newDirectory;
		File leaseHistoryListFile;
		File formatFile;
		File recordListFile;
		HttpSession httpSession;
		SessionFiles sessionFiles;
		Collection leaseHistoryCollection;
		Iterator leaseHistoryIterator;

		// This is a dummy variable.  It can go away after debug.
		String forwardPage = DEFAULT_FORWARD;

		LeaseHistoryListFormBean sysBean = (LeaseHistoryListFormBean) form;

		logger.debug("ReportLeaseHistoryListAction query string: " + request.getQueryString());

		String action = request.getParameter("action");
		logger.debug("ReportLeaseHistoryListAction.perform " + action);

		/*
		  request.setAttribute("subscriberList", sysBean.getSubscriberList().iterator());
		  request.setAttribute("com.cisco.nm.uii.taglib.framework.Constants.HIDE_TOC", "true");
		*/

		// This default page for this action is this report's "Home page" as
		// the triveni folk refer to it.
		// This page is defined by the application and contains the
		// inputs that the application needs to generate the report and a
		// uii:button that uses the 3 popup attributes to show the report.

		// This first "if block" gets run when a new report is being generated.
		String newReport = request.getParameter("submit");

		logger.debug("ReportLeaseHistoryListAction.newReport " + newReport);

		if (newReport != null && newReport.equals("Submit")) {

			logger.debug("ReportLeaseHistoryListAction: Creating our new report.");

			systemTempDir = System.getProperty ( TempDirectoryPropertyName );
			if (systemTempDir == null) {
				systemTempDir = DefaultTemporaryDirectory;
			}
			reportId = new Long(System.currentTimeMillis()).toString();
			tempFileDirectory = systemTempDir + File.separator + ApplicationTempDirectoryName + reportId;
			leaseHistoryReportFileName = tempFileDirectory + File.separator + SubscriberReportFileName;
			reportFormatFileName = tempFileDirectory + File.separator + FormatFileName;
			recordListFileName = tempFileDirectory + File.separator + RecordListFileName;
			newDirectory = new File(tempFileDirectory);
			leaseHistoryListFile = new File(leaseHistoryReportFileName);
			leaseHistoryListFile.deleteOnExit();
			formatFile = new File(reportFormatFileName);
			recordListFile = new File(recordListFileName);

			// We need to write out our subscriber list to a temporary file.
			httpSession = request.getSession();
			sessionFiles = new SessionFiles();
			sessionFiles.addFile(newDirectory);
			sessionFiles.addFile(leaseHistoryListFile);
			sessionFiles.addFile(formatFile);
			httpSession.setAttribute(reportId + "_rfFiles", sessionFiles);
			httpSession.setAttribute(reportId + "_recordListFile", recordListFile);
			httpSession.setAttribute(reportId + "_leaseHistoryXmlFile", leaseHistoryListFile);

			try {
				newDirectory.mkdir();
			}
			catch (SecurityException ex) {
				// Do some sort of error recovery here.
				String errorMsg = ETTXUtil.localizeMessage("queryLeaseReportSecurityException") + ex.getMessage();
				ActionErrors aes = new ActionErrors();
				logger.error(errorMsg);
				ActionError error = new ActionError(errorMsg);
				aes.add(Constants.ERROR, error);
				saveErrors(request, aes);
				return mapping.findForward(ERROR_FORWARD);
			}

			/*leaseHistoryCollection = sysBean.getLeaseHistoryList();*/
			//REVISIT
			leaseHistoryCollection = (Collection) request.getAttribute(LeaseHistoryQueryAction.LEASE_RECORDS);

			// We'll write out the subscriber information first;
			try {
				writeLeaseHistoryReport(leaseHistoryCollection, leaseHistoryListFile, request);

				ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(recordListFile));
				os.writeObject(leaseHistoryCollection);
				os.close();

				writeFormatFile(formatFile);

				// Now we need pass our report parameters back to the application.
				RFParameter rfbh = new RFParameter();

				rfbh.setNumOfRecords(Short.toString( (new Integer(leaseHistoryCollection.size())).shortValue()));

				// this action class name
				// This is the alias for this action.
				rfbh.setCallBack("leaseHistoryRecords.do");

				// directory path to where the XSLT processor can pick up report files
				rfbh.setPath(tempFileDirectory);

				// default sorted column name
				rfbh.setSortValue("startDate");

				// WinName is not the name of the JavaScript created window.
				// WinName should be used to bind each HttpRequest with this report's
				// files and query data. Since this example keeps that in the
				// user's session using the reportID as part of the key, I'll use
				// the reportID here.
				rfbh.setWinName(reportId);

				// if needed set up help link, otherwise help icon will not be drawn
				rfbh.setHelpLink("http://www.cisco.com");

				// put the RFParameter data into the request so the RF JSP pages
				// can get at it
				request.setAttribute("AppData", rfbh);
			}
			catch (IOException ex) {
				// Do some sort of error recovery here.
				String errorMsg = ETTXUtil.localizeMessage("queryLeaseReportIOException") + ex.getMessage();
				ActionErrors aes = new ActionErrors();
				logger.error(errorMsg);
				ActionError error = new ActionError(errorMsg);
				aes.add(Constants.ERROR, error);
				saveErrors(request, aes);
				return mapping.findForward(ERROR_FORWARD);
			}
		}
		else {
			// See if this request came from the report framework.
			// Seems to me like I should using a method in RFRequestHandler
			// that lets me know whether or not this request is a "RF" request
			// or not rather than using request.getParam.
			String isReport = request.getParameter("Report");
			logger.debug("ReportLeaseHistoryListAction.isReport " + isReport);

			if (isReport != null && isReport.equals("true")) {

				String reportID = request.getParameter("WinName");
				logger.debug("ReportLeaseHistoryListAction.reportID " + reportID);

				// call the helper to see what page it thinks we should goto
				// or what action we need to take
				RFRequestHandler rfHelper = new RFRequestHandler();
				String rfScreenID = rfHelper.getScreenId(request);
				logger.debug("ReportLeaseHistoryListAction.rfScreenID " + rfScreenID);

				String sortValue = request.getParameter("SortValue");
				String sortOrder = request.getParameter("Order");
				String sortColumn = request.getParameter("SortColumn");
				logger.debug("ReportLeaseHistoryListAction.sortValue " + sortValue);
				logger.debug("ReportLeaseHistoryListAction.sortOrder " + sortOrder);
				logger.debug("ReportLeaseHistoryListAction.sortColumn " + sortColumn);

				HttpSession userSession = request.getSession();
				recordListFile = (File) userSession.getAttribute(reportID + "_recordListFile");
				leaseHistoryListFile = (File) userSession.getAttribute(reportID + "_leaseHistoryXmlFile");

				Collection leaseHistRecCollection = null;
				try {
					ObjectInputStream ois = new ObjectInputStream(new FileInputStream(recordListFile));
					leaseHistRecCollection = (Collection) ois.readObject();
					ois.close();
				}
				catch (IOException ioex) {
					logger.error(ioex);
				}
				catch (ClassNotFoundException classEx) {
					logger.error(classEx);
				}

				List histRec = new ArrayList(leaseHistRecCollection);
				if (sortValue.equals("startDate")) {
					if (sortOrder.equals("ascending")) {
						logger.debug("Sorting startDate ascending");
						Collections.sort(histRec, START_DATE_ASC);
					}
					else {
						logger.debug("Sorting startDate decending");
						Collections.sort(histRec, START_DATE_DEC);
					}
				}
				if (sortValue.equals("endDate")) {
					if (sortOrder.equals("ascending")) {
						logger.debug("Sorting endDate ascending");
						Collections.sort(histRec, END_DATE_ASC);
					}
					else {
						logger.debug("Sorting endDate decending");
						Collections.sort(histRec, END_DATE_DEC);
					}
				}

				try {
					leaseHistoryListFile.delete();
					writeLeaseHistoryReport(histRec, leaseHistoryListFile, request);
				}
				catch (IOException ex1) {
					logger.error(ex1);
				}

				// Debug Sort
				// DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);
				// Iterator iter = histRec.iterator();
				// while (iter.hasNext()) {
				// 	LeaseHistoryRecord record = (LeaseHistoryRecord) iter.next();
				// 	System.out.println("StartDate: " + dateFormat.format(record.getStartDateAsDate()) +
				// 					    " EndDate: " + dateFormat.format(record.getEndDateAsDate()));
				// }

				if (rfScreenID != null) {
					// This functionality should only be called if we made more
					// than one report file, which we should never do...
					forwardPage = rfScreenID;

					// If the helper returns "Application" we know that the RF code
					// assumes the application will want to do something before going onto
					// the next RF JSP page.
					// This happens only if the NumOfRecords was greater than 2000.
					// So if your report generated a report with less than 2000 records
					// none of the code in the following if statement will be used.
					if (forwardPage.equals("exportAll")) {
						// We get here only when the application has specified that
						// there are more than 2000 records in the 'numOfRecords'
						// setting. If less than 2000 then the RFRequestHandler will
						// return 'exportReport' as the forwardPage.
						//
						// Since this action class already wrote out all of its XML files
						// lets forward to export XSLT JSP page.
						forwardPage = "exportReport";
					}
					else if (forwardPage.equals("sortAll")) {
						// This 'Sorting' action will take place whenever there are
						// more than 2000 records in the numOfRecords setting.
						// If there are more than 2000 then the XSLT processor takes
						// up too much memory and can not be used to sort the records
						// so control is passed back to the application so the app
						// can rerun the original query and sorting on the new field
						// ( using a database's sort on column feature )
						HttpSession sess = request.getSession();
						String query = (String) sess.getAttribute(reportID + "_rfQuery");
						// Seems to me that getSortValue and getOrder should be accessor
						// methods on RFRequestHelper. Then it would be even more helpful :)
						//String sortValue = request.getParameter( "SortValue" );
						//String sortOrder = request.getParameter( "Order" );
						logger.debug("ReportLeaseHistoryListAction.sortValue " + sortValue);
						logger.debug("ReportLeaseHistoryListAction.sortOrder " + sortOrder);
						// I don't have time to properly code an application that sorts
						// so you'll have to imagine that the report was resorted.
						forwardPage = "launchReport";
					}
				}

				// seems like the RFRequestHandler should tell us we should "Exit"
				String reportAction = request.getParameter("Action");
				if (reportAction != null && reportAction.equals("Exit")) {

					logger.debug("RF application must handle Exit");
					HttpSession sess = request.getSession();
					SessionFiles sf = (SessionFiles) sess.getAttribute(reportID + "_rfFiles");
					if (sf != null) {
						sf.deleteFiles();
					}
					recordListFile = (File) sess.getAttribute(reportID + "_recordListFile");
					leaseHistoryListFile = (File) sess.getAttribute(reportID + "_leaseHistoryXmlFile");
					recordListFile.delete();
					leaseHistoryListFile.delete();
					// return null so controller returns nothing to browser
					return null;
				}
			}
		}

		return mapping.findForward(forwardPage);
	}

	private void writeLeaseHistoryReport(Collection leaseHistory, File reportFile, HttpServletRequest request) throws
		IOException {
		boolean subscriberLinkEnabled;
		DateFormat dateFormat;

		Iterator iter2;
		Collection list;
		LeaseHistoryRecord record;
		FileWriter fw = new FileWriter(reportFile);

		logger.debug("writeLeaseHistoryReport - enter");

		dateFormat = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);

		fw.write("<?xml version=\"1.0\"?>\n<Report>\n<TableData>\n");

		Iterator iter = leaseHistory.iterator();
		while (iter.hasNext()) {
			record = (LeaseHistoryRecord) iter.next();

			fw.write("<GenericEvent>");
			fw.write("<startDate>" + dateFormat.format(record.getStartDate().getTime()) + "</startDate>");
			fw.write("<endDate>" + dateFormat.format(record.getEndDate().getTime()) + "</endDate>");

			fw.write("<leaseStatus>" + record.getLeaseStatus() + "</leaseStatus>");
			fw.write("<ipAddress>" + record.getIpAddress() + "</ipAddress>");
			fw.write("<macAddress>" + record.getMacAddress() + "</macAddress>");
			/*
				 fw.write( "<devicePort>" + record.getDevicePort() + "</devicePort>" );
			 */fw.write("<loginID>" + record.getLoginID() + "</loginID>");
			fw.write("<subscriberName>" + record.getSubscriberName() + "</subscriberName>");

			fw.write("</GenericEvent>\n");
		}

		fw.write("</TableData>\n</Report>\n");
		fw.close();

		logger.debug("writeLeaseHistoryReport - exit");
	}

	private void writeFormatFile(File formatFile) throws IOException {
		FileWriter fw = new FileWriter(formatFile);

		logger.debug("writeFormatFile - enter");

		fw.write("<?xml version=\"1.0\"?>\n");
		fw.write("<Format>\n");
		fw.write("<TableOuter>\n");
		fw.write("  <ApplicationName>BAC-ETTx Administration Tool</ApplicationName>\n");
		fw.write("  <ReportName>View Lease History Report</ReportName>\n");
		fw.write("  <Filter>false</Filter>\n");
		fw.write("  <NumberOfColumns>10</NumberOfColumns>\n");
		fw.write("  <HeaderFormat SplitColumn=\"false\"/>\n");
		fw.write("  <IgnoreRoot>2LevelDeep</IgnoreRoot>\n");
		fw.write("</TableOuter>\n");
		fw.write("<TableInner>\n");
		fw.write("  <Column displayName=\"" + StartDateColumnHeader + "\" xmlTag=\"startDate\" dataType=\"appSort\" sortable=\"true\">\n");
		fw.write("  </Column>\n");
		fw.write("  <Column displayName=\"" + EndDateColumnHeader + "\" xmlTag=\"endDate\" dataType=\"appSort\" sortable=\"true\">\n");
		fw.write("  </Column>\n");
		fw.write("  <Column displayName=\"" + StatusColumnHeader + "\" xmlTag=\"leaseStatus\" dataType=\"text\" sortable=\"false\">\n");
		fw.write("  </Column>\n");
		fw.write("  <Column displayName=\"" + IPAddressColumnHeader + "\" xmlTag=\"ipAddress\" dataType=\"text\" sortable=\"true\">\n");
		fw.write("  </Column>\n");
		fw.write("  <Column displayName=\"" + MACAddressColumnHeader + "\" xmlTag=\"macAddress\" dataType=\"text\" sortable=\"true\">\n");
		fw.write("  </Column>\n");
		/*
		 fw.write( "  <Column displayName=\"" + DevicePortColumnHeader + "\" xmlTag=\"devicePort\" dataType=\"text\" sortable=\"true\">\n" );
		 fw.write( "  </Column>\n" );
		 */fw.write("  <Column displayName=\"" + LoginIDColumnHeader + "\" xmlTag=\"loginID\" dataType=\"text\" sortable=\"true\" >\n");
		fw.write("  </Column>\n");
		fw.write("  <Column displayName=\"" + SubscriberNameColumnHeader + "\" xmlTag=\"subscriberName\" dataType=\"text\" sortable=\"true\" >\n");
		fw.write("  </Column>\n");
		fw.write("</TableInner>\n");
		fw.write("</Format>\n");
		fw.close();

		logger.debug("writeFormatFile - exit");
	}

	static final Comparator START_DATE_ASC = new Comparator() {
		public int compare(Object o1, Object o2) {
			LeaseHistoryRecord r1 = (LeaseHistoryRecord) o1;
			LeaseHistoryRecord r2 = (LeaseHistoryRecord) o2;
			return r2.getStartDateAsDate().compareTo(r1.getStartDateAsDate());

		}
	};

	static final Comparator START_DATE_DEC = new Comparator() {
		public int compare(Object o1, Object o2) {
			LeaseHistoryRecord r1 = (LeaseHistoryRecord) o1;
			LeaseHistoryRecord r2 = (LeaseHistoryRecord) o2;
			return r1.getStartDateAsDate().compareTo(r2.getStartDateAsDate());

		}
	};

	static final Comparator END_DATE_ASC = new Comparator() {
		public int compare(Object o1, Object o2) {
			LeaseHistoryRecord r1 = (LeaseHistoryRecord) o1;
			LeaseHistoryRecord r2 = (LeaseHistoryRecord) o2;
			return r2.getEndDateAsDate().compareTo(r1.getEndDateAsDate());

		}
	};

	static final Comparator END_DATE_DEC = new Comparator() {
		public int compare(Object o1, Object o2) {
			LeaseHistoryRecord r1 = (LeaseHistoryRecord) o1;
			LeaseHistoryRecord r2 = (LeaseHistoryRecord) o2;
			return r1.getEndDateAsDate().compareTo(r2.getEndDateAsDate());

		}
	};

}