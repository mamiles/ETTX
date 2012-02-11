 
<!----------------------------------------------------------------------------------------

One time starting point for Content Area developers, thereafter works independantly.

Use File - UII Extensions - Save As JSP to save this file as a JSP Page and this automagically

generates the HTML file. Upload the .htm file to the Mockup Server.



----------->
<!---- WARNING: Uncomment this in the JSP file to make it work properly. -----------------

--->
<%@ taglib uri="http://jakarta.apache.org/taglibs/i18n" prefix="i18n" %>
<%@ taglib uri="/WEB-INF/tlds/uii-contentarea-taglib.tld" prefix="uii" %>
<i18n:bundle baseName="com.cisco.ettx.admin.i18n.ettx" localeRef="userLocale" id="ettx_bundle" />
<i18n:bundle baseName="com.cisco.ettx.admin.i18n.instruction" localeRef="userLocale" id="instruction" />



<%



	String contextPath = request.getContextPath();



%>
<!-----
	The following defines the style for showing the disabled 
	text field.
------>
<style>
.styleDisabled {color:black;background-color:silver}
</style>

<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="5"  BGCOLOR="E1E1E1">
  <tr> 
    <td width="100%" align="center" valign="center"> <uii:form action="/viewSubscriberRecord.do"> 
        <table width="650" border="0" cellspacing="1" cellpadding="2" bgcolor="#FFFFFF">
          <tr> 
            <td colspan="4" nowrap background="<%=contextPath%>/RSRC/en_US/images/vertlines_dk.gif" bgcolor="#669999"> 
              <uii:img page="/RSRC/en_US/images/shim.gif"/></td>
          </tr>
          <tr> 
            <td noWrap height=22 background="<%=contextPath%>/RSRC/en_US/images/dia_title_bg2.gif" colspan="4" align="center"> 
              <b><i18n:message bundleRef="ettx_bundle" key="subscriberRecordTitle"/></b> </td>
	    </td>
          </tr>
<jsp:include page="SubscriberRecord.jsp" flush="true" />


          <tr bgcolor="#999999"> 
            <td colspan="4" nowrap ><uii:img page="/RSRC/en_US/images/shim.gif" /></td>
          </tr>
          <tr> 
            <td colspan="4" bgcolor="silver"> <table cellspacing=3 cellpadding=0 border=0 width="100%">
                <tr> 
                  <td width="100%"><uii:img page="/RSRC/en_US/images/shim.gif" width="20" height="5"/></td>
                  <td nowrap> <uii:button property="notUsed" 
					  submitValue="OK" 
					  onclick="javascript: window.close(); return false;" > 
					  <i18n:message bundleRef="ettx_bundle" key="ok"/>
			      </uii:button>
		  </td>
                </tr>
              </table></td>
          </tr>
          <tr> 
            <td colspan="4" nowrap bgcolor="#cecfce" background="<%=contextPath%>/RSRC/en_US/images/vertlines_dk.gif"> 
              <uii:img page="/RSRC/en_US/images/shim.gif"/></td>
          </tr>
        </table>
        </uii:form> </td>

  </tr>
</table>
