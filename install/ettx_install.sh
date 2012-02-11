#!/usr/bin/ksh

#
# this script is used to move built code from build area to runtime area
# this script is also used to move code from 3rdparty area to runtime area

CUR_DIR=`pwd`
if [ ! -f $CUR_DIR/ettx_install.sh ]; then
	echo "\nERROR - You must change to the directory where ettx_install.sh is located"
	echo "before running the script.\n"
	echo "Terminating...\n"
	exit 1
fi

if [ "$CUR_DIR" == "/vob/sms-admin/install" ]; then
	echo "\nLooks like you are doing a Install from the VOB.\n"
	VOB=1
	ETTX_VOB_ROOT=/vob/sms-admin
	ETTX_3RDPARTY_ROOT=/auto/austin-nms/ettx/3rdparty
else
	VOB=0
	ETTX_VOB_ROOT=$CUR_DIR
	ETTX_3RDPARTY_ROOT=$CUR_DIR/3rdparty
fi

version=`cat $ETTX_VOB_ROOT/install/version`
echo
echo "***************************************************************************"
echo "***************************************************************************"
echo
echo "                        ETTX Admin Application"
echo  
echo "                       Version $version"
echo
echo "***************************************************************************"
echo "***************************************************************************"


/usr/bin/sh $ETTX_VOB_ROOT/install/checkinstall $ETTX_VOB_ROOT/install/patches.dat
if [ $? -ne 0 ]; then
	exit 1
fi

#
# create the runtime directory structure.
create_rt_dir_pre() {
    echo "Runtime directory root is $1"
    if [ -d $1 ]; then
	echo "... Ok"
    else
	echo "... Oops"
	fatal_error "Bad root directory: $1"
    fi
}

