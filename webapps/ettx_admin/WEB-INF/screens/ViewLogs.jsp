
<!----------------------------------------------------------------------------------------
One time starting point for Content Area developers, thereafter works independantly.
Use File - UII Extensions - Save As JSP to save this file as a JSP Page and this automagically
generates the HTML file. Upload the .htm file to the Mockup Server.

----------->



<%@ taglib uri="/WEB-INF/tlds/uii-contentarea-taglib.tld" prefix="uii" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/i18n" prefix="i18n" %>
<i18n:bundle baseName="com.cisco.ettx.admin.i18n.ettx" localeRef="userLocale" id="ettx_bundle" />
<i18n:bundle baseName="com.cisco.ettx.admin.i18n.instruction" localeRef="userLocale" id="instruction" />

<%

	String contextPath = request.getContextPath();

%>




<table border="0" cellspacing="0" BGCOLOR="E1E1E1" align="center">
    
<tr> 
      
    
<td width="100%"> 
	<uii:form method="post" action="/viewLogs.do" >
<table border="0" cellspacing="1" bgcolor="#FFFFFF" align="center">
        
<tr> 
          
<td nowrap background="<%=contextPath%>/RSRC/en_US/images/vertlines_dk.gif" bgcolor="#669999">
		<uii:img page="/RSRC/en_US/images/shim.gif" /></td>
</tr>
        

<tr> <td  bgcolor="CCCCCC">

<i18n:message id="componentNameHdr" bundleRef="ettx_bundle" key="viewLogsComponentName"/>
<i18n:message id="hostNameHdr" bundleRef="ettx_bundle" key="viewLogsHostName"/>
<i18n:message id="logPathHdr" bundleRef="ettx_bundle" key="viewLogsLogPath"/>
<i18n:message id="logFileNameHdr" bundleRef="ettx_bundle" key="viewLogsLogFileName"/>
<i18n:message id="logSizeHdr" bundleRef="ettx_bundle" key="viewLogsLogSize"/>
<i18n:message id="dateTimeHdr" bundleRef="ettx_bundle" key="viewLogsDateTime"/>

<uii:scrollingTable id="applLogTable" keyColumn="Log File Name" height="250" width="700" 
tableName="AppLog" name="applLogList"  selectionType="none" >
	<uii:stColumn header="<%=componentNameHdr%>" property="labelName" dataType="text" width="110" /> 
	<uii:stColumn header="<%=hostNameHdr%>" property="hostName" dataType="text" width="90" /> 
	<uii:stColumn header="<%=logPathHdr%>" property="logPath" dataType="text" width="200" /> 
	<uii:stColumn header="<%=logFileNameHdr%>" property="logName" dataType="text" width="133" />
	<uii:stColumn header="<%=logSizeHdr%>" property="logSize" dataType="text" width="80" />
	<uii:stColumn header="<%=dateTimeHdr%>" property="dateTime" dataType="text" width="110" /> 
</uii:scrollingTable>



</td></tr>






    
      
</tr>
        
<tr> 
          
<td nowrap background="<%=contextPath%>/RSRC/en_US/images/vertlines_dk.gif"><uii:img page="/RSRC/en_US/images/shim.gif" /></td>
</tr>
    
</table>







	</td>
<td valign="top">

	
<TABLE WIDTH="100%" BORDER="0" CELLSPACING="0" CELLPADDING="0" ALIGN="center">
	    
<tr> 
	      
<td> 
	        
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	          
<tr> 
	            
<td><uii:img page="/RSRC/en_US/images/snv_left_04.gif" /></td>
<TD WIDTH="100%" NOWRAP background="<%=contextPath%>/RSRC/en_US/images/snv_bg_04.gif"><b><FONT COLOR="White">&nbsp;Instructions</FONT></b>
	            </TD>
<td><uii:img page="/RSRC/en_US/images/snv_right_04.gif" /></td>
</tr>
	        
</table>
</td>
</tr>
        
<tr> 
          
<td bgcolor="FFFFCC"> 
            
<table width="100%" border="0" cellspacing="0" cellpadding="3">
              
<tr> 
                
<td>
                  
<p> <i18n:message bundleRef="instruction" key="troubleshoot.log.list.p1"/></p>
</td>
</tr>


            
</table>
</td>
</tr>
       
<tr> 
          
<td> 
            
<table width="100%" border="0" cellspacing="0" cellpadding="0">
              
<tr> 
                
<td><uii:img page="/RSRC/en_US/images/snv_left_btm_04.gif"/></td>
<td width="100%" background="<%=contextPath%>/RSRC/en_US/images/snv_bg_btm_04.gif">
<uii:img page="/RSRC/en_US/images/shim.gif"/></td>
<td><uii:img page="/RSRC/en_US/images/snv_right_btm_04.gif"/></td>
</tr>
           
</table>

</td>
</tr>
</table>
</td>
</tr>


<script language="JavaScript" type="text/javascript">
function doGetLogFile(indexValue)
{
	popupapplLogListFBSubmit('display',indexValue,"1000","800","resizable=yes,scrollbars=yes,menubar=yes,toolbar=yes");
}
</script>
</uii:form>


      
</table>
