#!/bin/bash
# Install Tafel Statistik script
# 
# 
TAFELPATH=$HOME"/Tafel Kundenverwaltung"
DESKTOPFILE="Statistik.desktop"
STATISTIKSCRIPT="statistik.sh"
STATISTIKTEMPLATE="statistik_template.sql"
ICON="tafelstatistik.png"

SOURCEPATH=$(pwd)
 
echo "Install Tafel Statistik Script"
# 

# check 
if [ ! -f "$SOURCEPATH/$STATISTIKSCRIPT" ] ; then
	echo "ERROR Script Datei fehlt : $SOURCEPATH/$STATISTIKSCRIPT "
	echo "Abbruch !" 
	exit 2
fi
#
if [ ! -f "$SOURCEPATH/$STATISTIKTEMPLATE" ] ; then
	echo "ERROR Template Datei fehlt : $SOURCEPATH/$STATISTIKTEMPLATE "
	echo "Abbruch !" 
	exit 2
fi
#
if [ "$TAFELPATH" != "$SOURCEPATH" ] ; then
	cp -f "$SOURCEPATH/$STATISTIKSCRIPT" "$TAFELPATH/$STATISTIKSCRIPT"
	cp -f "$SOURCEPATH/$ICON" "$TAFELPATH/$ICON"
	cp -f "$SOURCEPATH/$STATISTIKTEMPLATE" "$TAFELPATH/$STATISTIKTEMPLATE"
fi
 
chmod +x "$TAFELPATH/$STATISTIKSCRIPT"

# Create Desktopfile
cat <<EOF >"$TAFELPATH/$DESKTOPFILE"
[Desktop Entry]
Version=1.0
Type=Application
Terminal=true
Exec="$TAFELPATH/$STATISTIKSCRIPT"
Name=Tafel Statistik
Comment=Tafel Statistik
Icon=$TAFELPATH/$ICON
EOF

ln -f -s  "$TAFELPATH/$DESKTOPFILE" "$HOME/Desktop/DESKTOPFILE"
