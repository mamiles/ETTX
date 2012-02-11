package com.cisco.ettx.admin.gui.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.*;
import org.apache.struts.action.ActionForm;

import com.cisco.ettx.admin.gui.web.util.ETTXUtil;
import com.cisco.nm.xms.rfw.RFParameter;
import com.cisco.ettx.admin.gui.web.datatypes.*;
import com.cisco.ettx.admin.gui.web.beans.SubsTSResultFormBean;
import com.cisco.ettx.admin.gui.web.datatypes.TroubleshootRecord;
import com.cisco.nm.uii.taglib.framework.Constants;
import com.cisco.nm.uii.reports.SessionFiles;

import java.io.*;
import java.util.*;
import java.text.*;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.ParsePosition;

public class SubsTSResultAction extends Action
{
    public static final String TROUBLESHOOT_ACTION = "Troubleshoot";
    public static final String TROUBLESHOOT_RESULT_FORWARD = "subsTSResult";
    public static final String EXPORT_RESULT_FORWARD = "exportReport2";
    public static final String ERROR_FORWARD = "closeWindowAndNotification";
    public static final String TELNET_ACTION = "Telnet";
    public static final String TELNET_FORWARD = "telnetToSwitch";
    public static final String SAVE_ACTION = "Save";

    private static final String DefaultTemporaryDirectory = "/tmp";
    private static final String ApplicationTempDirectoryName = "cisco";
    private static final String SubscriberReportFileName = "2000.xml";
    private static final String FormatFileName = "format.xml";
    private static final String TempDirectoryPropertyName = "java.io.tmpdir";
    private static final Logger logger = Logger.getLogger(SubsTSResultAction.class);

    private static String SubscriberNameColumnHeader = null;
    private static String DisplayNameColumnHeader = null;
    private static String LoginIDColumnHeader = null;
    private static String AccountNumberColumnHeader = null;
    private static String AccountStatusColumnHeader = null;
    private static String PostalAddressColumnHeader = null;
    private static String PostalCityColumnHeader = null;
    private static String PostalStateColumnHeader = null;
    private static String CountryCodeColumnHeader = null;
    private static String HomeNumberColumnHeader = null;
    private static String MobileNumberColumnHeader = null;
    private static String SubscriberServicesColumnHeader = null;
    private static String MACAddressColumnHeader = null;
    private static String IPAddressColumnHeader = null;
    private static String SwitchAddressColumnHeader = null;
    private static String SwitchPortColumnHeader = null;
    private static String PingUserColumnHeader = null;
    private static String RouterTraceColumnHeader = null;
    private static String DeviceIFStatusColumnHeader = null;

    static {
	SubscriberNameColumnHeader = ETTXUtil.localizeMessage("subscriberRecordSubscriberName");
	LoginIDColumnHeader = ETTXUtil.localizeMessage("subscriberRecordLoginID");
	DisplayNameColumnHeader = ETTXUtil.localizeMessage("subscriberRecordDisplayName");
	PostalAddressColumnHeader = ETTXUtil.localizeMessage("subscriberRecordPostalAddress");
	PostalCityColumnHeader = ETTXUtil.localizeMessage("subscriberRecordPostalCity");
	PostalStateColumnHeader = ETTXUtil.localizeMessage("subscriberRecordPostalState");
	HomeNumberColumnHeader = ETTXUtil.localizeMessage("subscriberRecordHomeNumber");
	MobileNumberColumnHeader = ETTXUtil.localizeMessage("subscriberRecordMobileNumber");
	AccountNumberColumnHeader = ETTXUtil.localizeMessage("subscriberRecordAccountNumber");
	AccountStatusColumnHeader = ETTXUtil.localizeMessage("subscriberRecordAccountStatus");
	SubscriberServicesColumnHeader = ETTXUtil.localizeMessage("subscriberRecordSubscriberServices");
	MACAddressColumnHeader = ETTXUtil.localizeMessage("subscriberRecordMACAddressList");
	IPAddressColumnHeader = ETTXUtil.localizeMessage("subscriberRecordIPAddressList");
	SwitchAddressColumnHeader = ETTXUtil.localizeMessage("subscriberRecordSwitchAddress");
	SwitchPortColumnHeader = ETTXUtil.localizeMessage("subscriberRecordSwitchPort");
	PingUserColumnHeader = ETTXUtil.localizeMessage("subscriberRecordPingOutput");
	RouterTraceColumnHeader = ETTXUtil.localizeMessage("subscriberRecordTraceOutput");
	DeviceIFStatusColumnHeader = ETTXUtil.localizeMessage("subscriberRecordShowIntOutput");
    }


