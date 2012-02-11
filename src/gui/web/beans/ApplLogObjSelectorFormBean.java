package com.cisco.ettx.admin.gui.web.beans;
//**************************************************
// Copyright (c) 2002 Cisco Systems, Inc.
// All rights reserved.
//**************************************************

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * The Struts controller will initialize this JavaBean's properties
 * from the corresponding JSP's form request parameters
 * @version  1.0 
 * @author  Marvin Miles
 */
public final class ApplLogObjSelectorFormBean extends ActionForm {
   private String searchType = "allFiles";
   private String searchString = "";
      
   public String getSearchType() {
	return this.searchType;
   }

   public void setSearchType(String searchType)	{
	this.searchType = searchType;
   }
   
   public String getSearchString() {
   	return this.searchString;
   }
   
   public void setSearchString(String searchString) {
   	this.searchString = searchString;
   }
   

   public ActionErrors validate( ActionMapping map, HttpServletRequest req ) {
     //ActionErrors ae = new ActionErrors();
     //return ae;
     return (null);
   }
}
