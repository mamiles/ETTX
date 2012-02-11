package com.cisco.ettx.admin.gui.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.*;
import org.apache.struts.action.ActionForm;
import com.cisco.ettx.admin.gui.web.beans.SystemConfigurationFormBean;

import java.util.*;

import java.text.ParseException;
import java.text.ParsePosition;
import org.apache.log4j.Logger;

import com.cisco.ettx.admin.common.SystemConfiguration;
import com.cisco.ettx.admin.common.AdminServicesException;
import com.cisco.ettx.admin.gui.web.util.ETTXUtil;
import com.cisco.nm.uii.taglib.framework.Constants;

public class SystemConfigurationAction extends Action 
{
	private Logger logger = Logger.getLogger(SystemConfigurationAction.class);
	public static final String DEFAULT_FORWARD = "systemConfig";
	public static final String APPLY_FORWARD = "systemConfig";
	public static final String APPLY_ACTION = "Apply";
	public static final String RESET_ACTION = "Reset";

	protected String getDefaultForward() {
		return DEFAULT_FORWARD;
	}

    public ActionForward perform(ActionMapping       mapping,
                                 ActionForm          form,
                                 HttpServletRequest  request,
                                 HttpServletResponse response)
    {       
	logger.debug("SystemConfigurationAction::perform()");
         HttpSession the_session = request.getSession();

	String action = request.getParameter("submit");
	ActionErrors aes = new ActionErrors();
	
        SystemConfigurationFormBean sysBean    = (SystemConfigurationFormBean) form;

	if (action != null) {
                String sessionID = ETTXUtil.getSessionID(request);
		if (action.equals(APPLY_ACTION)) {
			logger.debug("Save new SystemConfiguration");
			try {
				sysBean.saveNewConfiguration(sessionID);
			}
			catch (AdminServicesException ex) {
				logger.error("Exception while saving systemconfiguration ",ex);
				ActionError error = ETTXUtil.convertToError(ex);
				aes.add(Constants.ERROR,error);
				saveErrors(request,aes);
			}
			return mapping.findForward(APPLY_FORWARD);
		}
		if (action.equals(RESET_ACTION)) {
			logger.debug("Reset action");
		}
		//Follow thro. to the default action, which also resets to the old values
	}

	//Get the values from the Admin Server and reset Values
	try {
                String sessionID = ETTXUtil.getSessionID(request);
	       	sysBean.loadingSystemConfiguration(sessionID);
	}
	catch (AdminServicesException ex) {
		logger.error("Exception while loading system configuration " ,ex);
		ActionError error = ETTXUtil.convertToError(ex);
		aes.add(Constants.ERROR,error);
		saveErrors(request,aes);
	}

	return mapping.findForward(DEFAULT_FORWARD);
    }

}
