#!/bin/bash
# Install Tafel Statistik script
# 
# 
TAFELPATH=$HOME"/Tafel Kundenverwaltung"
DESKTOPFILE="Statistik.desktop"
STATISTIKSCRIPT="statistik.sh"
ICON="tafelstatistik.png"

SOURCEPATH=$(pwd)
 
echo "Install Tafel Statistik Script"
# 

# check 
if [ ! -f "$TAFELPATH/$STATISTIKSCRIPT" ] ; then
	echo "ERROR Script Datei fehlt : $TAFELPATH/$STATISTIKSCRIPT "
	echo "Abbruch !" 
	exit 2
fi
#
if [ "$TAFELPATH" != "$SOURCEPATH" ] ; then
	cp -f "$SOURCEPATH/$STATISTIKSCRIPT" "$TAFELPATH/$STATISTIKSCRIPT"
	cp -f "$SOURCEPATH/$ICON" "$TAFELPATH/$ICON"
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