    protected String getDefaultForward() {
	return TROUBLESHOOT_RESULT_FORWARD;
    }

    public ActionForward perform(ActionMapping       mapping,
                                 ActionForm          form,
                                 HttpServletRequest  request,
                                 HttpServletResponse response)
    {       
	HttpSession the_session = request.getSession();

	SubsTSResultFormBean sysBean = (SubsTSResultFormBean)form;
	String action = request.getParameter("action");
	logger.debug("In SubsTSResultAction::perform " + action);

	if (action != null) {
	    if (action.equals(TELNET_ACTION)) {
		//Get all the details about the switch from the subscriber
		//record
		return mapping.findForward(TELNET_FORWARD);
	    } else if (action.equals(SAVE_ACTION)) {
		//Save in the local archival folder
		// First we need to get our record
		TroubleshootRecord tsRecord = sysBean.getSubscriberRecord();
		if (tsRecord != null) {
		    String systemTempDir;
		    String reportId;
		    String tempFileDirectory;
		    String tsSubscriberReportFileName;
		    String reportFormatFileName;
		    File newDirectory;
		    File tsSubscriberReportFile;
		    File formatFile;
		    HttpSession httpSession;
		    SessionFiles sessionFiles;
		    RFParameter rfbh;

		    // We need to create our temporary Directory
		    httpSession = request.getSession();
		    systemTempDir = System.getProperty ( TempDirectoryPropertyName );
		    if (systemTempDir == null) {
			systemTempDir = DefaultTemporaryDirectory;
		    }
		    reportId = new Long ( System.currentTimeMillis() ).toString();
		    tempFileDirectory = systemTempDir + File.separator + ApplicationTempDirectoryName + reportId;
		    tsSubscriberReportFileName = tempFileDirectory + File.separator + SubscriberReportFileName;
		    reportFormatFileName = tempFileDirectory + File.separator + FormatFileName;
		    newDirectory = new File (tempFileDirectory);
		    tsSubscriberReportFile = new File (tsSubscriberReportFileName);
		    tsSubscriberReportFile.deleteOnExit();
		    formatFile = new File (reportFormatFileName);
		
		    // We need to write out our subscriber list to a temporary file.
		    sessionFiles = new SessionFiles();
		    sessionFiles.addFile (newDirectory);
		    sessionFiles.addFile (tsSubscriberReportFile);
		    sessionFiles.addFile (formatFile);
		    httpSession.setAttribute (reportId + "_rfFiles", sessionFiles);

		    try {
			newDirectory.mkdir();
			    
		    }
		    catch (SecurityException ex) {
			// Do some sort of error recovery here.
			String errorMsg = ETTXUtil.localizeMessage ("subsTSResultSecurityException") + 
			    ex.getMessage();
			ActionErrors aes = new ActionErrors();
			logger.error(errorMsg);
			ActionError error = new ActionError (errorMsg);
			aes.add(Constants.ERROR,error);
			saveErrors(request,aes);
			return mapping.findForward (ERROR_FORWARD);
		    }

		    // Write out the report information to our file.
		    try {
			writeTSSubscriberReport (tsRecord, tsSubscriberReportFile, request);
			writeFormatFile (formatFile);

			rfbh = new RFParameter();
			rfbh.setNumOfRecords(Short.toString( (new Integer (1).shortValue() ) ));

			// this action class name
			// This is the alias for this action.
			rfbh.setCallBack( "subsTSResult.do" );

			// directory path to where the XSLT processor can pick up report files 
			rfbh.setPath( tempFileDirectory );

			// default sorted column name
			rfbh.setSortValue( "subscriberName" );

			// WinName is not the name of the JavaScript created window.
			// WinName should be used to bind each HttpRequest with this report's
			// files and query data. Since this example keeps that in the 
			// user's session using the reportID as part of the key, I'll use
			// the reportID here.
			rfbh.setWinName( reportId );

			// if needed set up help link, otherwise help icon will not be drawn
			rfbh.setHelpLink( "http://www.cisco.com" );

			// We need to get the current report date/time
			Date currDate = new Date();
			DateFormat dt = DateFormat.getDateInstance (DateFormat.MEDIUM);
			
			// put the RFParameter data into the request so the RF JSP pages
			// can get at it
			request.setAttribute( "AppData", rfbh );
			request.setAttribute ("ReportTime", dt.format (currDate));
			request.setAttribute ("Path", tempFileDirectory);
			request.setAttribute ("NumOfRecord", "1");
			request.setAttribute ("SortValue", "subscriberName");
			request.setAttribute ("Order", "ascending");
		    }
		    catch (IOException ex) {
			// Do some sort of error recovery here.
			String errorMsg = ETTXUtil.localizeMessage ("subsTSResultIOException") + 
			    ex.getMessage();
			ActionErrors aes = new ActionErrors();
			logger.error(errorMsg);
			ActionError error = new ActionError (errorMsg);
			aes.add(Constants.ERROR,error);
			saveErrors(request,aes);
			return mapping.findForward (ERROR_FORWARD);
		    }
		}

		// return mapping.findForward(TROUBLESHOOT_RESULT_FORWARD);
		return mapping.findForward(EXPORT_RESULT_FORWARD);
	    }
	    logger.debug("Setting the troubleshoot data from the previous screen");
	    sysBean.setSubscriberRecord((TroubleshootRecord)request.getAttribute(TSSubscriberListAction.TS_RECORD));
	}
	//Set the Hide_TOC to true
	request.setAttribute("com.cisco.nm.uii.taglib.framework.Constants.HIDE_TOC", "true");
                    
	String screenID = mapping.getPath().substring(1);
	return mapping.findForward(screenID);
    }

