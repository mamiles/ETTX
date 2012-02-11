package com.cisco.ettx.admin.config.xml;
import java.io.File;
import org.w3c.dom.*; 

import org.apache.log4j.Logger;

import com.cisco.ettx.admin.common.ToolbarElement;
import com.cisco.ettx.admin.config.ConfigurationException;
import java.util.Vector;
import java.util.Enumeration;
import com.cisco.ettx.admin.common.util.XMLUtil;

public class ToolbarXMLInterface
{
    private static Logger logger = Logger.getLogger("ToolbarXMLInterface");

    private static String LINK = "LINK";
    private static String TOOLBAR = "TOOLBAR";
    private static String URL = "URL";
    private static String DESCRIPTION = "DESCRIPTION";

	public static ToolbarElement[] readToolbarConfig(String fileName) 
		throws ConfigurationException {
		Vector elems = new Vector();
		try {
			Document document = XMLUtil.loadXmlFile(fileName);
			NodeList nodelist = document.getElementsByTagName(TOOLBAR);
			logger.debug("Received Toolbar Elements " + nodelist.getLength());
			Node toolbar = nodelist.item(0);
			Vector links = XMLUtil.findChildren(toolbar,LINK);
			logger.debug("Loaded " + links.size() + " links from file");
			Enumeration iter = links.elements();
			while (iter.hasMoreElements()) {
				Node link = (Node)iter.nextElement();
				ToolbarElement elem = createLink(link);
				logger.debug("Created link " + elem.getDisplayName());
				elems.add(elem);
			}
			return (ToolbarElement.toArray(elems));
		}
		catch (Exception ex) {
			logger.error("Error Reading XML File " + fileName + " Exception " + ex.toString(),ex);
			throw new ConfigurationException(ConfigurationException.ERROR_READING_XML_FILE);
		}
	}

	private static ToolbarElement createLink(Node node) throws Exception {
		String url = XMLUtil.getChildValue(node,URL);
		String desc = XMLUtil.getChildValue(node,DESCRIPTION);
		ToolbarElement elem = new ToolbarElement(desc,url);
		return elem;
	}

	public static void writeToolbarConfig(ToolbarElement[] elems, 
		String fileName)
		throws ConfigurationException {
		try {
			Document document = XMLUtil.createDocument();
			//REVISIT - add doc type
			Element toolbar = document.createElement(TOOLBAR);
			document.appendChild(toolbar);
			for (int i= 0; i < elems.length; i++) {
				ToolbarElement elem = elems[i];
				Element link = document.createElement(LINK);
				toolbar.appendChild(link);
				XMLUtil.addTextNode(document,link,
					URL,elem.getUrlLink());
				XMLUtil.addTextNode(document,link,
					DESCRIPTION,elem.getDisplayName());
			}
			XMLUtil.writeXmlFile(fileName,document);
		}
		catch (Exception ex) {
			logger.error("Error Writing XML File " + fileName + "Exception " + ex.toString(),ex);
			throw new ConfigurationException(ConfigurationException.ERROR_WRITING_XML_FILE);
		}
	}
}
