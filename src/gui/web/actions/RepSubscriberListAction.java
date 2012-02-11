package com.cisco.ettx.admin.gui.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUtils;

import org.apache.struts.action.*;
import org.apache.struts.action.ActionForm;
import com.cisco.ettx.admin.gui.web.beans.SubscriberListFormBean;
import com.cisco.ettx.admin.gui.web.datatypes.SubscriberRecord;
import com.cisco.ettx.admin.gui.web.helper.CollEngineHelper;

import java.util.*;
import java.io.*;
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

public class RepSubscriberListAction extends Action 
{
    public static final String DEFAULT_FORWARD = "subsRecords";
    public static final String SUBS_RECORD = "detailsSub";
    public static final String ERROR_FORWARD = "closeWindowAndNotification";

    private static final String DefaultTemporaryDirectory = "/tmp";
    private static final String ApplicationTempDirectoryName = "cisco";
    private static final String SubscriberReportFileName = "2000.xml";
    private static final String FormatFileName = "format.xml";
    private static final String TempDirectoryPropertyName = "java.io.tmpdir";
    private static final Logger logger = Logger.getLogger(RepSubscriberListAction.class);

    private static String SubscriberNameColumnHeader = null;
    private static String LoginIDColumnHeader = null;
    private static String PostalAddressColumnHeader = null;
    private static String PostalCityColumnHeader = null;
    private static String PostalStateColumnHeader = null;
    private static String HomeNumberColumnHeader = null;
    private static String MobileNumberColumnHeader = null;
    private static String AccountNumberColumnHeader = null;
    private static String AccountStatusColumnHeader = null;
    private static String SubscriberServicesColumnHeader = null;

    static {
	SubscriberNameColumnHeader = ETTXUtil.localizeMessage("subscriberRecordSubscriberName");
	LoginIDColumnHeader = ETTXUtil.localizeMessage("subscriberRecordLoginID");
	PostalAddressColumnHeader = ETTXUtil.localizeMessage("subscriberRecordPostalAddress");
	PostalCityColumnHeader = ETTXUtil.localizeMessage("subscriberRecordPostalCity");
	PostalStateColumnHeader = ETTXUtil.localizeMessage("subscriberRecordPostalState");
	HomeNumberColumnHeader = ETTXUtil.localizeMessage("subscriberRecordHomeNumber");
	MobileNumberColumnHeader = ETTXUtil.localizeMessage("subscriberRecordMobileNumber");
	AccountNumberColumnHeader = ETTXUtil.localizeMessage("subscriberRecordAccountNumber");
	AccountStatusColumnHeader = ETTXUtil.localizeMessage("subscriberRecordAccountStatus");
	SubscriberServicesColumnHeader = ETTXUtil.localizeMessage("subscriberRecordSubscribedServices");
    }

    protected String getDefaultForward() {
	return DEFAULT_FORWARD;
    }

