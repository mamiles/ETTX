 
<!----------------------------------------------------------------------------------------

One time starting point for Content Area developers, thereafter works independantly.

Use File - UII Extensions - Save As JSP to save this file as a JSP Page and this automagically

generates the HTML file. Upload the .htm file to the Mockup Server.



----------->

<%@ taglib uri="http://jakarta.apache.org/taglibs/i18n" prefix="i18n" %>
<%@ taglib uri="/WEB-INF/tlds/uii-contentarea-taglib.tld" prefix="uii" %>
<i18n:bundle baseName="com.cisco.ettx.admin.i18n.ettx" localeRef="userLocale" id="ettx_bundle" />
<i18n:bundle baseName="com.cisco.ettx.admin.i18n.instruction" localeRef="userLocale" id="instruction" />
<i18n:bundle baseName="com.cisco.ettx.admin.i18n.error" localeRef="userLocale" id="error" />



<%



	String contextPath = request.getContextPath();



%>

<i18n:message id="hostNameMsg" bundleRef="ettx_bundle" key="hostNameMsg"/>
<i18n:message id="unixLoginIDMsg" bundleRef="ettx_bundle" key="unixLoginIDMsg"/>
<i18n:message id="logDirectoryMsg" bundleRef="ettx_bundle" key="logDirectoryMsg"/>
<i18n:message id="missingFeaturesMsg" bundleRef="ettx_bundle" key="missingFeaturesMsg"/>
<i18n:message id="invalidCharsMsg" bundleRef="error" key="invalidCharsMsg"/>
<script language="JavaScript" type="text/javascript">
function doButtonCheck(aButtonName,layerName) {
	if (aButtonName == "OK") {
		var totalErrorMsg = "";
		var errorFound = false;
		if (layerName == "remotehost") {
			var hostName = document.componentConfigFB.elements['currentHostBean.hostName'].value;
			var unixLoginID = document.componentConfigFB.elements['currentHostBean.unixLoginID'].value;
			if (hostName == "") {
				alert("<%=hostNameMsg%>");
				return false;
			}
			if (unixLoginID == "") {
				alert("<%=unixLoginIDMsg%>");
				return false;
			}
		}
		var logDirectory = document.componentConfigFB.elements['currentHostBean.logDirectory'].value;
		if (logDirectory == "") {
			totalErrorMsg = totalErrorMsg + "<%=logDirectoryMsg%>, ";
			errorFound = true;
		}
		if (errorFound == true) {
			totalErrorMsg = totalErrorMsg + "<%=missingFeaturesMsg%>";
			alert(totalErrorMsg);
		}
		return true;
	}
	return true;
}
</script>
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="5"  BGCOLOR="E1E1E1" align="center">
  <tr> 
    <td width="100%" align="center" valign="middle"> <uii:layerDialog id="editCompHost"> <uii:form action="/editCompHost.do"> 
        <!----------------------------------------------------------------------------------------

	// IMPORTANT: To fix the window size same for all the layer dialogs, calculate the height, width of the 

	// largest dialog. Then this table width, height should be the same as the width, height of the 

	// largest table. Then add one row (after all the input fields and before the buttons row) 

	// with height="100%" for each of the dialogs. Any tool that lets you calculate the width, height

	// in pixels will figure out the height, width of the table.    

	// Note: 1) In this template, "layer2" is the largest dialog and its height is 200, width is 511.

	// 2) To make the same file work with the mockup server, each layerContents is translted into a 

	// div section in the translted html file. Because div can only work with table sections, in the tranlsted htm file,

	// you have to remove the  width of the below table, close the table before the layerOptions end tag and 

	// add a table with width, height equals the width, height of the largest dialog 

	// inside each one of the layercontents section. THEN UPLOAD THE htm FILE TO THE MOCKUP SERVER.

	// OTHERWISE THE htm WILL NOT WORK PROPERLY IN THE MOCKUP SERVER.
