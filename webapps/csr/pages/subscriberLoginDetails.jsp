<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<table align="left" width="80%" cellspacing="0" cellpadding="3">
	<tr>
		<td align="right"><bean:message key="subscriber.username"/>:</td>
		<td align="left"><html:text property="username" size="22"/></td>
	</tr>
	<tr>
		<td align="right"><bean:message key="subscriber.password"/>:</td>
		<td align="left"><html:text property="password" size="22"/></td>
	</tr>
	<tr>
		<td align="right"><bean:message key="subscriber.screenName"/>:</td>
		<td align="left"><html:text property="screenName" size="22"/></td>
	</tr>
</table>