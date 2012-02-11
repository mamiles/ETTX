<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<table width="80%">
	<tr>
		<td align="right"><bean:message key="subscriber.card.type"/></td>
		<td align="left">
		<html:select property="cardType">
			<html:option key="registration.card.type.mastercard" value="mastercard" />
			<html:option key="registration.card.type.visa" value="visa" />
			<html:option key="registration.card.type.amex" value="amex" />
		</html:select>
	</tr>
	<tr>
		<td align="right"><bean:message key="subscriber.card.number"/></td>
		<td align="left"><html:text property="cardNumber" size="16"/></td>
	</tr>
	<tr>
		<td align="right"><bean:message key="subscriber.card.expiry"/></td>
		<td align="left"><html:text property="cardExpiry" size="5"/></td>
	</tr>
</table>