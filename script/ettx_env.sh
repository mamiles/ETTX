export ETTX_ROOT=/tmp

#  This script is used to create the envionment varables for ETTX Admin App

export ETTX_BIN=$ETTX_ROOT/bin
export ETTX_LIB=$ETTX_ROOT/lib
export ETTX_SCRIPT=$ETTX_ROOT/script
export ETTX_3RDPARTY=$ETTX_ROOT/3rdparty

export CATALINA_HOME=$ETTX_ROOT/jakarta-tomcat-4.0.6
export JAVA_HOME=$ETTX_3RDPARTY/j2sdk1.4.1_01

# Tibco 6.8
export TIBCO_HOME=$ETTX_3RDPARTY/rv-6.8
export TIBCO_LIB_DIR=$TIBCO_HOME/lib
export TIBCO_BIN_DIR=$TIBCO_HOME/bin

export CLASSPATH=\
$JAVA_HOME/lib/tools.jar:\
$ETTX_ROOT/lib/axis.jar:\
$ETTX_ROOT/lib/axis-ant.jar:\
$ETTX_ROOT/lib/commons-discovery.jar:\
$ETTX_ROOT/lib/commons-logging.jar:\
$ETTX_ROOT/lib/ettx_admin_server.jar:\
$ETTX_ROOT/lib/i18n.jar:\
$ETTX_ROOT/lib/jakarta-regexp-1.2.jar:\
$ETTX_ROOT/lib/jaxrpc.jar:\
$ETTX_ROOT/lib/jta25b.jar:\
$ETTX_ROOT/lib/log4j-1.2.4.jar:\
$ETTX_ROOT/lib/objselector.jar:\
$ETTX_ROOT/lib/ogs-objselector.jar:\
$ETTX_ROOT/lib/saaj.jar:\
$ETTX_ROOT/lib/sesm-sms.jar:\
$ETTX_ROOT/lib/struts.jar:\
$TIBCO_LIB_DIR/tibrvj.jar:\
$TIBCO_LIB_DIR/tibrvjweb.jar:\
$ETTX_ROOT/lib/uii.jar:\
$ETTX_ROOT/lib/wsdl4j.jar:\
$ETTX_ROOT/lib/sms_engine_api.jar:\
$ETTX_ROOT/lib/xerces.jar:\
$ETTX_ROOT/config:\
$CATALINA_HOME/common/lib/servlet.jar:\
$CATALINA_HOME/webapps/ettx_admin/config:\
$CATALINA_HOME/webapps/ettx_admin/WEB-INF/classes


export LD_LIBRARY_PATH=$ETTX_ROOT/lib:$TIBCO_LIB_DIR:$LD_LIBRARY_PATH
export PATH=$ETTX_BIN:$TIBCO_BIN_DIR:${JAVA_HOME}/jre/bin:${PATH}
