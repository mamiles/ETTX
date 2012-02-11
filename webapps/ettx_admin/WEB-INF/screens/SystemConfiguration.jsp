 
<!----------------------------------------------------------------------------------------

One time starting point for Content Area developers, thereafter works independantly.

Use File - UII Extensions - Save As JSP to save this file as a JSP Page and this automagically

generates the HTML file. Upload the .htm file to the Mockup Server.



----------->

<%@ taglib uri="http://jakarta.apache.org/taglibs/i18n" prefix="i18n" %>
<%@ taglib uri="/WEB-INF/tlds/uii-contentarea-taglib.tld" prefix="uii" %>
<i18n:bundle baseName="com.cisco.ettx.admin.i18n.ettx" localeRef="userLocale" id="ettx_bundle" />
<i18n:bundle baseName="com.cisco.ettx.admin.i18n.instruction" localeRef="userLocale" id="instruction" />
<i18n:bundle baseName="com.cisco.ettx.admin.i18n.error" localeRef="userLocale" id="error" />



<%



	String contextPath = request.getContextPath();



%>

<i18n:message id="passwdErrorMsg" bundleRef="ettx_bundle" key="passwdErrorMsg"/>
<i18n:message id="emptyUserIDMsg" bundleRef="ettx_bundle" key="emptyUserIDMsg"/>
<i18n:message id="emptyExportDirMsg" bundleRef="ettx_bundle" key="emptyExportDirMsg"/>
<i18n:message id="emptyLeaseExportDirMsg" bundleRef="ettx_bundle" key="emptyLeaseExportDirMsg"/>
<i18n:message id="invalidCharsMsg" bundleRef="error" key="invalidCharsMsg"/>
<script language="JavaScript" type="text/javascript">
function doButtonCheck(aButtonName) {
	if (aButtonName == "Apply") {
		var speUserID = document.systemConfigFB.elements['systemConfig.speUserID'].value;
		var spePassword = document.systemConfigFB.elements['systemConfig.spePassword'].value;
		var exportDir = document.systemConfigFB.elements['systemConfig.exportDir'].value;
		var leaseExportDir = document.systemConfigFB.elements['systemConfig.leaseHistoryExportDir'].value;
		if (speUserID == "" || spePassword == "") {
			alert('<%=emptyUserIDMsg%>');
			return false;
		}
		if (exportDir == "") {
			alert('<%=emptyExportDirMsg%>');
			return false;
		}
		if (leaseExportDir == "") {
			alert('<%=emptyLeaseExportDirMsg%>');
			return false;
		}
	}
	return true;
}
</script>
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="5"  BGCOLOR="E1E1E1">
  <tr> 
    <td width="100%" align="center" valign="center"> <uii:form action="/systemConfig.do"> 
        <table width="412" border="0" cellspacing="1" cellpadding="2" bgcolor="#FFFFFF">
          <tr> 
            <td colspan="2" nowrap background="<%=contextPath%>/RSRC/en_US/images/vertlines_dk.gif" bgcolor="#669999"> 
              <uii:img page="/RSRC/en_US/images/shim.gif"/></td>
          </tr>
          <tr> 
            <td noWrap height=22 background="<%=contextPath%>/RSRC/en_US/images/dia_title_bg2.gif" colspan="2" align="center"> 
		<b><i18n:message bundleRef="ettx_bundle" key="systemConfigTitle"/></b>
          </tr>
          <tr> 
            <td nowrap bgcolor="#cecfce" align="right" width="39%">
		<i18n:message bundleRef="ettx_bundle" key="speUserID"/>:
	    </td>
            <td width="61%" bgcolor="#cecfce"><uii:text minLen="1" maxLen="11" regExp="/[^a-zA-Z0-9-_]/g" errorDecision="true" errorMessage="<%=invalidCharsMsg%>" property="systemConfig.speUserID" /></td>
          </tr>
          <tr> 
            <td nowrap bgcolor="#cecfce" align="right" width="39%" height="31">
		<i18n:message bundleRef="ettx_bundle" key="spePassword"/>:
	    </td>

            <td width="61%" bgcolor="#cecfce" height="31"><uii:password property="systemConfig.spePassword" /> </td>
          </tr>
          <tr> 
            <td nowrap bgcolor="#cecfce" align="right" width="39%" >
		<i18n:message bundleRef="ettx_bundle" key="eventPurgeDur"/>:
	    </td>
            <td width="61%" bgcolor="#cecfce" ><uii:integer id="" property="systemConfig.purgeDur" minValue="1" maxValue="30"/> 
            </td>
          </tr>
          <tr> 
            <td nowrap bgcolor="#cecfce" align="right" width="39%">
		<i18n:message bundleRef="ettx_bundle" key="exportDir"/>:
	    </td>
            <td width="61%" bgcolor="#cecfce"><uii:text property="systemConfig.exportDir" /> </td>
          </tr>
          </tr>
		<tr> 
            <td nowrap bgcolor="#cecfce" align="right" width="39%">
			<i18n:message bundleRef="ettx_bundle" key="leaseExportSch"/>
               </td>
            <td width="61%" bgcolor="#cecfce">
		<uii:radio property="leaseExportSch" value="on" >
			<i18n:message bundleRef="ettx_bundle" key="on"/>
		</uii:radio>
		<uii:radio property="leaseExportSch" value="off" >
			<i18n:message bundleRef="ettx_bundle" key="off"/>
		</uii:radio>
			</td>
	</tr>
          <tr> 
            <td nowrap bgcolor="#cecfce" align="right" width="39%">
		<i18n:message bundleRef="ettx_bundle" key="leaseExportDay" />
	    </td>
            <td width="61%" bgcolor="#cecfce"><uii:integer property="systemConfig.leaseHistoryExportDay" minValue="1" maxValue="31"/> </td>
          </tr>
          <tr> 
            <td nowrap bgcolor="#cecfce" align="right" width="39%">
		<i18n:message bundleRef="ettx_bundle" key="leaseExportTime" />
	    </td>
            <td width="61%" bgcolor="#cecfce"><uii:time property="systemConfig.leaseHistoryExportTime" format="2"/></td>
          </tr>
          <tr> 
            <td nowrap bgcolor="#cecfce" align="right" width="39%">
		<i18n:message bundleRef="ettx_bundle" key="leaseExportDir"/>:
	    </td>
            <td width="61%" bgcolor="#cecfce"><uii:text property="systemConfig.leaseHistoryExportDir" /> </td>
          </tr>
          </tr>
          <tr bgcolor="#999999"> 
            <td colspan="2" nowrap><uii:img page="/RSRC/en_US/images/shim.gif" /></td>
          </tr>
          <tr> 
            <td colspan="2" bgcolor="silver"> 
              <table cellspacing=3 cellpadding=0 border=0 width="100%">
                <tr> 
                  <td width="100%"><uii:img page="/RSRC/en_US/images/shim.gif" width="20" height="5"/></td>
                  <td nowrap> <uii:button submitValue="Apply" 
				onclick="return doButtonCheck('Apply');">
					<i18n:message bundleRef="ettx_bundle" key="apply"/>
				</uii:button>
		   </td>
                  <td nowrap> <uii:button submitValue="Clear" 
				onclick="UIIFormSubmit('action','Clear');return false;">
					<i18n:message bundleRef="ettx_bundle" key="reset"/>
				</uii:button>
		  </td>
                </tr>
              </table>
            </td>
          </tr>
          <tr> 
            <td colspan="2" nowrap bgcolor="#cecfce" background="<%=contextPath%>/RSRC/en_US/images/vertlines_dk.gif"> 
              <uii:img page="/RSRC/en_US/images/shim.gif"/></td>
          </tr>
        </table>
        <p>&nbsp;</p></uii:form>
    </td>
    <td valign="top" align="right"> <uii:instructions>
                <p> <i18n:message bundleRef="instruction" key="config.system.p1"/></p>
                <p> <i18n:message bundleRef="instruction" key="config.system.p2"/></p>
                  </uii:instructions>
    </td>
  </tr>
</table>