create_rt_dir() {
    echo "Creating runtime directory structure..."

    create_rt_dir_pre $1
    ETTX_RUNTIME_ROOT=$1

    for i in 3rdparty logs iphist event; do
	mkdir -p $ETTX_RUNTIME_ROOT/$i 2>/dev/null
    done
    chmod 777 $ETTX_RUNTIME_ROOT/logs
    chmod 777 $ETTX_RUNTIME_ROOT/iphist
    chmod 777 $ETTX_RUNTIME_ROOT/event

    create_rt_dir_post $ETTX_RUNTIME_ROOT $1

    echo
    echo "The ETTX Admin Application needs to authenticate with SPE to allow the"
    echo "administrator to login."
    echo
    ETTX_USERID=`ckstr -Q -d "admin" -p "Please enter the SPE Admin App User Id (Default=admin)"` || exit $?

    echo

    /usr/bin/stty -echo
    ETTX_PASSWORD=`ckstr -Q -p "Please enter the password for $ETTX_USERID"` || (/usr/bin/stty echo; exit $?)
    ETTX_PASSWORD2=`ckstr -Q -p "Enter the password again"` || (/usr/bin/stty echo; exit $?)
    /usr/bin/stty echo

    if [ "$ETTX_PASSWORD" != "$ETTX_PASSWORD2" ]; then
	echo "\n\nThe passwords do not match...\n"
	/usr/bin/stty -echo
	ETTX_PASSWORD=`ckstr -Q -p "Please enter the password for $ETTX_USERID"` || (/usr/bin/stty echo; exit $?)
	ETTX_PASSWORD2=`ckstr -Q -p "Enter the password again"` || (/usr/bin/stty echo; exit $?)
	/usr/bin/stty echo
	if [ "$ETTX_PASSWORD" != "$ETTX_PASSWORD2" ]; then
		echo "\n\nThe passwords do not match...\n"
		echo "Exiting the install.  Please trying installing again."
		exit 1
	fi
    fi
    echo
    echo "Please enter the default UNIX host prompt for Telnet access."
    HOSTPROMPT=`ckstr -p "Press \"Return\" to accept the default (Default = $ )" -d $ ` || exit $?
    echo
    echo "ETTX Admin Application requires Tomcat Web Server 4.0.6 for the client and server."
    echo "This installer can install Tomcat for you or install into an existing tomcat installation."
    ans=`ckyorn -d n -p "Would you like to install Admin Application into existing Tomcat. Default=n "` || exit $?
    if [ "$ans" = "y" -o "$ans" = "yes" -o "$ans" = "Y" -o "$ans" = "YES" ]; then
    	INSTALL_TOMCAT=0
    	echo "\nPlease enter the path for the Tomcat Home or CATALINA_HOME."
    	echo "Make sure that your user id has write privileges to CATALINA_HOME/webapps and"
    	echo "CATALINA_HOME/bin"
    	CATALINA_HOME=`ckstr -p "Enter the path for CATALINA_HOME"  ` || exit $?
    	if [ ! -d $CATALINA_HOME/webapps ]; then
    		echo "\n\n$CATALINA_HOME/webapps does not exist."
    		echo "Please make sure CATALINA_HOME is correct and run the install again."
    		echo "Terminating. . ."
    		exit 1
    	fi
    else
    	INSTALL_TOMCAT=1
    	CATALINA_HOME=$ETTX_RUNTIME_ROOT/jakarta-tomcat-4.0.6
    fi
    
    if [ $INSTALL_TOMCAT -eq 0 ]; then
	TOMCAT_PORT=`ckint -p "Please enter the existing Web Server Port" ` || exit $?
	NUM_TOMPORT=`egrep -c "port=\"$TOMCAT_PORT\"" $CATALINA_HOME/conf/server.xml`
	if [ $NUM_TOMPORT -eq 0 ]; then
		echo "ERROR - Can not find port $TOMCAT_PORT in $CATALINA_HOME/conf/server.xml"
		echo "Please check to make sure this is the correct port"
		echo "Terminating..."
		exit 1
	fi
    else
    	echo "The Admin App Web Server requires 9 unused ports.  Please make sure"
    	echo "that these ports are free and not being used by other Web servers."
    	echo "The Web server port you enter will be the main port and the starting port."
    	echo "Please input the Admin App Web Server port number."
    	TOMCAT_PORT=`ckint -p "Press \"Return\" to accept the default (Default = 8080)" -d 8080` || exit $?
   	echo
       	TOMCAT_SHUT_PORT=`expr $TOMCAT_PORT + 5`
    	TOMCAT_SSL_PORT=`expr $TOMCAT_PORT + 3`
    	AJP13_PORT=`expr $TOMCAT_PORT + 9`
    	WARP_PORT=`expr $TOMCAT_PORT + 8`
    fi
    
    # Server host hard coded to local host for now.  When client is installed seperatly then this will change.
    SERVERHOST=`hostname`
    # Server port set to TOMCAT_PORT for now until client is installed on another machine.
    SERVERPORT=$TOMCAT_PORT
    
    if [ $INSTALL_TOMCAT -eq 1 ]; then
    	echo "The Admin App Client Tomcat port set to $TOMCAT_PORT"
    	echo "The Tomcat shutdown port set to $TOMCAT_SHUT_PORT"
    	echo "The Tomcat SSL port set to $TOMCAT_SSL_PORT"
    	echo "The AJP 13 Port set to $AJP13_PORT"
   	echo "The Apache Warp connector port set to $WARP_PORT"
   	echo "The Admin App Server port set to $SERVERPORT"
   	echo "The Admin App Server hostname set to $SERVERHOST"
   	echo
    fi
     
    echo "Please enter the SMS Hostname."
    SMSHOST=`ckstr -p "Press \"Return\" to accept the default (Default = localhost )" -d localhost ` || exit $?
    echo
    echo "Please input the SMS Web Server port number."
    SMSPORT=`ckint -p "Press \"Return\" to accept the default (Default = 8000)" -d 8000` || exit $?
    echo
    echo "Please enter the SPE Hostname."
    SPEHOST=`ckstr -p "Press \"Return\" to accept the default (Default = localhost )" -d localhost ` || exit $?
    echo
    echo "Please input the SPE Web Server port number."
    SPEPORT=`ckint -p "Press \"Return\" to accept the default (Default = 8000)" -d 8000` || exit $?
}

progress_function() {
    echo "Installing $1 components"
}

progress_function2() {
    echo "\t\t Installing $1 components"
}

create_rt_dir_post() {
    if [ -d $1 ]; then
	echo
    else
	echo "Error: $1 does not exist"
	fatal_error "Please check the permissions on $2"
    fi
}

install_3rdparty() {
    progress_function "3rd Party"

    install_java
    install_tibco
    install_jakarta_tomcat
}

