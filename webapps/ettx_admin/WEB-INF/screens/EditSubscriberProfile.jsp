 
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

<i18n:message id="invalidPhoneCharsMsg" bundleRef="error" key="invalidPhoneCharsMsg"/>
<i18n:message id="passwordLengthMsg" bundleRef="error" key="passwordLengthErrorMsg"/>
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="5"  BGCOLOR="E1E1E1">
  <tr> 
    <td width="100%" align="center" valign="center"> <uii:form action="/editSubsProfile.do"> 
        <table width="350" border="0" cellspacing="1" cellpadding="2" bgcolor="#FFFFFF">
          <tr> 
            <td colspan="2" nowrap background="<%=contextPath%>/RSRC/en_US/images/vertlines_dk.gif" bgcolor="#669999"> 
              <uii:img page="/RSRC/en_US/images/shim.gif"/></td>
          </tr>
          <tr> 
            <td noWrap height=22 background="<%=contextPath%>/RSRC/en_US/images/dia_title_bg2.gif" colspan="2" align="center"> 
		<b><i18n:message bundleRef="ettx_bundle" key="modifySubscriberTitle"/></b>
		</td>
          </tr>
            <tr> 
              <td nowrap bgcolor="#cecfce" align="right">&nbsp;<i18n:message bundleRef="ettx_bundle" key="subscriberRecordLoginID"/>:</td>
              <td width="100%" bgcolor="#cecfce"><uii:text readonly="true" property="subscriberRecord.loginID" styleClass="styleDisabled" /> </td>
            </tr>
            <tr> 
              <td nowrap bgcolor="#cecfce" align="right">&nbsp;<i18n:message bundleRef="ettx_bundle" key="subscriberRecordFamilyName"/>:</td>
              <td width="100%" bgcolor="#cecfce"><uii:text property="subscriberRecord.subscriberLastName" /> </td>
            </tr>
            <tr> 
              <td nowrap bgcolor="#cecfce" align="right">&nbsp;<i18n:message bundleRef="ettx_bundle" key="subscriberRecordGivenName"/>:</td>
              <td width="100%" bgcolor="#cecfce"><uii:text property="subscriberRecord.subscriberFirstName" /> </td>
            </tr>
			<tr> 
              <td nowrap bgcolor="#cecfce" align="right"><i18n:message bundleRef="ettx_bundle" key="subscriberRecordPostalAddress"/>:</td>
              <td width="100%" bgcolor="#cecfce"><uii:text property="subscriberRecord.postalAddress" /> </td>
            </tr>
			<tr> 
              <td nowrap bgcolor="#cecfce" align="right"><i18n:message bundleRef="ettx_bundle" key="subscriberRecordPostalCity"/>:</td>
              <td width="100%" bgcolor="#cecfce"><uii:text property="subscriberRecord.postalCity" /> </td>
            </tr>
			<tr> 
              <td nowrap bgcolor="#cecfce" align="right"><i18n:message bundleRef="ettx_bundle" key="subscriberRecordPostalState"/>:</td>
              <td width="100%" bgcolor="#cecfce"><uii:text property="subscriberRecord.postalState" /> </td>
            </tr>
			<tr> 
              <td nowrap bgcolor="#cecfce" align="right"><i18n:message bundleRef="ettx_bundle" key="subscriberRecordCountryCode"/>:</td>
              <td width="100%" bgcolor="#cecfce"><uii:text property="subscriberRecord.countryCode" /> </td>
            </tr>
			<tr> 
              <td nowrap bgcolor="#cecfce" align="right"><i18n:message bundleRef="ettx_bundle" key="subscriberRecordPostalCode"/>:</td>
              <td width="100%" bgcolor="#cecfce"><uii:text property="subscriberRecord.postalCode" /> </td>
            </tr>

            <tr> 
              <td nowrap bgcolor="#cecfce" align="right"><i18n:message bundleRef="ettx_bundle" key="subscriberRecordHomeNumber"/>:</td>
              <td width="100%" bgcolor="#cecfce"><uii:text property="subscriberRecord.homeNumber"  regExp="/[^0-9 ]/g" errorDecision="true" errorMessage="<%=invalidPhoneCharsMsg%>" /> </td>
            </tr>
            <tr> 
              <td nowrap bgcolor="#cecfce" align="right"><i18n:message bundleRef="ettx_bundle" key="subscriberRecordMobileNumber"/>:</td>
              <td width="100%" bgcolor="#cecfce"><uii:text property="subscriberRecord.mobileNumber" regExp="/[^0-9 ]/g" errorDecision="true" errorMessage="<%=invalidPhoneCharsMsg%>" /> </td>
            </tr>

            <tr> 
              <td nowrap bgcolor="#cecfce" align="right">&nbsp;<i18n:message bundleRef="ettx_bundle" key="subscriberRecordDisplayName"/>:</td>
              <td width="100%" bgcolor="#cecfce"><uii:text property="subscriberRecord.subscriberName" /> </td>
            </tr>
            <tr> 
              <td nowrap bgcolor="#cecfce" align="right">&nbsp;<i18n:message bundleRef="ettx_bundle" key="subscriberRecordPassword"/>:</td>
              <td width="100%" bgcolor="#cecfce"><uii:text minLen="6" errorDecision="true" errorMessage="<%=passwordLengthMsg%>" property="subscriberRecord.password" /> </td>
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
                <p> <i18n:message bundleRef="instruction" key="subscriber.edit.p1"/></p>
                  </uii:instructions>
    </td>
  </tr>
</table>

<script>
function doButtonCheck() {
	var password = document.subscriberInfoFB.elements['subscriberRecord.password'].value;
	if (password.length < 6) {
		alert("<%=passwordLengthMsg%>");
		return false;
	}
	return true;
}
</script>

