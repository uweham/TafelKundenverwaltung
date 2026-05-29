#!/bin/bash
# Tafel realtime test script
# Generate xml file and send it to MQTT Broker
# 
# Version 0.0.1

##
REALTIME="/tmp/tafelergrealtime_test.xml"
TAFELPATH=$HOME"/Tafel Kundenverwaltung"

## MQTT Broker
MOSQUITTO_USER="tafel"
MOSQUITTO_PWD="tafel#2025"
MOSQUITTO_HOST="localhost"
MOSQUITTO_PORT="1883"
############

#Test var
while true;
do

	DATE=$(date +%F)
	NOW="$(date +"%T")"
	HOUSEHOLD=$(( $(date "+10#%H * 60 + 10#%M") ))
	HOUSEHOLD=$(( (HOUSEHOLD)/10))
	PARENTS=$(( ($HOUSEHOLD)*2))
	CHILDS=$(( (HOUSEHOLD)*3))
	PERSONS=$(( PARENTS+CHILDS ))

	echo "$HOUSEHOLD"
	echo "$PARENTS"
	echo "$CHILDS"
	echo "$PERSONS"
	cat <<EOF >"$REALTIME"
<?xml version="1.0"?>

<resultset statement="select DATE(erfassungsZeit) as DATUM, 
	curtime() as time,
	count(*) as anzahlHaushalte, 
	sum(anzahlErwachsene) as anzahlErwachsene,
	sum(anzahlKinder) as anzahlKinder, 
	sum(anzahlErwachsene+anzahlKinder) as anzahlPersonen from einkauf  
	where isnull(storniertAm) and erfassungsZeit in (select max(erfassungsZeit) from einkauf)                
	group by DATUM
" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <row>
	<field name="DATUM">$DATE</field>
	<field name="time">$NOW</field>
	<field name="anzahlHaushalte">$HOUSEHOLD</field>
	<field name="anzahlErwachsene">$PARENTS</field>
	<field name="anzahlKinder">$CHILDS</field>
	<field name="anzahlPersonen">$PERSONS</field>
  </row>
</resultset>
EOF


	mosquitto_pub -h "$MOSQUITTO_HOST" -p "$MOSQUITTO_PORT" -t tafel/realtime  -f "$REALTIME"  -u "$MOSQUITTO_USER" -P "$MOSQUITTO_PWD"
	echo "Press <CTRL+C> to exit."

	sleep 10s
done

