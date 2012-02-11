package com.cisco.ettx.admin.gui.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.*;
import org.apache.struts.action.ActionForm;
import com.cisco.ettx.admin.gui.web.beans.SubscriberInfoFormBean;
import com.cisco.ettx.admin.gui.web.datatypes.SubscriberRecord;
import com.cisco.ettx.admin.gui.web.helper.SubscriberMgmtHelper;
import com.cisco.ettx.admin.gui.web.util.ETTXUtil;
import com.cisco.nm.uii.taglib.framework.Constants;

import java.util.*;

import java.text.ParseException;
import java.text.ParsePosition;
import org.apache.log4j.Logger;
import com.cisco.ettx.admin.common.AdminServicesException;

public class EditSubscriberProfileAction extends Action
{
	public static final String DEFAULT_FORWARD = "editSubsProfile";
	public static final String ERROR_FORWARD = "closeWindowAndNotification";
	private static final String SUCCESS_MESSAGE = "CHANGED_SUBSCRIBER_PROFILE_MESSAGE";
	public static final Logger logger = Logger.getLogger(SubscriberMgmtAction.class);

	protected String getDefaultForward() {
		return DEFAULT_FORWARD;
	}

    public ActionForward perform(ActionMapping       mapping,
                                 ActionForm          form,
                                 HttpServletRequest  request,
                                 HttpServletResponse response)
    {       
         HttpSession the_session = request.getSession();

	SubscriberInfoFormBean sysBean = (SubscriberInfoFormBean)form;
	String action = request.getParameter("submit");
	logger.debug("In EditSubscriberProfileAction.perform " + action);
	if (action != null) {
		if (action.equals("Apply")) {
			//Get all the information about the subscriber and display
			try {
				String sessionID = ETTXUtil.getSessionID(request);
				SubscriberMgmtHelper.modifySubscriberProfile(sessionID,
					sysBean.getSubscriberRecord());
				//Save the subscriber info
				ActionErrors aes = new ActionErrors();
				aes.add(Constants.INFO,new ActionError(SUCCESS_MESSAGE));
				saveErrors(request,aes);
				return mapping.findForward(ERROR_FORWARD);
			}
			catch (AdminServicesException ex) {
				ActionErrors aes = new ActionErrors();
				logger.error("Exception while performing the subscriber profile modification", ex);
				ActionError error = ETTXUtil.convertToError(ex);
				aes.add(Constants.ERROR,error);
				saveErrors(request,aes);
				return mapping.findForward(DEFAULT_FORWARD);
			}
		}
		else if (action.equals("Cancel")) {
			return mapping.findForward(ERROR_FORWARD);
		}
	    	sysBean.setSubscriberRecord((SubscriberRecord)request.getAttribute(SubscriberMgmtAction.SUBS_RECORD));
	}
	
	String screenID = mapping.getPath().substring(1);
	return mapping.findForward(screenID);
    }
}
