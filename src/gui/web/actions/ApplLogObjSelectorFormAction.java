package com.cisco.ettx.admin.gui.web.actions;

//**************************************************
// Copyright (c) 2002 Cisco Systems, Inc.
// All rights reserved.
//**************************************************

import com.cisco.nm.xms.ogs.client.uii.ObjectSelectorAdapter;
import com.cisco.nm.xms.ogs.client.uii.ObjectSelectorSession;
import com.cisco.nm.xms.ogs.client.uii.SelectionContainer;
import com.cisco.nm.xms.ogs.client.uii.SelectedItem;
import com.cisco.ettx.admin.gui.web.beans.ApplLogObjSelectorFormBean;
import com.cisco.ettx.admin.gui.web.helper.ApplLogHelper;
import com.cisco.nm.uii.taglib.framework.Constants;
import com.cisco.nm.uii.ScreenIDCeeSettings;
import com.cisco.nm.uii.CeeNavSetting;
import com.cisco.ettx.admin.gui.web.util.ETTXUtil;
import java.io.IOException;
import java.util.Enumeration;
import java.util.StringTokenizer;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.log4j.*;
import java.text.ParseException;
import java.text.ParsePosition;
import com.cisco.ettx.admin.common.SMSComponent;
import com.cisco.ettx.admin.common.ComponentHostData;
import com.cisco.ettx.admin.common.ApplLog;
import com.cisco.ettx.admin.common.AdminServicesException;
import java.util.Hashtable;
import java.util.Vector;

/* One instance of this class shared for all requests.
 * The controller servlet creates only one instance of the Action class, and uses it for all requests.
 * Therefore, we need to code the Action class so that it operates correctly in a multi-threaded
 * environment.
 */

public class ApplLogObjSelectorFormAction
	extends Action {
	public static final String SUBMIT_ACTION = "Submit";
	public static final String DEFAULT_FORWARD = "selectLogs";
	public static final String VIEW_LOGS = "viewLogs";
	public static final String ERROR_FORWARD = "closeWindowAndNotification";

	private static Logger logger = Logger.getLogger(ApplLogObjSelectorFormAction.class);

	protected String getDefaultForward() {
		return DEFAULT_FORWARD;
	}

	public ActionForward perform(ActionMapping actionMapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws
		IOException, ServletException {

		logger.debug("ApplLogObjSelectorFormAction::perform()");

		ActionErrors aes = new ActionErrors();
		String screenID = VIEW_LOGS;

		ObjectSelectorAdapter osAdapter = new ObjectSelectorAdapter(request, "contentos");

		String action = request.getParameter("action");

		ApplLogObjSelectorFormBean contentAreaOsForm = (ApplLogObjSelectorFormBean) form;
		ObjectSelectorSession osSession = ObjectSelectorSession.getOsSession(request, "contentos");

		String searchtype = contentAreaOsForm.getSearchType();
		String searchstring = contentAreaOsForm.getSearchString();

		osSession.setAttribute("searchType", searchtype);
		osSession.setAttribute("searchString", searchstring);

		if (action != null) {
			SelectionContainer selection = osAdapter.getSelection();
			if (action.equals(SUBMIT_ACTION) && selection != null) {
				try {
					// Get all components without the logDir populated
					logger.debug("Loading all components without the LogDir populated from the server..");
					String sessionID = ETTXUtil.getSessionID(request);
					Hashtable elems = ApplLogHelper.getAllCompWithoutLogDir(sessionID);

					// Set the Log Dir that the user selected
					SelectionContainer container = osAdapter.getSelection();
					for (int i = 0; i < container.size(); i++) {
						SelectedItem currentItem = container.selectedItemAt(i);
						StringTokenizer st = new StringTokenizer(currentItem.getGroupName(), "/");
						String rootName = st.nextToken();
						String componentName = st.nextToken();
						String hostName = st.nextToken();
						// Find the component
						SMSComponent comp = (SMSComponent) elems.get(componentName);
						// Find the host
						ComponentHostData compHostData = comp.getHost(hostName);
						Vector logDirs = compHostData.getLogDirs();
						// Add the log Dir the user selected to the componentHostData
						logDirs.add(currentItem.getObjectId());
						compHostData.setLogDirs(logDirs);
					}
					// Remove empty components and hosts
					elems = ApplLogHelper.removeEmptyComp(sessionID, elems);
					if (searchtype.equals("allFiles")) {
						searchstring = "";
					}
					if (searchtype.equals("searchFiles") && searchstring.equals("")) {
						aes = new ActionErrors();
						logger.info("Must enter Search Value when searching");
						aes.add(Constants.ERROR, new ActionError("noSearchValue"));
						saveErrors(request, aes);
						return actionMapping.findForward(ERROR_FORWARD);
					}
					ApplLog[] logs = ApplLogHelper.getLogFilenames(sessionID, elems, searchstring);
					if (logs.length == 0) {
						aes = new ActionErrors();
						logger.info("No log files found");
						aes.add(Constants.INFO, new ActionError("noLogFilesFound"));
						saveErrors(request, aes);
						return actionMapping.findForward(ERROR_FORWARD);
					}
					request.setAttribute("applLogArray", logs);

					logger.debug("Clearing OS Selection");
					osAdapter.clearSelection();

					logger.debug("Submit action button pressed");
					logger.debug("searchtype: " + searchtype + " searchstring: " + searchstring);
					screenID = VIEW_LOGS;
					logger.debug("Going to screen " + screenID);
					return actionMapping.findForward(screenID);
				}
				catch (AdminServicesException ex) {
					logger.error("Exception while loading component " + screenID, ex);
					ActionErrors aese = new ActionErrors();
					logger.error(ETTXUtil.localizeMessage("logDisplayExceptionEncountered") + ex.getMessage());
					ActionError error = ETTXUtil.convertToError(ex);
					aese.add(Constants.ERROR, error);
					saveErrors(request, aese);
					return actionMapping.findForward(ERROR_FORWARD);
				}

			}
			else {
				// Nothing selected
				ActionErrors aesn = new ActionErrors();
				aesn.add(Constants.ERROR, new ActionError("noComponentLogsSelected"));
				saveErrors(request, aesn);
				return actionMapping.findForward(ERROR_FORWARD);
			}
		}
		osAdapter.refreshTree();
		logger.debug("Forwarding to screenID: " + DEFAULT_FORWARD);
		return (actionMapping.findForward(DEFAULT_FORWARD));
	}
}