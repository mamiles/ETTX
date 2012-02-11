 
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
<i18n:message id="blankTimeMsg" bundleRef="ettx_bundle" key="BlankTimeMsg" debug="true"/>

<%



	String contextPath = request.getContextPath();



%>

<script language="JavaScript" type="text/javascript">
function doButtonCheck() {
	var startTime = document.leaseHistoryExportFB.startTime.value;
	var endTime = document.leaseHistoryExportFB.endTime.value;

	if (startTime == "" ||
	    endTime == "") {
		var msg = new uiiNotification ("ERROR", '<%= blankTimeMsg %>');
		popupNotifications (msg);
		return false;
	}

	return true;
}
</script>

<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="5"  BGCOLOR="E1E1E1">
  <tr> 
    <td width="100%" align="center" valign="center"> <uii:form action="/leaseHistoryExport.do"> 
        <table width="350" border="0" cellspacing="1" cellpadding="2" bgcolor="#FFFFFF">
          <tr> 
            <td colspan="2" nowrap background="<%=contextPath%>/RSRC/en_US/images/vertlines_dk.gif" bgcolor="#669999"> 
              <uii:img page="/RSRC/en_US/images/shim.gif"/></td>
          </tr>
          <tr> 
            <td noWrap height=22 background="<%=contextPath%>/RSRC/en_US/images/dia_title_bg2.gif" colspan="2" align="center"> 
		<b><i18n:message bundleRef="ettx_bundle" key="leaseHistoryTitle"/></b>
		</td>
          </tr>
            <tr> 
              <td nowrap bgcolor="#cecfce" align="right" height="21"><i18n:message bundleRef="ettx_bundle" key="cnrHostList"/>:</td>
              <td width="100%" bgcolor="#cecfce" height="21" > <uii:select property="cnrHost"><uii:options property="cnrHostList"/></uii:select></td>
            </tr>
          <tr> 
            <td nowrap bgcolor="#cecfce" align="right">
		<i18n:message bundleRef="ettx_bundle" key="leaseStartDate"/>:
	    </td>
            <td width="100%" bgcolor="#cecfce">
            		<uii:date id="startDate"> 
            			<uii:day property="startDay"/> 
              			<uii:month property="startMonth"/> 
              			<uii:year property="startYear"/>
              		</uii:date> / <uii:time id="startTime" property="startTime" allowBlankValue="true" format="24" />
            </td>
          </tr>
          <tr> 
            <td nowrap bgcolor="#cecfce" align="right">
		<i18n:message bundleRef="ettx_bundle" key="leaseEndDate"/>:
	    </td>
            <td width="100%" bgcolor="#cecfce">
            		<uii:date id=""> 
            			<uii:day property="endDay"/> 
              			<uii:month property="endMonth"/> 
              			<uii:year property="endYear"/>
              		</uii:date> / <uii:time id="endTime" property="endTime" allowBlankValue="true" format="24" /> 
            </td>
          </tr>
            <tr> 
              <td nowrap bgcolor="#cecfce" align="right">&nbsp;<i18n:message bundleRef="ettx_bundle" key="exportFileName"/>:</td>
              <td width="100%" bgcolor="#cecfce"><uii:text property="exportFileName" /> </td>
            </tr>
          <tr bgcolor="#999999"> 
            <td colspan="2" nowrap><uii:img page="/RSRC/en_US/images/shim.gif" /></td>
          </tr>
          <tr> 
            <td colspan="2" bgcolor="silver"> 
              <table cellspacing=3 cellpadding=0 border=0 width="100%">
                <tr> 
                  <td width="100%"><uii:img page="/RSRC/en_US/images/shim.gif" width="20" height="5"/></td>
                  <td nowrap> <uii:button value="Export" property="action"
				onclick="return doButtonCheck();">
				<i18n:message bundleRef="ettx_bundle" key="export"/>
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
                <p> <i18n:message bundleRef="instruction" key="reports.leasehistory.archive.p1"/></p>
                <p> <i18n:message bundleRef="instruction" key="reports.leasehistory.archive.p2"/></p>
                  </uii:instructions>
    </td>
  </tr>
</table>
