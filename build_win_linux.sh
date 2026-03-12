#!/bin/sh
TAFELPROJ=$(pwd)

echo $TAFELPROJ
echo 'Build win'
set -x #echo on
mvn clean install -DskipTests -Pwindows -Djavafx.platform=win -Ddir="$TAFELPROJ"/target_win
mvn clean install -DskipTests -Plinux -Djavafx.platform=linux -Ddir="$TAFELPROJ"/target_linux
