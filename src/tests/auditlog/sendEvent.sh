#!/bin/csh
setenv ETTX_ROOT /opt/ettx
setenv ETTX_LIB ${ETTX_ROOT}/lib
setenv CLASSPATH ${ETTX_ROOT}/config:${ETTX_LIB}/axis-ant.jar:${ETTX_LIB}/axis.jar:${ETTX_LIB}/commons-discovery.jar:${ETTX_LIB}/commons-logging.jar:${ETTX_LIB}/ettx_admin_server.jar:${ETTX_LIB}/jaxrpc.jar:${ETTX_LIB}/jta25b.jar:${ETTX_LIB}/log4j-1.2.4.jar:${ETTX_LIB}/saaj.jar:${ETTX_LIB}/sesm-sms.jar:/auto/austin-nms/ettx/3rdparty/rv-6.8/lib/tibrvj.jar:${ETTX_LIB}/wsdl4j.jar:${ETTX_LIB}/xerces.jar:${CATALINA_HOME}/common/lib/servlet.jar:/vob/sms-admin/build/tests/classes
java -DETTX_ROOT=${ETTX_ROOT} -classpath ${CLASSPATH} com.cisco.ettx.admin.tests.auditlog.SendEvent $*
