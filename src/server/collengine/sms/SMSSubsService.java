

package com.cisco.ettx.admin.collengine.sms;

import org.apache.log4j.Logger;
import com.cisco.ettx.admin.server.axis.sms.ServiceHelper;
import com.cisco.ettx.admin.server.axis.sms.SMSAPIException;
import com.cisco.sesm.sms.types.*;
import java.util.HashMap;
import java.util.List;
import com.cisco.ettx.admin.collengine.DataCollectionException;
import com.cisco.ettx.admin.collengine.CollectionHolder;
import com.cisco.ettx.admin.common.util.AttrNameConstants;
import java.rmi.RemoteException;
import java.util.Vector;
import java.util.Arrays;

//**************************************************
// Copyright (c) 2002 Cisco Systems, Inc.
// All rights reserved.
// Author : Chandrika R
// File : SMSSubsService.java
// Desc : Class which performs SMS queries
//**************************************************


public class SMSSubsService extends AttrNameConstants implements SMSService {
	private static Logger logger = Logger.getLogger(SMSSubsService.class);
	SMSTaskInfo taskInfo = null;
	CollectionHolder holder = null;

	private static String GET_SERVICES = "GetServices";

	public SMSSubsService() {
	}

	public void performCollection(
		SMSTaskInfo ltaskInfo,
		CollectionHolder lholder) 
		throws DataCollectionException {
		taskInfo = ltaskInfo;
		holder = lholder;
		if (taskInfo.getTaskName().equals(GET_SERVICES)) {
			queryServices();
			return;
		}
		logger.error("Unable to map the query name " + taskInfo.getTaskName() + " for SMSSubsService");
		throw new DataCollectionException(DataCollectionException.NO_SUCH_METHOD);
	}

	public void queryServices() throws DataCollectionException {
		try {
			ServiceInfo[] services = ServiceHelper.queryAllServices(holder.getSessionID());
			Vector records = new Vector();
			if (services != null) {
				for (int i = 0; i < services.length; i++) {
					ServiceInfo serviceInfo = services[i];
					HashMap record = new HashMap();
					record.put(SERVICE_NAME,serviceInfo.getServiceName());
					Vector serviceFeatures = new Vector();
					ServiceFeatureInfo[] sServiceFeatures =  serviceInfo.getServiceFeatures();
					for (int j = 0; j < sServiceFeatures.length; j++) {
						ServiceFeatureInfo serviceFeature = sServiceFeatures[j];
						HashMap featureRecord = new HashMap();
						//Find the associated feature record from subscriber
						featureRecord.put(FEATURE_NAME,serviceFeature.getServiceFeatureName());
						if (serviceFeature.getAllowedValues() != null) {
							List list = Arrays.asList(serviceFeature.getAllowedValues());
							featureRecord.put(ALLOWED_VALUES,new Vector(list));
						}
						featureRecord.put(DEFAULT_VALUE,serviceFeature.getDefaultValue());
						serviceFeatures.add(featureRecord);
					}
					record.put(SERVICE_FEATURES,serviceFeatures);
					records.add(record);
				}
			}
			taskInfo.setAttrMapValue(holder,SERVICE_PARAMETERS,records);
		}
		catch (SMSAPIException ex) {
			logger.error("Unable to execute service query ",ex);
			throw new DataCollectionException(ex.getMessage());
		}
		catch (Exception ex) {
			logger.error("Unable to get all services from SMS",ex);
			throw new DataCollectionException(DataCollectionException.UNABLE_TO_EXECUTE_TASK);
		}
	}
}
