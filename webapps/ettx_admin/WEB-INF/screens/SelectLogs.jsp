<%@ taglib uri="http://jakarta.apache.org/taglibs/i18n" prefix="i18n" %>
<%@ taglib uri="/WEB-INF/tlds/uii-contentarea-taglib.tld" prefix="uii" %>
<%@ taglib uri="/WEB-INF/tlds/ogs-objectselector-taglib.tld" prefix="ogs" %>
<i18n:bundle baseName="com.cisco.ettx.admin.i18n.ettx" localeRef="userLocale" id="ettx_bundle" />
<i18n:bundle baseName="com.cisco.ettx.admin.i18n.instruction" localeRef="userLocale" id="instruction" />

<%
	String contextPath = request.getContextPath();
%>

<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="5"  BGCOLOR="E1E1E1">
  <tr> 
  	<uii:form method="post" action="/selectLogs.do"> 
  	<td valign="top" width="39%" height="240" align="left" > 
        <ogs:contentAreaSelector 
            osName="contentos" 
            selectionMode="multiple" 
            preserveSelection="true" 
            isGroupSelector="false" 
            mixSelection="false" 
            width="200" 
            height="300"
            treeGenerator="com.cisco.ettx.admin.gui.web.datatypes.ApplLogTreeGenerator"/>
    </td>
    <td width="100%" align="left" valign="top"> 
        <table width="350" border="0" cellspacing="1" cellpadding="2" bgcolor="#FFFFFF">
          <tr> 
            <td colspan="2" nowrap background="<%=contextPath%>/RSRC/en_US/images/vertlines_dk.gif" bgcolor="#669999"> 
              <uii:img page="/RSRC/en_US/images/shim.gif"/></td>
          </tr>
          <tr> 
            <td noWrap height=22 background="<%=contextPath%>/RSRC/en_US/images/dia_title_bg2.gif" colspan="2" align="center"> 
              <b><i18n:message bundleRef="ettx_bundle" key="selectLogsTitle"/></b></td>
          </tr>
          <tr> 
            <td nowrap bgcolor="#cecfce" align="right">&nbsp;</td>
            <td width="100%" bgcolor="#cecfce">
				<uii:radio property="searchType" 
		        	value="allFiles" 
					mockupChecked="checked"/>
					<i18n:message bundleRef="ettx_bundle" key="selectLogsRetrieveSelectedLogs"/>
		    	</uii:radio>
			</td>
          </tr>
          <tr> 
            <td nowrap bgcolor="#cecfce" align="right">&nbsp;</td>
            <td width="100%" bgcolor="#cecfce">
				<uii:radio property="searchType" 
		        	value="searchFiles" 
					mockupChecked="unchecked"/>
   					<i18n:message bundleRef="ettx_bundle" key="selectLogsSearchLogs"/>
		    	</uii:radio>
			</td>
          </tr>
          <tr> 
            <td nowrap bgcolor="#cecfce" align="right">
				<i18n:message bundleRef="ettx_bundle" key="selectLogsSearchValue"/>: 
			</td>
            <td width="100%" bgcolor="#cecfce">
				<uii:text property="searchString"/> 
			</td>
          </tr>
          <tr> 
            <td nowrap bgcolor="#cecfce" align="right">&nbsp;</td>
            <td width="100%" bgcolor="#cecfce">
				<div align="right">
		        	<uii:button property="action" 
			    		value="Submit" 
			    		onclick="javascript:window.getSelection()" 
						popupWidth="850" 
                    	popupHeight="500" 
			    		popupFeatures="resizble=yes,scrollbars=yes">
			    		<i18n:message bundleRef="ettx_bundle" key="Submit"/>
					</uii:button>
		    	</div> 
			</td>
          </tr>
          <tr bgcolor="silver"> 
            <td colspan="2" nowrap><uii:img page="/RSRC/en_US/images/shim.gif" /></td>
          </tr>
          <tr> 
            <td colspan="2" nowrap bgcolor="#cecfce" background="<%=contextPath%>/RSRC/en_US/images/vertlines_dk.gif"> 
              <uii:img page="/RSRC/en_US/images/shim.gif"/></td>
          </tr>
        </table>
        
	</td>
    	<td valign="top" align="right"> <uii:instructions>
                <p> <i18n:message bundleRef="instruction" key="troubleshoot.log.p1"/></p>
                <p> <i18n:message bundleRef="instruction" key="troubleshoot.log.p2"/></p>
                  </uii:instructions>
      	</td>
	</uii:form> 
  </tr>
</table>

