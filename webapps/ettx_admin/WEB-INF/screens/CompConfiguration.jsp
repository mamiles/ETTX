 
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
<i18n:message id="selectErrorMsg" bundleRef="ettx_bundle" key="selectErrorMsg"/>
<script language="JavaScript" type="text/javascript">
function doButtonCheck(aButtonName) {
	if (aButtonName == "Edit" || aButtonName == "Delete") {
		var selectedHostName = document.componentConfigFB.selectedHostName.value;
		if (selectedHostName == "") {
			alert("<%=selectErrorMsg%>");
			return false;
		}
	}
	return true;
}
</script>
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="5"  BGCOLOR="E1E1E1"><tr>
  <td width="100%" align="center" valign="center"> <uii:form action="/componentConfig.do" > 
      <table border="0" cellspacing="1" cellpadding="2" bgcolor="#FFFFFF">
        <tr> 
          <td  nowrap background="<%=contextPath%>/RSRC/en_US/images/vertlines_dk.gif" bgcolor="#669999"> 
            <uii:img page="/RSRC/en_US/images/shim.gif" /></td>
        </tr>
        <tr> 
          <td  bgcolor="CCCCCC"> 
		<i18n:message id="hostName" bundleRef="ettx_bundle" key="hostName"/>
		<i18n:message id="unixLoginID" bundleRef="ettx_bundle" key="unixLoginID"/>
		<i18n:message id="useSecureShell" bundleRef="ettx_bundle" key="useSecureShell"/>
            <uii:scrollingTable id="currentComponent" keyColumn="<%=hostName%>" height="200" width="500" 
		tableName="selectedHostName" name="componentHosts"  selectionType="single"> 
			<uii:stColumn header="<%=hostName%>" property="hostName" />
			<uii:stColumn header="<%=unixLoginID%>" property="unixLoginID" />
			<uii:stColumn header="<%=useSecureShell%>" property="useSecureShell" />
	</uii:scrollingTable>
            
          </td>
        </tr>
        <tr> 
          <td  nowrap bgcolor="#999999"><uii:img page="/RSRC/en_US/images/shim.gif"/></td>
        </tr><tr bgcolor="silver">
        <td  > 
          <table cellspacing=3 cellpadding=0 border=0 width="100%"><tr>
            <td align="right"> 
              <table></tr>
                <td > <uii:button property="action" value="Add" 
				onclick="return doButtonCheck('Add');">
				<i18n:message bundleRef="ettx_bundle" key="add"/>
			</uii:button>
		</td>
                  <td nowrap> <uii:button property="action" value="Edit" 
				onclick="return doButtonCheck('Edit');">
					<i18n:message bundleRef="ettx_bundle" key="Edit"/>
				</uii:button>
		   </td>
                <td > <uii:button property="action" value="Delete" 
				onclick="return doButtonCheck('Delete');">
				<i18n:message bundleRef="ettx_bundle" key="delete"/>
			</uii:button>
		</td>
<!--
                <td > <uii:button property="action" value="Save" 
				onclick="return doButtonCheck('Save');">
				<i18n:message bundleRef="ettx_bundle" key="save"/>
			</uii:button>
		</td>
                <td > <uii:button property="action" value="Clear" 
				onclick="return doButtonCheck('Clear');">
				<i18n:message bundleRef="ettx_bundle" key="clear"/>
			</uii:button>
		</td>
-->
                </tr>
              </table>
            </td></tr>
          </table>
        </td></tr>
        <tr> 
          <td  nowrap background="<%=contextPath%>/RSRC/en_US/images/vertlines_dk.gif"><uii:img page="/RSRC/en_US/images/shim.gif" /></td>
        </tr>
      </table>
      </uii:form>
  </td>
  <td valign="TOP" align="right"> <uii:instructions>
                <p> <i18n:message bundleRef="instruction" key="config.comp.p1"/></p>
                <p> <i18n:message bundleRef="instruction" key="config.comp.p2"/></p>
                </uii:instructions>
  </td>
  </tr>
</table>
