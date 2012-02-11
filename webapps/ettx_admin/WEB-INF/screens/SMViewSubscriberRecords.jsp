<%@ taglib uri="/WEB-INF/tlds/uii-contentarea-taglib.tld" prefix="uii" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/i18n" prefix="i18n" %>
<i18n:bundle baseName="com.cisco.ettx.admin.i18n.ettx" localeRef="userLocale" id="ettx_bundle" />
<i18n:bundle baseName="com.cisco.ettx.admin.i18n.instruction" localeRef="userLocale" id="instruction" />

<%
    String contextPath = request.getContextPath();
%>


<script language="JavaScript" src="scripts/utils.js"></script> 

<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="5" BGCOLOR="E1E1E1">
  <tr>
    <td width="100%" align="center" valign="center">
      <uii:form method="post" action="/smViewSubs.do" >
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
            <uii:scrollingTable id="subsTable" keyColumn="loginID" height="300" width="600" 
		tableName="selectedSubsFDN" name="subscriberList"  selectionType="single"> 
			<uii:stColumn header="subscriberID" property="subscriberID" dataType="text" hide="true"/>
			<uii:stColumn header="<%=loginID%>" property="loginID" dataType="text" />
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
            <td colspan="8" nowrap bgcolor="#999999">
              <uii:img page="/RSRC/en_US/images/shim.gif"/></td>
          </tr>
          <tr bgcolor="silver"> 
            <td colspan="8" nowrap> 
              <table cellspacing=3 cellpadding=0 border=0 width="100%">
                <tr> 
                  <td nowrap width="22">
                    <uii:img page="/RSRC/en_US/images/shim.gif" width="22" height="5"/></td>
                    <td>
                      <uii:button submitValue="Modify Profile"
				popupWidth="1000" popupHeight="800" popupFeatures="resizable=yes,scrollbars=yes" 
                                  onclick="return doButtonCheck('ModifyProfile');" >
				  <i18n:message bundleRef="ettx_bundle" key="modifyProfileButton"/>
				</uii:button>
                    </td>

                    <td>
                      <uii:button submitValue="Modify Service"
				  popupWidth="1000" popupHeight="800" popupFeatures="resizable=yes,scrollbars=yes" 
                                  onclick="return doButtonCheck('Modify Service');" >
				  <i18n:message bundleRef="ettx_bundle" key="modifyServiceButton"/>
				</uii:button>
                    </td>
                    <td>
                      <uii:button submitValue="View MAC Addresses"
				popupWidth="1000" popupHeight="800" popupFeatures="resizable=yes,scrollbars=yes" 
                                  onclick="return doButtonCheck('View MAC Addresses');">
				  <i18n:message bundleRef="ettx_bundle" key="viewMACAddressButton"/>
				</uii:button>
                    </td>
                    <td>
                      <uii:button submitValue="Delete"
                                  onclick="return doButtonCheck('Delete');" >
				  <i18n:message bundleRef="ettx_bundle" key="delete"/>
				</uii:button>
                    </td>
                    <td>
                      <uii:button submitValue="Refresh">
				  <i18n:message bundleRef="ettx_bundle" key="refresh"/>
				</uii:button>
                    </td>
                  <td > 
			<uii:button submitValue="Close" 
				onclick="javascript: window.close(); return false;" > 
				<i18n:message bundleRef="ettx_bundle" key="close"/>
			      </uii:button>
		  </td>
                </tr>
              </table>
            </td>
          <tr> 
            <td colspan="8" nowrap background="<%=contextPath%>/RSRC/en_US/images/vertlines_dk.gif">
              <uii:img page="/RSRC/en_US/images/shim.gif" /></td>
          </tr>
        </table>
      </uii:form>
  </td>
  <td valign="TOP" align="right"> <uii:instructions>
                <p> <i18n:message bundleRef="instruction" key="subscriber.manage.p1"/></p>
                </uii:instructions>
  </td>
  </tr>
</table>

<script>
<i18n:message id="selectErrorMsg" bundleRef="ettx_bundle" key="selectSubsErrorMsg"/>
<i18n:message id="confirmSubsDelete" bundleRef="ettx_bundle" key="confirmSubsDelete"/>
function doButtonCheck(aButtonName) {
	var subs = document.subscriberListFB.selectedSubsFDN.value;
	if (subs == "") {
		alert("<%=selectErrorMsg%>");
		return false;
	}
	if (aButtonName == 'Delete') {
		return(confirm("<%=confirmSubsDelete%>"));
		
	}
	return true;
}
</script>
