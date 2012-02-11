 
<!----------------------------------------------------------------------------------------

One time starting point for Content Area developers, thereafter works independantly.

Use File - UII Extensions - Save As JSP to save this file as a JSP Page and this automagically

generates the HTML file. Upload the .htm file to the Mockup Server.



----------->

<%@ taglib uri="http://jakarta.apache.org/taglibs/i18n" prefix="i18n" %>
<%@ taglib uri="/WEB-INF/tlds/uii-contentarea-taglib.tld" prefix="uii" %>
<i18n:bundle baseName="com.cisco.ettx.admin.i18n.ettx" localeRef="userLocale" id="ettx_bundle" />
<i18n:bundle baseName="com.cisco.ettx.admin.i18n.instruction" localeRef="userLocale" id="instruction" />



<%



	String contextPath = request.getContextPath();



%>

<HTML>
<BODY>
</BODY>
</HTML>
<!----------------------------------------------------------------------------------------

// Use this template if you have three options or less to show. If you have more than three 

// options then use 'Layered Dialog with Dropdown Options' Template.

//The below script is only for the mockup server. Thats why it is enclosed between 

// mockupData tags. This script should be modified to

// match your requirements. 

----------->
<uii:mockupData> 
<script>



var layer_dialogs = new Array();

layer_dialogs.push('Subscriber');

layer_dialogs.push('Service');

layer_dialogs.push('Switch');



/*

// Add all layer names X where X is the name property of the "layer contents" tag.

// In this template there are two "layer contents" tag with name "layer1" and "layer2"

*/



var currentLayer;



function showLayer(layer) {

	var output;

	for(var i=0; i <layer_dialogs.length; i++) {

		var name = layer_dialogs[i];

		if (currentLayer == name) {

			document.all[name].innerHTML = document.all['clayer'].innerHTML;

		}

		if (layer == name) {

			output = document.all[name].innerHTML;

			currentLayer = name;

		}

		document.all[name].style.visibility = "hidden";					

	}

	document.all['clayer'].style.visibility = "visible";

	document.all['clayer'].innerHTML = output;

	

}

window.onload = function() {

	showLayer('Subscriber');

/*

Modify this to decide which layer you want to display initially...

In this template we want to display layer1.

*/	

}

