package com.cisco.ettx.admin.gui.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.*;
import org.apache.struts.action.ActionForm;
import com.cisco.nm.uii.taglib.framework.Constants;

import com.cisco.ettx.admin.gui.web.beans.ViewSubscriberRecordFormBean;
import com.cisco.ettx.admin.gui.web.datatypes.SubscriberRecord;
import com.cisco.ettx.admin.gui.web.helper.CollEngineHelper;
import com.cisco.ettx.admin.gui.web.util.ETTXUtil;

import java.util.*;
import org.apache.log4j.Logger;
import com.cisco.ettx.admin.common.AdminServicesException;

public class ViewSubscriberRecordAction extends Action
{
    public static final String DEFAULT_FORWARD = "viewSubscriberRecord";
	public static final String ERROR_FORWARD = "closeWindowAndNotification";
    public static final Logger logger = Logger.getLogger(ViewSubscriberRecordAction.class);

    protected String getDefaultForward() {
	return DEFAULT_FORWARD;
    }

    public ActionForward perform(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest  request,
				 HttpServletResponse response)
    {       
	HttpSession the_session = request.getSession();

	String subscriberId = request.getParameter("subscriberId");
                    
	ViewSubscriberRecordFormBean sysBean = (ViewSubscriberRecordFormBean) form;
	logger.debug("Getting details for subscriber " + subscriberId);
	try {
		String sessionID = ETTXUtil.getSessionID(request);
		SubscriberRecord rec = CollEngineHelper.getSubscriberDetails(sessionID,subscriberId);
		sysBean.setSubscriberRecord(rec);
		return mapping.findForward(DEFAULT_FORWARD);
	}
	catch (AdminServicesException ex) {
		ActionErrors aes = new ActionErrors();
		logger.error("Exception while getting details for subscriber " + subscriberId, ex);
		ActionError error = ETTXUtil.convertToError(ex);
		aes.add(Constants.ERROR,error);
		saveErrors(request,aes);
		//REVISIT
		return mapping.findForward(ERROR_FORWARD);
	}

    }
}
