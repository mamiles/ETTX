package com.cisco.ettx.admin.gui.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.*;
import org.apache.struts.action.ActionForm;
import com.cisco.ettx.admin.gui.web.beans.SubscriberListFormBean;
import com.cisco.ettx.admin.gui.web.beans.QuerySubscribersFormBean;
import com.cisco.ettx.admin.gui.web.datatypes.SubscriberRecord;
import com.cisco.ettx.admin.gui.web.helper.CollEngineHelper;
import com.cisco.ettx.admin.gui.web.helper.SubscriberMgmtHelper;
import com.cisco.ettx.admin.gui.web.util.ETTXUtil;
import com.cisco.nm.uii.taglib.framework.Constants;

import java.util.*;

import java.text.ParseException;
import java.text.ParsePosition;
import org.apache.log4j.Logger;
import com.cisco.ettx.admin.common.AdminServicesException;
import com.cisco.ettx.admin.common.AdminServicesNotification;

public class SubscriberMgmtAction extends Action
{
	public static final String DEFAULT_FORWARD = "smViewSubs";
	public static final String EDIT_PROFILE_FWD = "editSubsProfile";
	public static final String DELETE_SUBS_FWD = "delete";
	public static final String EDIT_SERVICE_FWD = "editServiceParms";
	public static final String VIEW_SUBS_DEVICES_FWD = "viewSubsDevices";
	public static final String ERROR_FORWARD = "closeWindowAndNotification";
	public static final String SUBS_RECORD = "subscriberRecord";
	public static final String SUBSCRIBER_DELETED = "SUBSCRIBER_DELETED_MSG";
	public static final String NO_SERVICE_FOR_SUBSCRIBER = "NO_SERVICE_FOR_SUBSCRIBER_MSG";
	public static final String OPTION_82_ENABLED_MSG = "OPTION_82_ENABLED_MSG";
	public static final String SERVICE_FEATURE = "serviceFeature";
	public static final String SUBSCRIBER_ID = "subscriberID";
	public static final String SUBS_DEVICES = "subsDevices";
	public static final String SUBS_LIST = "subscriberList";

	public static final String REFRESH = "Refresh";
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

