 
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

<%

	String contextPath = request.getContextPath();

%>
<script language="javascript" >
function invokeTroubleshoot(fdn) {
	popupsubscriberListFBSubmit("action",fdn,"800","600","resizable=yes, scrollbars=yes");
}
</script>
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="5"  BGCOLOR="E1E1E1"><tr>
  <td width="100%" align="center" valign="center"> <uii:form action="/tsSubsRecords.do" > 
      <table border="0" cellspacing="1" cellpadding="2" bgcolor="#FFFFFF">
        <tr> 
          <td  nowrap background="<%=contextPath%>/RSRC/en_US/images/vertlines_dk.gif" bgcolor="#669999"> 
            <uii:img page="/RSRC/en_US/images/shim.gif" /></td>
        </tr>
        <tr> 
          <td  bgcolor="CCCCCC"> 
		<i18n:message id="subsName" bundleRef="ettx_bundle" key="subscriberRecordSubscriberName"/>
		<i18n:message id="postalAddress" bundleRef="ettx_bundle" key="subscriberRecordPostalAddress"/>
		<i18n:message id="postalState" bundleRef="ettx_bundle" key="subscriberRecordPostalState"/>
		<i18n:message id="postalCity" bundleRef="ettx_bundle" key="subscriberRecordPostalCity"/>
		<i18n:message id="accountNumber" bundleRef="ettx_bundle" key="subscriberRecordAccountNumber"/>
		<i18n:message id="homeNumber" bundleRef="ettx_bundle" key="subscriberRecordHomeNumber"/>
		<i18n:message id="mobileNumber" bundleRef="ettx_bundle" key="subscriberRecordMobileNumber"/>
		<i18n:message id="loginID" bundleRef="ettx_bundle" key="subscriberRecordLoginID"/>
		<i18n:message id="subscribedServices" bundleRef="ettx_bundle" key="subscriberRecordSubscribedServices"/>
            <uii:scrollingTable id="subsTable" keyColumn="subscriberID" height="300" width="600" 
		tableName="selectedSubsFDN" name="subscriberList"  selectionType="single"> 
			<uii:stColumn header="subscriberID" property="subscriberID" hide="true" />
			<uii:stColumn header="<%=loginID%>" property="loginIDLink" dataType="text" />
			<uii:stColumn header="<%=subsName%>" property="subscriberFullName" dataType="text" />
			<uii:stColumn header="<%=postalAddress%>" property="postalAddress" width="100" dataType="text" />
			<uii:stColumn header="<%=postalCity%>" property="postalCity" width="100" dataType="text" />
			<uii:stColumn header="<%=postalState%>" property="postalState" width="100" dataType="text" />
			<uii:stColumn header="<%=homeNumber%>" property="homeNumber" width="100" dataType="number" />
			<uii:stColumn header="<%=mobileNumber%>" property="mobileNumber" width="100" dataType="number" />
			<uii:stColumn header="<%=accountNumber%>" property="accountNumber" dataType="number" />
			<uii:stColumn header="<%=subscribedServices%>" property="subscriberServicesCell" width="100" dataType="text" />
	</uii:scrollingTable>
            
          </td>
        </tr>
        <tr> 
          <td  nowrap bgcolor="#999999"><uii:img page="/RSRC/en_US/images/shim.gif"/></td>
        </tr>
        <tr> 
          <td  nowrap background="<%=contextPath%>/RSRC/en_US/images/vertlines_dk.gif"><uii:img page="/RSRC/en_US/images/shim.gif" /></td>
        </tr>
      </table>
      </uii:form>
  </td>
  <td valign="TOP" align="right"> <uii:instructions>
                <p> <i18n:message bundleRef="instruction" key="troubleshoot.list.p1"/></p>
                </uii:instructions>
  </td>
  </tr>
</table>
