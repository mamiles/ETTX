<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="http://jakarta.apache.org/taglibs/i18n" prefix="i18n" %><i18n:bundle baseName="com.cisco.ettx.admin.i18n.ettx" localeRef="userLocale" id="ettx_bundle" /><jsp:useBean id="subscriberCreateFB" scope="session" class="com.cisco.ettx.admin.gui.web.beans.SubscriberCreateFormBean"/><i18n:message bundleRef="ettx_bundle" key="SubscriberDetailsStep1"/>
----------------------------------------
<i18n:message bundleRef="ettx_bundle" key="subscriberRecordFamilyName"/>: <bean:write name="subscriberCreateFB" property="subscriberRecord.subscriberLastName" />
<i18n:message bundleRef="ettx_bundle" key="subscriberRecordGivenName" />: <bean:write name="subscriberCreateFB" property="subscriberRecord.subscriberFirstName" />
<i18n:message bundleRef="ettx_bundle" key="subscriberRecordPostalAddress"/>: <bean:write name="subscriberCreateFB" property="subscriberRecord.postalAddress" />
<i18n:message bundleRef="ettx_bundle" key="subscriberRecordPostalCity"/>: <bean:write name="subscriberCreateFB" property="subscriberRecord.postalCity" />
<i18n:message bundleRef="ettx_bundle" key="subscriberRecordPostalState"/>: <bean:write name="subscriberCreateFB" property="subscriberRecord.postalState" />
<i18n:message bundleRef="ettx_bundle" key="subscriberRecordPostalCode"/>: <bean:write name="subscriberCreateFB" property="subscriberRecord.postalCode" />
<i18n:message bundleRef="ettx_bundle" key="subscriberRecordCountryCode"/>: <bean:write name="subscriberCreateFB" property="subscriberRecord.countryCode" />
<i18n:message bundleRef="ettx_bundle" key="subscriberRecordHomeNumber"/>: <bean:write name="subscriberCreateFB" property="subscriberRecord.homeNumber" />
<i18n:message bundleRef="ettx_bundle" key="subscriberRecordMobileNumber"/>: <bean:write name="subscriberCreateFB" property="subscriberRecord.mobileNumber" />
<i18n:message bundleRef="ettx_bundle" key="portIdentifier"/>: <bean:write name="subscriberCreateFB" property="subscriberRecord.portIdentifier" />
<i18n:message bundleRef="ettx_bundle" key="subscriberRecordSwitchId"/>: <bean:write name="subscriberCreateFB" property="addrMap.switchIPAddress" />
<i18n:message bundleRef="ettx_bundle" key="subscriberRecordPortInterface"/>: <bean:write name="subscriberCreateFB" property="addrMap.switchPortName" />
<i18n:message bundleRef="ettx_bundle" key="SwitchModel"/>: <bean:write name="subscriberCreateFB" property="addrMap.model" />

<i18n:message bundleRef="ettx_bundle" key="LoginDetailStep2"/>
----------------------------------------
<i18n:message bundleRef="ettx_bundle" key="subscriberRecordLoginID"/>: <bean:write name="subscriberCreateFB" property="subscriberRecord.loginID" />
<i18n:message bundleRef="ettx_bundle" key="subscriberRecordPassword"/>: <bean:write name="subscriberCreateFB" property="subscriberRecord.password" />
<i18n:message bundleRef="ettx_bundle" key="subscriberRecordDisplayName"/>: <bean:write name="subscriberCreateFB" property="subscriberRecord.subscriberName" />