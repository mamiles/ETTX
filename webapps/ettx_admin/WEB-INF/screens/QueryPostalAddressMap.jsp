 
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


<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="5"  BGCOLOR="E1E1E1">
  <tr> 
    <td width="100%" align="center" valign="center"> <uii:form action="/queryAddressMap.do"> 
        <table width="350" border="0" cellspacing="1" cellpadding="2" bgcolor="#FFFFFF">
          <tr> 
            <td colspan="2" nowrap background="<%=contextPath%>/RSRC/en_US/images/vertlines_dk.gif" bgcolor="#669999"> 
              <uii:img page="/RSRC/en_US/images/shim.gif"/></td>
          </tr>
          <tr> 
            <td noWrap height=22 background="<%=contextPath%>/RSRC/en_US/images/dia_title_bg2.gif" colspan="2" align="center"> 
		<b><i18n:message bundleRef="ettx_bundle" key="postalAddressQueryTitle"/></b>
		</td>
          </tr>
			<tr> 
              <td nowrap bgcolor="#cecfce" align="right"><i18n:message bundleRef="ettx_bundle" key="postalAddress"/>:</td>
              <td width="100%" bgcolor="#cecfce"><uii:text property="postalAddress" /> </td>
            </tr>
			<tr> 
              <td nowrap bgcolor="#cecfce" align="right"><i18n:message bundleRef="ettx_bundle" key="postalCity"/>:</td>
              <td width="100%" bgcolor="#cecfce"><uii:text property="postalCity" /> </td>
            </tr>
			<tr> 
              <td nowrap bgcolor="#cecfce" align="right"><i18n:message bundleRef="ettx_bundle" key="postalState"/>:</td>
              <td width="100%" bgcolor="#cecfce"><uii:text property="postalState" /> </td>
            </tr>
			<tr> 
              <td nowrap bgcolor="#cecfce" align="right"><i18n:message bundleRef="ettx_bundle" key="postalCode"/>:</td>
              <td width="100%" bgcolor="#cecfce"><uii:text property="postalCode" /> </td>
            </tr>
			<tr> 
              <td nowrap bgcolor="#cecfce" align="right"><i18n:message bundleRef="ettx_bundle" key="portIdentifier"/>:</td>
              <td width="100%" bgcolor="#cecfce"><uii:text property="portIdentifier" /> </td>
            </tr>

          <tr bgcolor="#999999"> 
            <td colspan="2" nowrap><uii:img page="/RSRC/en_US/images/shim.gif" /></td>
          </tr>
          <tr> 
            <td colspan="2" bgcolor="silver"> 
              <table cellspacing=3 cellpadding=0 border=0 width="100%">
                <tr> 
                  <td width="100%"><uii:img page="/RSRC/en_US/images/shim.gif" width="20" height="5"/></td>
                  <td nowrap> <uii:button submitValue="Submit"
				popupWidth="1000" popupHeight="800" popupFeatures="resizable=yes,scrollbars=yes" >
				<i18n:message bundleRef="ettx_bundle" key="submit"/>
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
                <p> <i18n:message bundleRef="instruction" key="postalAddress.query.p1"/></p>
                  </uii:instructions>
    </td>
  </tr>
</table>
