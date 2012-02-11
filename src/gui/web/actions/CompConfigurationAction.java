package com.cisco.ettx.admin.gui.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.*;
import org.apache.struts.action.ActionForm;
import com.cisco.ettx.admin.gui.web.beans.CompConfigurationFormBean;
import com.cisco.ettx.admin.gui.web.helper.CompConfigHelper;
import com.cisco.nm.uii.taglib.framework.Constants;
import com.cisco.nm.uii.ScreenIDCeeSettings;
import com.cisco.nm.uii.CeeNavSetting;
import com.cisco.ettx.admin.gui.web.util.ETTXUtil;

import java.util.*;

import java.text.ParseException;
import java.text.ParsePosition;
import com.cisco.ettx.admin.common.SMSComponent;
import com.cisco.ettx.admin.common.ComponentHostData;
import com.cisco.ettx.admin.common.AdminServicesException;
import org.apache.log4j.*;

public class CompConfigurationAction extends Action 
{
	public static final String DEFAULT_FORWARD = "componentConfig";
	public static final String EDIT_FORWARD = "editCompHost";
	public static final String ADD_ACTION = "Add";
	public static final String EDIT_ACTION = "Edit";
	public static final String DELETE_ACTION = "Delete";
	public static final String SAVE_ACTION = "Save";
	public static final String RESET_ACTION = "Clear";
	public static final String OK_ACTION = "OK";
	public static final String CANCEL_ACTION = "Cancel";
	public static final String ADD_LOG_ACTION = "AddLog";
	public static final String DELETE_LOG_ACTION = "DeleteLog";

	private static Logger logger = Logger.getLogger(CompConfigurationAction.class);
	public static final String ETTX_HOST_PROMPT_DEF = "ETTX_DEFAULT_HOST_PROMPT";

	protected String getDefaultForward() {
		return DEFAULT_FORWARD;
	}