    private void writeTSSubscriberReport (TroubleshootRecord tsRecord,
				     File reportFile,
				     HttpServletRequest request) throws IOException
    {
	FileWriter fw = new FileWriter (reportFile);

	StringBuffer msg = new StringBuffer();

        fw.write( "<?xml version=\"1.0\"?>\n<Report>\n<TableData>\n" );

	fw.write( "<GenericEvent>" );

	fw.write( "<subscriberName>" + tsRecord.getSubscriberFullName()  + "</subscriberName>" );
	fw.write( "<loginID>" + tsRecord.getLoginID() + "</loginID>" );
	fw.write( "<displayName>" + tsRecord.getSubscriberName()  + "</displayName>" );
	fw.write( "<accountNumber>" + tsRecord.getAccountNumber() + "</accountNumber>");
	fw.write( "<accountStatus>" + tsRecord.getAccountStatus() + "</accountStatus>");
	fw.write( "<postalAddress>" + tsRecord.getPostalAddress() + "</postalAddress>");
	fw.write( "<postalCity>" + tsRecord.getPostalCity() + "</postalCity>");
	fw.write( "<postalState>" + tsRecord.getPostalState() + "</postalState>");
	fw.write( "<countryCode>" + tsRecord.getCountryCode() + "</countryCode>");
	fw.write( "<homeNumber>" + tsRecord.getHomeNumber() + "</homeNumber>");
	fw.write( "<mobileNumber>" + tsRecord.getMobileNumber() + "</mobileNumber>");
	fw.write( "<subscribedServices>" + tsRecord.getSubscriberServicesText() + "</subscribedServices>");
	fw.write( "<macAddresses>" + tsRecord.getMacAddressText() + "</macAddresses>");
	fw.write( "<ipAddresses>" + tsRecord.getIpAddressText() + "</ipAddresses>");
	fw.write( "<switchAddress>" + tsRecord.getSwitchIPAddress() + "</switchAddress>");
	fw.write( "<switchPortName>" + tsRecord.getSwitchPortName() + "</switchPortName>");
	fw.write( "<pingOutput>" + tsRecord.getPingOutput() + "</pingOutput>");
	fw.write( "<traceOutput>" + tsRecord.getTraceOutput() + "</traceOutput>");
	fw.write( "<portOutput>" + tsRecord.getShowPortOutput() + "</portOutput>");
	fw.write( "</GenericEvent>\n" );
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
	fw.write( "  <ReportName>Subscriber Troubleshoot Action Report</ReportName>\n" );
	fw.write( "  <Filter>false</Filter>\n" );
	fw.write( "  <NumberOfColumns>2</NumberOfColumns>\n" );
	fw.write( "  <HeaderFormat SplitColumn=\"false\"/>\n" );
	fw.write( "  <IgnoreRoot>2LevelDeep</IgnoreRoot>\n" );
	fw.write( "</TableOuter>\n" );
	fw.write( "<TableInner>\n" );
	fw.write( "  <Column displayName=\"" + SubscriberNameColumnHeader + "\" xmlTag=\"subscriberName\" dataType=\"text\" sortable=\"false\">\n" );
	fw.write( "  </Column>\n" );
	fw.write( "  <Column displayName=\"" + 	LoginIDColumnHeader + "\" xmlTag=\"loginID\" dataType=\"text\" sortable=\"false\">\n" );
	fw.write( "  </Column>\n" );
	fw.write( "  <Column displayName=\"" + DisplayNameColumnHeader + "\" xmlTag=\"displayName\" dataType=\"text\" sortable=\"false\">\n" );
	fw.write( "  </Column>\n" );
	fw.write( "  <Column displayName=\"" + 	AccountNumberColumnHeader + "\" xmlTag=\"accountNumber\" dataType=\"text\" sortable=\"false\">\n" );
	fw.write( "  </Column>\n" );
	fw.write( "  <Column displayName=\"" + 	AccountStatusColumnHeader + "\" xmlTag=\"accountStatus\" dataType=\"text\" sortable=\"false\">\n" );
	fw.write( "  </Column>\n" );
	fw.write( "  <Column displayName=\"" + 	PostalAddressColumnHeader + "\" xmlTag=\"postalAddress\" dataType=\"text\" sortable=\"false\">\n" );
	fw.write( "  </Column>\n" );
	fw.write( "  <Column displayName=\"" + 	PostalCityColumnHeader + "\" xmlTag=\"postalCity\" dataType=\"text\" sortable=\"false\">\n" );
	fw.write( "  </Column>\n" );
	fw.write( "  <Column displayName=\"" + 	PostalStateColumnHeader + "\" xmlTag=\"postalState\" dataType=\"text\" sortable=\"false\">\n" );
	fw.write( "  </Column>\n" );
	fw.write( "  <Column displayName=\"" + 	CountryCodeColumnHeader + "\" xmlTag=\"countryCode\" dataType=\"text\" sortable=\"false\">\n" );
	fw.write( "  </Column>\n" );
	fw.write( "  <Column displayName=\"" + 	HomeNumberColumnHeader + "\" xmlTag=\"homeNumber\" dataType=\"text\" sortable=\"false\">\n" );
	fw.write( "  </Column>\n" );
	fw.write( "  <Column displayName=\"" + 	MobileNumberColumnHeader + "\" xmlTag=\"mobileNumber\" dataType=\"text\" sortable=\"false\">\n" );
	fw.write( "  </Column>\n" );
	fw.write( "  <Column displayName=\"" + 	SubscriberServicesColumnHeader + "\" xmlTag=\"subscribedServices\" dataType=\"text\" sortable=\"false\">\n" );
	fw.write( "  </Column>\n" );
	fw.write( "  <Column displayName=\"" + 	MACAddressColumnHeader + "\" xmlTag=\"macAddresses\" dataType=\"text\" sortable=\"false\">\n" );
	fw.write( "  </Column>\n" );
	fw.write( "  <Column displayName=\"" + 	IPAddressColumnHeader + "\" xmlTag=\"ipAddresses\" dataType=\"text\" sortable=\"false\">\n" );
	fw.write( "  </Column>\n" );
	fw.write( "  <Column displayName=\"" + 	SwitchAddressColumnHeader + "\" xmlTag=\"switchAddress\" dataType=\"text\" sortable=\"false\">\n" );
	fw.write( "  </Column>\n" );
	fw.write( "  <Column displayName=\"" + 	SwitchPortColumnHeader + "\" xmlTag=\"switchPortName\" dataType=\"text\" sortable=\"false\">\n" );
	fw.write( "  </Column>\n" );
	fw.write( "  <Column displayName=\"" + 	PingUserColumnHeader + "\" xmlTag=\"pingOutput\" dataType=\"text\" sortable=\"false\">\n" );
	fw.write( "  </Column>\n" );
	fw.write( "  <Column displayName=\"" + 	RouterTraceColumnHeader + "\" xmlTag=\"traceOutput\" dataType=\"text\" sortable=\"false\">\n" );
	fw.write( "  </Column>\n" );
	fw.write( "  <Column displayName=\"" + 	DeviceIFStatusColumnHeader + "\" xmlTag=\"portOutput\" dataType=\"text\" sortable=\"false\">\n" );
	fw.write( "  </Column>\n" );
	fw.write( "</TableInner>\n" );
	fw.write( "</Format>\n" );
	fw.close();
    }
}
