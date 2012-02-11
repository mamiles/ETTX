package com.cisco.ettx.admin.gui.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.*;
import org.apache.struts.action.ActionForm;
import com.cisco.nm.uii.taglib.framework.Constants;
import com.cisco.ettx.admin.gui.web.beans.QuerySubscribersFormBean;
import com.cisco.ettx.admin.gui.web.helper.CollEngineHelper;
import com.cisco.ettx.admin.gui.web.util.ETTXUtil;

import java.util.*;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.ParsePosition;
import com.cisco.ettx.admin.common.AdminServicesException;
import com.cisco.ettx.admin.common.AdminServicesNotification;

public class TSQuerySubscribersAction extends Action 
{
	public static final String DEFAULT_FORWARD = "tsQuerySubs";
	public static final String ERROR_FORWARD = "closeWindowAndNotification";
	public static final String APPLY_FORWARD = "tsSubsRecords";
	public static final String APPLY_ACTION = "Submit";
	public static final String RESET_ACTION = "Reset";
	public static final String SUBS_LIST = "subscriberList";
	private static Logger logger = Logger.getLogger(TSQuerySubscribersAction.class);

	protected String getDefaultForward() {
		return DEFAULT_FORWARD;
	}

    public ActionForward perform(ActionMapping       mapping,
                                 ActionForm          form,
                                 HttpServletRequest  request,
                                 HttpServletResponse response)
    {       
	System.out.println("QuerySubscribers.perform");
         HttpSession the_session = request.getSession();

	String action = request.getParameter("submit");
	System.out.println("QuerySubscribers.perform " + action);
         QuerySubscribersFormBean sysBean    = (QuerySubscribersFormBean) form;
                    
	if (action != null) {
		if (action.equals(APPLY_ACTION)) {
			//Perform query 
			try {
				Vector subsRecords = null;
				String sessionID = ETTXUtil.getSessionID(request);
				subsRecords = CollEngineHelper.getSubscriberRecords(sessionID,sysBean);
				//Got the records - set the request so that the list bean be filled up
				request.setAttribute(SUBS_LIST,subsRecords.iterator());
				return mapping.findForward(APPLY_FORWARD);
			}
			catch (AdminServicesException ex) {
				ActionErrors aes = new ActionErrors();
				logger.error("Exception while performing the query", ex);
				ActionError error = ETTXUtil.convertToError(ex);
				aes.add(Constants.ERROR,error);
				saveErrors(request,aes);
				return mapping.findForward(ERROR_FORWARD);
			}
			catch (AdminServicesNotification ex) {
				ActionErrors aes = new ActionErrors();
				logger.error("Exception while performing the query", ex);
				ActionError error = ETTXUtil.convertToError(ex);
				aes.add(Constants.INFO,error);
				saveErrors(request,aes);
				return mapping.findForward(ERROR_FORWARD);
			}

		}
		if (action.equals(RESET_ACTION)) {
			sysBean.resetValues();
			return mapping.findForward(DEFAULT_FORWARD);
		}
			
	}
	//Get the list of services
	action = request.getParameter("currentLayer");
	System.out.println("TSQuerySubscribers.currentLayer " + action);
	if (action != null && action.equals("Service")) {
		try {
			String sessionID = ETTXUtil.getSessionID(request);
			Vector services = CollEngineHelper.getServiceNames(sessionID);
			sysBean.setServiceList(services);
		}
		catch (AdminServicesException ex) {
			ActionErrors aes = new ActionErrors();
			logger.error("Exception while retrieving services " , ex);
			ActionError error = ETTXUtil.convertToError(ex);
			aes.add(Constants.ERROR,error);
			saveErrors(request,aes);
		}
	}
	else {
		sysBean.resetValues();
	}
	return mapping.findForward(DEFAULT_FORWARD);
    }

}
