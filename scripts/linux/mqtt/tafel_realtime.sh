#!/bin/bash
# Tafel realtime script
# Generate xml file from the tafel database and send it to MQTT Broker
# 
# Version 0.0.1

#
read_ini_key()
{

	key=${1}
	ini=${2}
	# read line with the key
	line=$(grep '^\'$key'' -A 0  "$ini")
	# remove first = , key and remove all leading and trailing spaces 
	value=$(echo $line|sed 's/=//'|sed 's/'$key'//'|sed -E 's/^[[:space:]]+//'|sed -E 's/[[:space:]]+$//')
	 
	echo $value

}
#Database var ###
REALTIME="/tmp/tafelergrealtime.xml"
TAFELPATH=$HOME"/Tafel Kundenverwaltung"
DATABASEINFO=$TAFELPATH"/DatabaseInfo.properties"
SQLFILE="tafel_realtime.sql"
#Broker
MOSQUITTO_USER="<mosquitto user>"
MOSQUITTO_PWD="<mosquitto_password>"
MOSQUITTO_HOST="<mosquitto_host>"
MOSQUITTO_PORT="<mosquitto_port>"

#from ini DatabaseInfo.properties
DATABASE=$(read_ini_key "jdbc.dbName" "$DATABASEINFO")
DATAUSER=$(read_ini_key "jdbc.username" "$DATABASEINFO")
DATAPASSWD=$(read_ini_key "jdbc.password" "$DATABASEINFO" | openssl enc -d -bf-ecb -base64 -K 71783923485962574825346840497a55 -provider legacy -provider default)
DATAHOST=$(read_ini_key "jdbc.hostname" "$DATABASEINFO")
DATAPORT=$(read_ini_key "jdbc.port" "$DATABASEINFO")
##################
mysql --xml --host="$DATAHOST"  --user="$DATAUSER" --password="$DATAPASSWD" --port="$DATAPORT" "$DATABASE" < "$TAFELPATH"/"$SQLFILE" >"$REALTIME"
mosquitto_pub -h "$MOSQUITTO_HOST" -p "$MOSQUITTO_PORT" -t tafel/realtime  -f "$REALTIME"  -u "$MOSQUITTO_USER" -P "$MOSQUITTO_PWD"

rm -f "$REALTIME"