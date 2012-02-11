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
<p align="left" class="pageTitle">&nbsp;&nbsp;<bean:write name="TASKNAME"/> <bean:message key="task.inprogress.message.postfix"/></p>

<%
	Integer percentage = (Integer)request.getAttribute(ProgressCheckAction.PERCENTAGE_COMPLETE);	
	Integer paddingHeight = new Integer(200 - (2*percentage.intValue()));

	// put both values into page context for later usage
	pageContext.setAttribute("percentage",percentage);
	pageContext.setAttribute("paddingHeight",paddingHeight);	
%>

<table align="left" border="0" width="100%" cellspacing="0" cellpadding="5">
<tr valign="top">
	<td noWrap align="left" width="180">
	<table border="0" cellspacing="0" cellpadding="3">
		<tr>
			<th><p class="pageSubTitle"><bean:message key="task.percentage.complete.message"/></p></th>
		</tr>
		<tr>
			<td noWrap align="center"><!-- Progress bar -->
			<table bgcolor="#FFFFFF" rules="cols" width="100" height="200" border="1">
			<logic:greaterThan name="paddingHeight" scope="page" value="0">
			<tr>
				<td bgcolor="#FFFFFF" height="<%= paddingHeight %> valign="top">&nbsp;</td>
			<tr>
			</logic:greaterThan>
			<logic:greaterThan name="percentage" scope="page" value="0">
			<tr>
				<td bgcolor="#316563" valign="bottom">&nbsp;</td>
			</tr>
			</logic:greaterThan>
			</table>
			</td>
		</tr>
		<tr>
			<td align="center"><p class="pageSubTitle"><%= String.valueOf(percentage) %>%</p></td>
		</tr>
	</table>
	</td>
	<td noWrap valign="top">
	<!-- All the messages within the TaskProgress -->
	<logic:messagesPresent message="true">
	<table align="left" valign="top" border="0" cellspacing="8" cellpadding="2">
		<html:messages id="msg" message="true">
		<tr valign="top">
			<td bgcolor="#E7E7E7"><bean:write name="msg"/></td>
		</tr>
		</html:messages>
	</table>
	</logic:messagesPresent>
	</td>
</tr>
</table>

<html:form action="/progress/check.do">
	<html:hidden property="taskId" value="<%= (String)request.getAttribute(ProgressCheckAction.TASK_ID) %>" />
</html:form>