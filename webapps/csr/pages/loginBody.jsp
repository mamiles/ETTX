<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<BR>
<html:form action="Login"
	   	   focus="username">
<table align="center" border="0" bgcolor="#639A9C" valign="middle" width="350"
	   cellspacing="0" cellpadding="0">
	<tr>
		<td align="center" bgcolor="#316563" height="6" width="100%"><html:img page="/images/shim.gif" /></td>
	</tr>
	<tr>
		<td align="center" bgcolor="#316563" valign="middle"><font size="2" color="#FFFFFF" face="Arial, Helvetica, sans-serif"><strong><bean:message key="login.prompt"/></strong></font></td>
	</tr>
	<tr>
		<td align="center" bgcolor="#316563" height="6" width="100%"><html:img page="/images/shim.gif" /></td>
	</tr>
	<tr>
		<td>
		<table width="100%" height="107" border="0" cellspacing="0" cellpadding="4">	
		<tr>
			<td align="right"><font size="2" face="Arial, Helvetica, sans-serif"><bean:message key="login.username"/>:</font></td>
			<td align="left"><html:text property="username" size="24" /></td>
		</tr>
		<tr>
			<td align="right"><font size="2" face="Arial, Helvetica, sans-serif"><bean:message key="login.password"/>:</font></td>
			<td align="left"><html:password property="password" size="24" /></td>
		</tr>
		<tr>
			<td colspan="2" align="right"><html:submit styleClass="btn"><bean:message key="login.button"/></html:submit></td>
		</tr>
		</table>		
		</td>
	</tr>
</table>
</html:form>

<tiles:insert definition='.error.table' />	