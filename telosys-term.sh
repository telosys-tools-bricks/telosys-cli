#!/bin/sh
# Shell script to open a terminal 
# You can customize this file to add another terminal command  

EDITOR=$1
#echo $EDITOR
FILE=$2
#echo $FILE

# Try to find the terminal command for the current Linux OS
COMMAND=""
if [ -x "$(command -v xterm)" ]
then
  COMMAND="xterm -e"
elif [ -x "$(command -v gnome-terminal)" ]
then
  COMMAND="gnome-terminal -e"
elif [ -x "$(command -v konsole)" ]
then
  COMMAND="konsole -e"
elif [ -x "$(command -v uxterm)" ]
then
  COMMAND="uxterm -e"
elif [ -x "$(command -v lxterm)" ]
then
  COMMAND="lxterm -e"
elif [ -x "$(command -v xfce4-terminal)" ]
then
  COMMAND="xfce4-terminal -e"
elif [ -x "$(command -v koi8rxterm)" ]
then
  COMMAND="koi8rxterm -e"
fi

# Launch the terminal command to execute the given editor
echo $COMMAND "$EDITOR $FILE"
$COMMAND "$EDITOR $FILE"