l
	----------->
        <table width="511" height="400" border="0" cellspacing="1" cellpadding="2" bgcolor="#FFFFFF">
          <tr> 
            <td colspan="2" nowrap background="<%=contextPath%>/RSRC/en_US/images/vertlines_dk.gif" bgcolor="#669999"> 
              <uii:img page="/RSRC/en_US/images/shim.gif"/></td>
          </tr>
          <tr> 
            <td noWrap height=22 background="<%=contextPath%>/RSRC/en_US/images/dia_title_bg2.gif" colspan="2" align="center"> 
		<b><i18n:message bundleRef="ettx_bundle" key="compHostTitle"/></b>
		</td>
          </tr>
          <uii:layerOptions> 
          <!----------------------------------------------------------------------------------------

				//This is the layer options section. 

				// for each radio button, onclick should be there calling "showLayer('layername')"

				// this is only for the mockup server and runtime engine will override this function

				// and have its own run time implementation for radio button inside "layerOptions" of a 

				// "layer dialog". specify "property" and onclick function for the radio tag.

				----------->
          <tr> 
            <td nowrap bgcolor="#cecfce" colspan="2"> &nbsp;<i18n:message bundleRef="ettx_bundle" key="host"/>: 
		<uii:radio property="currentHostBean.currentLayer" value="localhost" layerName="localhost" onclick="showLayer('locahost')" mockupChecked="checked"> 
			<i18n:message bundleRef="ettx_bundle" key="localhost"/>
		</uii:radio>
		<uii:radio property="currentHostBean.currentLayer" value="remotehost" layerName="remotehost" onclick="showLayer('remotehost')" >
			<i18n:message bundleRef="ettx_bundle" key="remotehost"/>
		</uii:radio>
	    </td>
          </tr>
          </uii:layerOptions>
          <uii:layerContents name="localhost"> 
	  	<tr> 
            <td nowrap bgcolor="#cecfce" align="right" width="39%">
			<i18n:message bundleRef="ettx_bundle" key="logDirectory"/>
              </td>
            <td width="61%" bgcolor="#cecfce"><uii:textarea property="currentHostBean.logDirectory" rows="10" cols="40" /> </td>
          </tr>
              <!----------------------------------------------------------------------------------------

				// This is the row with height="100%". If this is not present, the dialog will not look perfect.

			---------------------------->
              <td nowrap bgcolor="#cecfce" align="right" colspan="2" height="100%">&nbsp; 
              </td>
            </tr>
            <tr bgcolor="999999"> 
              <td colspan="2" nowrap><uii:img page="/RSRC/en_US/images/shim.gif" /></td>
            </tr>
            <tr bgcolor="silver"> 
              <td colspan="2" bgcolor="silver"> 
                <table cellspacing=3 cellpadding=0 border=0 width="100%">
                  <tr> 
                    <td width="100%"> <uii:img page="/RSRC/en_US/images/shim.gif" width="20" height="5"/> </td>
                  <td nowrap> <uii:button property="action" value="OK" 
				onclick="return doButtonCheck('OK','localhost');">
				<b><i18n:message bundleRef="ettx_bundle" key="ok"/></b>
				</uii:button>
		     </td>
                  <td nowrap> <uii:button property="action" value="Cancel" 
				onclick="UIIFormSubmit('action','Cancel');return false;">
				<b><i18n:message bundleRef="ettx_bundle" key="cancel"/></b>
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
            </uii:layerContents>
          <uii:layerContents name="remotehost"> 
          <tr> 
            <td nowrap bgcolor="#cecfce" align="right" width="39%">
				<i18n:message bundleRef="ettx_bundle" key="hostName"/>
		</td>
            <td width="61%" bgcolor="#cecfce"><uii:text maxLen="16" regExp="/[^A-Za-z0-9-_.]/g" errorDecision="true" errorMessage="<%=invalidCharsMsg%>" property="currentHostBean.hostName" /></td>
          </tr>
          <tr> 
            <td nowrap bgcolor="#cecfce" align="right" width="39%">
			<i18n:message bundleRef="ettx_bundle" key="unixLoginID"/>
              </td>
            <td width="61%" bgcolor="#cecfce"><uii:text maxLen="11" regExp="/[^A-Za-z0-9-_]/g" errorDecision="true" errorMessage="<%=invalidCharsMsg%>" property="currentHostBean.unixLoginID" /> </td>
          </tr>

          <tr> 
            <td nowrap bgcolor="#cecfce" align="right" width="39%">
			<i18n:message bundleRef="ettx_bundle" key="unixPassword"/>
               </td>
            <td width="61%" bgcolor="#cecfce"><uii:password property="currentHostBean.unixPassword" /> </td>
          </tr>
          <tr> 
            <td nowrap bgcolor="#cecfce" align="right" width="39%">
			<i18n:message bundleRef="ettx_bundle" key="unixPrompt"/>
               </td>
            <td width="61%" bgcolor="#cecfce"><uii:text property="currentHostBean.unixPrompt" /> </td>
          </tr>
		<tr> 
            <td nowrap bgcolor="#cecfce" align="right" width="39%">
			<i18n:message bundleRef="ettx_bundle" key="useSecureShell"/>
               </td>
            <td width="61%" bgcolor="#cecfce">
		<uii:radio property="currentHostBean.useSecureShell" value="true" >
			<i18n:message bundleRef="ettx_bundle" key="valueTrue"/>
		</uii:radio>
		<uii:radio property="currentHostBean.useSecureShell" value="false" >
			<i18n:message bundleRef="ettx_bundle" key="valueFalse"/>
		</uii:radio>
			</td>
          </tr>
	  <tr> 
            <td nowrap bgcolor="#cecfce" align="right" width="39%">
			<i18n:message bundleRef="ettx_bundle" key="logDirectory"/>
              </td>
            <td width="61%" bgcolor="#cecfce"><uii:textarea property="currentHostBean.logDirectory" rows="10" cols="40" /> </td>
          </tr>
          <tr bgcolor="#999999"> 
            <td colspan="2" nowrap><uii:img page="/RSRC/en_US/images/shim.gif" /></td>
          </tr>
            <tr bgcolor="999999"> 
              <td colspan="2" nowrap><uii:img page="/RSRC/en_US/images/shim.gif" /></td>
            </tr>
            <tr bgcolor="silver"> 
              <td colspan="2" bgcolor="silver"> 
                <table cellspacing=3 cellpadding=0 border=0 width="100%">
                  <tr> 
                    <td width="100%"><uii:img page="/RSRC/en_US/images/shim.gif" width="20" height="5"/></td>
                  <td nowrap> <uii:button property="action" value="OK" 
				onclick="return doButtonCheck('OK','remotehost');">
				<b><i18n:message bundleRef="ettx_bundle" key="ok"/></b>
				</uii:button>
		  </td>
                  <td nowrap> <uii:button property="action" value="Cancel" 
				onclick="UIIFormSubmit('action','Cancel');return false;">
				<b><i18n:message bundleRef="ettx_bundle" key="cancel"/></b>
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
            </uii:layerContents>

        </table>
        </uii:form>
      </uii:layerDialog>
    </td>
    <td valign="top" align="RIGHT"> <uii:instructions>
                <p> <i18n:message bundleRef="instruction" key="edit.comp.p1"/></p>
                <p> <i18n:message bundleRef="instruction" key="edit.comp.p2"/></p>
                  </uii:instructions>
    </td>
  </tr>
</table>