    public ActionForward perform(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest  request,
				 HttpServletResponse response)
    {       
	String systemTempDir;
	String reportId;
	String tempFileDirectory;
	String subscriberReportFileName;
	String reportFormatFileName;
	File newDirectory;
	File subscriberListFile;
	File formatFile;
	HttpSession httpSession;
	SessionFiles sessionFiles;
	Collection subscriberCollection;
	Iterator subscriberIterator;

	// This is a dummy variable.  It can go away after debug.
	String forwardPage = DEFAULT_FORWARD;

	HttpSession the_session = request.getSession();
	SubscriberListFormBean sysBean = (SubscriberListFormBean)form;


	// This default page for this action is this report's "Home page" as
	// the triveni folk refer to it.
	// This page is defined by the application and contains the
	// inputs that the application needs to generate the report and a
	// uii:button that uses the 3 popup attributes to show the report.

	// This first "if block" gets run when a new report is being generated.
	String newReport = request.getParameter( "submit" );

	System.out.println ("RepSubscriberListAction.newReport " + newReport);

	if ( newReport != null && newReport.equals( "Submit" ) ) {

	    System.out.println ("RepSubscriberListAction: Creating our new report.");

	    systemTempDir = System.getProperty ( TempDirectoryPropertyName );
	    if (systemTempDir == null) {
		systemTempDir = DefaultTemporaryDirectory;
	    }
	    reportId = new Long ( System.currentTimeMillis() ).toString();
	    tempFileDirectory = systemTempDir + File.separator + ApplicationTempDirectoryName + reportId;
	    subscriberReportFileName = tempFileDirectory + File.separator + SubscriberReportFileName;
	    reportFormatFileName = tempFileDirectory + File.separator + FormatFileName;
	    newDirectory = new File (tempFileDirectory);
	    subscriberListFile = new File (subscriberReportFileName);
	    subscriberListFile.deleteOnExit();
	    formatFile = new File (reportFormatFileName);
		
	    // We need to write out our subscriber list to a temporary file.
	    httpSession = request.getSession();
	    sessionFiles = new SessionFiles();
	    sessionFiles.addFile (newDirectory);
	    sessionFiles.addFile (subscriberListFile);
	    sessionFiles.addFile (formatFile);
	    httpSession.setAttribute (reportId + "_rfFiles", sessionFiles);

	    try {
		newDirectory.mkdir();
		    
	    }
	    catch (SecurityException ex) {
		// Do some sort of error recovery here.
		String errorMsg = ETTXUtil.localizeMessage ("querySubscriberReportSecurityException") + 
		    ex.getMessage();
		ActionErrors aes = new ActionErrors();
		logger.error(errorMsg);
		ActionError error = new ActionError (errorMsg);
		aes.add(Constants.ERROR,error);
		saveErrors(request,aes);
		return mapping.findForward (ERROR_FORWARD);
	    }

	    subscriberCollection  = (Collection)request.getAttribute("subscriberList");

	    // We'll write out the subscriber information first;
	    try {
		writeSubscriberReport (subscriberCollection, subscriberListFile, request);
		writeFormatFile (formatFile);

		// Now we need pass our report parameters back to the application.
		RFParameter rfbh = new RFParameter();

		rfbh.setNumOfRecords( Short.toString( ( new Integer (subscriberCollection.size())).shortValue() ) );

		// this action class name
		// This is the alias for this action.
		rfbh.setCallBack( "subsRecords.do" );

		// directory path to where the XSLT processor can pick up report files 
		rfbh.setPath( tempFileDirectory );

		// default sorted column name
		rfbh.setSortValue( "subsName" );

		// WinName is not the name of the JavaScript created window.
		// WinName should be used to bind each HttpRequest with this report's
		// files and query data. Since this example keeps that in the 
		// user's session using the reportID as part of the key, I'll use
		// the reportID here.
		rfbh.setWinName( reportId );

		// if needed set up help link, otherwise help icon will not be drawn
		rfbh.setHelpLink( "http://www.cisco.com" );

		// put the RFParameter data into the request so the RF JSP pages
		// can get at it
		request.setAttribute( "AppData", rfbh );
	    }
	    catch (IOException ex) {
		// Do some sort of error recovery here.
		String errorMsg = ETTXUtil.localizeMessage ("querySubscriberReportIOException") + 
		    ex.getMessage();
		ActionErrors aes = new ActionErrors();
		logger.error(errorMsg , ex);
		ActionError error = new ActionError (errorMsg);
		aes.add(Constants.ERROR,error);
		saveErrors(request,aes);
		return mapping.findForward (ERROR_FORWARD);
	    }
	} else {
	    // See if this request came from the report framework.
	    // Seems to me like I should using a method in RFRequestHandler
	    // that lets me know whether or not this request is a "RF" request
	    // or not rather than using request.getParam.
	    String isReport = request.getParameter( "Report" );

	    if ( isReport != null && isReport.equals( "true" ) ) {

		String reportID = request.getParameter( "WinName" );

		// call the helper to see what page it thinks we should goto
		// or what action we need to take
		RFRequestHandler rfHelper = new RFRequestHandler();
		String rfScreenID = rfHelper.getScreenId( request );

		if ( rfScreenID != null ) {
		    // This functionality should only be called if we made more
		    // than one report file, which we should never do...
		    forwardPage = rfScreenID;

		    // If the helper returns "Application" we know that the RF code
		    // assumes the application will want to do something before going onto
		    // the next RF JSP page.
		    // This happens only if the NumOfRecords was greater than 2000.
		    // So if your report generated a report with less than 2000 records
		    // none of the code in the following if statement will be used.
		    if ( forwardPage.equals( "exportAll" ) ) {
			// We get here only when the application has specified that
			// there are more than 2000 records in the 'numOfRecords'
			// setting. If less than 2000 then the RFRequestHandler will
			// return 'exportReport' as the forwardPage.
			//
			// Since this action class already wrote out all of its XML files
			// lets forward to export XSLT JSP page.
			forwardPage = "exportReport";
		    } else if ( forwardPage.equals( "sortAll" ) ) {
			// This 'Sorting' action will take place whenever there are
			// more than 2000 records in the numOfRecords setting.
			// If there are more than 2000 then the XSLT processor takes
			// up too much memory and can not be used to sort the records
			// so control is passed back to the application so the app
			// can rerun the original query and sorting on the new field
			// ( using a database's sort on column feature )
			HttpSession sess = request.getSession();
			String query = ( String )sess.getAttribute( reportID + "_rfQuery" );
			// Seems to me that getSortValue and getOrder should be accessor
			// methods on RFRequestHelper. Then it would be even more helpful :)
			String sortValue = request.getParameter( "SortValue" );
			String sortOrder = request.getParameter( "Order" );
			// I don't have time to properly code an application that sorts
			// so you'll have to imagine that the report was resorted.
			forwardPage = "launchReport";
		    }
		}

		// seems like the RFRequestHandler should tell us we should "Exit"
		String reportAction = request.getParameter( "Action" );
		if ( reportAction != null && reportAction.equals( "Exit" ) ) {

		    System.out.println( "RF application must handle Exit" );
		    HttpSession sess = request.getSession();
		    SessionFiles sf = 
			( SessionFiles ) sess.getAttribute( reportID + "_rfFiles" );
		    if ( sf != null )
			sf.deleteFiles();

		    // return null so controller returns nothing to browser
		    return null;
		}
	    }
	}

	// debug
	System.out.println ("RepSubscriberListAction:: Forwarding to: " + forwardPage);

	return mapping.findForward(forwardPage);
    }

