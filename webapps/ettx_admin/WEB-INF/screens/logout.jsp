 
<!----------------------------------------------------------------------------------------
One time starting point for Content Area developers, thereafter works independantly.
Use File - UII Extensions - Save As JSP to save this file as a JSP Page and this automagically
generates the HTML file. Upload the .htm file to the Mockup Server.

----------->
<!---- WARNING: Uncomment this in the JSP file to make it work properly. -----------------
--->
<%@ taglib uri="/WEB-INF/tlds/uii-contentarea-taglib.tld" prefix="uii" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/i18n" prefix="i18n" %>
<i18n:bundle baseName="com.cisco.ettx.admin.i18n.ettx" localeRef="userLocale" id="ettx_bundle" />

<%

	String contextPath = request.getContextPath();

%>
<title>CCNSC-SP GUI Logout</title>
<table border="0" cellspacing="0" cellpadding="5"  BGCOLOR="E1E1E1" align="center">
  <tr> 
    <td width="100%"> 
      <table border="0" cellspacing="1" cellpadding="2" align="center">
        <tr> 
          <td height="350"> <uii:form action="/logoutAction.do"> 
              <table width="350" border="0" cellspacing="1" cellpadding="1" align="center" bgcolor="#FFFFFF">
                <tr> 
                  <td colspan="2" nowrap background="<%=contextPath%>/RSRC/en_US/images/vertlines_dk.gif" bgcolor="#669999"> 
                    <uii:img page="/RSRC/en_US/images/shim.gif"/></td>
                </tr>
                <tr> 
                  <td noWrap height=22 background="<%=contextPath%>/RSRC/en_US/images/dia_title_bg2.gif" colspan="2" align="center"> 
                    <b><i18n:message bundleRef="ettx_bundle" key="logoutTitle"/></b></td>
                </tr>
                <tr> 
                  <td nowrap bgcolor="#cecfce" align="right">&nbsp;</td>
                </tr>
                <tr> 
                  <td nowrap bgcolor="#cecfce" align="right"> 
                    <div align="center"><i18n:message bundleRef="ettx_bundle" key="logoutExitConfirmation"/></div>
                  </td>
                </tr>
                <tr> 
                  <td nowrap bgcolor="#cecfce" align="right">&nbsp;</td>
                </tr>
                <tr bgcolor="#999999"> 
                  <td colspan="2" nowrap><uii:img page="/RSRC/en_US/images/shim.gif" /></td>
                </tr>
                <tr> 
                  <td colspan="2" bgcolor="silver"> 
                    <table cellspacing=3 cellpadding=0 border=0 width="100%">
                      <tr> 
                        <td width="100%"><uii:img page="/RSRC/en_US/images/shim.gif" width="20" height="5"/></td>
                        <td nowrap> 
				<uii:button submitValue="Ok" >
  			        	    <i18n:message bundleRef="ettx_bundle" key="ok"/>
				</uii:button>
			</td>
                        <td nowrap> 
				<uii:button submitValue="Cancel" >
  			        	    <i18n:message bundleRef="ettx_bundle" key="cancel"/>
				</uii:button>
			</td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr> 
                  <td colspan="2" nowrap bgcolor="#cecfce" background="<%=contextPath%>/RSRC/en_US/images/vertlines_dk.gif"> 
                    <uii:img page="/RSRC/en_US/images/shim.gif"/></td>
                </tr>
              </table>
              </uii:form>
          </td>
        </tr>
      </table>
    </td>
    <td valign="top"> 
      <TABLE WIDTH="100%" BORDER="0" CELLSPACING="0" CELLPADDING="0" ALIGN="center">
        <tr> 
          <td> 
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr> 
                <td><uii:img page="/RSRC/en_US/images/snv_left_04.gif" /></td>
                <TD WIDTH="100%" NOWRAP BACKGROUND="<%=contextPath%>/RSRC/en_US/images/snv_bg_04.gif"><b><FONT COLOR="White">&nbsp;Instruction</FONT></b> 
                </TD>
                <td><uii:img page="/RSRC/en_US/images/snv_right_04.gif" /></td>
              </tr>
            </table>
          </td>
        </tr>
        <tr> 
          <td bgcolor="FFFFCC"> 
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr> 
                <td> 
                  <p>Click Ok to exit the application.</p>
				  <p>Click Cancel to go back to the home page.</p>
                </td>
              </tr>
            </table>
          </td>
        </tr>
        <tr> 
          <td> 
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr> 
                <td><uii:img page="/RSRC/en_US/images/snv_left_btm_04.gif"/></td>
                <td width="100%" background="<%=contextPath%>/RSRC/en_US/images/snv_bg_btm_04.gif"> 
                  <uii:img page="/RSRC/en_US/images/shim.gif"/></td>
                <td><uii:img page="/RSRC/en_US/images/snv_right_btm_04.gif"/></td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
