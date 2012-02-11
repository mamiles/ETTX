package com.cisco.ettx.admin.gui.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.*;
import com.cisco.ettx.admin.gui.web.beans.LeaseHistoryQueryFormBean;
import com.cisco.ettx.admin.gui.web.helper.CollEngineHelper;
import com.cisco.ettx.admin.gui.web.util.ETTXUtil;
import com.cisco.nm.uii.taglib.framework.Constants;

import java.util.*;
import org.apache.log4j.Logger;
import com.cisco.ettx.admin.common.AdminServicesException;
import com.cisco.ettx.admin.common.AdminServicesNotification;

public class LeaseHistoryQueryAction extends Action 
{
    public static final String BASE_NAME = "com.cisco.ettx.admin.i18n.ettx";
    public static final String DEFAULT_FORWARD = "leaseHistoryQuery";
    public static final String APPLY_FORWARD = "leaseHistoryRecords";
    public static final String SUBMIT_ACTION = "Submit";
    public static final String LEASE_RECORDS = "leaseHistoryRecords";
	public static final String ERROR_FORWARD = "closeWindowAndNotification";
    private static final Logger logger = Logger.getLogger(LeaseHistoryQueryAction.class);

    protected String getDefaultForward() {
	return DEFAULT_FORWARD;
    }

    public ActionForward perform(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest  request,
				 HttpServletResponse response)
    {       
	StringBuffer errorMsg = new StringBuffer();
	HttpSession the_session = request.getSession();

	String action = request.getParameter("submit");
	logger.debug("LeaseHistoryQueryAction.perform " + action);

	LeaseHistoryQueryFormBean sysBean = (LeaseHistoryQueryFormBean) form;
                    
	if (action != null) {
	    if (action.equals(SUBMIT_ACTION)) {
		logger.debug("Getting lease records for query ");
		try {
				String sessionID = ETTXUtil.getSessionID(request);
				if (sysBean.getCurrentLayer().equals(LeaseHistoryQueryFormBean.SUBSCRIBER)) {
					Vector recs = CollEngineHelper.getLeaseHistory(sessionID,
						sysBean.getLoginID(),
						null,
						sysBean.getStartPeriod(),
						sysBean.getEndPeriod());
					request.setAttribute(LEASE_RECORDS,recs);
				}
				else {
					Vector recs = CollEngineHelper.getLeaseHistory(sessionID,
						null,
						sysBean.getIpAddress(),
						sysBean.getStartPeriod(),
						sysBean.getEndPeriod());
					request.setAttribute(LEASE_RECORDS,recs);
				}
		    		return mapping.findForward(APPLY_FORWARD);
			}
			catch (AdminServicesException ex) {
				ActionErrors aes = new ActionErrors();
				logger.error(ETTXUtil.localizeMessage ("leaseQueryExceptionPerformingTroubleshoot"), ex);
				ActionError error = ETTXUtil.convertToError(ex);
				aes.add(Constants.ERROR,error);
				saveErrors(request,aes);
				return mapping.findForward(ERROR_FORWARD);
			}
			catch (AdminServicesNotification ex) {
				ActionErrors aes = new ActionErrors();
				logger.error(ETTXUtil.localizeMessage ("leaseQueryExceptionPerformingTroubleshoot"), ex);
				ActionError error = ETTXUtil.convertToError(ex);
				aes.add(Constants.INFO,error);
				saveErrors(request,aes);
				return mapping.findForward(ERROR_FORWARD);
			}

	    }
	}


	return mapping.findForward(DEFAULT_FORWARD);
    }

/*
    private boolean ValidQueryFormBean (LeaseHistoryQueryFormBean sysBean,
					StringBuffer errorMsg) 
    {
	boolean validQuery = true;

	if (sysBean.getStartTime().length() == 0) {
	    errorMsg.append (ETTXUtil.localizeMessage ("InvalidStartTimeMsg") + sysBean.getStartTime());
	    validQuery = false;
	}

	if (sysBean.getEndTime().length() == 0) {
	    if (validQuery == false) {
		// errorMsg.append ("\n");
	    }
	    errorMsg.append (ETTXUtil.localizeMessage("InvalidEndTimeMsg") + sysBean.getEndTime());
	    validQuery = false;
	}

	if (sysBean.getCurrentLayer().compareTo ("Subscriber") == 0 &&
	    sysBean.getSubscriberName().length() == 0) {
	    if (validQuery == false) {
		// errorMsg.append ("\n");
	    }
	    errorMsg.append (ETTXUtil.localizeMessage("InvalidSubscriberName") + sysBean.getSubscriberName());
	    validQuery = false;
	}

	if (sysBean.getCurrentLayer().compareTo ("IpAddress") == 0 &&
	    sysBean.getIpAddress().length() == 0) {
	    if (validQuery == false) {
		// errorMsg.append ("\n");
	    }
	    errorMsg.append (ETTXUtil.localizeMessage("InvalidIpAddress") + sysBean.getIpAddress());
	    validQuery = false;
	}

	return validQuery;
    }
*/
}
