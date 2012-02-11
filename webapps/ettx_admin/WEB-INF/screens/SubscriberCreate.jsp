<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/tlds/uii-contentarea-taglib.tld" prefix="uii" %>

<%

	String contextpath = request.getContextPath();
%>


<table width="98%" border="0" cellspacing="0" cellpadding="0" align="center" bgcolor="#E1E1E1">
  <tr> 
    <td rowspan="3" bgcolor="#669999"><img src="<%=contextpath%>/images/shim.gif"></td>
    <td bgcolor="#669999"><img src="<%=contextpath%>/images/shim.gif"></td>
    <td rowspan="3" bgcolor="#669999"><img src="<%=contextpath%>/images/shim.gif"></td>
  </tr>
  <tr> 
    <td width="100%" height="345" align="center">
			    
<table width="100%" border="0" cellspacing="3" cellpadding="0" height="100%" bgcolor="#FFFFFF">
  <tr valign=top> 
    <td width="100%">


<%
  String wizid = request.getParameter( "wizid" );
  String wizAction = request.getParameter( "wizAction" );
  if ( wizAction != null && !wizAction.equals( "" ) )
    out.write( "<font color=green>You pressed \"" + wizAction + "\" on a wizard page that had a screenID of \"" + wizid + "\"</font><p>" );
%>

<uii:form method="post" action="/createSubs.do"> 
              <table>
                <!--DWLayoutTable-->
                <tr> 
                  <td colspan="2"> <p>Click on the button below to create a new 
                      Subscriber.</p>
                    <p>&nbsp;</p></td>
                </tr>
                <tr> 
                  <td> <uii:button property="wiz1action" value="Create Subscriber" submitValue="create"/> </td>
                  
                  <td width="100%">&nbsp; </td>
                </tr>
                
                
              </table>



</uii:form>

<!-- Second Column Finish -->

    </td>
  </tr>

</table>

			</td>
  </tr>
  <tr> 
    <td bgcolor="#669999"><img src="<%=contextpath%>/images/shim.gif"></td>
  </tr>
  <tr> 
    <td><img src="<%=contextpath%>/images/shim.gif" width="1" height="3" border="0" alt=""></td>
  </tr>
</table>