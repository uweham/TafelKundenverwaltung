#!/bin/bash
# Tafel Database statistik script
#
# functions
replace_placeholder()
{
	sed -i 's!'$1'!'$2'!g' "$3"
}

die()
{
	echo $1
	read -r -n 1  -p "Press any key to continue..."	
	exit 2
}

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

STATISTIK_CSV_1="/data_export/tmp/Statistik_Teil1.csv"
STATISTIK_CSV_2="/data_export/tmp/Statistik_Teil2.csv"
STATISTIK_CSV_3="/data_export/tmp/Statistik_Teil3.csv"
VAR_HOLDER="##"
TAFELPATH=$HOME"/Tafel Kundenverwaltung"
SQLFILE="Tafel_statistik_gen_"$(date +"%Y-%m-%d_%H-%M-%S")".sql"
SQLTEMPLATE="statistik_template.sql"
DATABASEINFO=$TAFELPATH"/DatabaseInfo.properties"

#Database var ###
#from ini DatabaseInfo.properties
DATABASE=$(read_ini_key "jdbc.dbName" "$DATABASEINFO")
DATAUSER=$(read_ini_key "jdbc.username" "$DATABASEINFO")
DATAPASSWD=$(read_ini_key "jdbc.password" "$DATABASEINFO" | openssl enc -d -bf-ecb -base64 -K 71783923485962574825346840497a55 -provider legacy -provider default)
DATAHOST=$(read_ini_key "jdbc.hostname" "$DATABASEINFO")
DATAPORT=$(read_ini_key "jdbc.port" "$DATABASEINFO")
##################

echo  "**********************************************"
echo "Tafel Statistik Auswertung"

IFS="." read -p "Enter Zeitraum JJJJ.MM : " YEAR MONTH 
# check input values
YEAR="${YEAR//[^0-9]/}"
MONTH="${MONTH//[^0-9/]}"
if [ $YEAR -lt 2025 ]; then
	die "ERROR Jahr falsch: $YEAR"
fi
if [ $MONTH -lt 1 ]; then
	die "ERROR MONAT falsch: $MONTH"
fi
if [ $MONTH -gt 12 ]; then
	die "ERROR MONAT falsch: $MONTH"
fi
# end check

echo "Jahr $YEAR Monat $MONTH"


# 
# check template file exists
if [ ! -f "$TAFELPATH/$SQLTEMPLATE" ] ; then
	die "ERROR Template Datei fehlt : $TAFELPATH/$SQLTEMPLATE "
fi
# 


cp "$TAFELPATH"/"$SQLTEMPLATE" "$TAFELPATH"/"$SQLFILE"

# Replace Placeholder
replace_placeholder "$VAR_HOLDER"YEAR"$VAR_HOLDER" $YEAR "$TAFELPATH"/"$SQLFILE"
replace_placeholder "$VAR_HOLDER"MONTH"$VAR_HOLDER" $MONTH "$TAFELPATH"/"$SQLFILE"
replace_placeholder "$VAR_HOLDER"STATISTIK_CSV_1"$VAR_HOLDER" $STATISTIK_CSV_1 "$TAFELPATH"/"$SQLFILE"
replace_placeholder "$VAR_HOLDER"STATISTIK_CSV_2"$VAR_HOLDER" $STATISTIK_CSV_2 "$TAFELPATH"/"$SQLFILE"
replace_placeholder "$VAR_HOLDER"STATISTIK_CSV_3"$VAR_HOLDER" $STATISTIK_CSV_3 "$TAFELPATH"/"$SQLFILE"
rm -f "$STATISTIK_CSV_1"
rm -f "$STATISTIK_CSV_2"
rm -f "$STATISTIK_CSV_3"

mysql --host="$DATAHOST"  --user="$DATAUSER" --password="$DATAPASSWD" --port="$DATAPORT" "$DATABASE" < "$TAFELPATH"/"$SQLFILE"
rm -f "$TAFELPATH"/"$SQLFILE"
echo "CSV Dateien erzeugt !"
echo  "$STATISTIK_CSV_1" 
echo  "$STATISTIK_CSV_2" 
echo  "$STATISTIK_CSV_3" 
echo  "**********************************************"
echo  "Fertig  "
read -r -n 1  -p "Press any key to continue..."
