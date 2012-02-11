<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<tiles:useAttribute id="hide" ignore="true" name="hideMenu" 
					scope="page" classname="java.lang.String" />

<HTML>
<HEAD>
	<title><bean:message key="app.title"/></title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
	<link rel="stylesheet"
		  href="<html:rewrite page="/css/global.css" />" type="text/css">
</HEAD>
<body bgcolor="#ffffff" text="#000000" link="#023264" alink="#023264" vlink="#023264">
<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF" align="center">
<tr>
  <td colspan="2"><tiles:insert attribute='header' /></td>
</tr>
<tr>
<logic:notPresent name="hide" scope="page">
  <td width="170" valign="top" style="background-image:url(<html:rewrite page="/images/horz-stripe-bg.png" />);"><br><tiles:insert attribute='menu'/></td>
</logic:notPresent>  
  <td valign="top" align="left"><tiles:insert attribute='body' /></td>
</tr>
<tr>
  <td colspan="2"><tiles:insert attribute='footer' /></td>
</tr>
</table>
</body>
</HTML>

