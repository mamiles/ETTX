package com.cisco.ettx.admin.gui.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.*;
import org.apache.struts.action.ActionForm;
import com.cisco.ettx.admin.gui.web.beans.SubsDevicesFormBean;
import com.cisco.ettx.admin.gui.web.datatypes.SubscriberRecord;
import com.cisco.ettx.admin.common.SubsDeviceInfo;
import java.util.Vector;
import com.cisco.ettx.admin.gui.web.helper.SubscriberMgmtHelper;
import com.cisco.ettx.admin.gui.web.helper.CollEngineHelper;
import com.cisco.ettx.admin.gui.web.util.ETTXUtil;
import com.cisco.nm.uii.taglib.framework.Constants;

import java.text.ParseException;
import java.text.ParsePosition;
import org.apache.log4j.Logger;
import com.cisco.ettx.admin.common.AdminServicesException;
import com.cisco.ettx.admin.common.AdminServicesNotification;

public class EditSubsDevicesAction extends Action
{
	public static final String DEFAULT_FORWARD = "viewSubsDevices";
	public static final String EDIT_SUBS_DEVICE_FWD = "editSubsDevice";
	public static final String DEVICE_DELETED_MSG = "DEVICE_DELETED_MSG";
	public static final String ERROR_FORWARD = "closeWindowAndNotification";
	public static final String SUBSCRIBER_ID = "subscriberID";
	public static final String SUBS_DEVICES = "subsDevices";
	public static final String OPTION_82_ENABLED_MSG = "OPTION_82_ENABLED_MSG";
	private static final String SUCCESS_MESSAGE = "CHANGED_SUBSCRIBER_PROFILE_MESSAGE";

	public static final String REFRESH = "Refresh";
	public static final Logger logger = Logger.getLogger(EditSubsDevicesAction.class);

	protected String getDefaultForward() {
		return DEFAULT_FORWARD;
	}

    public ActionForward perform(ActionMapping       mapping,
                                 ActionForm          form,
                                 HttpServletRequest  request,
                                 HttpServletResponse response)
    {       
         HttpSession the_session = request.getSession();

	SubsDevicesFormBean sysBean = (SubsDevicesFormBean)form;
	String action = request.getParameter("submit");
	logger.debug("EditSubsDevicesAction:submit " + action);
	if (action != null) {
		if (action.equals("Add")) {
			sysBean.setCurntDevice(new SubsDeviceInfo());
			sysBean.setModify(false);
			return mapping.findForward(EDIT_SUBS_DEVICE_FWD);
		}
/*
		if (action.equals("Edit")) {
			//Get the service parameters about the subscriber and display
			sysBean.setModify(true);
			return mapping.findForward(EDIT_SUBS_DEVICE_FWD);
		}
*/
		if (action.equals("Delete")) {
			//Get the service parameters about the subscriber and display
			try {
				String sessionID = ETTXUtil.getSessionID(request);
				SubscriberMgmtHelper.deleteSubsDevice(sessionID,sysBean.getSubscriberID(),sysBean.getCurntDevice());
				ActionErrors aes = new ActionErrors();
				aes.add(Constants.INFO,new ActionError(DEVICE_DELETED_MSG));
				saveErrors(request,aes);
				return mapping.findForward(ERROR_FORWARD);
			}
			catch (Exception ex) {
				ActionErrors aes = new ActionErrors();
				ActionError error = ETTXUtil.convertToError(ex);
				aes.add(Constants.ERROR,error);
				saveErrors(request,aes);
				return mapping.findForward(ERROR_FORWARD);
			}
		}
		if (action.equals("Apply")) {
			try {
				String sessionID = ETTXUtil.getSessionID(request);
/*
				if (sysBean.modify()) {
					SubscriberMgmtHelper.modifySubsDevice(sessionID,sysBean.getSubscriberID(),sysBean.getCurntDevice(),sysBean.getPrevStatus());
				}
				else {
*/
					SubscriberMgmtHelper.addSubsDevice(sessionID,sysBean.getSubscriberID(),sysBean.getCurntDevice());
/*
				}
*/
				ActionErrors aes = new ActionErrors();
				aes.add(Constants.INFO,new ActionError(SUCCESS_MESSAGE));
				saveErrors(request,aes);
				return mapping.findForward(ERROR_FORWARD);
			}
			catch (Exception ex) {
				ActionErrors aes = new ActionErrors();
				ActionError error = ETTXUtil.convertToError(ex);
				aes.add(Constants.ERROR,error);
				saveErrors(request,aes);
				return mapping.findForward(ERROR_FORWARD);
			}
		}
		else if (action.equals("Cancel")) {
			return mapping.findForward(ERROR_FORWARD);
		}
		else if (action.equals(REFRESH) || 
			action.equals("View MAC Addresses")) {
			//Check if the subscriber is connected to a switch that is non option 82
			if (action.equals("View MAC Addresses")) {
				sysBean.setSubscriberID((String)request.getAttribute(SubscriberMgmtAction.SUBSCRIBER_ID));
			}
			try {
				String sessionID = ETTXUtil.getSessionID(request);
				SubscriberRecord subsRecord= CollEngineHelper.getSubscriberDetails(sessionID,sysBean.getSubscriberID());
				if (subsRecord.getSwitchOption82() == true) {
					ActionErrors aes = new ActionErrors();
					aes.add(Constants.INFO,new ActionError(OPTION_82_ENABLED_MSG));
					saveErrors(request,aes);
					return mapping.findForward(ERROR_FORWARD);
				}
				sysBean.setSubsDevices(subsRecord.getSubsDevices());
				sysBean.setSubscriberName(subsRecord.getSubscriberFullName());
			}
			catch (Exception ex) {
				ActionErrors aes = new ActionErrors();
				ActionError error = ETTXUtil.convertToError(ex);
				aes.add(Constants.ERROR,error);
				saveErrors(request,aes);
				return mapping.findForward(ERROR_FORWARD);
			}
		}
	}
	request.setAttribute(SUBS_DEVICES,sysBean.getSubsDevices());
	String screenID = mapping.getPath().substring(1);
	return mapping.findForward(screenID);
    }
}
