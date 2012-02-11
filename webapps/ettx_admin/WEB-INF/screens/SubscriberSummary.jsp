 
<!----------------------------------------------------------------------------------------
One time starting point for Content Area developers, thereafter works independantly.
Use File - UII Extensions - Save As JSP to save this file as a JSP Page and this automagically
generates the HTML file. Upload the .htm file to the Mockup Server.

----------->

<%@ taglib uri="/WEB-INF/tlds/uii-contentarea-taglib.tld" prefix="uii" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/i18n" prefix="i18n" %>
<i18n:bundle baseName="com.cisco.ettx.admin.i18n.ettx" localeRef="userLocale" id="ettx_bundle" />
<i18n:bundle baseName="com.cisco.ettx.admin.i18n.instruction" localeRef="userLocale" id="instruction" />
<%@ page language="java" %>
<%@ page import="java.util.Vector" %>
<%@ page import="com.cisco.ettx.admin.gui.web.datatypes.ServiceFeatureList" %>
<%

	String contextPath = request.getContextPath();
%>
<jsp:useBean id="subscriberCreateFB" scope="session" class="com.cisco.ettx.admin.gui.web.beans.SubscriberCreateFormBean"/>
<%
	Vector featureList = subscriberCreateFB.getService().getFeatureList();
%>
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="5"  BGCOLOR="E1E1E1">
  
<tr>     
	
<td width="100%" align="center" valign="center"> 
		 <uii:form action="/subDetails.do"> 
              
<table width="350" border="0" cellspacing="1" cellpadding="2" bgcolor="#FFFFFF">
                
<tr> 
                  
<td colspan="2" nowrap background="<%=contextPath%>/RSRC/en_US/images/vertlines_dk.gif" bgcolor="#669999"> 
                    <uii:img page="/RSRC/en_US/images/shim.gif"/></td>
</tr>
                
<tr> 
                  
<td noWrap height=22 background="<%=contextPath%>/RSRC/en_US/images/dia_title_bg2.gif" colspan="2" align="center"> 
                    <b><i18n:message bundleRef="ettx_bundle" key="SubscriberSummaryTitle"/></b></td>
</tr>

                
               
<tr> 
<td nowrap bgcolor="#cecfce" align="right">
<i18n:message bundleRef="ettx_bundle" key="Summary"/>:	
</td>                
<td nowrap bgcolor="#cecfce" align="left">
<textarea name="textfield2" style="width:350px;font-family:arial;font-size:8pt;background:#e1e1e1" rows="20" cols="35" wrap="VIRTUAL" onFocus="blur();">
<jsp:include page="SubscriberSummary2.jsp" flush="true" />

<i18n:message bundleRef="ettx_bundle" key="ServiceDetailsStep3"/>
----------------------------------------
<i18n:message bundleRef="ettx_bundle" key="subscriberRecordSubscribedServices"/>: <bean:write name="subscriberCreateFB" property="service.serviceName" />
<logic:iterate id="serviceFeature" collection="<%=featureList%>" type="com.cisco.ettx.admin.gui.web.datatypes.ServiceFeature"><bean:write name="serviceFeature" property="featureName"/>: <bean:write name="serviceFeature" property="featureValue" />
</logic:iterate>
<i18n:message bundleRef="ettx_bundle" key="SwitchDetailsTitle"/>
----------------------------------------
<i18n:message bundleRef="ettx_bundle" key="macAddress"/>: <bean:write name="subscriberCreateFB" property="subsDeviceInfo.macAddress" />
<i18n:message bundleRef="ettx_bundle" key="macAddressAlias"/>: <bean:write name="subscriberCreateFB" property="subsDeviceInfo.displayName" />
</textarea>

</td>
</tr>

                               
                
<tr> 
                  
<td colspan="2" nowrap bgcolor="#cecfce" background="<%=contextPath%>/RSRC/en_US/images/vertlines_dk.gif"> 
                    <uii:img page="/RSRC/en_US/images/shim.gif"/></td>
</tr>
              
</table>
</uii:form>
    </td>
<td valign="top" align="right"> <uii:instructions>
                <p> <i18n:message bundleRef="instruction" key="subscriber.create.summary.p1"/></p>
                  </uii:instructions>
</td>
</tr>
</table>
