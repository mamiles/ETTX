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
<%@ page language="java" %>
<%@ page import="java.util.Vector" %>
<%@ page import="com.cisco.ettx.admin.common.ToolbarElement" %>
<%
    String contextPath = request.getContextPath();
%>
<jsp:useBean id="toolbarFB" scope="session" class="com.cisco.ettx.admin.gui.web.beans.ToolbarFormBean"/>
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="5"  BGCOLOR="E1E1E1">
  <tr> 
    <td width="100%" align="center" valign="center"> <uii:form action="/toolbarView.do"> 
        <table width="350" border="0" cellspacing="1" cellpadding="2" bgcolor="#FFFFFF">
          <tr> 
            <td nowrap background="<%=contextPath%>/RSRC/en_US/images/vertlines_dk.gif" bgcolor="#669999"> 
              <uii:img page="/RSRC/en_US/images/shim.gif"/></td>
          </tr>
          <tr> 
            <td noWrap width="100%" background="<%=contextPath%>/RSRC/en_US/images/dia_title_bg2.gif" align="center"> 
		<b><i18n:message bundleRef="ettx_bundle" key="toolbarView"/></b></td>
          </tr>
          <tr> 
            <td width="100%" bgcolor="#cecfce">&nbsp; </td>
          </tr>
<%
	Vector toolbarList = toolbarFB.getToolbarList();
	for (int i = 0; i < toolbarList.size(); i++) {
		ToolbarElement element = (ToolbarElement)toolbarList.elementAt(i);
		String displayName = element.getDisplayName();
		String link = element.getUrlLink();
%>
          	<tr> 
            		<td noWrap width="100%" bgcolor="#cecfce"> 
              			<div align="center"><b><a href="<%=link%>" target="_blank"> <%=displayName%> </a></b></div>
            		</td>
          	</tr>
          	<tr> 
            	<td width="100%" bgcolor="#cecfce">&nbsp;</td>
          	</tr>
<%
	 }
%>
          <tr bgcolor="#999999"> 
            <td nowrap><uii:img page="/RSRC/en_US/images/shim.gif" /></td>
          </tr>
          <tr> 
            <td nowrap bgcolor="#cecfce" background="<%=contextPath%>/RSRC/en_US/images/vertlines_dk.gif"> 
              <uii:img page="/RSRC/en_US/images/shim.gif"/></td>
          </tr>
        </table>
        </uii:form>
    </td>
    <td valign="top" align="right"> <uii:instructions>
                <p> <i18n:message bundleRef="instruction" key="toolbar.p1"/></p>
                  </uii:instructions>
    </td>
  </tr>
</table>