</script>
</uii:mockupData>
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="5"  BGCOLOR="E1E1E1" align="center">
  <tr> 
    <td width="100%" align="center" valign="middle"> <uii:layerDialog id="query_subs"> <uii:form action="/tsQuerySubs.do"> 
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
        <table width="511" height="200" border="0" cellspacing="1" cellpadding="2" bgcolor="#FFFFFF">
          <tr> 
            <td colspan="2" nowrap background="<%=contextPath%>/RSRC/en_US/images/vertlines_dk.gif" bgcolor="#669999"> 
              <uii:img page="/RSRC/en_US/images/shim.gif"/></td>
          </tr>
          <tr> 
            <td noWrap height=22 background="<%=contextPath%>/RSRC/en_US/images/dia_title_bg2.gif" colspan="2" align="center"> 
		<b><i18n:message bundleRef="ettx_bundle" key="subsQueryTitle"/></b>
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
            <td nowrap bgcolor="#cecfce" colspan="2"> &nbsp;<i18n:message bundleRef="ettx_bundle" key="querySubCategory"/>: 
		<uii:radio property="currentLayer" value="Subscriber" layerName="Subscriber" onclick="showLayer('Subscriber')" mockupChecked="checked"> 
			<i18n:message bundleRef="ettx_bundle" key="subscriber"/>
		</uii:radio>
		<uii:radio property="currentLayer" value="MacAddress" layerName="MacAddress" onclick="showLayer('MacAddress')" >
			<i18n:message bundleRef="ettx_bundle" key="macAddress"/>
		</uii:radio>
		<uii:radio property="currentLayer" value="Service" layerName="Service" onclick="showLayer('Service')" >
			<i18n:message bundleRef="ettx_bundle" key="service"/>
		</uii:radio>
		<uii:radio property="currentLayer" value="Switch" layerName="Switch" onclick="showLayer('Switch')" >
			<i18n:message bundleRef="ettx_bundle" key="switch"/>
		</uii:radio>
	    </td>
          </tr>
          </uii:layerOptions>
          <!----------------------------------------------------------------------------------------

				// To make the translated .htm file to work properly in the mockup server, each layer contents should have

				// its own table with width, height equals to the width, height

				// of the largest layer dialog. Modify the htm file to do that. To figure out the width, 

				// height first upload this screen to the mockup server, capture the screen shot and use 

				// any tool like photoshop which tells you

				// any portion of the screen shot in pixels.  Contact UE team if you have any problems in figuring 

				// out the width, height. Specify "name" attribute. Also, for all the dialogs add one row with

				// height "100%" (before the buttons row).

				----------->
          <uii:layerContents name="Subscriber"> 
            <tr> 
              <td nowrap bgcolor="#cecfce" align="right">&nbsp;<i18n:message bundleRef="ettx_bundle" key="familyName"/>:</td>
              <td width="100%" bgcolor="#cecfce"><uii:text property="familyName" /> </td>
            </tr>
            <tr> 
              <td nowrap bgcolor="#cecfce" align="right">&nbsp;<i18n:message bundleRef="ettx_bundle" key="givenName"/>:</td>
              <td width="100%" bgcolor="#cecfce"><uii:text property="givenName" /> </td>
            </tr>
            <tr> 
              <td nowrap bgcolor="#cecfce" align="right">&nbsp;<i18n:message bundleRef="ettx_bundle" key="loginID"/>:</td>
              <td width="100%" bgcolor="#cecfce"><uii:text property="loginID" /> </td>
            </tr>
              <td nowrap bgcolor="#cecfce" align="right">&nbsp;<i18n:message bundleRef="ettx_bundle" key="accountNumber"/>:</td>
              <td width="100%" align="left" bgcolor="#cecfce"><uii:text property="accountNumber" /> </td>
            </tr>
            <tr> 
            <tr> 
              <td nowrap bgcolor="#cecfce" align="right"><i18n:message bundleRef="ettx_bundle" key="homeNumber"/>:</td>
              <td width="100%" bgcolor="#cecfce"><uii:text property="homeNumber" /> </td>
            </tr>
            <tr> 
              <td nowrap bgcolor="#cecfce" align="right"><i18n:message bundleRef="ettx_bundle" key="mobileNumber"/>:</td>
              <td width="100%" bgcolor="#cecfce"><uii:text property="mobileNumber" /> </td>
            </tr>
			<tr> 
              <td nowrap bgcolor="#cecfce" align="right"><i18n:message bundleRef="ettx_bundle" key="postalAddress"/>:</td>
              <td width="100%" bgcolor="#cecfce"><uii:text property="postalAddress" /> </td>
            </tr>
              <td nowrap bgcolor="#cecfce" align="right"><i18n:message bundleRef="ettx_bundle" key="postalState"/>:</td>
              <td width="100%" align="left" bgcolor="#cecfce"><uii:text property="postalState" /> </td>
            </tr>
            <tr> 
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
                    <td nowrap> <uii:button submitValue="Submit"
				popupWidth="1000" popupHeight="800"
				popupFeatures="resizable=yes, scrollbars=yes">
				<i18n:message bundleRef="ettx_bundle" key="Submit"/>
				</uii:button> 
		     </td>

              <!----------------------------------------------------------------------------------------
                    <td nowrap> <uii:button submitValue="Reset">
					<i18n:message bundleRef="ettx_bundle" key="reset"/>
				</uii:button> 
		    </td>
		---------------------------->

                  </tr>
                </table>
              </td>
            </tr>
            <tr> 
              <td colspan="2" nowrap bgcolor="#cecfce" background="<%=contextPath%>/RSRC/en_US/images/vertlines_dk.gif"> 
                <uii:img page="/RSRC/en_US/images/shim.gif"/></td>
            </tr>
            </uii:layerContents>

          <uii:layerContents name="MacAddress"> 
            <tr> 
              <td nowrap bgcolor="#cecfce" align="right">&nbsp;<i18n:message bundleRef="ettx_bundle" key="macAddress"/>:</td>
              <td width="100%" bgcolor="#cecfce"><uii:text property="macAddress" /> </td>
            </tr>
            <tr> 
              <td nowrap bgcolor="#cecfce" align="right">&nbsp;<i18n:message bundleRef="ettx_bundle" key="macAddressAlias"/>:</td>
              <td width="100%" bgcolor="#cecfce"><uii:text property="macAddressAlias" /> </td>
            </tr>
            <tr> 
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
                    <td nowrap> <uii:button submitValue="Submit" 
					    popupWidth="1000" popupHeight="800" popupFeatures="resizable=yes,scrollbars=yes" >
					<i18n:message bundleRef="ettx_bundle" key="submit"/>
				</uii:button> 
		     </td>
	
              <!----------------------------------------------------------------------------------------
                    <td nowrap> <uii:button submitValue="Reset">
					<i18n:message bundleRef="ettx_bundle" key="reset"/>
				</uii:button> 
		    </td>
		---------------------------->

                  </tr>
                </table>
              </td>
            </tr>
            <tr> 
              <td colspan="2" nowrap bgcolor="#cecfce" background="<%=contextPath%>/RSRC/en_US/images/vertlines_dk.gif"> 
                <uii:img page="/RSRC/en_US/images/shim.gif"/></td>
            </tr>
            </uii:layerContents>


          <uii:layerContents name="Service"> 
            <tr> 
              <td nowrap bgcolor="#cecfce" align="right" height="21"><i18n:message bundleRef="ettx_bundle" key="serviceList"/>:</td>
              <td width="100%" bgcolor="#cecfce" height="21" > <uii:select property="service"><uii:options property="serviceList"/></uii:select></td>
            </tr>
            <tr> 
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
                    <td width="100%"><uii:img page="/RSRC/en_US/images/shim.gif" width="20" height="5"/></td>
                    <td nowrap> <uii:button submitValue="Submit"
				popupWidth="1000" popupHeight="800"
				popupFeatures="resizable=yes, scrollbars=yes">
				<i18n:message bundleRef="ettx_bundle" key="submit"/>
				</uii:button> 
		     </td>

              <!----------------------------------------------------------------------------------------
                    <td nowrap> <uii:button submitValue="Reset">
					<i18n:message bundleRef="ettx_bundle" key="reset"/>
				</uii:button> 
		    </td>
		---------------------------->

                  </tr>
                </table>
              </td>
            </tr>
            <tr> 
              <td colspan="2" nowrap bgcolor="#cecfce" background="<%=contextPath%>/RSRC/en_US/images/vertlines_dk.gif"> 
                <uii:img page="/RSRC/en_US/images/shim.gif"/></td>
            </tr>
            </uii:layerContents>
		<uii:layerContents name="Switch"> 
            <tr> 
              <td nowrap bgcolor="#cecfce" align="right">&nbsp;<i18n:message bundleRef="ettx_bundle" key="switchDetails"/>:</td>
              <td width="100%" bgcolor="#cecfce"><uii:text property="switchDetails" /> </td>
            </tr>
            <tr> 
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
                    <td nowrap> <uii:button submitValue="Submit"
				popupWidth="1000" popupHeight="800"
				popupFeatures="resizable=yes, scrollbars=yes">
				<i18n:message bundleRef="ettx_bundle" key="submit"/>
				</uii:button> 
		     </td>

              <!----------------------------------------------------------------------------------------
                    <td nowrap> <uii:button submitValue="Reset">
					<i18n:message bundleRef="ettx_bundle" key="reset"/>
				</uii:button> 
		    </td>
		---------------------------->

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
                <p> <i18n:message bundleRef="instruction" key="troubleshoot.query.p1"/></p>
                <p> <i18n:message bundleRef="instruction" key="troubleshoot.query.p2"/></p>
                  </uii:instructions>
    </td>
  </tr>
</table>
