
package com.cisco.ettx.admin.collengine.command;

import com.cisco.ettx.admin.collengine.*;
import com.cisco.ettx.admin.common.util.XMLUtil;
import org.w3c.dom.Node;
import org.apache.log4j.Logger;

public class NoParser implements OutputParser {
	private static String OUTPUT_ATTR = "OUTPUT_ATTR";
	public String outputAttrName;
	public static Logger logger = Logger.getLogger(OutputXMLParser.class);

	public NoParser(String loutputAttrName) {
		outputAttrName = loutputAttrName;
	}

	public NoParser() {
		outputAttrName = null;
	}

	public void setOutputAttrName(String lname) {
		outputAttrName = lname;
	}

	public void formatOutput(CollectionHolder holder,String output) 
			throws DataCollectionException  {
		//Save the output in the attribute object directly
		holder.addOutputAttr(outputAttrName,output);
	}


	public void parseConfig(Node node) throws DataCollectionException {
		try {
			outputAttrName = XMLUtil.getChildValue(node,OUTPUT_ATTR);
			if (outputAttrName == null) {
				throw new DataCollectionException(DataCollectionException.PARSE_ERROR);
			}
		}
		catch (Exception ex) {
			logger.error("Unable to get the OUTPUT_ATTR for NoParser ",
				ex);
			throw new DataCollectionException(DataCollectionException.PARSE_ERROR);
		}
	}
}
