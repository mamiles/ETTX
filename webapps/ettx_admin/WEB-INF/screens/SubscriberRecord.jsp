<%@ taglib uri="http://jakarta.apache.org/taglibs/i18n" prefix="i18n" %>
<%@ taglib uri="/WEB-INF/tlds/uii-contentarea-taglib.tld" prefix="uii" %>
<i18n:bundle baseName="com.cisco.ettx.admin.i18n.ettx" localeRef="userLocale" id="ettx_bundle" />
<i18n:bundle baseName="com.cisco.ettx.admin.i18n.instruction" localeRef="userLocale" id="instruction" />
<%
String contextPath = request.getContextPath();
%>
<style>
.styleDisabled {color:black;background-color:silver}
</style>
          <tr> 
            <td nowrap width="50" bgcolor="#cecfce" align="right">
		<i18n:message bundleRef="ettx_bundle" key="subscriberRecordSubscriberName"/>
	    </td>
            <td width="250" bgcolor="#cecfce"><uii:text property="subscriberRecord.subscriberFullName" readonly="true" styleClass="styleDisabled"/></td>
            <td nowrap width="50" bgcolor="#cecfce" align="right">
		<i18n:message bundleRef="ettx_bundle" key="subscriberRecordLoginID"/>
	    </td>
            <td width="250" bgcolor="#cecfce"><uii:text property="subscriberRecord.loginID" readonly="true" styleClass="styleDisabled"/></td>
          </tr>

          <tr> 
            <td nowrap width="50" bgcolor="#cecfce" align="right">
		<i18n:message bundleRef="ettx_bundle" key="subscriberRecordDisplayName"/>
	    </td>
            <td width="250" colspan="3" bgcolor="#cecfce"><uii:text property="subscriberRecord.subscriberName" readonly="true" styleClass="styleDisabled"/></td>
	</tr>
	<tr>
            <td nowrap width="50" bgcolor="#cecfce" align="right" >
		<i18n:message bundleRef="ettx_bundle" key="subscriberRecordSubscribedServices"/>
	    </td>
            <td width="250" bgcolor="#cecfce"><uii:textarea cols="40" rows="2" readonly="true" property="subscriberRecord.subscriberServicesText" styleClass="styleDisabled"/>
		</td>
            <td nowrap width="50" bgcolor="#cecfce" align="right" >
		<i18n:message bundleRef="ettx_bundle" key="subscriberRecordConfiguredDevices"/>
	    </td>
            <td width="250" colspan="3" bgcolor="#cecfce"><uii:textarea cols="40" rows="2" readonly="true" property="subscriberRecord.subsDevicesText" styleClass="styleDisabled"/>
		</td>
          </tr>

          <tr> 
            <td nowrap width="50" bgcolor="#cecfce" align="right">
		<i18n:message bundleRef="ettx_bundle" key="subscriberRecordAccountNumber"/>
	    </td>
            <td width="250" bgcolor="#cecfce"><uii:text property="subscriberRecord.accountNumber" readonly="true" styleClass="styleDisabled"/></td>
            <td nowrap width="50" bgcolor="#cecfce" align="right">
		<i18n:message bundleRef="ettx_bundle" key="subscriberRecordAccountStatus"/>
	    </td>
            <td width="250" bgcolor="#cecfce"><uii:text property="subscriberRecord.accountStatus" readonly="true" styleClass="styleDisabled"/></td>
          </tr>

          <tr> 
            <td nowrap width="50" bgcolor="#cecfce" align="right" >
		<i18n:message bundleRef="ettx_bundle" key="subscriberRecordPostalAddress"/>
	    </td>
            <td width="250" bgcolor="#cecfce"><uii:text property="subscriberRecord.postalAddress" readonly="true" styleClass="styleDisabled"/></td>
            <td nowrap width="50" bgcolor="#cecfce" align="right" >
		<i18n:message bundleRef="ettx_bundle" key="subscriberRecordPostalCity"/>
	    </td>
            <td width="250" bgcolor="#cecfce"><uii:text property="subscriberRecord.postalCity" readonly="true" styleClass="styleDisabled"/></td>
	</tr>
	<tr>
            <td nowrap width="50" bgcolor="#cecfce" align="right" >
		<i18n:message bundleRef="ettx_bundle" key="subscriberRecordPostalState"/>
	    </td>
            <td width="250" bgcolor="#cecfce"><uii:text property="subscriberRecord.postalState" readonly="true" styleClass="styleDisabled"/></td>
            <td nowrap width="50" bgcolor="#cecfce" align="right" >
		<i18n:message bundleRef="ettx_bundle" key="subscriberRecordCountryCode"/>
	    </td>
            <td width="250" bgcolor="#cecfce"><uii:text property="subscriberRecord.countryCode" readonly="true" styleClass="styleDisabled"/></td>
	</tr>
	<tr>
            <td nowrap width="50" bgcolor="#cecfce" align="right" >
		<i18n:message bundleRef="ettx_bundle" key="subscriberRecordHomeNumber"/>
	    </td>
            <td width="250" bgcolor="#cecfce"><uii:text property="subscriberRecord.homeNumber" readonly="true" styleClass="styleDisabled"/></td>
            <td nowrap width="50" bgcolor="#cecfce" align="right" >
		<i18n:message bundleRef="ettx_bundle" key="subscriberRecordMobileNumber"/>
	    </td>
            <td width="250" bgcolor="#cecfce"><uii:text property="subscriberRecord.mobileNumber" readonly="true" styleClass="styleDisabled"/></td>
          </tr>
          <tr> 
            <td nowrap width="50" bgcolor="#cecfce" align="right" >
		<i18n:message bundleRef="ettx_bundle" key="subscriberRecordMACAddressList"/>
	    </td>
            <td width="250" bgcolor="#cecfce"><uii:textarea cols="40" rows="3" property="subscriberRecord.macAddressText" readonly="true" styleClass="styleDisabled"/>
		</td>
            <td nowrap width="50" bgcolor="#cecfce" align="right" >
		<i18n:message bundleRef="ettx_bundle" key="subscriberRecordIPAddressList"/>
	    </td>
            <td width="250" bgcolor="#cecfce"><uii:textarea cols="40" rows="3" property="subscriberRecord.ipAddressText" readonly="true" styleClass="styleDisabled"/>
		</td>
          </tr>

          <tr> 
            <td nowrap width="50" bgcolor="#cecfce" align="right">
		<i18n:message bundleRef="ettx_bundle" key="subscriberRecordSwitchAddress"/>
	    </td>
            <td width="250" bgcolor="#cecfce"><uii:text property="subscriberRecord.switchIPAddress" readonly="true" styleClass="styleDisabled"/></td>
            <td nowrap width="50" bgcolor="#cecfce" align="right">
		<i18n:message bundleRef="ettx_bundle" key="subscriberRecordSwitchPort"/>
	    </td>
            <td width="250" bgcolor="#cecfce"><uii:text property="subscriberRecord.switchPortName" readonly="true" styleClass="styleDisabled"/></td>
	</tr>
