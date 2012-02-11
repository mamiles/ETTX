<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<tiles:insert definition='.error.table' />

<logic:messagesNotPresent>
<table valign="middle" align="center" cellspacing="10" cellpadding="2">
	<tr>
		<td align="center"><html:img pageKey="homepage.image" border="0" /></td>
	</tr>
	<tr>
		<td align="center" class="pageTitle"><bean:message key="homepage.title"/><td>
	</tr>
	<tr>
		<td align="center" class="pageSubTitle"><bean:message key="homepage.subtitle"/></td>
	</tr>
</table>
</logic:messagesNotPresent>