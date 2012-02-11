package com.cisco.ettx.admin.config;

import org.apache.log4j.Logger;
import com.cisco.ettx.admin.common.SystemConfiguration;
import com.cisco.ettx.admin.config.xml.XmlSystemConfig;
import java.io.File;

public class SystemConfigManager extends ChangeNotification {

    private SystemConfiguration systemconfig = null;
    private static SystemConfigManager _instance = null;
    private Logger logger = Logger.getLogger(SystemConfigManager.class);
    XmlSystemConfig	xmlParser = null;

    public static SystemConfigManager getInstance()
    {
	return _instance;
    }

    public SystemConfigManager(String ettxDir)
	throws Exception
    {
	if (_instance  != null) {
		logger.error("Creating more than one instance");
		throw new ConfigurationException(ConfigurationException.SINGLETON_VIOLATION);
	}
	xmlParser = XmlSystemConfig.getInstance(ettxDir);
	systemconfig = xmlParser.processSystemConfig();
	_instance = this;
    }

    public SystemConfiguration getSystemConfiguration()
    {
	return systemconfig;
    }

    public void setSystemConfiguration(SystemConfiguration newconfig)
	throws ConfigurationException
    {

	//If values have changed
	if (!newconfig.getLeaseHistoryExportDir().equals(systemconfig.getLeaseHistoryExportDir())) {
		//Check if new directory is writable
		File file = new File(newconfig.getLeaseHistoryExportDir());
		if (!file.canWrite()) {
			logger.error("The new directory for lease history is not writable");
			throw new ConfigurationException(ConfigurationException.CANNOT_WRITE_TO_LEASE_HISTORY_EXPORT_DIR);
		}
	}
	if (!newconfig.getExportDir().equals(systemconfig.getExportDir())) {
		//Check if new directory is writable
		File file = new File(newconfig.getExportDir());
		if (!file.canWrite()) {
			logger.error("The new directory for lease history is not writable");
			throw new ConfigurationException(ConfigurationException.CANNOT_WRITE_TO_EVENT_EXPORT_DIR);
		}
	}

	try {
	    xmlParser.writeSystemConfiguration(newconfig);
	} catch (Exception e) {
	    logger.error("Cannot write new SystemConfiguration");
	    logger.error("Unexected Exception: " + e);
	throw new ConfigurationException(ConfigurationException.ERROR_WRITING_XML_FILE);
	} 

	// save new config   
	SystemConfiguration oldconfig = systemconfig;
	systemconfig = newconfig;
	notifyChange(oldconfig,systemconfig);
    }
}
