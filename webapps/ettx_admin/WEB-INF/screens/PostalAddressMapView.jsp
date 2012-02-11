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
      <uii:form method="post" action="/viewAddressMap.do" >
        <table border="0" cellspacing="1" cellpadding="2" bgcolor="#FFFFFF">
       <tr> 
          <td  nowrap background="<%=contextPath%>/RSRC/en_US/images/vertlines_dk.gif" bgcolor="#669999"> 
            <uii:img page="/RSRC/en_US/images/shim.gif" /></td>
        </tr>
        <tr> 
          <td  bgcolor="CCCCCC"> 
            	<i18n:message id="postalAddress" bundleRef="ettx_bundle" key="postalAddress"/>
		<i18n:message id="postalState" bundleRef="ettx_bundle" key="postalState"/>
		<i18n:message id="postalCity" bundleRef="ettx_bundle" key="postalCity"/>
		<i18n:message id="postalCode" bundleRef="ettx_bundle" key="postalCode"/>
		<i18n:message id="switchIPAddress" bundleRef="ettx_bundle" key="switchDetails"/>
		<i18n:message id="switchPortName" bundleRef="ettx_bundle" key="switchPortName"/>
		<i18n:message id="portIdentifier" bundleRef="ettx_bundle" key="portIdentifier"/>
            <uii:scrollingTable id="addressMapTable" height="300" width="600" 
		name="addressMapList"  keyColumn="switchIPAddress" tableName="switchPortName" selectionType="none"> 
			<uii:stColumn header="<%=postalAddress%>" property="postalAddress" dataType="text" />
			<uii:stColumn header="<%=postalCity%>" property="postalCity" dataType="text" />
			<uii:stColumn header="<%=postalState%>" property="postalState" dataType="text" />
			<uii:stColumn header="<%=postalCode%>" property="postalCode" dataType="text" />
			<uii:stColumn header="<%=portIdentifier%>" property="portIdentifier" />
			<uii:stColumn header="<%=switchIPAddress%>" property="switchIPAddress" />
			<uii:stColumn header="<%=switchPortName%>" property="switchPortName" />
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
                </uii:instructions>
  </td>
  </tr>
</table>

