 
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
<script language="JavaScript" src="scripts/utils.js"></script> 

<style>
.styleDisabled {color:black;background-color:silver}
</style>

<jsp:useBean id="subsDeviceFB" scope="session" class="com.cisco.ettx.admin.gui.web.beans.SubsDevicesFormBean"/>
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="5"  BGCOLOR="E1E1E1">
  <tr> 
    <td width="100%" align="center" valign="center"> <uii:form action="/editSubsDevice.do"> 
        <table width="350" border="0" cellspacing="1" cellpadding="2" bgcolor="#FFFFFF">
          <tr> 
            <td colspan="2" nowrap background="<%=contextPath%>/RSRC/en_US/images/vertlines_dk.gif" bgcolor="#669999"> 
              <uii:img page="/RSRC/en_US/images/shim.gif"/></td>
          </tr>
          <tr> 
            <td noWrap height=22 background="<%=contextPath%>/RSRC/en_US/images/dia_title_bg2.gif" colspan="2" align="center"> 
		<b><i18n:message bundleRef="ettx_bundle" key="modifySubsDeviceTitle"/></b>
		</td>
          </tr>
<%
	if (subsDeviceFB.modify()) {
%>
            <tr> 
              <td nowrap bgcolor="#cecfce" align="right">&nbsp;<i18n:message bundleRef="ettx_bundle" key="macAddress"/>:</td>
              <td width="100%" bgcolor="#cecfce"><uii:text readonly="true" property="curntDevice.macAddress" styleClass="styleDisabled" /> </td>
            </tr>
<%
	}
	else {
%>
            <tr> 
              <td nowrap bgcolor="#cecfce" align="right">&nbsp;<i18n:message bundleRef="ettx_bundle" key="macAddress"/>:</td>
              <td width="100%" bgcolor="#cecfce"><uii:mac property="curntDevice.macAddress" /> </td>
            </tr>
<%
	}
%>
            <tr> 
              <td nowrap bgcolor="#cecfce" align="right">&nbsp;<i18n:message bundleRef="ettx_bundle" key="macAddressAlias"/>:</td>
              <td width="100%" bgcolor="#cecfce"><uii:text property="curntDevice.displayName" /> </td>
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
				onclick="return doButtonCheck();">
				<i18n:message bundleRef="ettx_bundle" key="apply"/>
			</uii:button>
		  </td>
                  <td nowrap> <uii:button submitValue="Cancel" 
			onclick="UIIFormSubmit('submit','Cancel');return false;">
				<i18n:message bundleRef="ettx_bundle" key="cancel"/>
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
                <p> <i18n:message bundleRef="instruction" key="macAddress.edit.p1"/></p>
                  </uii:instructions>
    </td>
  </tr>
</table>

<script>
function doButtonCheck() {
	return true;
}
</script>