    public ActionForward perform(ActionMapping       mapping,
                                 ActionForm          form,
                                 HttpServletRequest  request,
                                 HttpServletResponse response)
    {       
         HttpSession the_session = request.getSession();
	ActionErrors aes = new ActionErrors();

	String action = request.getParameter("action");

         CompConfigurationFormBean sysBean    = (CompConfigurationFormBean) form;
	if (action != null) {
		SMSComponent currentComponent = sysBean.getCurrentComponent();
		logger.debug("ActionForward:perform " + action + " for component " + currentComponent.getName());
		String selectedHostName = sysBean.getSelectedHostName();
		if (action.equals(ADD_ACTION)) {
			ComponentHostData data = null;
			if (selectedHostName != null && selectedHostName.length() != 0) {
				data = new ComponentHostData(
					currentComponent.getHost(selectedHostName));
				data.setHostName(""); //Reset the host name
				sysBean.setSelectedHostName(null);
			}
			else {
				data = new ComponentHostData();
			}

			//Set the data prompt information from web.xml
			String value = System.getProperty(ETTX_HOST_PROMPT_DEF);
			data.setUnixPrompt(value);

			sysBean.setCurrentCompHost(data);
			String screenID = EDIT_FORWARD + currentComponent.getName();
			logger.debug("Forwardning to screen " + screenID);
			return mapping.findForward(screenID);
		}
		if (action.equals(EDIT_ACTION)) {
			ComponentHostData data = new ComponentHostData(
				currentComponent.getHost(selectedHostName));
			sysBean.setCurrentCompHost(data);
			String screenID = EDIT_FORWARD + currentComponent.getName();
			return mapping.findForward(screenID);
		}
		if (action.equals(SAVE_ACTION)) {
			try {
				logger.debug("Saving configuration in server..");
				String sessionID = ETTXUtil.getSessionID(request);
				CompConfigHelper.saveComponent(sessionID,sysBean,
					sysBean.getCurrentComponent());
			}
			catch (AdminServicesException ex) {
				logger.error("Exception while saving toolbar " , ex);
				ActionError error = ETTXUtil.convertToError(ex);
				aes.add(Constants.ERROR,error);
				saveErrors(request,aes);
			}
			//mapping to the same screen in both cases
		}
		if (action.equals(RESET_ACTION)) {
			try {
				logger.debug("Loading configuration in server..");
				String sessionID = ETTXUtil.getSessionID(request);
				SMSComponent comp  =CompConfigHelper.loadComponent(sessionID, sysBean,sysBean.getCurrentComponent().getName());
				sysBean.setCurrentComponent(comp);
			}
			catch (AdminServicesException ex) {
				logger.error("Exception while loading component " + sysBean.getCurrentComponent().getName() , ex);
				ActionError error = ETTXUtil.convertToError(ex);
				aes.add(Constants.ERROR,error);
				saveErrors(request,aes);
			}
		}
		if (action.equals(DELETE_ACTION)) {
			logger.debug("Deleting the host information in the back end");
			try {
				String sessionID = ETTXUtil.getSessionID(request);
				CompConfigHelper.deleteHost(sessionID,
					sysBean.getCurrentComponent().getName(),
					sysBean.getSelectedHostName());
				currentComponent.deleteHost(sysBean.getSelectedHostName());
			}
			catch (AdminServicesException ex) {
				logger.error("Exception while changing the host name for the component " + sysBean.getCurrentComponent().getName(),ex);
				ActionError error = ETTXUtil.convertToError(ex);
				aes.add(Constants.ERROR,error);
				saveErrors(request,aes);
			}
			//Follow thro. for the same action
		}
		if (action.equals(OK_ACTION)) {
			ComponentHostData newHost = sysBean.getCurrentCompHost();
			ComponentHostData oldHost = null;
			if (selectedHostName != null) {
				oldHost = currentComponent.getHost(selectedHostName);
			}
			if (oldHost == null || 
				!selectedHostName.equals(newHost.getHostName())) {
				//Either this is an add operation or the host
				//name has changed. Verify if the host name is unique
				if (currentComponent.getHost(newHost.getHostName()) != null) {
					ActionError error = new ActionError(
						"CONFIG.COMP.FAILURE.UNIQUE_HOSTNAME",newHost.getHostName());
					aes.add(Constants.ERROR,error);
					saveErrors(request,aes);
					String screenID = EDIT_FORWARD + currentComponent.getName();
					return mapping.findForward(screenID);
				}
			}
			logger.debug("Updating the host information in the back end");
			try {
				String sessionID = ETTXUtil.getSessionID(request);
				if (oldHost == null) {
					CompConfigHelper.changeHost(sessionID,
						sysBean.getCurrentComponent().getName(),
						null, newHost);
				}
				else {
					CompConfigHelper.changeHost(sessionID,
						sysBean.getCurrentComponent().getName(),
						oldHost.getHostName(), newHost);
					currentComponent.deleteHost(oldHost);
				}
				currentComponent.addHost(newHost);
			}
			catch (AdminServicesException ex) {
				logger.error("Exception while changing the host name for the component " + sysBean.getCurrentComponent().getName(),ex);
				ActionError error = ETTXUtil.convertToError(ex);
				aes.add(Constants.ERROR,error);
				saveErrors(request,aes);
				String screenID = EDIT_FORWARD + currentComponent.getName();
				return mapping.findForward(screenID);
			}
			//return to the component screen
		}
		if (action.equals(CANCEL_ACTION)) {
			//Do nothing. Return to the main page
		}
	}
	else {
		String screenID = mapping.getPath().substring(1);
		logger.debug("ActionFormard:perform Default  for screen " + screenID);
		if (screenID.startsWith(EDIT_FORWARD)) {
			return mapping.findForward(EDIT_FORWARD + sysBean.getCurrentComponent().getName());
		}
		try {
			//Load the component from the node
			logger.debug("Loading configuration in server..");
			String sessionID = ETTXUtil.getSessionID(request);
			SMSComponent comp = CompConfigHelper.loadComponent(sessionID,sysBean,screenID);
			sysBean.setCurrentComponent(comp);
		}
		catch (AdminServicesException ex) {
			logger.error("Exception while loading component " + screenID, ex);
			ActionError error = ETTXUtil.convertToError(ex);
			aes.add(Constants.ERROR,error);
			saveErrors(request,aes);
			return mapping.findForward(screenID);
		}
	}
	request.setAttribute("componentHosts",sysBean.getCurrentComponent().getComponentHostData().iterator());
	return mapping.findForward(sysBean.getCurrentComponent().getName());
    }
}
