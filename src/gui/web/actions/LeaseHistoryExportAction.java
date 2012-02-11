package com.cisco.ettx.admin.gui.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.cisco.nm.uii.taglib.framework.Constants;
import com.cisco.nm.uii.ScreenIDCeeSettings;
import com.cisco.nm.uii.CeeNavSetting;
import com.cisco.ettx.admin.gui.web.util.ETTXUtil;
import org.apache.struts.action.*;
import org.apache.struts.action.ActionForm;
import com.cisco.ettx.admin.gui.web.beans.LeaseHistoryExportFormBean;
import com.cisco.ettx.admin.gui.web.helper.LeaseHistoryExportHelper;
import com.cisco.ettx.admin.common.AdminServicesException;
import org.apache.log4j.Logger;

import java.util.*;

import java.text.ParseException;
import java.text.ParsePosition;

public class LeaseHistoryExportAction
	extends Action {
	private static Logger logger = Logger.getLogger(LeaseHistoryExportAction.class);
	public static final String DEFAULT_FORWARD = "leaseHistoryExport";
	public static final String EXPORT_FORWARD = "leaseHistoryExport";
	public static final String ERROR_FORWARD = "closeWindowAndNotification";
	public static final String EXPORT_ACTION = "Export";

	protected String getDefaultForward() {
		return DEFAULT_FORWARD;
	}

	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		HttpSession the_session = request.getSession();

		String action = request.getParameter("action");

		LeaseHistoryExportFormBean sysBean = (LeaseHistoryExportFormBean) form;
		if (action != null) {
			if (action.equals(EXPORT_ACTION)) {
				try {
					if (sysBean.getCnrHost().equals("none")) {
						ActionErrors aesn = new ActionErrors();
						aesn.add(Constants.ERROR, new ActionError("noCNRHostConfigured"));
						saveErrors(request, aesn);
					}
					else if (sysBean.getStartPeriod().compareTo(sysBean.getEndPeriod()) >= 0) {
						ActionErrors aesn = new ActionErrors();
						aesn.add(Constants.ERROR, new ActionError("endDateGreaterThanStartDate"));
						saveErrors(request, aesn);
					}
					else {
						String sessionID = ETTXUtil.getSessionID(request);
						String resp = LeaseHistoryExportHelper.runLeaseHistoryArchive(sessionID, sysBean.getCnrHost(), sysBean.getStartPeriod(),
							sysBean.getEndPeriod(), sysBean.getExportFileName());
						if (resp.equals("success")) {
							ActionErrors aesn = new ActionErrors();
							aesn.add(Constants.INFO, new ActionError("leaseHistoryArchiveSuccessful"));
							saveErrors(request, aesn);
						}
						else {
							ActionErrors aesn = new ActionErrors();
							aesn.add(Constants.ERROR, new ActionError("leaseHistoryArchiveFailed"));
							saveErrors(request, aesn);
						}
					}
				}
				catch (AdminServicesException ex) {
					ActionErrors aes = new ActionErrors();
					logger.error("leaseHistoryExportExceptionEncountered" + ex.getMessage());
					ActionError error = ETTXUtil.convertToError(ex);
					aes.add(Constants.ERROR, error);
					saveErrors(request, aes);
					return mapping.findForward(EXPORT_FORWARD);
				}

				return mapping.findForward(EXPORT_FORWARD);
			}
		}
		String sessionID = ETTXUtil.getSessionID(request);
		sysBean.setInitialValues(sessionID);
		return mapping.findForward(DEFAULT_FORWARD);
	}
}