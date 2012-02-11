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
      <uii:form method="post" action="/viewSubsDevices.do" >
        <table border="0" cellspacing="1" cellpadding="2" bgcolor="#FFFFFF">
       <tr> 
          <td  nowrap background="<%=contextPath%>/RSRC/en_US/images/vertlines_dk.gif" bgcolor="#669999"> 
            <uii:img page="/RSRC/en_US/images/shim.gif" /></td>
        </tr>
<style>
.styleDisabled {color:black;background-color:silver}
</style>
          <tr> 
            <td nowrap width="50" bgcolor="#cecfce" align="right">
		<i18n:message bundleRef="ettx_bundle" key="subscriberRecordSubscriberName"/>
	    </td>
            <td width="250" bgcolor="#cecfce"><uii:text property="subscriberName" readonly="true" styleClass="styleDisabled"/></td>
            <td nowrap width="50" bgcolor="#cecfce" align="right">
		<i18n:message bundleRef="ettx_bundle" key="subscriberRecordLoginID"/>
	    </td>
            <td width="250" bgcolor="#cecfce"><uii:text property="subscriberID" readonly="true" styleClass="styleDisabled"/></td>
          </tr>
        <tr> 
          <td  colspan="4" bgcolor="CCCCCC"> 
		<i18n:message id="macAddress" bundleRef="ettx_bundle" key="macAddress"/>
		<i18n:message id="macAddressAlias" bundleRef="ettx_bundle" key="macAddressAlias"/>
		<i18n:message id="activeStatus" bundleRef="ettx_bundle" key="activeStatus"/>
            <uii:scrollingTable id="macTable" keyColumn="<%=macAddress%>" height="300" width="600" 
		tableName="selectedSubsDevice" name="subsDevices"  selectionType="single"> 
			<uii:stColumn header="<%=macAddress%>" property="macAddress" dataType="text" />
			<uii:stColumn header="<%=macAddressAlias%>" property="displayName" dataType="text" />
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
                      <uii:button submitValue="Add"
				popupWidth="1000" popupHeight="800" popupFeatures="resizable=yes,scrollbars=yes" >
				  <i18n:message bundleRef="ettx_bundle" key="add"/>
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
<i18n:message id="selectMacAddrErrorMsg" bundleRef="ettx_bundle" key="selectMacAddrErrorMsg"/>
<i18n:message id="confirmMacAddrDelete" bundleRef="ettx_bundle" key="confirmMacAddrDelete"/>
function doButtonCheck(aButtonName) {
	var addr = document.subsDeviceFB.selectedSubsDevice.value;
	if (addr == "") {
		alert("<%=selectMacAddrErrorMsg%>");
		return false;
	}
	if (aButtonName == 'Delete') {
		return(confirm("<%=confirmMacAddrDelete%>"));
		
	}
	return true;
}
</script>
