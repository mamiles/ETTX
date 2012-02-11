var validator=new Object();

validator.addValidation=function(widgetName,code){
    eval("validator[\""+widgetName+"\"]=function(widget){return "+code+"}");
}

/*
validator["Form0Tf1"]=function(v){
   chackMandatoy(v)
}
validator["Form0Tf2"]=function(v){
   checkTextRange(v,4,7); 
}

validator["Form0Tf3"]=function(v){
   if(checkIntRange(v,4,7) || checkIntRange(v,10,15) || checkIntRange(v,20,30)) 
	return true;
}

validator["Form0Tf2"]=function(v){
   var arr=new Array(  new Array(4,7),
		    new Array(12,15))
   checkTextRangeArray(v,arr);	
}
validator.addValidation("Form0Tf2","checkTextRangeArray(widget,new Array(  new Array(4,7),new Array(12,15)));");
*/

function checkIntDataType(v){
    if(v.value!=null && v.value!=""){
        val = parseInt(v.value);
        if(val!=v.value) {//isNaN(val) returns false when v starts with number. Eg:v=123WQQ.
            alert("Value should be a number.");
            return false;
        }
    }
    return true;
}

function checkIntRange(val,r1,r2){
    if(val>=r1&&val<=r2){
        return true;
    }else{
        return false;
    }
}

function checkIntRangeArray(v,rangeArray){
//    var val = checkIntDataType(v)
    
    if(!checkIntDataType(v)){
        return false;
    }
    val = v.value;
    rangeMsg="Value is out of range: ";
    for(i=0; i<rangeArray.length;i++){
        var r1 = rangeArray[i][0];
        var r2 = rangeArray[i][1];
        var retvalue=checkIntRange(val,r1,r2);
        if(retvalue){
            return retvalue;
        }else{
            rangeMsg=rangeMsg + "\nMin: "+r1+" & Max:"+r2
        }
    }
    alert(rangeMsg);
    return false;
}

function checkTextRangeArray(v,rangeArray){
    rangeMsg="Value is out of range: ";

    var val=v.value.length;

    for(i=0; i<rangeArray.length;i++){
        var r1 = rangeArray[i][0];
        var r2 = rangeArray[i][1];
        var retvalue=checkTextRange(val,r1,r2);
        if(retvalue){
            return retvalue;
        }
        else{
            rangeMsg=rangeMsg + "\nMin: "+r1+" & Max:"+r2
        }
    }

    alert(rangeMsg);
    return false;
}

function checkTextRange(val,r1,r2){
    if(val>=r1&&val<=r2){
        return true;
    }else{
        return false;
    }
}

function    chackMandatoy(v){
    if(v.value.length>0){
        return true;
    }
    else alert("Mandatory");
    return false;
}

function handleWidgetBlur(widget, formName){
    if(!formName){
        formName="";
    }
    var widgetName=widget.name+formName;
    
    //See if there is a custom Handler for this widget.
    var customHandler =  "window.handleWidgetBlur"+widgetName;
    if(typeof eval(customHandler) != "undefined"){
	    var customReturnValue=eval(customHandler+'(widget)');
	    if(!customReturnValue)return false;
    }

    eval("var fn = validator."+widgetName)
    if(fn){
        eval("var ret = fn(widget);")
        if(ret==false){ return ret;}
    }
    
    eval("var dfn = dependencies."+widgetName)
    if(dfn){
        eval("var dret=dfn.recompute(widget);")
    }
    
    if(window.setSummary)
	    setSummary(widget);
    return true;
}


var dependencies = new Object();

dependencies.createDependency=function(arg){
   dependencies[arg]=new Dependent();
}
function Dependent(){
	this.recompute= function(widget){
		for(i=0;i<this.params.length;i++){
			eval("var ret=recompute"+this.params[i]+"(widget)");
		}
	}
	this.addDependents=function(){
		this.params=arguments;
	}
}

function disable(widget){
   if(document.all||document.getElementById){
	widget.disabled=true;
   }else{
   widget.onFocus=widget.blur;
   }
}

