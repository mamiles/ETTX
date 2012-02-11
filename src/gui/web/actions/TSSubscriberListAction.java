package com.cisco.ettx.admin.gui.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.*;
import org.apache.struts.action.ActionForm;
import com.cisco.ettx.admin.gui.web.beans.SubscriberListFormBean;
import com.cisco.ettx.admin.gui.web.datatypes.TroubleshootRecord;
import com.cisco.ettx.admin.gui.web.helper.CollEngineHelper;
import com.cisco.ettx.admin.gui.web.util.ETTXUtil;
import com.cisco.nm.uii.taglib.framework.Constants;

import java.util.*;

import java.text.ParseException;
import java.text.ParsePosition;
import org.apache.log4j.Logger;
import com.cisco.ettx.admin.common.AdminServicesException;

public class TSSubscriberListAction extends Action
{
	public static final String DEFAULT_FORWARD = "tsSubsRecords";
	public static final String TROUBLESHOOT_ACTION = "Troubleshoot";
	public static final String TROUBLESHOOT_RESULT_FORWARD = "subsTSResult";
	public static final String ERROR_FORWARD = "closeWindowAndNotification";
	public static final String TS_RECORD = "subsTSResult";
	public static final Logger logger = Logger.getLogger(TSSubscriberListAction.class);

	protected String getDefaultForward() {
		return DEFAULT_FORWARD;
	}

    public ActionForward perform(ActionMapping       mapping,
                                 ActionForm          form,
                                 HttpServletRequest  request,
                                 HttpServletResponse response)
    {       
         HttpSession the_session = request.getSession();

	SubscriberListFormBean sysBean = (SubscriberListFormBean)form;
	String subsID = request.getParameter("action");
	logger.debug("In TSSubscriberListAction.perform " + subsID);
	if (subsID != null) {
		logger.debug("Getting troubleshoot data for subscriber " + subsID);
		try {
			String sessionID = ETTXUtil.getSessionID(request);
			TroubleshootRecord rec = CollEngineHelper.getTroubleshootDetails(sessionID,subsID);
			request.setAttribute(TS_RECORD,rec);
			return mapping.findForward(TROUBLESHOOT_RESULT_FORWARD);
		}
		catch (AdminServicesException ex) {
			ActionErrors aes = new ActionErrors();
			logger.error("Exception while performing the troubleshoot", ex);
			ActionError error = ETTXUtil.convertToError(ex);
			aes.add(Constants.ERROR,error);
			saveErrors(request,aes);
			return mapping.findForward(ERROR_FORWARD);
		}
	}
	//request.setAttribute("subscriberList", sysBean.getSubscriberList().iterator());
	//Set the Hide_TOC to true
	request.setAttribute("com.cisco.nm.uii.taglib.framework.Constants.HIDE_TOC", "true");
                    
	String screenID = mapping.getPath().substring(1);
	return mapping.findForward(screenID);
    }

}
