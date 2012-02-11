<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<%@ taglib uri="/WEB-INF/tlds/uii-contentarea-taglib.tld" prefix="uii" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/i18n" prefix="i18n" %>
<i18n:bundle baseName="com.cisco.ettx.admin.i18n.ettx" localeRef="userLocale" id="ettx_bundle" />

<%
	String contextPath = request.getContextPath();
%>
<HEAD>
<TITLE><i18n:message bundleRef="ettx_bundle" key="loginTitle"/></TITLE>
<META content=EN name=MS.LOCALE>
<META http-equiv=Content-Type content="text/html; charset=iso-8859-1">
<LINK href="<%= contextPath%>/uii/global.css" rel=stylesheet type="text/css">

</HEAD>
<BODY LANGUAGE=JavaScript onload="return document.getElementById('userId').focus();">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td height="332"> <table width="440" height="198" border="1" align="center" cellpadding="0" cellspacing="0" bordercolor="#666666">
        <tr> 
          <td width="436" height="196"> <table cellspacing=0 cellpadding=0 width="436" border=0>
              <!--DWLayoutTable-->
              <tbody>
                <tr> 
                  <td width=160 height="1" valign="top" nowrap><img height=1 alt="" 
      src="<%= contextPath%>/images/spacer.gif" 
      width=160 border=0></td>
                  <td width="9" valign="top" nowrap><img height=1 alt="" 
      src="<%= contextPath%>/images/spacer.gif" 
      width=9 border=0></td>
                  <td width="267" valign="top" nowrap><img height=1 alt="" 
      src="<%= contextPath%>/images/spacer.gif" 
      width=267 border=0></td>
                </tr>
                <tr> 
                  <td rowspan=4 valign="top" nowrap><img height=8 alt="" 
      src="<%= contextPath%>/images/spacer.gif" 
      width=25 border=0><a href="http://www.cisco.com/" target=_blank><img 
      height=73 alt="" 
      src="<%= contextPath%>/images/cisco_logo.gif" 
      width=110 border=0></a><img height=8 alt="" 
      src="<%= contextPath%>/images/spacer.gif" 
      width=25 border=0></td>
                  <td height="18" valign="top"><img height=18 alt="" 
      src="<%= contextPath%>/images/spacer.gif" 
      width=9 border=0></td>
                  <td><img height=18 alt="" 
      src="<%= contextPath%>/images/spacer.gif" 
      width=9 border=0></td>
                </tr>
                <tr> 
                  <td height="8" align=right valign="top"><img height=8 alt="" 
      src="<%= contextPath%>/images/vertlines_left.gif" 
      width=9></td>
                  <td align=right valign="top" 
    background="<%=contextPath%>/images/vertlines.gif"><img src="<%= contextPath%>/images/clear.gif" width="1" height="8"></td>
                </tr>
                <tr> 
                  <td valign="top" bgcolor=#336666><img height=33 alt="" 
      src="<%= contextPath%>/images/spacer.gif" 
      width=8 border=0></td>
                  <td bgcolor=#336666><span 
      class=BannerTitle><font color="#FFFFFF"><i18n:message bundleRef="ettx_bundle" key="loginTitle"/></font></span></td>
                </tr>
                <tr> 
                  <td valign="top"><img src="<%= contextPath%>/images/spacer.gif" width="1" height="14"></td>
                  <td valign="top"><img src="<%= contextPath%>/images/spacer.gif" width="1" height="14"></td>
                </tr>
              </tbody>
            </table>
			<form name="form1" method="post" action="<%= contextPath%>/servlet/com.cisco.ettx.admin.gui.web.servlet.LoginServlet">
            <table width="436" border="0" cellpadding="0" cellspacing="0">
              <tr> 
                <td width="160" height="120"><img src="<%= contextPath%>/images/login_logo.jpg" width="160"></td>
                <td width="276" valign="top"> <table width="276" border="0" align="center" cellpadding="0" cellspacing="4" bgcolor="#FFFFFF">
                    <!--DWLayoutTable-->
	  			<% if (session.getAttribute(com.cisco.ettx.admin.gui.web.util.ETTXUtil.AUTHENTICATION_ERROR) != null) { %>
					<tr>
						<td colspan="5"><center><i><font color="red">
	  			<%=session.getAttribute(com.cisco.ettx.admin.gui.web.util.ETTXUtil.AUTHENTICATION_ERROR)%>
</font></i></center></td>
						<td colspan="5"><img src="<%= contextPath%>/images/clear.gif" width="23" height="8"></td>
					</tr>
				<% } else { %>
                    <tr> 
                      <td width="23"><img src="<%= contextPath%>/images/clear.gif" width="23" height="1"></td>
                      <td><!--DWLayoutEmptyCell-->&nbsp;</td>
                      <td colspan="2"><!--DWLayoutEmptyCell-->&nbsp;</td>
                      <td><img src="<%= contextPath%>/images/clear.gif" width="23" height="8"></td>
                    </tr>
				<% } %>
                    <tr> 
                      <td width="23"><!--DWLayoutEmptyCell-->&nbsp;</td>
                      <td width="47"><i18n:message bundleRef="ettx_bundle" key="loginPageUserID"/></td>
                      <td colspan="2"><input type="text" name="userId"></td>
                      <td width="23"><!--DWLayoutEmptyCell-->&nbsp;</td>
                    </tr>
                    <tr> 
                      <td><!--DWLayoutEmptyCell-->&nbsp;</td>
                      <td> <i18n:message bundleRef="ettx_bundle" key="loginPagePassword"/></td>
                      <td colspan="2"> <input type="password" name="userPasswd"> </td>
                      <td><!--DWLayoutEmptyCell-->&nbsp;</td>
                    </tr>
                    <tr> 
                      <td height="24">&nbsp;</td>
                      <td>&nbsp;</td>
                      <td width="91">&nbsp;</td>
                      <td width="68" align="left" nowrap> 
				<input type="submit" name="Login" 
				       value='<i18n:message bundleRef="ettx_bundle" key="loginLogin"/>' >
		      </td>
                      <td><!--DWLayoutEmptyCell-->&nbsp;</td>
                    </tr>
                  </table></td>
              </tr>
            </table>
			</form>
			</td>
        </tr>
      </table></td>
  </tr>
</table>
</BODY>
</HTML>
