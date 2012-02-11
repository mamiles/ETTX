#!/usr/bin/ksh

/usr/bin/ls -l `/usr/bin/egrep -i -l $1 $2 || echo null`
