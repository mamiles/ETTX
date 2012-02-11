<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%-- Layout component 
  parameters : title, header, body, footer 
--%>
<HTML>
  <HEAD>
    <title><bean:message key="app.title"/></title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
	<link rel="stylesheet"
		  href="<html:rewrite page="/css/global.css" />" type="text/css">
    
  </HEAD>

<body onload="setTimeout('document.forms[0].submit()', <tiles:getAsString name="milliseconds"/>)" 
	  bgcolor="#ffffff" text="#000000" link="#023264" alink="#023264" vlink="#023264">
<table border="0" width="100%" cellspacing="5">
<tr>
  <td colspan="2"><tiles:insert attribute="header" /></td>
</tr>
<tr>
  <td valign="top"  align="left">
    <tiles:insert attribute='body' />
  </td>
</tr>
<tr>
  <td colspan="2">
    <tiles:insert attribute="footer" />
  </td>
</tr>
</table>
</body>
</html>

