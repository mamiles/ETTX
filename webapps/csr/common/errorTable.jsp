<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%-- Present a table containing an unordered list of error messages --%>
<logic:messagesPresent>
<table align="center" width="75%">
	<tr>
		<td bgcolor="#E7E7E7">
		<ul>
		<html:messages id="error">
		<li><bean:write name="error"/></li>
		</html:messages>
		</ul>
		</td>
	</tr>
	</table>
</logic:messagesPresent>