	SubscriberListFormBean sysBean = (SubscriberListFormBean)form;
	String action = request.getParameter("submit");
	logger.debug("In SubscriberMgmtAction.perform " + action);
	if (action != null) {
		String subsId = sysBean.getSelectedSubsFDN();
		if (subsId == null) {
			ActionErrors aes = new ActionErrors();
			aes.add(Constants.ERROR,new ActionError("selectSubsErrorMsg"));
			saveErrors(request,aes);
			return mapping.findForward(ERROR_FORWARD);
		}
		if (action.equals("Modify Profile")) {
			//Get all the information about the subscriber and display
			logger.debug("Getting information for the subscriber " + subsId);
			try {
				String sessionID = ETTXUtil.getSessionID(request);
				SubscriberRecord rec = CollEngineHelper.getSubscriberInfo(sessionID,subsId);
				request.setAttribute(SUBS_RECORD,rec);
				return mapping.findForward(EDIT_PROFILE_FWD);
			}
			catch (AdminServicesException ex) {
				ActionErrors aes = new ActionErrors();
				logger.error("Exception while getting subscriber details for " + subsId,ex);
				ActionError error = ETTXUtil.convertToError(ex);
				aes.add(Constants.ERROR,error);
				saveErrors(request,aes);
				return mapping.findForward(ERROR_FORWARD);
			}
		}

		if (action.equals("Modify Service")) {
			//Get the service parameters about the subscriber and display
			try {
				String sessionID = ETTXUtil.getSessionID(request);
				Vector serviceParams = CollEngineHelper.getServiceFeatureList(sessionID,subsId);
				if (serviceParams.size() <= 0) {
					//No services available for modification
					logger.info("No services available for subscriber " + subsId);
					ActionErrors aes = new ActionErrors();
					aes.add(Constants.INFO,new ActionError(NO_SERVICE_FOR_SUBSCRIBER));
					saveErrors(request,aes);
					return mapping.findForward(ERROR_FORWARD);
				}
				if (serviceParams.size() > 1) {
					logger.warn("Assuming only one service for subscriber");
				}
				request.setAttribute(SERVICE_FEATURE,serviceParams.elementAt(0));
				request.setAttribute(SUBSCRIBER_ID,subsId);
				return mapping.findForward(EDIT_SERVICE_FWD);
			}
			catch (AdminServicesException ex) {
				ActionErrors aes = new ActionErrors();
				logger.error("Exception while getting subscriber service parameters for " + subsId,ex);
				ActionError error = ETTXUtil.convertToError(ex);
				aes.add(Constants.ERROR,error);
				saveErrors(request,aes);
				return mapping.findForward(ERROR_FORWARD);
			}
		}

		if (action.equals("View MAC Addresses")) {
			String sessionID = ETTXUtil.getSessionID(request);
			request.setAttribute(SUBSCRIBER_ID,subsId);
			return mapping.findForward(VIEW_SUBS_DEVICES_FWD);

		}

		if (action.equals("Delete")) {
			//Delete the subscriber
			try {
				String sessionID = ETTXUtil.getSessionID(request);
				SubscriberMgmtHelper.deleteSubscriber(sessionID,subsId);
				ActionErrors aes = new ActionErrors();
				aes.add(Constants.INFO, new ActionError(SUBSCRIBER_DELETED));
				saveErrors(request,aes);
				return mapping.findForward(ERROR_FORWARD);
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
		if (action.equals(REFRESH) ||
			action.equals(SMQuerySubscribersAction.SUBMIT_ACTION)) {
			try {
				if (action.equals(SMQuerySubscribersAction.SUBMIT_ACTION)) {
					QuerySubscribersFormBean bean = 
						(QuerySubscribersFormBean)request.getAttribute(SMQuerySubscribersAction.QUERY_PARMS);
					sysBean.save(bean);
				}
				Vector subsRecords = null;
				String sessionID = ETTXUtil.getSessionID(request);

				System.out.println ("Performing the query.");
				subsRecords = CollEngineHelper.getSubscriberRecords(sessionID,sysBean);
				//Got the records - set the request so that the list bean be filled up
				request.setAttribute(SUBS_LIST,subsRecords);
				sysBean.setSubscriberList(subsRecords);
				return mapping.findForward(DEFAULT_FORWARD);
				
			}
			catch (AdminServicesException ex) {
				ActionErrors aes = new ActionErrors();
				ActionError error = ETTXUtil.convertToError(ex);
				aes.add(Constants.ERROR,error);
				saveErrors(request,aes);
				return mapping.findForward(ERROR_FORWARD);
			}
			catch (AdminServicesNotification ex) {
				ActionErrors aes = new ActionErrors();
				ActionError error = ETTXUtil.convertToError(ex);
				aes.add(Constants.INFO,error);
				saveErrors(request,aes);
				return mapping.findForward(ERROR_FORWARD);
			}

			catch (OutOfMemoryError ex) {
			    // We ran out of memory.  If we ever have a hope of doing anything,
			    // We should suggest running the garbage collector to restore some
			    // memory.  We'll sleep for a second to give it time to run.
			    System.gc();
			    try {
				Thread.sleep (1000);
			    }
			    catch (InterruptedException e) {
			    }

			    ex.printStackTrace();
			    ActionErrors aes = new ActionErrors();
			    logger.error("SMQuerySubscribersAction - Out of memory error.");
			    ActionError error = new ActionError ("Out of memory encountered retreiving data.");
			    aes.add(Constants.ERROR,error);
			    saveErrors(request,aes);
			    return mapping.findForward(ERROR_FORWARD);
			}
		}
	}
	request.setAttribute(SUBS_LIST,sysBean.getSubscriberList());
	String screenID = mapping.getPath().substring(1);
	return mapping.findForward(screenID);
    }
}
