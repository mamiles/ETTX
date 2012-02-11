<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%-- import the wizard page number into request for use in html:hidden --%>
<tiles:useAttribute id="stepNumber" name="pageNumber" classname="java.lang.String" />
<%-- import the message key for the page title --%>
<tiles:useAttribute id="titleKey" name="title" classname="java.lang.String" />
<%-- import the field to receive initial focus on the form --%>
<tiles:useAttribute id="focusField" name="focus" classname="java.lang.String" />
<%-- import the title key of the page that will be next in the wizard --%>
<tiles:useAttribute id="nextKey" name="nextTitle" classname="java.lang.String"/>
<%-- import the action this form should submit to --%>
<tiles:useAttribute id="formAction" name="action" classname="java.lang.String" />

<%	// nasty hack required due to weird interaction between
	// the tiles tags and the html:javascript
	// Can't use a tiles:useAttribute page variable in the
	// html:javascript tag for some reason
	Integer temp = new Integer(stepNumber);
	int pac = temp.intValue();
%>

<HTML>
  <HEAD>
    <title><bean:message key="app.title"/></title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
	<link rel="stylesheet"
		  href="<html:rewrite page="/css/global.css" />" type="text/css">
  </HEAD>

<body bgcolor="#ffffff" text="#000000" link="#023264" alink="#023264" vlink="#023264">

<html:javascript formName="subscriberDetailsForm" page="<%= pac %>" />

<html:form action="<%= formAction %>"
	   	   focus="<%= focusField %>"
	   	   onsubmit="return validateSubscriberDetailsForm(this);">

<table valign="middle" border="0" width="100%" cellspacing="0" cellpadding="0">
<tr>
  <td><tiles:insert attribute='header' /></td>
</tr>
<tr>
	<td height="5"><!--spacer--><html:img page="/images/shim.gif" /></td>
</tr>
<tr>
  <td align="left"><span CLASS="pageTitle">&nbsp;&nbsp;<bean:message key="<%= titleKey %>"/></span></td>
</tr>
<tr>
	<td align="left"><tiles:insert definition='.error.table' /></td>
</tr>
<tr>
	<td>
		<!-- hidden attribute indicating the page of the registration wizard this screen is -->
		<html:hidden property="page" value="<%= stepNumber %>"/>
		
		<!-- hidden attribute indicating the title of the step of the registration
			 wizard being submitted. Having the title independent of the page number
			 allows for easy reorganizing of the page order without requiring
			 modification of the Action
		-->
		<html:hidden property="pageTitle" value="<%= titleKey %>"/>
		
		<!-- hidden attribute specifying to the Action class what page it should
			 forward to next
		-->
		<html:hidden property="nextTitle" value="<%= nextKey %>"/>
	
		<tiles:insert attribute='wizardBody' />
	</td>
</tr>
<tr>
	<td valign="bottom">	
		<!-- Button Row -->
		<table align="center" valign="bottom" width="100%" border="0" cellspacing="2" cellpadding="2">
			<tr>
<!--				<td noWrap align="left"><html:submit styleClass="btn" accesskey="c" property="submit"><bean:message key="registration.button.cancel"/></html:submit></td> -->
				<td noWrap align="left"><html:cancel styleClass="btn" accesskey="c"><bean:message key="registration.button.cancel"/></html:cancel></td>
				<td noWrap width="100%">&nbsp;</td>
				<td noWrap align="right"><html:submit styleClass="btn" accesskey="n" property="submit"><bean:message key="registration.button.next"/></html:submit></td>
				<td noWrap align="right"><bean:message key="<%= nextKey %>"/></td>
			</tr>
		</table>
	</td>
</tr>
<tr>
	<td height="5"><!--spacer--><html:img page="/images/shim.gif" /></td>
</tr>
<tr>
  <td>
    <tiles:insert attribute='footer' />
  </td>
</tr>
</table>
</html:form>
</body>
</html>

