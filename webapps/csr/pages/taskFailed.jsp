<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page language="java"
		 import="com.cisco.ettx.provisioning.progress.ProgressMessage,
		 		 com.cisco.ettx.provisioning.action.ProgressCheckAction,
		 		 java.util.ArrayList,
		 		 java.util.Iterator,
		 		 java.util.List" 
%>


<p align="left" class="pageTitle">&nbsp;&nbsp;<bean:write name="TASKNAME"/> <bean:message key="task.failed.message"/></p>
<p align="left" class="pageSubTitle">&nbsp;&nbsp;&nbsp;<bean:message key="task.progress.message.report" /></p>
<!-- All the messages within the TaskProgress -->
<logic:messagesPresent message="true">
<table align="center" border="0" width="95%" cellspacing="8" cellpadding="2">
	<html:messages id="msg" message="true">
	<tr>
		<td align="left" bgcolor="#E7E7E7"><bean:write name="msg"/></td>
	</tr>
	</html:messages>
</table>
</logic:messagesPresent>

<table align="center" border="0" width="30%" cellspacing="8" cellpadding="2">
<tr>
	<td align="right"><bean:message key="task.progress.errorCode"/>:</td>
	<td bgcolor="##FFAAAA">
	<%= (Integer)request.getAttribute(com.cisco.ettx.provisioning.action.ProgressCheckAction.FINISH_CODE) %>
	</td>
</tr>
</table>