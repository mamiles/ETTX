package com.cisco.ettx.admin.gui.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.*;
import org.apache.struts.action.ActionForm;
import com.cisco.ettx.admin.gui.web.beans.ServiceInfoFormBean;
import com.cisco.ettx.admin.gui.web.datatypes.ServiceFeatureList;
import com.cisco.ettx.admin.gui.web.helper.SubscriberMgmtHelper;
import com.cisco.ettx.admin.gui.web.util.ETTXUtil;
import com.cisco.nm.uii.taglib.framework.Constants;

import java.util.*;

import java.text.ParseException;
import java.text.ParsePosition;
import org.apache.log4j.Logger;
import com.cisco.ettx.admin.common.AdminServicesException;

public class EditServiceParametersAction extends Action
{
	public static final String DEFAULT_FORWARD = "editServiceParms";
	public static final String ERROR_FORWARD = "closeWindowAndNotification";
	private static final String SUCCESS_MESSAGE = "CHANGED_SERVICE_PARAMETERS_MSG";
	public static final Logger logger = Logger.getLogger(EditServiceParametersAction.class);

	protected String getDefaultForward() {
		return DEFAULT_FORWARD;
	}

    public ActionForward perform(ActionMapping       mapping,
                                 ActionForm          form,
                                 HttpServletRequest  request,
                                 HttpServletResponse response)
    {       
         HttpSession the_session = request.getSession();

	ServiceInfoFormBean sysBean = (ServiceInfoFormBean)form;
	String action = request.getParameter("submit");
	logger.debug("In EditServiceParametersAction.perform " + action);
	if (action != null) {
		if (action.equals("Apply")) {
			//Get all the information about the subscriber and display
			try {
				String sessionID = ETTXUtil.getSessionID(request);
				Vector list = new Vector();
				list.add(sysBean.getService());
				SubscriberMgmtHelper.modifyServiceParameters(sessionID,sysBean.getSubscriberID(), list);
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
	    	sysBean.setSubscriberID((String)request.getAttribute(SubscriberMgmtAction.SUBSCRIBER_ID));
	    	sysBean.setService((ServiceFeatureList)request.getAttribute(SubscriberMgmtAction.SERVICE_FEATURE));
	}
	
	String screenID = mapping.getPath().substring(1);
	return mapping.findForward(screenID);
    }
}
