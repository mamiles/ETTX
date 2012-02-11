#!/usr/bin/ksh 

#  This script creats a installable tar file to install ETTX Admin Application
DATE=`date '+%Y%m%d'`

ETTX_VOB_ROOT=/vob/sms-admin
ETTX_3RDPARTY_ROOT=/auto/austin-nms/ettx/3rdparty
if [ $# -eq 0 ]; then
	ETTX_TAR_DIR=`ckpath -a -y -p "Please input the Directory where you would like to put the tar file.  Press \"Return\" to accept the default (Default = /tmp)" \
		-d /tmp` || exit $?
elif [ $# -eq 1 ]; then
	ETTX_TAR_DIR=$1
else
	echo "Incorrect usage.  You must enter only one directory on the command."
	echo "Terminating..."
fi

VER=`cat $ETTX_VOB_ROOT/install/version`
ETTX_INSTALL=/tmp/ettx_admin_${VER}_$DATE

rm -rf $ETTX_INSTALL
mkdir -p $ETTX_INSTALL
echo "Copying build..."
cp -r $ETTX_VOB_ROOT/build $ETTX_INSTALL
cp -r $ETTX_VOB_ROOT/config $ETTX_INSTALL
cp -r $ETTX_VOB_ROOT/script $ETTX_INSTALL
mkdir -p $ETTX_INSTALL/install
cp $ETTX_VOB_ROOT/install/checkinstall $ETTX_INSTALL/install
cp $ETTX_VOB_ROOT/install/patches.dat $ETTX_INSTALL/install
cp $ETTX_VOB_ROOT/install/version $ETTX_INSTALL/install
cp $ETTX_VOB_ROOT/README $ETTX_INSTALL

echo "Copying Java..."
mkdir -p $ETTX_INSTALL/3rdparty/j2sdk1.4.1_01
cp -r $ETTX_3RDPARTY_ROOT/java/j2sdk1.4.1_01/bin $ETTX_INSTALL/3rdparty/j2sdk1.4.1_01
cp -r $ETTX_3RDPARTY_ROOT/java/j2sdk1.4.1_01/jre $ETTX_INSTALL/3rdparty/j2sdk1.4.1_01
cp -r $ETTX_3RDPARTY_ROOT/java/j2sdk1.4.1_01/lib $ETTX_INSTALL/3rdparty/j2sdk1.4.1_01

echo "Copying Tibco..."
mkdir -p $ETTX_INSTALL/3rdparty/rv-6.8
cp -r $ETTX_3RDPARTY_ROOT/rv-6.8/bin $ETTX_INSTALL/3rdparty/rv-6.8
cp -r $ETTX_3RDPARTY_ROOT/rv-6.8/lib $ETTX_INSTALL/3rdparty/rv-6.8
cp -r $ETTX_3RDPARTY_ROOT/rv-6.8/include $ETTX_INSTALL/3rdparty/rv-6.8

echo "Copying Tomcat..."
mkdir -p $ETTX_INSTALL/3rdparty/jakarta-tomcat
cp $ETTX_3RDPARTY_ROOT/jakarta-tomcat/jakarta-tomcat-4.0.6.tar $ETTX_INSTALL/3rdparty/jakarta-tomcat

echo "Copying Expect..."
mkdir -p $ETTX_INSTALL/3rdparty/Expect-Tcl
cp $ETTX_3RDPARTY_ROOT/Expect-Tcl/expectEttx.tar $ETTX_INSTALL/3rdparty/Expect-Tcl

echo "Copying ObjSelector..."
mkdir -p $ETTX_INSTALL/3rdparty/ObjSelector
cp $ETTX_3RDPARTY_ROOT/ObjSelector/runtime.tar $ETTX_INSTALL/3rdparty/ObjSelector

echo "Copying UII..."
mkdir -p $ETTX_INSTALL/3rdparty/uii/uii_5.1_i18n_Fix1
cp $ETTX_3RDPARTY_ROOT/uii/uii_5.1_i18n_Fix1/uii-blank.war $ETTX_INSTALL/3rdparty/uii/uii_5.1_i18n_Fix1

cp $ETTX_VOB_ROOT/install/ettx_install.sh $ETTX_INSTALL
chmod +x $ETTX_INSTALL/ettx_install.sh

echo "Creating tar..."

#  Create TAR
cd /tmp
tar -cf - ./ettx_admin_${VER}_$DATE | compress > $ETTX_TAR_DIR/ettx_admin_${VER}_$DATE.tar.Z
echo "$ETTX_TAR_DIR/ettx_admin_${VER}_$DATE.tar.Z created"

# remove directory
rm -rf /tmp/ettx_admin_${VER}_$DATE
