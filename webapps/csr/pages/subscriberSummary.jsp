<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<p align="left" class="pageSubTitle">
&nbsp;&nbsp;&nbsp;<bean:message key="registration.summary.prompt" />
</p>

<bean:define id="subscriber" name="subscriberDetailsForm"
			 type="org.apache.struts.validator.DynaValidatorForm"/>

<%@ page import="java.util.Locale" %>

<%
	String userCountry = (String)subscriber.get("country");
	Locale countryLocale = new Locale("", userCountry);
	String displayCountry = countryLocale.getDisplayCountry();
	
	// now make displayCountry available to Struts tags by putting into 
	// page scope.
	pageContext.setAttribute("displayCountry",displayCountry);
%>


<table align="left" width="80%" cellspacing="0" cellpadding="3">
	<tr>
		<td align="right"><bean:message key="subscriber.forename"/>:</td>
		<td align="left"><bean:write name="subscriber" property="forename"/></td>
	</tr>
	<tr>
		<td align="right"><bean:message key="subscriber.initials"/>:</td>
		<td align="left"><bean:write name="subscriber" property="initials"/></td>
	</tr>
	<tr>
		<td align="right"><bean:message key="subscriber.surname"/>:</td>
		<td align="left"><bean:write name="subscriber" property="surname"/></td>
	</tr>
	<tr>
		<td align="right"><bean:message key="subscriber.street"/>:</td>
		<td align="left"><bean:write name="subscriber" property="street"/></td>
	</tr>
	<tr>
		<td align="right"><bean:message key="subscriber.city"/>:</td>
		<td align="left"><bean:write name="subscriber" property="city"/></td>
	</tr>
	<tr>
		<td align="right"><bean:message key="subscriber.state"/>:</td>
		<td align="left"><bean:write name="subscriber" property="state"/></td>
	</tr>
	<tr>
		<td align="right"><bean:message key="subscriber.postcode"/>:</td>
		<td align="left"><bean:write name="subscriber" property="postcode"/></td>
	</tr>
	<tr>
		<td align="right"><bean:message key="subscriber.country"/>:</td>
		<td align="left"><bean:write name="displayCountry"/></td>
	</tr>
	<tr>
		<td align="right"><bean:message key="subscriber.phone.home"/>:</td>
		<td align="left"><bean:write name="subscriber" property="homeNumber"/></td>
	</tr>
	<tr>
		<td align="right"><bean:message key="subscriber.phone.mobile"/>:</td>
		<td align="left"><bean:write name="subscriber" property="mobileNumber" /></td>
	</tr>
	<tr>
		<td align="right"><bean:message key="subscriber.dob"/>:</td>
		<td align="left"><bean:write name="subscriber" property="dob" /></td>
	</tr>
	<!-- Spacer Row -->
    <tr><td colspan="2" height="5"><html:img height="1" alt=" " page="/images/shim.gif" /></td></tr>    	
	<tr>
		<td align="right"><bean:message key="subscriber.username"/>:</td>
		<td align="left"><bean:write name="subscriber" property="username" /></td>
	</tr>
	<tr>
		<td align="right"><bean:message key="subscriber.password"/>:</td>
		<td align="left"><bean:write name="subscriber" property="password" /></td>
	</tr>
	<tr>
		<td align="right"><bean:message key="subscriber.screenName"/>:</td>
		<td align="left"><bean:write name="subscriber" property="screenName" /></td>
	</tr>	
	<!-- Spacer Row -->
    <tr><td colspan="2" height="5"><html:img height="1" alt=" " page="/images/shim.gif" /></td></tr>    	
	<tr>
		<td align="right"><bean:message key="subscriber.service.selectedService"/>:</td>
		<td align="left"><bean:write name="subscriber" property="selectedService" /></td>
	</tr>
	<tr>
		<td align="right"><bean:message key="subscriber.service.switch"/>:</td>
		<td align="left"><bean:write name="subscriber" property="selectedSwitch" /></td>
	</tr>
	<tr>
		<td align="right"><bean:message key="subscriber.service.port"/>:</td>
		<td align="left"><bean:write name="subscriber" property="selectedPort" /></td>
	</tr>
</table>