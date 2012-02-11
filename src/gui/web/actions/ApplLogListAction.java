package com.cisco.ettx.admin.gui.web.actions;
//**************************************************
// Copyright (c) 2002 Cisco Systems, Inc.
// All rights reserved.
//**************************************************
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.Hashtable;
import java.util.StringTokenizer;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import com.cisco.ettx.admin.common.*;
import com.cisco.ettx.admin.gui.web.beans.ApplLogListActionFormBean;
import com.cisco.nm.uii.taglib.table.ScrollingTable;
import com.cisco.nm.uii.taglib.table.STColumn;
import com.cisco.ettx.admin.gui.web.helper.ApplLogHelper;
import org.apache.log4j.*;
import com.cisco.ettx.admin.gui.web.util.ETTXUtil;
import com.cisco.nm.uii.taglib.framework.Constants;

public final class ApplLogListAction extends Action {

	private static Logger logger = Logger.getLogger(ApplLogListAction.class);
	public static final String DEFAULT_FORWARD = "viewLogs";
	public static final String LOG_FILE_FORWARD = "displayLogFile";
	public static final String ERROR_FORWARD = "closeWindowAndNotification";

	protected String getDefaultForward() {
		return DEFAULT_FORWARD;
	}

	public ActionForward perform( ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response )
			throws IOException, ServletException {


		logger.debug("ApplLogObjSelectorFormAction::perform()");
		ActionErrors aes = new ActionErrors();
    		ApplLogListActionFormBean sysBean = (ApplLogListActionFormBean) form;
    		String strPath = mapping.getPath();
		String screenID = strPath.substring(1);
		String action = request.getParameter("display");
		logger.debug("Display index : " + action);
		if (action != null) {
			TreeMap applLogList = sysBean.getApplLogList();
			ApplLog log = (ApplLog)applLogList.get(Integer.valueOf(action));
			StringTokenizer st = new StringTokenizer(log.getLogName(), "<");
			log.setLogName(st.nextToken());
			logger.debug("User selected to view " + log);
			try {
				String sessionID = ETTXUtil.getSessionID(request);
				String logUrl = ApplLogHelper.getLogFile(sessionID,log);
				logger.debug("URL of LogFile: " + logUrl);
				return new RedirectingActionForward(logUrl);
			}
			catch (AdminServicesException ex) {
				logger.error("Exception while loading component " + screenID, ex);
				ActionError error = ETTXUtil.convertToError(ex);
				aes.add(Constants.ERROR,error);
				saveErrors(request,aes);
				// return mapping.findForward(ERROR_FORWARD);
				return mapping.findForward(screenID);
			}
		}
		else {
			ApplLog[] logs = (ApplLog[])request.getAttribute("applLogArray");

			TreeMap applLogList = new TreeMap();
			String sessionID = ETTXUtil.getSessionID(request);
			Hashtable compLabel = ApplLogHelper.getLabelNames(sessionID);

			for (int i=0; i<logs.length; i++) {
				//String logNameJS = logs[i].getLogName() + "<javascript:doGetLogFile('" + logs[i].getApplName() + "', '" + logs[i].getHostName() + "', '" + logs[i].getLogPath() + "', '" + logs[i].getLogName() + "')>";
				String logNameJS = logs[i].getLogName() + "<javascript:doGetLogFile('" + i + "')>";
				logger.debug("Updating LogName: " + logNameJS);
				logs[i].setLogName(logNameJS);
				logs[i].setLabelName((String)compLabel.get(logs[i].getApplName()));
				applLogList.put( new Integer(i), logs[i]);
			}

			request.setAttribute("applLogList", applLogList.values().iterator());
			sysBean.setApplLogList(applLogList);
		}

		logger.debug("Forwarding to Screen ID: " + screenID);
    		return ( mapping.findForward(screenID) );
  	}
}
