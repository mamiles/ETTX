<%@ taglib uri="http://jakarta.apache.org/taglibs/i18n" prefix="i18n" %>
<i18n:bundle baseName="com.cisco.ettx.admin.i18n.ettx" localeRef="userLocale" id="ettx_bundle" />

<html>
<head>
<title>
<i18n:message bundleRef="ettx_bundle" key="thankYouTitle" /> 
</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<body bgcolor="#FFFFFF" text="#000000">
<h2 align="center">&nbsp;</h2>
<h2 align="center">&nbsp;</h2>
<h2 align="center"><i18n:message bundleRef="ettx_bundle" key="thankYouMsg" /> </h2>
<center>
  <p>&nbsp;</p>
  <form name="form1" method="post" action="">
    	<input type="button" 
		name="Button" 
		value='<i18n:message bundleRef="ettx_bundle" key="thankYouCloseBtn"/>'
		onclick="window.close()"/>
  </form>
  <p>&nbsp;</p>
</center>

</body>
</html>
