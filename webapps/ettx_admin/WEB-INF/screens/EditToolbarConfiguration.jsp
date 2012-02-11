 
<!----------------------------------------------------------------------------------------

One time starting point for Content Area developers, thereafter works independantly.

Use File - UII Extensions - Save As JSP to save this file as a JSP Page and this automagically

generates the HTML file. Upload the .htm file to the Mockup Server.



----------->
<!---- WARNING: Uncomment this in the JSP file to make it work properly. -----------------
--->

<%@ taglib uri="/WEB-INF/tlds/uii-contentarea-taglib.tld" prefix="uii" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/i18n" prefix="i18n" %>
<i18n:bundle baseName="com.cisco.ettx.admin.i18n.ettx" localeRef="userLocale" id="ettx_bundle" />
<i18n:bundle baseName="com.cisco.ettx.admin.i18n.instruction" localeRef="userLocale" id="instruction" />
<i18n:bundle baseName="com.cisco.ettx.admin.i18n.error" localeRef="userLocale" id="error" />



<%



	String contextPath = request.getContextPath();



%>

<i18n:message id="urlDisplayName" bundleRef="ettx_bundle" key="urlDisplayName"/>
<i18n:message id="urlLink" bundleRef="ettx_bundle" key="urlLink"/>
<i18n:message id="invalidURLMsg" bundleRef="error" key="invalidURLMsg"/>
<script language="JavaScript" type="text/javascript">
function doButtonCheck(aButtonName) {
	if (aButtonName == "OK") {
		var urlLink = document.toolbarFB.urlLink.value;
		if (!urlLink.startsWith("http://")) {
			alert("<%=invalidURLMsg%>");
			return false;
		}
	}
	return true;
}
</script>
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="5"  BGCOLOR="E1E1E1">
  <tr> 
    <td width="100%" align="center" valign="center"> <uii:form action="/editToolbar.do"> 
        <table width="350" border="0" cellspacing="1" cellpadding="2" bgcolor="#FFFFFF">
          <tr> 
            <td colspan="2" nowrap background="<%=contextPath%>/RSRC/en_US/images/vertlines_dk.gif" bgcolor="#669999"> 
              <uii:img page="/RSRC/en_US/images/shim.gif"/></td>
          </tr>
          <tr> 
            <td noWrap height=22 background="<%=contextPath%>/RSRC/en_US/images/dia_title_bg2.gif" colspan="2" align="center"> 
		<b><i18n:message bundleRef="ettx_bundle" key="urlEditTitle"/></b>
		</td>
          </tr>
          <tr> 
            <td nowrap bgcolor="#cecfce" align="right" width="39%">
				<i18n:message bundleRef="ettx_bundle" key="urlDisplayName"/>
		</td>
            <td width="61%" bgcolor="#cecfce"><uii:text minLen="1" maxLen="40" property="displayName" /></td>
          </tr>
          <tr> 
            <td nowrap bgcolor="#cecfce" align="right" width="39%">
			<i18n:message bundleRef="ettx_bundle" key="urlLink"/>
              </td>
            <td width="61%" bgcolor="#cecfce"><uii:text minLen="1" property="urlLink" /> </td>
          </tr>
          <tr bgcolor="#999999"> 
            <td colspan="2" nowrap><uii:img page="/RSRC/en_US/images/shim.gif" /></td>
          </tr>
          <tr> 
            <td colspan="2" bgcolor="silver"> 
              <table cellspacing=3 cellpadding=0 border=0 width="100%">
                <tr> 
                  <td width="100%"><uii:img page="/RSRC/en_US/images/shim.gif" width="20" height="5"/></td>
                  <td nowrap> <uii:button property="action" value="OK" 
				onclick="return doButtonCheck('OK');">
				<b><i18n:message bundleRef="ettx_bundle" key="ok"/></b>
				</uii:button>
		  </td>
                  <td nowrap> <uii:button property="action" value="Cancel" 
				onclick="UIIFormSubmit('action','Cancel');return false;">
				<b><i18n:message bundleRef="ettx_bundle" key="cancel"/></b>
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
        </uii:form>
    </td>
    <td valign="top" align="right"> <uii:instructions>
                <p> <i18n:message bundleRef="instruction" key="edit.toolbar.p1"/></p>
                <p> <i18n:message bundleRef="instruction" key="edit.toolbar.p2"/></p>
                  </uii:instructions>
    </td>
  </tr>
</table>
