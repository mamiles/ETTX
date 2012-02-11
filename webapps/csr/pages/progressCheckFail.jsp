<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<p align="left" class="pageTitle">&nbsp;&nbsp;<bean:message key="task.progress.check.fail"/></p>
<tiles:insert definition='.error.table' />	

