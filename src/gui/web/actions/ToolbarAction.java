package com.cisco.ettx.admin.gui.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.*;
import org.apache.struts.action.ActionForm;
import com.cisco.ettx.admin.gui.web.beans.ToolbarFormBean;
import com.cisco.nm.uii.taglib.framework.Constants;
import com.cisco.nm.uii.ScreenIDCeeSettings;
import com.cisco.nm.uii.CeeNavSetting;

import java.util.*;

import java.text.ParseException;
import java.text.ParsePosition;
import com.cisco.ettx.admin.common.ToolbarElement;
import com.cisco.ettx.admin.gui.web.helper.ToolbarHelper;
import com.cisco.ettx.admin.gui.web.util.ETTXUtil;
import org.apache.log4j.Logger;
import com.cisco.ettx.admin.common.AdminServicesException;

public class ToolbarAction extends Action 
{
	public static final String VIEW_FORWARD= "toolbarView";
	public static final String EDIT_FORWARD= "editToolbar";
	public static final String CONFIG_FORWARD= "toolbarConfig";
	public static final String ADD_ACTION = "Add";
	public static final String EDIT_ACTION = "Edit";
	public static final String DELETE_ACTION = "Delete";
	public static final String SAVE_ACTION = "Save";
	public static final String RESET_ACTION = "Clear";
	public static final String OK_ACTION = "OK";
	public static final String CANCEL_ACTION = "Cancel";

	private static Logger logger = Logger.getLogger(ToolbarAction.class);

	protected String getDefaultForward() {
		return VIEW_FORWARD;
	}

