<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested" %>

<p align="left" class="pageSubTitle">
&nbsp;&nbsp;&nbsp;<bean:message key="registration.service.prompt" />
</p>

<!-- if only one service choice, just pre-select it -->
<!-- PAC: Todo -->


<table align="center" width="90%" cellspacing="0" cellpadding="3">
    <nested:iterate id="service" name="subscriberDetailsForm"
    			    property="availableServices" indexId="svcIdx"
    			    type="com.cisco.ettx.provisioning.action.ETTXServiceBean">
    <%-- display a service --%>
    <tr> 
      <td width="30" align="center"> 
        <html:radio property="selectedService" idName="service" value="serviceName" />
      </td>
      <td>
        <bean:write name="service" property="serviceName"/>
      </td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td>
      	<%-- Display features on service --%>
        <table border="0" cellspacing="5" cellpadding="0" align="left">
        <%-- Need to iterate over each feature the service has --%>
        <tr>
       	<nested:iterate id="feature" name="service" property="features"
      				    type="com.cisco.ettx.provisioning.action.ETTXServiceFeatureBean"
      				    indexId="ftrIdx">
      				    
            <td>
            	<bean:write name="feature" property="featureName" />
            </td>
            <td>
				<select name="availableServices[<bean:write name="svcIdx"/>].features[<bean:write name="ftrIdx"/>].selectedValue" size="1" >
	                <nested:iterate id="featureValue" name="feature" property="possibleValues">
                	<option value="<nested:write name="featureValue" />"><nested:write name="featureValue" /></option>
                	</nested:iterate>
                </select>
            </td>
            <td>&nbsp;</td>
        </nested:iterate>
        </tr>
        </table>
      </td>
    </tr>
    <tr><td align="center" colspan="2"><hr></td></tr>
    <tr><td colspan="2" height="3"><html:img height="1" alt=" " page="/images/shim.gif" /></td></tr>    
    </nested:iterate> 
</table>

<br>

<%-- Define the switch options --%>
<bean:define id="switchOptions" name="subscriberDetailsForm" 
			 property="availableSwitches"
			 type="com.cisco.ettx.provisioning.action.ETTXSwitchBean[]"/>		 
<%
	String[] switchValues = new String[switchOptions.length];
	String[] switchDisplayNames = new String[switchOptions.length];
	for (int i=0; i < switchValues.length; i++) {
		switchValues[i] = switchOptions[i].getId();
		switchDisplayNames[i] = switchOptions[i].getIpAddress();
	}

	// now make both Structures available to Struts tags	
	pageContext.setAttribute("swVals",switchValues);
	pageContext.setAttribute("swDisplay",switchDisplayNames);	
	
%>			 
<table width="80%" cellspacing="0" cellpadding="3">
	<tr>
		<td align="right"><bean:message key="registration.service.switch.prompt"/>:</td>
		<td align="left">
			<html:select property="selectedSwitch">
				<html:options name="swVals" labelName="swDisplay"/>
			</html:select>
		</td>
		<td><bean:message key="registration.service.port.prompt"/></td>
		<td><html:text property="selectedPort" size="18"/> &nbsp;<bean:message key="registration.service.port.example"/></td>
	</tr>
</table>

<br>