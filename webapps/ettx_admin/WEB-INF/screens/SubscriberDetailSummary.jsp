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
<%@ page language="java" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.util.Locale" %>
<%@ page import="com.cisco.ettx.admin.gui.web.datatypes.ServiceFeatureList" %>
<%@ page import="com.cisco.ettx.admin.gui.web.datatypes.ServiceFeature" %>

<%

	String contextPath = request.getContextPath();

%>
--->

<jsp:useBean id="subscriberCreateFB" scope="session" class="com.cisco.ettx.admin.gui.web.beans.SubscriberCreateFormBean"/>

<style>
.styleDisabled {color:black;background-color:silver}
</style>

<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="5"  BGCOLOR="E1E1E1">
  <tr> 
    <td width="100%" align="center" valign="center"> <uii:form action="/subDetailsSummary.do"> 
        <table width="350" border="0" cellspacing="1" cellpadding="2" bgcolor="#FFFFFF">
          <tr> 
            <td colspan="4" nowrap background="/RSRC/en_US/images/vertlines_dk.gif" bgcolor="#669999"> 
              <uii:img page="/RSRC/en_US/images/shim.gif"/></td>
          </tr>
          <tr> 
            <td noWrap height=22 background="/RSRC/en_US/images/dia_title_bg2.gif" colspan="4" align="center"> 
              <b><i18n:message bundleRef="ettx_bundle" key="SUBSCRIBER_CREATED_SUCCESS"/></b></td>
          </tr>
          <tr>
            <td nowrap bgcolor="#cecfce" align="right" colspan="4"> </td>
          </tr>
          <tr> 
            <td noWrap height=22 background="/RSRC/en_US/images/dia_title_bg2.gif" colspan="2" align="center"> 
              <b><i18n:message bundleRef="ettx_bundle" key="createSubscriberTitle"/></b></td>
            <td noWrap height=22 background="/RSRC/en_US/images/dia_title_bg2.gif" colspan="2" align="center"> 
              <b><i18n:message bundleRef="ettx_bundle" key="createSubLoginTitle"/></b></td>
          </tr>
          <tr> 
            <td nowrap bgcolor="#cecfce" align="right"><i18n:message bundleRef="ettx_bundle" key="subscriberRecordFamilyName"/>:</td>
            <td width="100%" bgcolor="#cecfce"> <uii:text readonly="true" styleClass="styleDisabled" property="subscriberRecord.subscriberLastName" size="22" /></td>
            <td nowrap bgcolor="#cecfce" align="right"><i18n:message bundleRef="ettx_bundle" key="subscriberRecordLoginID"/>:</td>
            <td width="100%" bgcolor="#cecfce"> <uii:text readonly="true" styleClass="styleDisabled" property="subscriberRecord.loginID" size="22" /></td>
          </tr>
          <tr> 
            <td nowrap bgcolor="#cecfce" align="right"><i18n:message bundleRef="ettx_bundle" key="subscriberRecordGivenName" />:</td>
            <td width="100%" bgcolor="#cecfce"> <uii:text readonly="true" styleClass="styleDisabled" property="subscriberRecord.subscriberFirstName" size="22" /> </td>
            <td nowrap bgcolor="#cecfce" align="right"><i18n:message bundleRef="ettx_bundle" key="subscriberRecordPassword" />:</td>
            <td width="100%" bgcolor="#cecfce"> <uii:text readonly="true" styleClass="styleDisabled" property="subscriberRecord.password" size="22" /> </td>
          </tr>
          <tr> 
            <td nowrap bgcolor="#cecfce" align="right"><i18n:message bundleRef="ettx_bundle" key="subscriberRecordPostalAddress"/>:</td>
            <td width="100%" bgcolor="#cecfce"> <uii:text readonly="true" styleClass="styleDisabled"  property="subscriberRecord.postalAddress" size="22" /> </td>
            <td nowrap bgcolor="#cecfce" align="right"><i18n:message bundleRef="ettx_bundle" key="subscriberRecordDisplayName"/>:</td>
            <td width="100%" bgcolor="#cecfce"> <uii:text readonly="true" styleClass="styleDisabled"  property="subscriberRecord.subscriberName" size="22" /> </td>
          </tr>
	  <tr> 
            <td nowrap bgcolor="#cecfce" align="right"><i18n:message bundleRef="ettx_bundle" key="subscriberRecordPostalCity"/>:</td>
            <td width="100%" bgcolor="#cecfce"> <uii:text readonly="true" styleClass="styleDisabled"  property="subscriberRecord.postalCity" size="22" /> </td>
            <td nowrap bgcolor="#cecfce" align="right" colspan="2"> </td>
          </tr>
	  <tr> 
            <td nowrap bgcolor="#cecfce" align="right"><i18n:message bundleRef="ettx_bundle" key="subscriberRecordPostalState"/>:</td>
            <td width="100%" bgcolor="#cecfce"> <uii:text readonly="true" styleClass="styleDisabled"  property="subscriberRecord.postalState" size="22" /> </td>
            <td noWrap height=22 background="/RSRC/en_US/images/dia_title_bg2.gif" colspan="2" align="center"> 
              <b><i18n:message bundleRef="ettx_bundle" key="SwitchDetailsTitle"/></b></td>
          </tr>
	  <tr> 
            <td nowrap bgcolor="#cecfce" align="right"><i18n:message bundleRef="ettx_bundle" key="subscriberRecordPostalCode"/>:</td>
            <td width="100%" bgcolor="#cecfce"> <uii:text readonly="true" styleClass="styleDisabled"  property="subscriberRecord.postalCode" size="22" /> </td>
            <td nowrap bgcolor="#cecfce" align="right"><i18n:message bundleRef="ettx_bundle" key="subscriberRecordSwitchId"/>:</td>
            <td width="100%" bgcolor="#cecfce"> <uii:text readonly="true" styleClass="styleDisabled"  property="subscriberRecord.switchId" size="22" /> </td>
          </tr>
	  <tr> 
            <td nowrap bgcolor="#cecfce" align="right"><i18n:message bundleRef="ettx_bundle" key="subscriberRecordCountryCode"/>:</td>
            <td width="100%" bgcolor="#cecfce"> <uii:text readonly="true" styleClass="styleDisabled"  property="subscriberRecord.countryCode" size="22" />  </td>
            <td nowrap bgcolor="#cecfce" align="right"><i18n:message bundleRef="ettx_bundle" key="subscriberRecordPortInterface"/>:</td>
            <td width="100%" bgcolor="#cecfce"> <uii:text readonly="true" styleClass="styleDisabled"  property="subscriberRecord.portInterface" size="22" /> </td>
          </tr>
	  <tr> 
            <td nowrap bgcolor="#cecfce" align="right"><i18n:message bundleRef="ettx_bundle" key="subscriberRecordHomeNumber"/>:</td>
            <td width="100%" bgcolor="#cecfce"> <uii:text readonly="true" styleClass="styleDisabled"  property="subscriberRecord.homeNumber" size="22" /> </td>
            <td nowrap bgcolor="#cecfce" align="right" colspan="2"> </td>
          </tr>
	  <tr> 
            <td nowrap bgcolor="#cecfce" align="right"><i18n:message bundleRef="ettx_bundle" key="subscriberRecordMobileNumber"/>:</td>
            <td width="100%" bgcolor="#cecfce"> <uii:text readonly="true" styleClass="styleDisabled"  property="subscriberRecord.mobileNumber" size="22" /> </td>
            <td noWrap height=22 background="/RSRC/en_US/images/dia_title_bg2.gif" colspan="2" align="center"> 
              <b><i18n:message bundleRef="ettx_bundle" key="ServiceDetailsTitle"/></b></td>
          </tr>
	  <tr> 
	    <td nowrap bgcolor="#cecfce" align="right" colspan="2"> </td>
            <td nowrap bgcolor="#cecfce" align="right"><i18n:message bundleRef="ettx_bundle" key="service"/>:</td>
            <td width="100%" bgcolor="#cecfce"> <uii:text readonly="true" styleClass="styleDisabled"  property="selectedServiceName" size="22" /> </td>
          </tr>

	  <%
		ServiceFeature[] featureArray = subscriberCreateFB.getService().getFeatureArray();
		for (int i = 0; i < featureArray.length;i++) {
			String featureName = featureArray[i].getFeatureName();
			String selectProperty = "service.featureArray[" + i + "].featureValue";
			String optionsProperty = "service.featureArray[" + i + "].allowedValues";
	  %>
	  <tr>
	    <td nowrap bgcolor="#cecfce" align="right" colspan="2"> </td>
            <td nowrap bgcolor="#cecfce" align="right">&nbsp;<%=featureName%>:</td>
            <td width="100%" bgcolor="#cecfce" > <uii:text readonly="true" styleClass="styleDisabled"  property="<%=selectProperty%>" size="22" /> </td>
          </tr>
          <%
	        }
          %>                        

          <tr bgcolor="silver"> 
            <td colspan="4" nowrap><uii:img page="/RSRC/en_US/images/shim.gif" /></td>
          </tr>
          <tr> 
            <td colspan="4" nowrap bgcolor="#cecfce" background="/RSRC/en_US/images/vertlines_dk.gif"> 
              <uii:img page="/RSRC/en_US/images/shim.gif"/></td>
          </tr>
        </table>
  </tr>
</table>