    public ActionForward perform(ActionMapping       mapping,
                                 ActionForm          form,
                                 HttpServletRequest  request,
                                 HttpServletResponse response)
    {       
         HttpSession the_session = request.getSession();
	ActionErrors aes = new ActionErrors();

	String action = request.getParameter("action");

         ToolbarFormBean sysBean    = (ToolbarFormBean) form;
	if (action != null) {
		logger.debug("ToolbarAction:ActionPerfor() with action " + action);
		Vector toolbarList = sysBean.getToolbarList();
		if (action.equals(ADD_ACTION)) {
			String selectedURL = sysBean.getSelectedURLLink();
			if (selectedURL != null && selectedURL.length() != 0) {
				ToolbarElement data = sysBean.getToolbarElement(selectedURL);
				sysBean.setDisplayName(data.getDisplayName());
				sysBean.setUrlLink(data.getUrlLink());
			}
			else {
				sysBean.setDisplayName(null);
				sysBean.setUrlLink("http://");
			}
			sysBean.setSelectedURL(null);
			String screenID = EDIT_FORWARD;
			return mapping.findForward(screenID);
		}
		if (action.equals(EDIT_ACTION)) {
			ToolbarElement data = sysBean.getToolbarElement(sysBean.getSelectedURLLink());
			sysBean.setSelectedURL(data);
			sysBean.setDisplayName(data.getDisplayName());
			sysBean.setUrlLink(data.getUrlLink());
			String screenID = EDIT_FORWARD;
			return mapping.findForward(screenID);
		}
		if (action.equals(SAVE_ACTION)) {
			try {
				logger.debug("Saving configuration in server..");
				String sessionID = ETTXUtil.getSessionID(request);
				ToolbarHelper.saveElements(sessionID,sysBean,
					sysBean.getToolbarList());
			}
			catch (AdminServicesException ex) {
				logger.error("Exception while saving toolbar " ,ex);
				ActionError error = ETTXUtil.convertToError(ex);
				aes.add(Constants.ERROR,error);
				saveErrors(request,aes);
			}
		}
		if (action.equals(RESET_ACTION)) {
			try {
				logger.debug("Loading configuration in server..");
				String sessionID = ETTXUtil.getSessionID(request);
				Vector elems = ToolbarHelper.loadElements(sessionID,sysBean);
				sysBean.setToolbarList(elems);
			}
			catch (AdminServicesException ex) {
				logger.error("Exception while loading toolbar " , ex);
				ActionError error = ETTXUtil.convertToError(ex);
				aes.add(Constants.ERROR,error);
				saveErrors(request,aes);
			}
			request.setAttribute("toolbarList",sysBean.getToolbarList());
		}
		if (action.equals(DELETE_ACTION)) {
			ToolbarElement data = sysBean.getToolbarElement(sysBean.getSelectedURLLink());
			try {
				String sessionID = ETTXUtil.getSessionID(request);
				ToolbarHelper.deleteElement(sessionID,
						sysBean.getSelectedURLLink());
				toolbarList.remove(data);
			}
			catch (AdminServicesException ex) {
				logger.error("Exception while saving toolbar " ,ex);
				ActionError error = ETTXUtil.convertToError(ex);
				aes.add(Constants.ERROR,error);
				saveErrors(request,aes);
			}
			//Follow thro. for default action
		}
		if (action.equals(OK_ACTION)) {
			ToolbarElement data = sysBean.getSelectedURL();
			if (data == null || !data.getUrlLink().equals(sysBean.getUrlLink())) {
				//Verify if the link is unique
				if (sysBean.getToolbarElement(sysBean.getUrlLink()) != null)
				{
					ActionError error = new ActionError(
						"CONFIG.TOOLBAR.FAILURE.UNIQUE_URL",
						sysBean.getUrlLink());
					aes.add(Constants.ERROR,error);
					saveErrors(request,aes);
					return mapping.findForward(EDIT_FORWARD);
				}
			}
			try {
				String sessionID = ETTXUtil.getSessionID(request);
				if (data == null) {
					ToolbarHelper.addElement(sessionID,
						sysBean.getDisplayName(),
						sysBean.getUrlLink());
					data = new ToolbarElement();
					toolbarList.add(data);
				}
				else {
					ToolbarHelper.changeElement(sessionID,
						data.getUrlLink(),
						sysBean.getDisplayName(),
						sysBean.getUrlLink());
				}
				data.setDisplayName(sysBean.getDisplayName());
				data.setUrlLink(sysBean.getUrlLink());
			}
			catch (AdminServicesException ex) {
				logger.error("Exception while saving toolbar " ,ex);
				ActionError error = ETTXUtil.convertToError(ex);
				aes.add(Constants.ERROR,error);
				saveErrors(request,aes);
				return mapping.findForward(EDIT_FORWARD);
			}
			String screenID = CONFIG_FORWARD;
			request.setAttribute("toolbarList",sysBean.getToolbarList());
			return mapping.findForward(screenID);
			
		}
		if (action.equals(CANCEL_ACTION)) {
			//Do nothing. Return to the main page
			String screenID = CONFIG_FORWARD;
			request.setAttribute("toolbarList",sysBean.getToolbarList());
			return mapping.findForward(screenID);
		}
	}
	else {
		String screenID = mapping.getPath().substring(1);
		logger.debug("ToolbarAction:ActionPerform() Forwarding to screen " + screenID);
		if (screenID.equals(CONFIG_FORWARD) || screenID.equals(VIEW_FORWARD)) {
			try {
				logger.debug("Loading configuration in server..");
				String sessionID = ETTXUtil.getSessionID(request);
				Vector elems = ToolbarHelper.loadElements(sessionID,sysBean);
				sysBean.setToolbarList(elems);
			}
			catch (AdminServicesException ex) {
				logger.error("Exception while loading toolbar " ,ex);
				ActionError error = ETTXUtil.convertToError(ex);
				aes.add(Constants.ERROR,error);
				saveErrors(request,aes);
			}
		}
	}
	String screenID = mapping.getPath().substring(1);
	if (screenID.equals(CONFIG_FORWARD)) {
		request.setAttribute("toolbarList",sysBean.getToolbarList());
	}
	return mapping.findForward(screenID);
    }
}
