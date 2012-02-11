#!/bin/csh
setenv ETTX_HOME /vob/sms-admin
setenv ETTX_LIB ${ETTX_HOME}/lib
setenv CLASSPATH .:${ETTX_HOME}/config:${ETTX_LIB}/axis-ant.jar:${ETTX_LIB}/axis.jar:${ETTX_LIB}/commons-discovery.jar:${ETTX_LIB}/commons-logging.jar:${ETTX_LIB}/ettx_admin_server.jar:${ETTX_LIB}/jaxrpc.jar:${ETTX_LIB}/jta25b.jar:${ETTX_LIB}/log4j-1.2.4.jar:${ETTX_LIB}/saaj.jar:${ETTX_LIB}/sesm-sms.jar:${ETTX_LIB}/tibrvj.jar:${ETTX_LIB}/wsdl4j.jar:${ETTX_LIB}/xerces.jar:${CATALINA_HOME}/common/lib/servlet.jar:${ETTX_LIB}/sms_engine_api.jar:/vob/sms-admin/build/tests/classes
java -Dorg.apache.commons.logging.Log=log.properties -classpath ${CLASSPATH} org.apache.axis.transport.http.SimpleAxisServer -p 8000
