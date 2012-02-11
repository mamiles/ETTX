package com.cisco.ettx.admin.gui.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.*;
import org.apache.struts.action.ActionForm;
import com.cisco.ettx.admin.gui.web.beans.QuerySubscribersFormBean;
import com.cisco.ettx.admin.gui.web.helper.CollEngineHelper;
import com.cisco.ettx.admin.gui.web.util.ETTXUtil;
import com.cisco.nm.uii.taglib.framework.Constants;

import java.util.*;
import com.cisco.ettx.admin.common.AdminServicesException;
import com.cisco.ettx.admin.common.AdminServicesNotification;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.ParsePosition;

public class RepQuerySubscribersAction extends Action 
{
	public static final String DEFAULT_FORWARD = "querySubs";
	public static final String ERROR_FORWARD = "closeWindowAndNotification";
	public static final String APPLY_FORWARD = "subsRecords";
	public static final String SUBMIT_ACTION = "Submit";
	private static Logger logger = Logger.getLogger(RepQuerySubscribersAction.class);

	public static final String SUBS_LIST = "subscriberList";
	protected String getDefaultForward() {
		return DEFAULT_FORWARD;
	}

	public ActionForward perform(ActionMapping mapping,
		ActionForm form,
		HttpServletRequest  request,
		HttpServletResponse response)
	{       
		HttpSession the_session = request.getSession();

		String action = request.getParameter("submit");
		logger.debug("RepQuerySubscribers.perform " + action);
         	QuerySubscribersFormBean sysBean = (QuerySubscribersFormBean) form;
                    
		if (action != null) {
			if (action.equals(SUBMIT_ACTION)) {
				try {
					Vector subsRecords = null;
					String sessionID = ETTXUtil.getSessionID(request);

					System.out.println ("Performing the query.");
					subsRecords = CollEngineHelper.getSubscriberRecords(sessionID,sysBean);
					//Got the records - set the request so that the list bean be filled up
					request.setAttribute(SUBS_LIST,subsRecords);
					return mapping.findForward(APPLY_FORWARD);
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
				    logger.error("RepQuerySubscribersAction - Out of memory error.");
				    ActionError error = new ActionError ("Out of memory encountered retreiving data.");
				    aes.add(Constants.ERROR,error);
				    saveErrors(request,aes);
				    return mapping.findForward(ERROR_FORWARD);
				}
			}
			return mapping.findForward(DEFAULT_FORWARD);
		}
		//Get the list of services
		action = request.getParameter("currentLayer");
		System.out.println("RepQuerySubscribers.currentLayer " + action);
		if (action != null && action.equals("Service")) {
			try {
				String sessionID = ETTXUtil.getSessionID(request);
				Vector services = CollEngineHelper.getServiceNames(sessionID);
				sysBean.setServiceList(services);
			}
			catch (AdminServicesException ex) {
				ActionErrors aes = new ActionErrors();
				logger.error(ETTXUtil.localizeMessage ("querySubscriberServiceRetrievalException") , ex);
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
