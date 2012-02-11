package com.cisco.ettx.admin.gui.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.*;
import org.apache.struts.action.ActionForm;
import com.cisco.nm.uii.taglib.framework.Constants;

import com.cisco.ettx.admin.gui.web.util.ETTXUtil;
import com.cisco.ettx.admin.gui.web.beans.SubscriberCreateFormBean;
import com.cisco.ettx.admin.gui.web.datatypes.*;
import com.cisco.ettx.admin.common.SubsDeviceInfo;
import com.cisco.ettx.admin.gui.web.helper.SubscriberCreateHelper;
import com.cisco.ettx.admin.gui.web.helper.CollEngineHelper;

import java.util.*;
import org.apache.log4j.Logger;
import com.cisco.ettx.admin.common.AdminServicesException;
import com.cisco.ettx.admin.common.AdminServicesNotification;

public class SubscriberCreateAction
	extends Action {
	private static final String SUCCESS_MESSAGE = "SUBSCRIBER_CREATED_SUCCESS";
	private static final String WIZARD_MESSAGE = "WIZARD_ERROR_MESSAGE";
	private static final String POSTAL_ADDRESS_MAP_ERROR = "POSTAL_ADDRESS_MAP_ERROR";
	public static final String DEFAULT_FORWARD = "smQuerySubs";
	public static final String SUBDETAILS = "subDetails";
	public static final String LOGIN = "loginDetails";
	public static final String SERVICE = "serviceDetails";
	public static final String SUMMARY = "subSummary";
	public static final String SUB_DETAILS_SUMMARY = "subDetailsSummary";
	public static final Logger logger = Logger.getLogger(SubscriberCreateAction.class);

	protected String getDefaultForward() {
		return DEFAULT_FORWARD;
	}

	public ActionForward perform(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response) {

		SubscriberCreateFormBean sysBean = (SubscriberCreateFormBean) form;
		logger.debug("Entered perform method of ActionForward.");
		String wiz1action = request.getParameter("wiz1action");
		logger.debug("wizlaction: " + wiz1action);
		String wizAction = request.getParameter("wizAction");
		logger.debug("wizAction: " + wizAction);
		String wizId = request.getParameter("wizid");
		logger.debug("wizid: " + wizId);

		String screenID = null;

		if (wiz1action == null && wizId == null && wizAction == null) {
			logger.debug("creating new subscriber record");
			screenID = SUBDETAILS;
			sysBean.setSubscriberRecord(new SubscriberRecord());
			sysBean.setSubsDeviceInfo(new SubsDeviceInfo());
			sysBean.setAddrMap(new PostalAddressMap());
			Locale locale = getLocale(request);
			sysBean.getSubscriberRecord().setCountryCode(locale.getCountry());
		}
		else if (wiz1action == null && wizId != null) {
			screenID = wizId;
		}

		if (wizAction != null && wizAction.equals("Finish")) {
			//  Add Subscriber
			try {
				String sessionID = ETTXUtil.getSessionID(request);
				Vector list = new Vector();
				list.add(sysBean.getService());
				// put the SubsDeviceInfo in the SubscriberRecord
				sysBean.getSubscriberRecord().getSubsDevices().add(sysBean.getSubsDeviceInfo());
				SubscriberCreateHelper.createSubscriberProfile(sessionID,
					 sysBean.getSubscriberRecord(), sysBean.getAddrMap(), list);
			}
			catch (AdminServicesException ex) {
				ActionErrors aes = new ActionErrors();
				logger.error("Exception while performing the create subscriber profile",ex);
				ActionError error = ETTXUtil.convertToError(ex);
				aes.add(Constants.ERROR, error);
				saveErrors(request, aes);
				return mapping.findForward(DEFAULT_FORWARD);
			}

			ActionErrors aesn = new ActionErrors();
			aesn.add(Constants.INFO, new ActionError(SUCCESS_MESSAGE));
			saveErrors(request, aesn);

			screenID = DEFAULT_FORWARD;
		}
		else if (wizAction != null && wizAction.equals("Cancel")) {
			screenID = DEFAULT_FORWARD;
		}

		if (screenID.equals(LOGIN)) {
			// Validate Subscriber Screen first
			ActionErrors aesn = new ActionErrors();
			String err = "false";
			if (sysBean.getSubscriberRecord().getSubscriberLastName().length() < 1) {
				aesn.add(Constants.ERROR, new ActionError("FamilyNameRequired"));
				err = "true";
			}
			if (sysBean.getSubscriberRecord().getSubscriberFirstName().length() < 1) {
				aesn.add(Constants.ERROR, new ActionError("GivenNameRequired"));
				err = "true";
			}
			if (sysBean.getSubscriberRecord().getPostalAddress().length() < 1) {
				aesn.add(Constants.ERROR, new ActionError("PostalAddressRequired"));
				err = "true";
			}
			if (sysBean.getSubscriberRecord().getPostalCity().length() < 1) {
				aesn.add(Constants.ERROR, new ActionError("PostalCityRequired"));
				err = "true";
			}
			if (sysBean.getSubscriberRecord().getPostalCode().length() < 1) {
				aesn.add(Constants.ERROR, new ActionError("PostalCodeRequired"));
				err = "true";
			}
			if (err.equals("true")) {
				saveErrors(request, aesn);
				return mapping.findForward(SUBDETAILS);
			}
			// Query the Postal Address Map for this subscriber to map to switch and port Identifier
			try {
				String sessionID = ETTXUtil.getSessionID(request);
				sysBean.setAddrMap(SubscriberCreateHelper.queryPostalAddressMap(sessionID,
					sysBean.getSubscriberRecord()));
			}
			catch (AdminServicesException ex) {
				ActionErrors aes = new ActionErrors();
				logger.error("Exception while performing the query Postal Address Map ",ex);
				ActionError error = ETTXUtil.convertToError(ex);
				aes.add(Constants.ERROR, error);
				saveErrors(request, aes);
				return mapping.findForward(SUBDETAILS);
			}
			catch (AdminServicesNotification ex) {
				ActionErrors aes = new ActionErrors();
				logger.error("Exception while performing the query Postal Address Map ",ex);
				aes.add(Constants.ERROR, new ActionError(POSTAL_ADDRESS_MAP_ERROR));
				saveErrors(request, aes);
				return mapping.findForward(SUBDETAILS);
			}

			// Now we can continue with the things we need to do on loginDetails screen

			if (wizAction.equals("Next")) {
				// Set the default Userid if we know it
				sysBean.getSubscriberRecord().setLoginID(SubscriberCreateHelper.
					suggestUsernames(sysBean.getSubscriberRecord().getSubscriberFirstName(),
									 sysBean.getSubscriberRecord().getSubscriberLastName()));

				// Set the Subscriber Name or screen name
				sysBean.getSubscriberRecord().setSubscriberName(SubscriberCreateHelper.
					suggestScreennames(sysBean.getSubscriberRecord().
									   getSubscriberFirstName(),
									   sysBean.getSubscriberRecord().
									   getSubscriberLastName(),
									   sysBean.getSubscriberRecord().getPostalCity()));
			}
		}

		if (screenID.equals(SERVICE)) {
			// Validate the login Details screen first
			ActionErrors aesn = new ActionErrors();
			String err = "false";
			if (sysBean.getSubscriberRecord().getLoginID().length() < 1) {
				aesn.add(Constants.ERROR, new ActionError("LoginIDRequired"));
				err = "true";
			}
			if (sysBean.getSubscriberRecord().getPassword().length() < 6) {
				aesn.add(Constants.ERROR, new ActionError("PasswordRequired"));
				err = "true";
			}
			if (sysBean.getSubscriberRecord().getSubscriberName().length() < 1) {
				aesn.add(Constants.ERROR, new ActionError("DisplayNameRequired"));
				err = "true";
			}
			if (err.equals("true")) {
				saveErrors(request, aesn);
				return mapping.findForward(LOGIN);
			}
			// End of validataion of login Details.

			// Set the subscriberId the same as the LoginID
			sysBean.getSubscriberRecord().setSubscriberID(sysBean.getSubscriberRecord().
				getLoginID());

			// Get the available services
			if (wizAction.equals("Next")) {
				try {
					String sessionID = ETTXUtil.getSessionID(request);
					sysBean.setAvailableServices(CollEngineHelper.getServiceRecords(
						sessionID));
				}
				catch (AdminServicesException ex) {
					ActionErrors aes = new ActionErrors();
					logger.error("Exception while getting the available services", ex);
					ActionError error = ETTXUtil.convertToError(ex);
					aes.add(Constants.ERROR, error);
					saveErrors(request, aes);
					return mapping.findForward(DEFAULT_FORWARD);
				}
			}

		}

		if (screenID.equals(SUMMARY)) {
			// Validate to make sure that the user enters a MAC accress if the switch they
			// are on does not support Option-82  (3500XL does not support Option-82)
			logger.debug("MAC Address entered: " + sysBean.getSubsDeviceInfo().getMacAddress() +
						 " switch model: " + sysBean.getAddrMap().getModel());
			if ((sysBean.getSubsDeviceInfo().getMacAddress() == null ||
				    sysBean.getSubsDeviceInfo().getMacAddress().equals("")) &&
				    sysBean.getAddrMap().getModel() != null ) {
					if (sysBean.getAddrMap().getModel().equals("3500XL")) {
						ActionErrors aemac = new ActionErrors();
						aemac.add(Constants.ERROR, new ActionError("MacAddressRequired"));
						saveErrors(request, aemac);
						return mapping.findForward(SERVICE);
					}
				}
			logger.debug("Create Subscriber Summary Screen");
		}

		if (screenID.equals("")) {
			ActionErrors wiz = new ActionErrors();
			wiz.add(Constants.ERROR, new ActionError(WIZARD_MESSAGE));
			saveErrors(request, wiz);
			screenID = SERVICE;
		}

		logger.debug("forwarding to screen: " + screenID);
		return mapping.findForward(screenID);
	}
}