function enable(widget){
  if(document.all||document.getElementById){
	widget.disabled=false;       
  }else{
       widget.onFocus="";
  }
}
function recomputeVtpDomainNameForm1(widget){
    if(widget.selectedIndex ==2){
        disable(document.Form1.VtpDomainName);
    }else{
        enable(document.Form1.VtpDomainName);
    }
}

var treeBrowserHandle=-1;
function browseForValue(widgetName,formName,objType){
    
    if(treeBrowserHandle!=-1 && typeof treeBrowserHandle!="undefined")treeBrowserHandle.close();
    var props="height=400, width=300, location=no, toolbar=no, scrollbars=yes, menubar=no,status=yes,top=100,left=" + (screen.width-450);    
browserFilter=eval("document.forms."+formName+"."+widgetName+".browserFilter");
if(browserFilter=="undefined"){
browserFilter="";
}
//alert("customer:"+eval("document.forms."+formName+"."+widgetName+".browserFilter"));
//------------
//construct broserFilter XML and pass it to the browse.do action.
//Just pass the value of customer in browserFilter for testing....

    treeBrowserHandle = window.open("/ipopt/browse.do?svcObjType="+objType+"&browserFilter="+browserFilter,null, props);
    treeBrowserHandle.widgetName=widgetName;
    treeBrowserHandle.formName=formName;
    treeBrowserHandle.objType=objType;
    window.onfocus=function(){
        if(treeBrowserHandle!=-1 && typeof treeBrowserHandle!="undefined"){
            treeBrowserHandle.focus();
        }
    }
	//alert(widget.name);
}

function setValueFromBrowser(widgetName,formName,value){
    var expr='document.forms["'+formName+'"].'+widgetName+'.value="'+value+'"'  ;
    eval(expr);
    return true;
}

function isCombo(comp){
    if(comp.type.indexOf("select")>=0){
        return true;
    }else {
        return false;
    }
}
function isCheckBox(comp){
    if(comp.type.indexOf("checkbox")>=0){
        return true;
    }
    return false;
}


<!--
//These functions are for Generic List Custom Component.
function onChange(selectList,currentText) {
    var Current = selectList.selectedIndex;
    currentText.value = selectList.options[Current].text;
}
function deleteOption(selectList) {
    var Current = selectList.selectedIndex;
    selectList.options[Current] = null;
}
function addOption(selectList,currentText) {
    var defaultSelected = false;
    var selected = false;
    var optionName = new Option(currentText.value, currentText.value, defaultSelected, selected)
    var length = selectList.length;
    selectList.options[length] = optionName;
}
function replaceOption(selectList,currentText) {
    var Current = selectList.selectedIndex;
    selectList.options[Current].text = currentText.value;
    selectList.options[Current].value = currentText.value;
}

//-->



//These functions are for Showing messages.
var messageHandle=-1
function messageBox(msg,title){
    message=msg;
    windowTitle=title;
    if(!windowTitle)windowTitle="Message Box";
    messageHandle=window.open("about:blank","","resizable=yes,status=yes,scrollbars=yes,height=200,width=400,left=300,top=200");
    setTimeout('updateMsg()',200);
    
    window.onfocus=function(){
        if(messageHandle!=-1){
            messageHandle.focus();
        }
    }
}
function updateMsg(msg){
    doc=messageHandle.document;
    doc.open('text/html');
    doc.write('<html><head><title>'+windowTitle+'</title></head><body bgcolor="white"  onUnload="opener.messageHandle=-1">')
    doc.write('<table border=1 bordercolor="blue" cellspacing=0 cellpadding=0 width="100%" height="80%" align="center" valign="center"><tr><td width="100%" height="100%">');
    doc.write(message);
    doc.write('</td></tr></table>');
    
	doc.write('<br><center><form name="viewn"><input type="button" value="Close" onClick="self.close()"><\/form></center><\/BODY><\/HTML>');
    doc.close();
	
    messageHandle.focus();
}



function include(src){
    var path="/ipopt/";
    var str='<script language="Javascript1.2" src="../%27%2Bpath%2Bsrc%2B%27"><\/script>';
    document.write(str);
}
