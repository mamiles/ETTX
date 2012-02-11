<!-- DW6 -->
<!----------------------------------------------------------------------------------------
One time starting point for Content Area developers, thereafter works independantly.
Use File - UII Extensions - Save As JSP to save this file as a JSP Page and this automagically
generates the HTML file. Upload the .htm file to the Mockup Server.

----------->
<!---- WARNING: Uncomment this in the JSP file to make it work properly. -----------------
<%@ taglib uri="/WEB-INF/tlds/uii-contentarea-taglib.tld" prefix="uii" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/i18n" prefix="i18n" %>
<i18n:bundle baseName="com.cisco.ettx.admin.i18n.ettx" localeRef="userLocale" id="ettx_bundle" />
<i18n:bundle baseName="com.cisco.ettx.admin.i18n.instruction" localeRef="userLocale" id="instruction" />
<i18n:bundle baseName="com.cisco.ettx.admin.i18n.error" localeRef="userLocale" id="error" />

<%

	String contextPath = request.getContextPath();

%>
--->

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
<i18n:message id="invalidPhoneCharsMsg" bundleRef="error" key="invalidPhoneCharsMsg"/>
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="5"  BGCOLOR="E1E1E1">
  <tr> 
    <td width="100%" align="center" valign="center"> <uii:form action="/subDetails.do"> 
        <table width="350" border="0" cellspacing="1" cellpadding="2" bgcolor="#FFFFFF">
          <tr> 
            <td colspan="2" nowrap background="<%=contextPath%>/RSRC/en_US/images/vertlines_dk.gif" bgcolor="#669999"> 
              <uii:img page="/RSRC/en_US/images/shim.gif"/></td>
          </tr>
          <tr> 
            <td noWrap height=22 background="<%=contextPath%>/RSRC/en_US/images/dia_title_bg2.gif" colspan="2" align="center"> 
              <b><i18n:message bundleRef="ettx_bundle" key="createSubscriberTitle"/></b></td>
          </tr>
          <tr> 
            <td nowrap bgcolor="#cecfce" align="right"><i18n:message bundleRef="ettx_bundle" key="subscriberRecordFamilyName"/> *:</td>
            <td width="100%" bgcolor="#cecfce"> <uii:text property="subscriberRecord.subscriberLastName" size="22" /></td>
          </tr>
          <tr> 
            <td nowrap bgcolor="#cecfce" align="right"><i18n:message bundleRef="ettx_bundle" key="subscriberRecordGivenName" /> *:</td>
            <td width="100%" bgcolor="#cecfce"> <uii:text property="subscriberRecord.subscriberFirstName" size="22" /> </td>
          </tr>
          <tr> 
            <td nowrap bgcolor="#cecfce" align="right"><i18n:message bundleRef="ettx_bundle" key="subscriberRecordPostalAddress"/> *:</td>
            <td width="100%" bgcolor="#cecfce"> <uii:text property="subscriberRecord.postalAddress" size="22" /> </td>
          </tr>
	  <tr> 
            <td nowrap bgcolor="#cecfce" align="right"><i18n:message bundleRef="ettx_bundle" key="subscriberRecordPostalCity"/> *:</td>
            <td width="100%" bgcolor="#cecfce"> <uii:text property="subscriberRecord.postalCity" size="22" /> </td>
          </tr>
	  <tr> 
            <td nowrap bgcolor="#cecfce" align="right"><i18n:message bundleRef="ettx_bundle" key="subscriberRecordPostalState"/>:</td>
            <td width="100%" bgcolor="#cecfce"> <uii:text property="subscriberRecord.postalState" size="22" /> </td>
          </tr>
	  <tr> 
            <td nowrap bgcolor="#cecfce" align="right"><i18n:message bundleRef="ettx_bundle" key="subscriberRecordPostalCode"/> *:</td>
            <td width="100%" bgcolor="#cecfce"> <uii:text property="subscriberRecord.postalCode" size="22" /> </td>
          </tr>
	  <tr> 
            <td nowrap bgcolor="#cecfce" align="right"><i18n:message bundleRef="ettx_bundle" key="subscriberRecordCountryCode"/> *:</td>
            <td width="100%" bgcolor="#cecfce"> <uii:select property="subscriberRecord.countryCode" > 
				<uii:options labelName="countryNames" name="countryCodes" /> </uii:select>  </td>
          </tr>
	  <tr> 
            <td nowrap bgcolor="#cecfce" align="right"><i18n:message bundleRef="ettx_bundle" key="subscriberRecordHomeNumber"/>:</td>
            <td width="100%" bgcolor="#cecfce"> <uii:text property="subscriberRecord.homeNumber" size="22" regExp="/[^0-9 ]/g" errorDecision="true" errorMessage="<%=invalidPhoneCharsMsg%>" /> </td>
          </tr>
	  <tr> 
            <td nowrap bgcolor="#cecfce" align="right"><i18n:message bundleRef="ettx_bundle" key="subscriberRecordMobileNumber"/>:</td>
            <td width="100%" bgcolor="#cecfce"> <uii:text property="subscriberRecord.mobileNumber" size="22" regExp="/[^0-9 ]/g" errorDecision="true" errorMessage="<%=invalidPhoneCharsMsg%>" /> </td>
          </tr>
	  <tr> 
            <td nowrap bgcolor="#cecfce" align="right"><i18n:message bundleRef="ettx_bundle" key="portIdentifier"/>:</td>
            <td width="100%" bgcolor="#cecfce"> <uii:text property="subscriberRecord.portIdentifier" size="22" /> </td>
          </tr>
          <tr bgcolor="silver"> 
            <td colspan="2" nowrap><uii:img page="/RSRC/en_US/images/shim.gif" /></td>
          </tr>
          <tr> 
            <td colspan="2" nowrap bgcolor="#cecfce" background="<%=contextPath%>/RSRC/en_US/images/vertlines_dk.gif"> 
              <uii:img page="/RSRC/en_US/images/shim.gif"/></td>
          </tr>
        </table>
        </uii:form> </td>
    <td valign="top" align="right"> <uii:instructions>
                <p> <i18n:message bundleRef="instruction" key="subscriber.create.profile.p1"/></p>
                  </uii:instructions>
    </td>
  </tr>
</table>

