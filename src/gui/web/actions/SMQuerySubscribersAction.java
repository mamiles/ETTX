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
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.ParsePosition;

public class SMQuerySubscribersAction extends Action
{
	public static final String DEFAULT_FORWARD = "smQuerySubs";
	public static final String ERROR_FORWARD = "closeWindowAndNotification";
	public static final String APPLY_FORWARD = "smViewSubs";
	public static final String CREATE_SUB = "subDetails";
	public static final String CREATE_ACTION = "Create";
	public static final String SUBMIT_ACTION = "Submit";
	private static Logger logger = Logger.getLogger(SMQuerySubscribersAction.class);

	public static final String QUERY_PARMS = "subscriberList";
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
		logger.debug("SMQuerySubscribers.perform " + action);
         	QuerySubscribersFormBean sysBean = (QuerySubscribersFormBean) form;

		if (action != null) {
			if (action.equals(SUBMIT_ACTION)) {
				//Set sysBean in the request and send to the action
				request.setAttribute(QUERY_PARMS, sysBean);
				return mapping.findForward(APPLY_FORWARD);

			}
			if (action.equals(CREATE_ACTION)) {
				return mapping.findForward(CREATE_SUB);
			}
			return mapping.findForward(DEFAULT_FORWARD);
		}
		sysBean.resetValues();
		return mapping.findForward(DEFAULT_FORWARD);
	}
}
