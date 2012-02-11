

function setSelectWithString(aSelect, aStr) {
	for(i=0; i < aSelect.options.length; i++) {
		if(aSelect.options[i].value == aStr) {
			aSelect.selectedIndex = i;
		}
	}
}

function setFieldWithString(aField, aStr) {
    aField.value = aStr;
}



// Used by disableWizardCancelAndBackValidation()
function checkName(anId, anAction) {
    // If the Cancel or Back buttons are pressed we do not need to do any validation.
    if ( anAction == 'Cancel' || anAction == 'Back') {
        document.getElementById('wizAction').value = anAction;
        document.getElementById('wizid').value = anId;
	    UIIFormSubmit('wizSubmit', 'true');
        return false;
    }
    
    // otherwise return true
    return true;
}
function disableWizardCancelAndBackValidation() {
    // Following line hooks up the user defined wizard validation function to the
    // UII submit process. 'wizardItemHandlers' is an array of functions that will
    // be evaluated when a wizard button or wizard item is clicked on.
    //
    // Variable names 'wizid', and 'wizAction' have to be used exactly.
    wizardItemHandlers.push( "checkName(wizid, wizAction)" );
}





//------------------------------------------------------------------
// TWO FUNCTIONS FOR VALIDATING USER INPUT.
//    THE FORM MUST CONTAIN THE FOLLOWING HIDDEN ATTRIBUTE THAT
//    LISTS THE FIELDS TO VALIDATE:
//      <uii:hidden property="validationList" value="customer.id" />
//
//    THERE IS AN OPTIONAL HIDDEN FIELD TO CHANGE THE REGEXPR:
//      <uii:hidden property="validationRegExp" value="^[a-z]+$" />
//
//------------------------------------------------------------------
function initFieldValidation() {
    if (parseFloat(navigator.appVersion) >= 4) {
		if (navigator.appName=="Netscape") {
			document.captureEvents(Event.KEYPRESS);
		}
		document.onkeypress = validateKey
	}
}
function validateKey(e) {
	var theKey, theName
	if (navigator.appName == "Netscape") {
		theKey  = e.which;
		theName = e.target.name;
	} else {
		theKey  = window.event.keyCode;
		theName = window.event.srcElement.name;
	}

	if (theKey==13) {
		return true;
	}
	
	var theFieldsToCheck = document.getElementById("validationList").value;
	
	if (theFieldsToCheck.indexOf(theName) >= 0) {
	    // Convert theKey from ascii to a char
		theKey = String.fromCharCode(theKey);
		
		// If a validationRegExp has been set then use it, otherwise use the default one.
		var theRE;
		if(document.getElementById("validationRegExp") != null) {
		    theRE = document.getElementById("validationRegExp").value;
		} else {
		    theRE = "^[a-zA-Z0-9_\\-\\@ ]+$";
		}
		
		theRE = new RegExp(theRE);
		
		if(!theRE.test(theKey)) {
		    alert('Invalid character: "' + theKey + '"');
		    return false;
		} else {
		    return true;
		}
	}
	return true;
}