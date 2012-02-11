<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ page import="java.util.Locale" %>
<%
	// get a list of countries, and sort alphabetically
	String[] countryCodes = Locale.getDefault().getISOCountries();
	String[] countryNames = new String[countryCodes.length];
	
	for (int i=0; i < countryCodes.length; i++) {
		Locale countryLocale = new Locale("", countryCodes[i]);
		countryNames[i] = countryLocale.getDisplayCountry();
	}
	
	// have display names, now sort. Need to sort manually instead of 
	// using Arrays.sort since we need to keep the country codes in sync
	// with the country names
    boolean unsorted = true;
    while (unsorted) {
        boolean swapped = false;
        for (int j = 0; j < (countryCodes.length - 1); j++) {
            String country1 = countryNames[j];
            String country2 = countryNames[j + 1];
            if (country1.compareTo(country2) > 0) {
                // swap the "country" entries
                countryNames[j] = country2;
                countryNames[j + 1] = country1;

                // also need to swap the corresponding "country code" entries
                String tempCountryCode = countryCodes[j];
                countryCodes[j] = countryCodes[j + 1];
                countryCodes[j + 1] = tempCountryCode;

                swapped = true;
            }
        }

        unsorted = swapped;
    }    
		
	// now make countryCodes and countryNames arrays available to Struts tags
	// by putting into page scope.
	pageContext.setAttribute("countryNames",countryNames);
	pageContext.setAttribute("countryCodes",countryCodes);	
%>

<table align="left" width="80%" cellspacing="0" cellpadding="3">
	<tr>
		<td align="right"><bean:message key="subscriber.forename"/>&nbsp;<bean:message key="mandatory.indicator" />:</td>
		<td align="left"><html:text property="forename" size="22" /></td>
	</tr>
	<tr>
		<td align="right"><bean:message key="subscriber.initials"/>:</td>
		<td align="left"><html:text property="initials" size="3"/></td>
	</tr>
	<tr>
		<td align="right"><bean:message key="subscriber.surname"/>&nbsp;<bean:message key="mandatory.indicator" />:</td>
		<td align="left"><html:text property="surname" size="22"/></td>
	</tr>
	<tr>
		<td align="right"><bean:message key="subscriber.street"/>&nbsp;<bean:message key="mandatory.indicator" />:</td>
		<td align="left"><html:text property="street" size="22"/></td>
	</tr>
	<tr>
		<td align="right"><bean:message key="subscriber.city"/>&nbsp;<bean:message key="mandatory.indicator" />:</td>
		<td align="left"><html:text property="city" size="22"/></td>
	</tr>
	<tr>
		<td align="right"><bean:message key="subscriber.state"/>:</td>
		<td align="left"><html:text property="state" size="22"/></td>
	</tr>
	<tr>
		<td align="right"><bean:message key="subscriber.postcode"/>&nbsp;<bean:message key="mandatory.indicator" />:</td>
		<td align="left"><html:text property="postcode" size="9"/></td>
	</tr>
	<tr>
		<td align="right"><bean:message key="subscriber.country"/>&nbsp;<bean:message key="mandatory.indicator" />:</td>
		<td align="left">
		<html:select property="country">
			<html:options labelName="countryNames" name="countryCodes" />
		</html:select>
	</tr>
	<tr>
		<td align="right"><bean:message key="subscriber.phone.home"/>:</td>
		<td align="left"><html:text property="homeNumber" size="22"/></td>
	</tr>
	<tr>
		<td align="right"><bean:message key="subscriber.phone.mobile"/>:</td>
		<td align="left"><html:text property="mobileNumber" size="22"/></td>
	</tr>
	<tr>
		<td align="right"><bean:message key="subscriber.dob"/>:</td>
		<td align="left"><html:text property="dob" size="10"/> &nbsp(<bean:message key="subscriber.dob.example"/>)</td>
	</tr>
</table>