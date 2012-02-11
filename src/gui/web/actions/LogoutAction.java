package com.cisco.ettx.admin.gui.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.*;
import org.apache.struts.action.ActionForm;
import com.cisco.ettx.admin.gui.web.helper.AuthenticateHelper;
import com.cisco.ettx.admin.gui.web.util.ETTXUtil;
import com.cisco.nm.uii.taglib.framework.Constants;

import java.util.*;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.ParsePosition;

public class LogoutAction extends Action
{
	public static final String OK_FORWARD = "loggedout";
	public static final String CANCEL_FORWARD = "cancel";
	public static final String OK_ACTION = "Ok";
	public static final String CANCEL_ACTION = "Cancel";
	public static final String DEFAULT_FORWARD = "logout";
    private static final Logger logger = Logger.getLogger(LogoutAction.class);

	protected String getDefaultForward() {
		return DEFAULT_FORWARD;
	}

    public ActionForward perform(ActionMapping       mapping,
                                 ActionForm          form,
                                 HttpServletRequest  request,
                                 HttpServletResponse response)
    {       
	String action = request.getParameter("submit");
	logger.debug("In LogoutAction::perform " + action);
	if (action != null) {
		if (action.equals(OK_ACTION)) {
			//Get all the details about the switch from the subscriber
			//record
			try {
				String sessionID = ETTXUtil.getSessionID(request);
				AuthenticateHelper.logout(sessionID);
			}
			catch (Exception ex) {
				logger.error(ETTXUtil.localizeMessage ("logoutException"),ex);
			}
			ETTXUtil.setSessionID(request,null);
			return mapping.findForward(OK_FORWARD);
		}
		else if (action.equals(CANCEL_ACTION)) {
			//Save in the local archival folder
			return mapping.findForward(CANCEL_FORWARD);
		}
	}
	return mapping.findForward(DEFAULT_FORWARD);
    }
}