    private void writeSubscriberReport (Collection subscribers,
					File reportFile,
					HttpServletRequest request) throws IOException
    {
	boolean subscriberLinkEnabled;
	int     webAppNameIndex;
	int     portIndex;
	String  requestURL;
	String httpPath = null;
	Iterator   iter2;
	Collection list;
	StringBuffer completeString;
	SubscriberRecord record;
	FileWriter fw = new FileWriter (reportFile);

	requestURL = HttpUtils.getRequestURL(request).toString();
	portIndex = requestURL.indexOf (':');

	subscriberLinkEnabled = false;
	if (portIndex > 0) {
	    webAppNameIndex = requestURL.indexOf ("/ettx_admin", portIndex);
	    if (webAppNameIndex > 0) {
		httpPath = requestURL.substring (0, webAppNameIndex);
		System.out.println ("HTTP Path: " + httpPath);
		if (httpPath.length() > 0) {
		    subscriberLinkEnabled = true;
		}
	    }
	}

        fw.write( "<?xml version=\"1.0\"?>\n<Report>\n<TableData>\n" );

	Iterator iter = subscribers.iterator();
	while ( iter.hasNext() ) {
	    record = (SubscriberRecord) iter.next();

	    fw.write( "<GenericEvent>" );

	    completeString = new StringBuffer();
	    completeString.append ("<loginID");
	    if (subscriberLinkEnabled == true) {
		completeString.append (" HREF=\"" + httpPath + "/ettx_admin/viewSubscriberRecord.do?subscriberId=" + record.getSubscriberID() + "\"");
	    }
	    completeString.append ("> " + record.getLoginID());
	    completeString.append ("</loginID>");
	    fw.write (completeString.toString());
	    fw.write( "<subsName>" + record.getSubscriberFullName() + "</subsName>" );

	    fw.write( "<postalAddress>" + record.getPostalAddress() + "</postalAddress>" );
	    fw.write( "<postalCity>" + record.getPostalCity() + "</postalCity>" );
	    fw.write( "<postalState>" + record.getPostalState() + "</postalState>" );

	    fw.write( "<homeNumber>" + record.getHomeNumber() + "</homeNumber>" );
	    fw.write( "<mobileNumber>" + record.getMobileNumber() + "</mobileNumber>" );

	    fw.write( "<accountNumber>" + record.getAccountNumber() + "</accountNumber>" );
	    fw.write( "<accountStatus>" + record.getAccountStatus() + "</accountStatus>" );
	    
	    list = record.getSubscribedServices();
	    completeString = new StringBuffer();
	    if (list != null) {
		iter2 = list.iterator();
		while (iter2.hasNext()) {
		    completeString.append ( (String) iter2.next());
		    if (iter2.hasNext()) {
			// We need to add a carriage return
			completeString.append ("\n");
		    }
		}
	    }
	    fw.write( "<subscribedServices>" + completeString.toString() + "</subscribedServices>" );
	    fw.write( "</GenericEvent>\n" );
	}
	
        fw.write( "</TableData>\n</Report>\n" );
        fw.close();
    }

