#!/usr/bin/ksh
i=0;
while true; do
	i=$(($i+1));
	./sendEvent.sh CISCO.MGMT.CNS.SPE.SERVICE.SUBSCRIBE.SUCCESS ./SPEEvent2.xml
	./sendEvent.sh CISCO.MGMT.CNS.SPE.OBJECT.CREATE.SUCCESS ./SPEEvent1.xml
	./sendEvent.sh CISCO.MGMT.CNS.SPE.OBJECT.DELETE.ERROR ./SPEEvent3.xml
	sleep 600;
done
