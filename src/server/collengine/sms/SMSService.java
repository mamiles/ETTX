

package com.cisco.ettx.admin.collengine.sms;

import com.cisco.ettx.admin.collengine.DataCollectionException;
import com.cisco.ettx.admin.collengine.CollectionHolder;
import java.util.Hashtable;
import java.util.Vector;

//**************************************************
// Copyright (c) 2002 Cisco Systems, Inc.
// All rights reserved.
// Author : Chandrika R
// File : SMSService.java
// Desc : Class which performs SMS queries
//**************************************************


public interface SMSService {
	public void performCollection(SMSTaskInfo taskInfo,
		CollectionHolder holder) 
		throws DataCollectionException;

}
