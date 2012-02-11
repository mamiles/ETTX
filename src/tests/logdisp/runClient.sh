#!/bin/csh
echo "Running config test..";
setenv ETTX_LIB ${ETTX_ROOT}/lib
setenv CLASSPATH ${ETTX_ROOT}/jakarta-tomcat-4.0.6/webapps/ettx_admin/WEB-INF/classes:${ETTX_LIB}/axis-ant.jar:${ETTX_LIB}/axis.jar:${ETTX_LIB}/commons-discovery.jar:${ETTX_LIB}/commons-logging.jar:${ETTX_LIB}/ettx_admin_server.jar:${ETTX_LIB}/i18n.jar:${ETTX_LIB}/jakarta-regexp-1.2.jar:${ETTX_LIB}/jaxrpc.jar:${ETTX_LIB}/jta25b.jar:${ETTX_LIB}/log4j-1.2.4.jar:${ETTX_LIB}/objselector.jar:${ETTX_LIB}/ogs-objselector.jar:${ETTX_LIB}/saaj.jar:${ETTX_LIB}/sesm-sms.jar:${ETTX_LIB}/struts.jar:${ETTX_LIB}/tibrvj.jar:${ETTX_LIB}/tibrvjweb.jar:${ETTX_LIB}/uii.jar:wsdl4j.jar:${ETTX_LIB}/xerces.jar:${ETTX_ROOT}/jakarta-tomcat-4.0.6/common/lib/servlet.jar:${ETTX_ROOT}/jakarta-tomcat-4.0.6/webapps/ettx_admin/config
java -DETTX_ROOT=${ETTX_ROOT} -classpath ${CLASSPATH} com.cisco.ettx.admin.tests.logdisp.LogDispTest