    private void writeFormatFile (File formatFile) throws IOException
    {
	FileWriter fw = new FileWriter( formatFile ); 

	fw.write( "<?xml version=\"1.0\"?>\n" );
	fw.write( "<Format>\n" );
	fw.write( "<TableOuter>\n" );
	fw.write( "  <ApplicationName>BAC-ETTx Administration Tool</ApplicationName>\n" );
	fw.write( "  <ReportName>View Subscribers Report</ReportName>\n" );
	fw.write( "  <Filter>false</Filter>\n" );
	fw.write( "  <NumberOfColumns>10</NumberOfColumns>\n" );
	fw.write( "  <HeaderFormat SplitColumn=\"false\"/>\n" );
	fw.write( "  <IgnoreRoot>2LevelDeep</IgnoreRoot>\n" );
	fw.write( "</TableOuter>\n" );
	fw.write( "<TableInner>\n" );
	fw.write( "  <Column displayName=\"" + LoginIDColumnHeader + "\" xmlTag=\"loginID\" dataType=\"text\" sortable=\"true\">\n" );
	fw.write( "  </Column>\n" );
	fw.write( "  <Column displayName=\"" + SubscriberNameColumnHeader + "\" xmlTag=\"subsName\" dataType=\"text\" sortable=\"true\" >\n" );
	fw.write( "  </Column>\n" );
	fw.write( "  <Column displayName=\"" + PostalAddressColumnHeader + "\" xmlTag=\"postalAddress\" dataType=\"text\" sortable=\"false\">\n" );
	fw.write( "  </Column>\n" );
	fw.write( "  <Column displayName=\"" + PostalCityColumnHeader + "\" xmlTag=\"postalCity\" dataType=\"text\" sortable=\"false\">\n" );
	fw.write( "  </Column>\n" );
	fw.write( "  <Column displayName=\"" + PostalStateColumnHeader + "\" xmlTag=\"postalState\" dataType=\"text\" sortable=\"false\">\n" );
	fw.write( "  </Column>\n" );
	fw.write( "  <Column displayName=\"" + HomeNumberColumnHeader + "\" xmlTag=\"homeNumber\" dataType=\"text\" sortable=\"false\">\n" );
	fw.write( "  </Column>\n" );
	fw.write( "  <Column displayName=\"" + MobileNumberColumnHeader + "\" xmlTag=\"mobileNumber\" dataType=\"text\" sortable=\"false\">\n" );
	fw.write( "  </Column>\n" );
	fw.write( "  <Column displayName=\"" + AccountNumberColumnHeader + "\" xmlTag=\"accountNumber\" dataType=\"number\" sortable=\"true\" >\n" );
	fw.write( "  </Column>\n" );
	fw.write( "  <Column displayName=\"" + AccountStatusColumnHeader + "\" xmlTag=\"accountStatus\" dataType=\"text\" sortable=\"true\">\n" );
	fw.write( "  </Column>\n" );
	fw.write( "  <Column displayName=\"" + SubscriberServicesColumnHeader + "\" xmlTag=\"subscribedServices\" dataType=\"text\" sortable=\"true\">\n" );
	fw.write( "  </Column>\n" );
	fw.write( "</TableInner>\n" );
	fw.write( "</Format>\n" );
	fw.close();
    }

}
