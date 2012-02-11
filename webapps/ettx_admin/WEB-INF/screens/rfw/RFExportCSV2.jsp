<% 
/*****************************************************
Copyright (c) 2002 Cisco Systems Inc.
All rights reserved
*********************************************************/
%>
<%@ page import="java.lang.*, java.text.*, java.util.*, java.io.*, com.cisco.nm.uii.i18n.UiiResourceBundle" %> <%@ taglib uri="/WEB-INF/tlds/uii-framework-taglib.tld" prefix="embu" %> <%@ taglib uri="http://jakarta.apache.org/taglibs/i18n" prefix="i18n" %> <i18n:bundle baseName="com.cisco.nm.uii.i18n.uii" localeRef="userLocale" id="uiiResourceBundle" /> <jsp:useBean id="rfTransformer" class="com.cisco.nm.xms.rfw.RFDataTransformer" scope="request" />
<%


/************** Setting the save window for the export file ******************/
    String eMessage = UiiResourceBundle.instance().localizeString("error");

    boolean alreadyWrittenSomeCSV = false;

    try {

      /*************** Getting Parameters from Request object ****************/

      int RFReportSize = 2000;
      String currtime = (String) request.getAttribute( "ReportTime" );
      String sortBy = null;
      String sortColumn = "true";
      String pageNum = "1";
      int numFaults = 0; 

      // to localize the current time, convert it into a Date (so we can pass it as a formatable parameter)
      Date currDate = null;
      if ( currtime == null )
      {
        currDate = new Date();
        SimpleDateFormat ueFormat = new SimpleDateFormat( "k:mm:ss  'on' d MMM yyyy" );
        currtime = ueFormat.format( currDate );
      }
      else
      {
        DateFormat df = DateFormat.getDateTimeInstance();
        try
        {
          currDate = df.parse(currtime);
        }
        catch (Exception e)
        {
          System.out.println("RFExportCSV.jsp: exception caught while converting \"" + currtime + "\" to Date (exception = " + e + ")");
        }
        if ( currDate == null )
        {
          // currtime has a value but cannot be parsed
          // - this may not be right, but just use current date/time
          currDate = new Date();
        }
      }
      // currDate is set now, use this as an arg to i18n messaging

    
      String path = (String) request.getAttribute("Path");    
      String numRecord = (String) request.getAttribute("NumOfRecord");
      numFaults = Integer.parseInt(numRecord);

      sortBy = (String) request.getAttribute("SortValue");
      if(sortBy==null)
         sortBy="none";

      sortColumn = (String) request.getAttribute("SortColumn");
      if(sortColumn==null)
          sortColumn= sortBy;

      String order = (String) request.getAttribute("Order");
      if(order ==null)
         order="descending";


      rfTransformer.setPath(path);
      rfTransformer.parseFormat(sortBy);

      String dType= rfTransformer._dataType;
      
      String reportTitle = rfTransformer._reportTitle;
      String appName = rfTransformer._appTitle ;
    
      String xslPath = pageContext.getServletContext().getRealPath(
          "/WEB-INF/rfw/XSL");

      rfTransformer.setXslPath(xslPath);

      if(rfTransformer._levelDeep.equals("1LevelDeep"))
          rfTransformer.setXslFile("csvExport.xsl");
      else
          rfTransformer.setXslFile("csvExport2.xsl");

      String data = null;
      Integer num;
      File tempFile;

      String fileName;

      /**** Looping to transform all existing data files 2000.xml, 4000.xml, ...
            at given path depend on number of records  ***********/

      for( int i = RFReportSize; i-RFReportSize < numFaults ; i+=RFReportSize) {

       num = new Integer(i);

       fileName =  num.toString() + ".xml"; 

       tempFile = new File(path+ File.separator +fileName);

       String fdn = path + File.separator + fileName;
       if( ! ( tempFile.exists() ) )
       {
           Object[] msgArgs1 = { fdn };
           eMessage = UiiResourceBundle.instance().localizeString("report.notification.fileNotFound",msgArgs1);
       }

       rfTransformer.setXmlFile(fileName);
       data = rfTransformer.getAllData(sortBy, dType, order, sortColumn);

       // the next few sections create all the output if no exception occur

       // next line sometimes causes IE to jump into Excel before saving file
       // first
       //response.setContentType("application/octet-stream");
       response.setContentType("application/csv");
       response.setHeader("Content-Disposition", " filename=export.csv ;"); 

       // next line causes CMF 2.2's Apache to throw 'site-not-found' error
       // response.setHeader("Cache-Control", "no-cache"); 

       // Override what the Struts controller sets because the /WEB-INF/web.xml
       // file has the 'nocache' flag set to 'true'. For some reason this
       // causes IE to fail when used with the exporting of CSV and/or the
       // "Content-Dispsition" setHeader call used above.
       response.setHeader("Pragma", "cache");
       response.setHeader("Cache-Control", "cache");

       out.write(appName +"\n");
       //out.write(reportTitle +" - As of " + currtime +"\n");
       Object[] msgArgs = { currDate, currDate };
       out.write(reportTitle + " - " + UiiResourceBundle.instance().localizeString("report.asOfTOnY",msgArgs) + "\n");
       out.write(data);
       alreadyWrittenSomeCSV = true;

      }
    }
    catch (Exception e)
    {
      //Integrate with the error mechanism ???
      eMessage = e.getMessage();

      // if we have already written some csv data then output a text error
      // into the CSV data
      // else output the HTML error page
      if ( alreadyWrittenSomeCSV )
         out.write( eMessage + "\n"); 
      else {
          
        // replace \ with \\
        if ( File.separator.equals( "\\" ) ) {
          if ( eMessage.indexOf( "\\" ) > 0 ) {
            StringBuffer sb = new StringBuffer();
            int loc = 0;
            while ( eMessage.indexOf( "\\", loc ) > -1 ) {
              int ind = eMessage.indexOf( "\\", loc );
              sb.append( eMessage.substring( loc, ind ) + "\\\\" );
              loc = ind + 1;
            }
            sb.append( eMessage.substring( loc, eMessage.length() ) );
            eMessage = sb.toString();
          }
        }

%><html>
<head>
<title><i18n:message bundleRef="uiiResourceBundle" key="report.title.exportError"/></title>
<script language="JavaScript">
<i18n:message id="notificationHeaderFatal"   bundleRef="uiiResourceBundle" key ="notification.header.fatal"/>
<i18n:message id="notificationHeaderError"   bundleRef="uiiResourceBundle" key ="notification.header.error"/>
<i18n:message id="notificationHeaderWarning" bundleRef="uiiResourceBundle" key ="notification.header.warning"/>
<i18n:message id="notificationHeaderInfo"    bundleRef="uiiResourceBundle" key ="notification.header.info"/>
<i18n:message id="notificationHeaderMsgs"    bundleRef="uiiResourceBundle" key ="notification.header.msgs"/>
<i18n:message id="buttonTextOk" bundleRef="uiiResourceBundle" key="button.text.ok"/>
var notificationHeaderFatal = "<%=notificationHeaderFatal%>"
var notificationHeaderError = "<%=notificationHeaderError%>"
var notificationHeaderWarning = "<%=notificationHeaderWarning%>"
var notificationHeaderInfo = "<%=notificationHeaderInfo%>"
var notificationHeaderMsgs = "<%=notificationHeaderMsgs%>"
var buttonTextOk = "<%=buttonTextOk%>"
var uiiContextPath = "<%= request.getContextPath() %>";
// ------------- start javascript for popup notifications --------------
var notificationWin;
function popupNotifications( um ) {

  var dialogWin = null;
  var winTop = (screen.height / 2) - (200 / 2);
  var winLeft = (screen.width / 2) - (300 / 2);
  var windowFeatures = "width=" + 420 + ",";
  windowFeatures += "height=" + 250 + ",";
  windowFeatures += "left=" + winLeft + ",";
  windowFeatures += "top=" + winTop + ",";
  windowFeatures += "scrollbars=yes";

  var notificationWindow = null;
  var header = ""; 
  var body = ""; // notification HTML;
  var data = new Array(
      { type:"FATAL",   image:"fatal.gif",   header:notificationHeaderFatal   },
      { type:"ERROR",   image:"error.gif",   header:notificationHeaderError   },
      { type:"WARNING", image:"warning.gif", header:notificationHeaderWarning },
      { type:"INFO",    image:"info.gif",    header:notificationHeaderInfo    } );

  // build up notification HTML
  if ( um.length ) {

    // when we have more than one message use this as the header
    header = notificationHeaderMsgs; 

    for ( var i = 0; i < data.length; i++ )
      for ( var j = 0; j < um.length; j++ )
        if ( um[j].type == data[i].type )
          body += '\n<tr><td align="center" valign="top"><img src="RSRC/en_US/images/icons/' + data[i].image + '"></td><td>' + um[j].message + '</td></tr>\n';

  }
  else {
    for ( var i = 0; i < data.length; i++ )
      if ( um.type == data[i].type ) {
        header = data[i].header;
        body += '\n<tr><td align="center" valign="top"><img src="RSRC/en_US/images/icons/' + data[i].image + '"></td><td>' + um.message + '</td></tr>\n';
      }
  }

  if ( body == "" ) return;

  notificationWin = window.open( uiiContextPath + "/uii/blank.htm", randomName(), windowFeatures );
  notificationWin.document.write( '<html><head><title>Notifications</title>' );
  // using the browser sniff code from jsrsClient.js
  // Netscape
  if ( document.layers )
    notificationWin.document.write( '<link rel="stylesheet" href="uii/global_ns.css">' );
  // IE
  if ( document.all )
    notificationWin.document.write( '<link rel="stylesheet" href="uii/global.css">' );

  notificationWin.document.write( '</head>\n<body><table width="100%" border="0" cellspacing="0" cellpadding="5" height="100%" bgcolor="#C0C0C0" align="center"><tr><td width="100%" background="images/bg_010.gif" valign="middle"><table width="350" border="0" cellspacing="1" cellpadding="2" align="center" valign="middle" bgcolor="#FFFFFF"><tbody><tr><td nowrap background="RSRC/en_US/images/vertlines_dk.gif" bgcolor="#669999"><img src="images/shim.gif"></td></tr><tr><td noWrap height=22 background="RSRC/en_US/images/dia_title_bg2.gif" align="center"><b>' + header + '</b></td></tr><tr><td bgcolor="silver" align="center"><table width="80%" border="0" cellpadding="5">' ); 


  notificationWin.document.write( body );

  notificationWin.document.write( '</table></td></tr><tr bgcolor="#999999"><td nowrap bgcolor="#999999"><img src="images/shim.gif"></td></tr><tr><td bgcolor="silver"><table cellspacing=3 cellpadding=0 border=0 width="100%"><tr><td width="100%"><img src="RSRC/en_US/images/shim.gif" width="20" height="5"></td><td nowrap><div align="center"><table border="0" cellspacing="0" cellpadding="0"><tr><td><img src="RSRC/en_US/images/btn_lt_1.gif"></td><TD align="center" NOWRAP BACKGROUND="RSRC/en_US/images/btn_bg_1.gif" width="" id=><strong><a href="javascript:window.close()"> ' + buttonTextOk + '</a></strong></TD><td><img src="RSRC/en_US/images/btn_rt_1.gif"></td></tr></table></div></td></tr></table></td></tr><tr><td nowrap bgcolor="#cecfce" background="RSRC/en_US/images/vertlines_dk.gif"><img src="images/shim.gif"></td></tr></tbody></table></td></tr></table></body></html>' );
  notificationWin.document.close();
  notificationWin.focus();

}

window.onunload = function() {
 if ( notificationWin != null )
      notificationWin.close();
}

// the uiiNotification object definition
function uiiNotification( type, message ) {
  this.type = type;
  this.message = message;
}

// ------------- end javascript for popup notifications --------------

function randomName() {
  // create a random name, Math.random() generates 16 digit numbers
  var random = Math.random().toString();
  return ( random.substr( random.indexOf( "." ) + 1 ) );
}

// this is where the error notification gets shown
var exportError = new uiiNotification( "ERROR", "<%= eMessage %>" );
popupNotifications( exportError );

// close the main window
window.close();

</script>
</head>
<body>
</body>
</html>
<%
    }
  }
%>
