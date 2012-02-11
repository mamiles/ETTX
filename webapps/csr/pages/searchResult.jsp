<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<p align="left" class="pageTitle">&nbsp;&nbsp;<bean:message key="subscriberSearch.result.title"/></p>
<p align="left" class="pageSubTitle">&nbsp;&nbsp;&nbsp;<bean:message key="subscriberSearch.delete.prompt"/></p>

<tiles:insert definition='.error.table' />

<html:form action="/auth/delete/subscriber">

<table align="left" width="95%" cellspacing="0" cellpadding="2">
<tr>
	<th>&nbsp;</th>
	<th><bean:message key="subscriberSearch.result.id" /></th>
	<th><bean:message key="subscriber.forename" /></th>
	<th><bean:message key="subscriber.surname" /></th>
	<th><bean:message key="subscriber.service.selectedService" /></th>
</tr>
<tr>
	<td colspan="5"><hr></td>
</tr>
<%-- Iterate over all the results in the form, displaying each in it's own
     row
--%>
<logic:iterate id="subscriber" name="allSubscribersForm"
    			   property="searchResult"
    			   type="com.cisco.ettx.provisioning.action.ETTXSubscriberBean">
<tr>
	<td><html:radio property="subscriber" idName="subscriber" value="subscriberId" /></td>
	<td><bean:write name="subscriber" property="subscriberId"/></td>
	<td><bean:write name="subscriber" property="forename"/></td>
	<td><bean:write name="subscriber" property="surname"/></td>
	<td><bean:write name="subscriber" property="serviceName"/></td>
</tr>
</logic:iterate>    
<tr>
<td colspan="5" align="right"><html:submit styleClass="btn" accesskey="d" property="submit"><bean:message key="subscriberSearch.delete.button"/></html:submit></td>
</tr>			   
</table>
</html:form>