install_java() {

    # Install the jdk 1.4
    progress_function2 "j2sdk 1.4.1_01"

    mkdir -p $ETTX_RUNTIME_ROOT/3rdparty/j2sdk1.4.1_01
    chmod 755 $ETTX_RUNTIME_ROOT/3rdparty/j2sdk1.4.1_01
    if [ $VOB -eq 0 ]; then
	cp -r $ETTX_3RDPARTY_ROOT/j2sdk1.4.1_01 $ETTX_RUNTIME_ROOT/3rdparty
    else
	cp -r $ETTX_3RDPARTY_ROOT/java/j2sdk1.4.1_01/bin $ETTX_RUNTIME_ROOT/3rdparty/j2sdk1.4.1_01
	cp -r $ETTX_3RDPARTY_ROOT/java/j2sdk1.4.1_01/jre $ETTX_RUNTIME_ROOT/3rdparty/j2sdk1.4.1_01
	cp -r $ETTX_3RDPARTY_ROOT/java/j2sdk1.4.1_01/lib $ETTX_RUNTIME_ROOT/3rdparty/j2sdk1.4.1_01
    fi
}

install_tibco() {
    progress_function2 "Tibco"

    mkdir -p $ETTX_RUNTIME_ROOT/3rdparty/rv-6.8
    chmod 755 $ETTX_RUNTIME_ROOT/3rdparty/rv-6.8
    if [ $VOB -eq 0 ]; then
	cp -r $ETTX_3RDPARTY_ROOT/rv-6.8 $ETTX_RUNTIME_ROOT/3rdparty
    else
	 cp -r $ETTX_3RDPARTY_ROOT/rv-6.8/bin $ETTX_RUNTIME_ROOT/3rdparty/rv-6.8
	 cp -r $ETTX_3RDPARTY_ROOT/rv-6.8/lib $ETTX_RUNTIME_ROOT/3rdparty/rv-6.8
	 cp -r $ETTX_3RDPARTY_ROOT/rv-6.8/include $ETTX_RUNTIME_ROOT/3rdparty/rv-6.8
    fi
}

