<%@ taglib uri="http://jakarta.apache.org/taglibs/i18n" prefix="i18n" %>
<%@ taglib uri="/WEB-INF/tlds/uii-contentarea-taglib.tld" prefix="uii" %>
<i18n:bundle baseName="com.cisco.ettx.admin.i18n.ettx" localeRef="userLocale" id="ettx_bundle" />
<%



	String contextPath = request.getContextPath();



%>
<%@ page language="java" %>
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="5"  BGCOLOR="E1E1E1">
  <tr> 
    <td width="100%" align="center" valign="center"> <uii:form action="/subsTSResult.do"> 

	<table width="600" height="400" border="0" cellspacing="0" cellpadding="5" bgcolor="E1E1E1">
	  <tr> 
	    <td>
		<applet CODE="de.mud.jta.Applet" 
			CODEBASE="/ettx_admin/jtelnet" 
			ARCHIVE="jta25b.jar" 
			width = "100%" height="100%" bgcolor="E1E1E1" >
			<PARAM NAME = "Socket.host" VALUE = "<jsp:getProperty name="subsTSResultFB" property="switchIPAddress" />"/>
			<PARAM NAME = "Socket.port" VALUE = "23"/>
			<PARAM NAME = "config" VALUE = "telnet.conf"/>
		</applet>
	    </td>
	  </tr>
	</table>
        </uii:form>
    </td>
  </tr>
</table>
