
package com.cisco.ettx.admin.gui.web.datatypes;

import com.cisco.nm.xms.ogs.client.ostaglib.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Iterator;
import java.util.Hashtable;
import java.io.*;
import com.cisco.ettx.admin.common.*;
import com.cisco.ettx.admin.gui.web.helper.CompConfigHelper;
import com.cisco.ettx.admin.gui.web.util.ETTXUtil;
import org.apache.log4j.Logger;

public class ApplLogTreeGenerator extends BasicObjectSelectorTreeGenerator {

	public static final String UNABLE_TO_EXECUTE_SERVICE = "Unable to execute service";
	private static Logger logger = Logger.getLogger(ApplLogTreeGenerator.class);
	public static final String rootName = "ETTX";

	public  String getTreeEntries(TreeAttributes attribute) {
        	HttpServletRequest request = attribute.getRequest();
        	String groupKey = attribute.getGroupKey();
        	boolean isGroupSelector = attribute.getGroupSelector();
        	String currentUserSelectedFilterID = attribute.getCurrentFilterID();
        	Object ogsTreeProperties = attribute.getTreeProperties();

        	Vector componentHostData;
		String componentName;
		String logDirectory;

		String entries = "";

		if (groupKey.equals("/")) {

			try {
				// Setup root level name
				entries = "^" + rootName + "|" + rootName + "|root|c|o|0|1";

				SMSComponent[] comps = CompConfigHelper.getAllComponents(ETTXUtil.getSessionID(request));
				for (int i = 0; i < comps.length; i++) {
					componentHostData = comps[i].getComponentHostData();
					componentName = comps[i].getLabel();
					logger.debug("Component No: " + i  + " Component: " + componentName + " Number of Components: " + comps.length);
					entries = entries + "^" + rootName + "/" + componentName + "|" + componentName + "|" + rootName + "|c|c|0|1";
					for (int j = 0; j < componentHostData.size(); j++) {
						ComponentHostData host = (ComponentHostData)componentHostData.elementAt(j);
						entries = entries + "^" + rootName + "/" + componentName + "/" + host.getHostName() +
							"|" + host.getHostName() +
							"|" + rootName + "/" + componentName +
							"|c|c|0|1";
						logger.debug("Processing " + j + " host: " + host.getHostName() + " Unix UID: " + host.getUnixLoginID() + " PWD: " + host.getUnixPassword() + " Prompt: " + host.getUnixPrompt());
						Iterator debugIt = host.getLogDirs().iterator();
						logger.debug("Number of Log Directories: " +host.getLogDirs().size());
						while (debugIt.hasNext()) {
							logDirectory = debugIt.next().toString();
							logger.debug("Processing LogPathDir: " + logDirectory);
							entries = entries + "^" + rootName + "/" + componentName + "/" + host.getHostName() + "#" + logDirectory +
								"|" + logDirectory +
								"|" + rootName + "/" + componentName + "/" + host.getHostName() +
								"|n|c|0|1";
						}
					}

				}

				logger.debug(entries);
				return entries;
			}
			catch (AdminServicesException ex) {
				logger.error("Exception while loading component " , ex);
				return entries;
			}
			catch (Exception e)
			{
	    			e.printStackTrace();
	    			return entries;
			}

/*            		entries = "^SMS|SMS|root|c|o|0|1^SMS/BPR|BPR|SMS|c|c|0|1^SMS/BPR/bprhost1|bprhost1|SMS/BPR|c|c|0|1^SMS/BPR/bprhost1#/opt/CSCObpr/log/DHCP*|/opt/CSCObpr/log/DHCP*|SMS/BPR/bprhost1|n|c|0|1^SMS/BPR/bprhost1#/opt/CSCObpr/log/DNS*|/opt/CSCObpr/log/DNS*|SMS/BPR/bprhost1|n|c|0|1^SMS/CNR|CNR|SMS|c|c|0|1^SMS/CNR/cnrhost1|cnrhost1|SMS/CNR|c|c|0|1^SMS/CNR/cnrhost1#/opt/CSCOcnr/log|/opt/CSCOcnr/log|SMS/CNR/cnrhost1|n|c|0|1^SMS/CNR/cnrhost2|cnrhost2|SMS/CNR|c|c|0|1^SMS/CNR/cnrhost2#/opt/CSCOcnr/log|/opt/CSCOcnr/log|SMS/CNR/cnrhost2|n|c|0|1^SMS/SPE|SPE|SMS|c|c|0|1^SMS/SPE/ettxhost1|ettxhost1|SMS/SPE|c|c|0|1^SMS/SPE/ettxhost1#/opt/CSCOspe/log|/opt/CSCOspe/log|SMS/SPE/ettxhost1|n|c|0|1^SMS/Admin App|Admin App|SMS|c|c|0|1^SMS/Admin App/ettxhost1|ettxhost1|SMS/Admin App|c|c|0|1^SMS/Admin App/ettxhost1#/opt/CSCOeaa/log|/opt/CSCOeaa/log|SMS/Admin App/ettxhost1|n|c|0|1";

			^Key|Name|Parent-Key|Type|State|More|Enabled
			                    |c=Container
			                    |n=Leaf node (log dir)
			                         |c    |0   |1

			^SMS|SMS|root|c|o|0|1

			^SMS/BPR|BPR|SMS|c|c|0|1
			^SMS/BPR/bprhost1|bprhost1|SMS/BPR|c|c|0|1
			^SMS/BPR/bprhost1#/opt/CSCObpr/log/DHCP*|/opt/CSCObpr/log/DHCP*|SMS/BPR/bprhost1|n|c|0|1
			^SMS/BPR/bprhost1#/opt/CSCObpr/log/DNS*|/opt/CSCObpr/log/DNS*|SMS/BPR/bprhost1|n|c|0|1

			^SMS/CNR|CNR|SMS|c|c|0|1
			^SMS/CNR/cnrhost1|cnrhost1|SMS/CNR|c|c|0|1
			^SMS/CNR/cnrhost1#/opt/CSCOcnr/log|/opt/CSCOcnr/log|SMS/CNR/cnrhost1|n|c|0|1
			^SMS/CNR/cnrhost2|cnrhost2|SMS/CNR|c|c|0|1
			^SMS/CNR/cnrhost2#/opt/CSCOcnr/log|/opt/CSCOcnr/log|SMS/CNR/cnrhost2|n|c|0|1

			^SMS/SPE|SPE|SMS|c|c|0|1
			^SMS/SPE/ettxhost1|ettxhost1|SMS/SPE|c|c|0|1
			^SMS/SPE/ettxhost1#/opt/CSCOspe/log|/opt/CSCOspe/log|SMS/SPE/ettxhost1|n|c|0|1
			^SMS/Admin App|Admin App|SMS|c|c|0|1
			^SMS/Admin App/ettxhost1|ettxhost1|SMS/Admin App|c|c|0|1
			^SMS/Admin App/ettxhost1#/opt/CSCOeaa/log|/opt/CSCOeaa/log|SMS/Admin App/ettxhost1|n|c|0|1";

			return entries;
*/
        	}
        	return entries;
	}
}