install_jakarta_tomcat() {
    progress_function2 "Jakarta Tomcat 4.0.6"

    if [ $INSTALL_TOMCAT -eq 1 ]; then
    	cd $ETTX_RUNTIME_ROOT
    	tar -xf $ETTX_3RDPARTY_ROOT/jakarta-tomcat/jakarta-tomcat-4.0.6.tar
    	echo "\t\t\tRemoving unneeded files from the tomcat installation..."
    	rm -f $CATALINA_HOME/*.txt
   	rm -rf $CATALINA_HOME/webapps/examples
   	rm -rf $CATALINA_HOME/webapps/manager
    	rm -rf $CATALINA_HOME/webapps/tomcat-docs
    	rm -rf $CATALINA_HOME/webapps/webdav
    	rm -rf $CATALINA_HOME/webapps/ROOT

    	cp -r $ETTX_VOB_ROOT/config/webui/config/* $CATALINA_HOME/conf

    	echo "\t\t\tUpdating the server.xml with log directory and port..."
    	sed -e "s/8080/$TOMCAT_PORT/g" \
		-e "s/8005/$TOMCAT_SHUT_PORT/g" \
		-e "s/8443/$TOMCAT_SSL_PORT/g" \
		-e "s/8009/$AJP13_PORT/g" \
		-e "s/8008/$WARP_PORT/g" \
		-e "s!ETTXPATH!$CATALINA_HOME/conf/keystore!g" \
		$CATALINA_HOME/conf/server.xml >/tmp/server.xml.$$
    	mv /tmp/server.xml.$$ $CATALINA_HOME/conf/server.xml

    	#  Setup a link from tomcat log dir to ettx_root/logs

	echo "\t\t\tMoving the tomcat logs directory to $ETTX_RUNTIME_ROOT/logs ..."
    	rmdir $CATALINA_HOME/logs
    	ln -s $ETTX_RUNTIME_ROOT/logs $CATALINA_HOME/logs
    fi
    # Create the envrionment for Tomcat
    if [ -f $CATALINA_HOME/bin/setenv.sh ]; then
    	SETENV=`head -1 $CATALINA_HOME/bin/setenv.sh`
    	if [ "$SETENV" = "##ETTX-ADMIN##" ]; then
    		echo "##ETTX-ADMIN##"							>  $CATALINA_HOME/bin/setenv.sh
    	fi
    else
    	echo "##ETTX-ADMIN##"								>  $CATALINA_HOME/bin/setenv.sh
    fi
    echo "ETTX_HOME=$ETTX_RUNTIME_ROOT"							>> $CATALINA_HOME/bin/setenv.sh
    echo "export ETTX_HOME"								>> $CATALINA_HOME/bin/setenv.sh
    echo "CATALINA_HOME=$CATALINA_HOME" 						>> $CATALINA_HOME/bin/setenv.sh
    echo "export CATALINA_HOME"						 		>> $CATALINA_HOME/bin/setenv.sh
    echo "JAVA_HOME=$ETTX_RUNTIME_ROOT/3rdparty/j2sdk1.4.1_01"				>> $CATALINA_HOME/bin/setenv.sh
    echo "export JAVA_HOME"								>> $CATALINA_HOME/bin/setenv.sh
    echo "TIBCO_HOME=$ETTX_RUNTIME_ROOT/3rdparty/rv-6.8"				>> $CATALINA_HOME/bin/setenv.sh
    echo "export TIBCO_HOME"								>> $CATALINA_HOME/bin/setenv.sh
    echo "TIBCO_LIB_DIR=$ETTX_RUNTIME_ROOT/3rdparty/rv-6.8/lib"				>> $CATALINA_HOME/bin/setenv.sh
    echo "export TIBCO_LIB_DIR"								>> $CATALINA_HOME/bin/setenv.sh
    echo "TIBCO_BIN_DIR=$ETTX_RUNTIME_ROOT/3rdparty/rv-6.8/bin"				>> $CATALINA_HOME/bin/setenv.sh
    echo "export TIBCO_BIN_DIR"								>> $CATALINA_HOME/bin/setenv.sh
    echo "LD_LIBRARY_PATH=$ETTX_RUNTIME_ROOT/lib:\$TIBCO_HOME/lib:\$LD_LIBRARY_PATH" 	>> $CATALINA_HOME/bin/setenv.sh
    echo "export LD_LIBRARY_PATH" 							>> $CATALINA_HOME/bin/setenv.sh
    echo "PATH=$ETTX_RUNTIME_ROOT/bin:\$TIBCO_HOME/bin:\$JAVA_HOME/jre/bin:\${PATH}" 	>> $CATALINA_HOME/bin/setenv.sh
    echo "export PATH" 									>> $CATALINA_HOME/bin/setenv.sh
}

install_ui() {
    echo
    progress_function "UI"

    if [ -d $CATALINA_HOME/webapps/ettx_admin ]; then
    	rm -rf $CATALINA_HOME/webapps/ettx_admin
    fi
    echo "\tCreating $CATALINA_HOME/webapps/ettx_admin ..."
    mkdir -p $CATALINA_HOME/webapps/ettx_admin
    
    echo "\tCopying UII Blank WAR file..."
    # cp $ETTX_3RDPARTY_ROOT/uii/uii_5.1_i18n/uii-blank.war $CATALINA_HOME/webapps/ettx_admin
    cp $ETTX_3RDPARTY_ROOT/uii/uii_5.1_i18n_Fix1/uii-blank.war $CATALINA_HOME/webapps/ettx_admin
    current=`pwd`
    cd $CATALINA_HOME/webapps/ettx_admin
    $ETTX_RUNTIME_ROOT/3rdparty/j2sdk1.4.1_01/bin/jar -xf uii-blank.war
    rm -f uii-blank.war

    echo "\tCopying ObjSelector..."
    cp $ETTX_3RDPARTY_ROOT/ObjSelector/runtime.tar $CATALINA_HOME/webapps/ettx_admin
    tar -xf runtime.tar
    rm -f runtime.tar
    
    echo "\tCopying GUI WAR file..."
    cp $ETTX_VOB_ROOT/build/dist/ettx_admin.war $CATALINA_HOME/webapps/ettx_admin
    $ETTX_RUNTIME_ROOT/3rdparty/j2sdk1.4.1_01/bin/jar -xf ettx_admin.war
    rm -f ettx_admin.war

    ##echo "\tCopying CSR WAR file..."
    ##mkdir -p $CATALINA_HOME/webapps/csr
    ##cd $CATALINA_HOME/webapps/csr
    ##cp $ETTX_VOB_ROOT/build/dist/csr.war $CATALINA_HOME/webapps/csr
    ##$ETTX_RUNTIME_ROOT/3rdparty/j2sdk1.4.1_01/bin/jar -xf csr.war
    ##rm -f $CATALINA_HOME/webapps/csr.war
    
    cd $current

    # Update the log4j.config with logging dir

    echo "\tUpdating the GUI log4j.config file with log directory $ETTX_RUNTIME_ROOT/logs/WebGui.log ..."
    sed -e "s!ettxAdminGui.log!$ETTX_RUNTIME_ROOT/logs/ettxAdminGui.log!" $CATALINA_HOME/webapps/ettx_admin/WEB-INF/config/log4j.config > /tmp/server-log4j.prop.$$
    mv /tmp/server-log4j.prop.$$ $CATALINA_HOME/webapps/ettx_admin/WEB-INF/config/log4j.config

    # Udate web.xml with the tomcat ettx_admin home directory

    echo "\tUpdating the GUI web.xml file with correct ETTX_ADMIN_GUI_HOME, HOSTPROMPT, SERVERHOST, SERVERPORT ..."
    sed -e "s!GUIHOME!$CATALINA_HOME/webapps/ettx_admin!" \
	-e "s!HOSTPROMPT!$HOSTPROMPT!" \
	-e "s!SERVERHOST!$SERVERHOST!" \
	-e "s!SERVERPORT!$SERVERPORT!" \
	$CATALINA_HOME/webapps/ettx_admin/WEB-INF/web.xml > /tmp/web.xml.$$
    mv /tmp/web.xml.$$ $CATALINA_HOME/webapps/ettx_admin/WEB-INF/web.xml
    
    echo "\tUpdate About box with install date and version"
    ETTXVERSION=`cat $ETTX_RUNTIME_ROOT/.version`
    ETTXINSTDATE=`date +'%d %b %Y'`
    sed -e "s/ETTXVERSION/$ETTXVERSION/" \
        -e "s/ETTXINSTDATE/$ETTXINSTDATE/" \
        $CATALINA_HOME/webapps/ettx_admin/WEB-INF/screens/about.jsp > /tmp/about.jsp.$$
        mv /tmp/about.jsp.$$ $CATALINA_HOME/webapps/ettx_admin/WEB-INF/screens/about.jsp
}

install_server() {
    echo
    progress_function "Server"
    
    if [ -d $CATALINA_HOME/webapps/ettx_admin_server ]; then
    	rm -rf $CATALINA_HOME/webapps/ettx_admin_server
    fi
    echo "\tCreating $CATALINA_HOME/webapps/ettx_admin_server ..."
    mkdir -p $CATALINA_HOME/webapps/ettx_admin_server
    echo "\tCopying Server WAR file..."
    cp $ETTX_VOB_ROOT/build/dist/ettx_admin_server.war $CATALINA_HOME/webapps/ettx_admin_server
    current=`pwd`
    cd $CATALINA_HOME/webapps/ettx_admin_server
    $ETTX_RUNTIME_ROOT/3rdparty/j2sdk1.4.1_01/bin/jar -xf ettx_admin_server.war
    rm -f ettx_admin_server.war
    cd $current

    # Update the server-log4j.prop with the log directory

    echo "\tUpdating the server-log4j.prop file with $ETTX_RUNTIME_ROOT/logs/ettxServer.log ..."
    sed -e "s!ettxAdminServer.log!$ETTX_RUNTIME_ROOT/logs/ettxAdminServer.log!" $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/server-log4j.prop > /tmp/server-log4j.prop.$$
    mv /tmp/server-log4j.prop.$$ $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/server-log4j.prop

    echo "\tUpdating the Server web.xml file with correct TOMCAT_PORT, CATALINA_HOME ..."
    sed -e "s!TOMCATPORT!$TOMCAT_PORT!" \
	-e "s!CATALINAHOME!$CATALINA_HOME!" \
	-e "s!SMSHOST!$SMSHOST!" \
	-e "s!SMSPORT!$SMSPORT!" \
	-e "s!SPEHOST!$SPEHOST!" \
	-e "s!SPEPORT!$SPEPORT!" \
	$CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/web.xml > /tmp/web.xml.$$
    mv /tmp/web.xml.$$ $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/web.xml

    # generate the SystemConfig.xml and generate the password

    setup_system_config

    chmod +x $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/scripts/*

    echo "\tInstalling Expect and Tcl"
    cd $ETTX_RUNTIME_ROOT
    tar -xf $ETTX_3RDPARTY_ROOT/Expect-Tcl/expectEttx.tar
}

setup_system_config() {
	# Log4j properties must be setup in ETTX_ROOT/config before running encription utility
	# Also scripts (envionment variables must be setup first

	echo "\tGenerating the default $ETTX_USERID password and generationg the config/.key file ..."
	$ETTX_RUNTIME_ROOT/3rdparty/j2sdk1.4.1_01/bin/java -DETTX_ROOT=$CATALINA_HOME/webapps/ettx_admin_server/WEB-INF -classpath $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/classes:$CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/lib/log4j-1.2.4.jar com.cisco.ettx.admin.authentication.Encryption -e $ETTX_PASSWORD >/tmp/ettx-password.$$ 2>/dev/null
	ETTX_PASSWORD=`cat /tmp/ettx-password.$$ | tail -1 | /usr/bin/awk '{print $1}'`
	rm -f /tmp/ettx-password.$$

	echo "\tGenerating the SystemConfig.xml file..."

	echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"					>  $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/SystemConfig.xml
	echo "<SYSTEM_CONFIG>"									>> $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/SystemConfig.xml
	echo "	<USER_ID>$ETTX_USERID</USER_ID>"						>> $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/SystemConfig.xml
	echo "	<PASSWORD>$ETTX_PASSWORD</PASSWORD>"						>> $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/SystemConfig.xml
	echo "	<EVENT_PURGE_DURATION>12</EVENT_PURGE_DURATION>"				>> $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/SystemConfig.xml
	echo "	<EVENT_EXPORT_DIR>$ETTX_RUNTIME_ROOT/event</EVENT_EXPORT_DIR>"			>> $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/SystemConfig.xml
	echo "	<LEASE_HIST_EXPORT_SCHEDULE>true</LEASE_HIST_EXPORT_SCHEDULE>"			>> $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/SystemConfig.xml
	echo "	<LEASE_HIST_EXPORT_DAY>5</LEASE_HIST_EXPORT_DAY>"				>> $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/SystemConfig.xml
	echo "	<LEASE_HIST_EXPORT_TIME>23:00:00</LEASE_HIST_EXPORT_TIME>"			>> $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/SystemConfig.xml
	echo "	<LEASE_HIST_EXPORT_DIR>$ETTX_RUNTIME_ROOT/iphist</LEASE_HIST_EXPORT_DIR>"	>> $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/SystemConfig.xml
	echo "</SYSTEM_CONFIG>"									>> $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/SystemConfig.xml

	# Generate changes to the XML Components XML file
	
	echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"					>  $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/smscomponents.xml
	echo "<COMP_CONFIG>"									>> $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/smscomponents.xml
	echo "\t<COMPONENT NAME=\"CNR\">"							>> $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/smscomponents.xml
	echo "\t\t<COMPONENT_LABEL>DHCP Server</COMPONENT_LABEL>"				>> $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/smscomponents.xml
	echo "\t</COMPONENT>"									>> $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/smscomponents.xml

	echo "\t<COMPONENT NAME=\"SMS\">"							>> $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/smscomponents.xml
	echo "\t\t<COMPONENT_LABEL>System Configuration</COMPONENT_LABEL>"			>> $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/smscomponents.xml
	echo "\t</COMPONENT>"									>> $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/smscomponents.xml
	
	echo "\t<COMPONENT NAME=\"SPE\">"							>> $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/smscomponents.xml
	echo "\t\t<COMPONENT_LABEL>Policy Engine</COMPONENT_LABEL>"				>> $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/smscomponents.xml
	echo "\t</COMPONENT>"									>> $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/smscomponents.xml

	echo "\t<COMPONENT NAME=\"BAC\">"							>> $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/smscomponents.xml
	echo "\t\t<COMPONENT_LABEL>Inventory Management</COMPONENT_LABEL>"			>> $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/smscomponents.xml
	echo "\t</COMPONENT>"									>> $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/smscomponents.xml

	echo "\t<COMPONENT NAME=\"BPR\">"							>> $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/smscomponents.xml
	echo "\t\t<COMPONENT_LABEL>Provisioning Server</COMPONENT_LABEL>"			>> $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/smscomponents.xml
	echo "\t</COMPONENT>"									>> $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/smscomponents.xml
	
	echo "\t<COMPONENT NAME=\"AdminApplication\">"						>> $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/smscomponents.xml
	echo "\t\t<COMPONENT_LABEL>Administration Management</COMPONENT_LABEL>"			>> $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/smscomponents.xml
	echo "\t\t<HOST NAME=\"localhost\">"							>> $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/smscomponents.xml
	echo "\t\t\t<APPL_LOGIN_ID/>"								>> $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/smscomponents.xml
	echo "\t\t\t<HOST_PROMPT>$HOSTPROMPT</HOST_PROMPT>"					>> $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/smscomponents.xml
	echo "\t\t\t<USE_SECURE_SHELL>false</USE_SECURE_SHELL>"					>> $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/smscomponents.xml
	echo "\t\t\t<LOG_DIRS>"									>> $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/smscomponents.xml
	echo "\t\t\t\t<PATH>$ETTX_RUNTIME_ROOT/logs</PATH>"					>> $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/smscomponents.xml
	echo "\t\t\t\t<PATH>$ETTX_RUNTIME_ROOT/iphist</PATH>"					>> $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/smscomponents.xml
	echo "\t\t\t\t<PATH>$ETTX_RUNTIME_ROOT/event</PATH>"					>> $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/smscomponents.xml
	if [ $INSTALL_TOMCAT -eq 0 ]; then
		echo "\t\t\t\t<PATH>$CATALINA_HOME/logs</PATH>"					>> $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/smscomponents.xml
	fi
	echo "\t\t\t</LOG_DIRS>"								>> $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/smscomponents.xml
	echo "\t\t</HOST>"									>> $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/smscomponents.xml
	echo "\t</COMPONENT>"									>> $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/smscomponents.xml
	
	echo "</COMP_CONFIG>"									>> $CATALINA_HOME/webapps/ettx_admin_server/WEB-INF/config/smscomponents.xml

}

install_scripts() {
	progress_function "scripts"
	if [ $INSTALL_TOMCAT -eq 1 ]; then
		cp $ETTX_VOB_ROOT/script/ettx $ETTX_RUNTIME_ROOT/
		chmod 755 $ETTX_RUNTIME_ROOT/ettx
	fi
	rm -f $ETTX_RUNTIME_ROOT/.version
	cp $ETTX_VOB_ROOT/install/version $ETTX_RUNTIME_ROOT/.version
}

#
# create the runtime structure and install various components
main() {
    create_rt_dir $1
    install_scripts
    install_3rdparty
    install_ui
    install_server

    echo "All done"
}

#
#
# initialize setup
init_setup() {
    echo
    echo "Preparing to install ETTX Admin into: $1 \n"
    if [ -d $1 ]; then
	echo "Checking to see if system is up before removing. . ."
	if [ -f $1/ettx ]; then
		$1/ettx status
		if [ $? -eq 1 ]; then
			echo "Bringing down system. . ."
			$1/ettx stop
		fi
	fi
	echo "Removing old install if present.\n"
	echo
	ans=`ckyorn -d y \
		-p "Is it ok to delete all files under $1/* [Default=y] "` || exit $?
		if [ "$ans" = "y" -o "$ans" = "yes" -o "$ans" = "Y" -o "$ans" = "YES" ]; then
			echo "Deleteing current install. . ."
			rm -rf $1/*
		else
			echo "exiting. . ."
			exit 1
		fi
    else
	echo "Creating install directory $1 ..."
	mkdir -p $1
	if [ $? -ne 0 ]; then
		echo 
		echo "Cannot create directory.  Please check permissions on the parent directory"
		echo "before trying to install to this directory."
		echo "Script terminating..."
		exit 1
	fi
    fi
}

#
# Usage statement
usage()
{
    echo "Usage: ettx_install.sh [ -h[elp] | <target dir>]"
    echo " Target directory must be specified for installation."
    echo "  - With the \"-h[elp]\" option, this usage statement is displayed."
    echo
    exit 1
}

#
# call main

case $# in
    0)
      ETTX_RUNTIME_ROOT=`ckstr -p "Please enter the target installation directory: "` || exit $?
      init_setup $ETTX_RUNTIME_ROOT
      main $ETTX_RUNTIME_ROOT
   ;;
    1)
   case $1 in
       -h | -he | -hel | -help)
      echo
      usage
      ;;
       *)
      ETTX_RUNTIME_ROOT=$1
      init_setup $ETTX_RUNTIME_ROOT
      main $ETTX_RUNTIME_ROOT
      ;;
   esac
   ;;
    *)
   echo "ERROR - incorrect usage"
   echo
   usage
   ;;
esac
