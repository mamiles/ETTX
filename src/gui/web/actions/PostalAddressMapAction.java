package com.cisco.ettx.admin.gui.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.*;
import org.apache.struts.action.ActionForm;
import com.cisco.ettx.admin.gui.web.beans.PostalAddressMapFormBean;
import com.cisco.ettx.admin.gui.web.datatypes.PostalAddressMap;
import com.cisco.ettx.admin.gui.web.helper.CollEngineHelper;
import com.cisco.ettx.admin.gui.web.util.ETTXUtil;
import com.cisco.nm.uii.taglib.framework.Constants;

import java.util.*;

import java.text.ParseException;
import java.text.ParsePosition;
import org.apache.log4j.Logger;
import com.cisco.ettx.admin.common.AdminServicesException;
import com.cisco.ettx.admin.common.AdminServicesNotification;

public class PostalAddressMapAction extends Action
{
	public static final String DEFAULT_FORWARD = "queryAddressMap";
	public static final String QUERY_FORWARD = "viewAddressMap";
	public static final String ERROR_FORWARD = "closeWindowAndNotification";
	public static final String MAP_LIST = "addressMapList";
	public static final String SUBMIT_ACTION = "Submit";

	public static final Logger logger = Logger.getLogger(PostalAddressMapAction.class);

	protected String getDefaultForward() {
		return DEFAULT_FORWARD;
	}

    public ActionForward perform(ActionMapping       mapping,
                                 ActionForm          form,
                                 HttpServletRequest  request,
                                 HttpServletResponse response)
    {       
         HttpSession the_session = request.getSession();

	PostalAddressMapFormBean sysBean = (PostalAddressMapFormBean)form;
	String action = request.getParameter("submit");
	logger.debug("In PostalAddressMapAction.perform " + action);
	if (action != null) {
		if (action.equals(SUBMIT_ACTION)) {
			try {
				Vector postalRecords = null;
				String sessionID = ETTXUtil.getSessionID(request);

				postalRecords = CollEngineHelper.queryPostalAddressMap(sessionID,sysBean);
				//Got the records - set the request so that the list bean be filled up
				request.setAttribute(MAP_LIST,postalRecords);
				return mapping.findForward(QUERY_FORWARD);
				
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
	String screenID = mapping.getPath().substring(1);
	return mapping.findForward(screenID);
    }
}
