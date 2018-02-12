#!/bin/sh
# Shell script to open a new terminal and launch the given editor
# Be sure to use and keep Unix line delimiters for this file
# ( with Eclipse use : File / Convert Line Delimiters To / Unix )
# You can customize this file to add another terminal command  
#  

EDITOR=$1
#echo $EDITOR
FILE=$2
#echo $FILE

# Try to find the terminal command for the current Linux OS
if [ -x "$(command -v xterm)" ] ; then
  xterm -e "$EDITOR $FILE"
elif [ -x "$(command -v gnome-terminal)" ] ; then
  gnome-terminal -e "$EDITOR $FILE"
elif [ -x "$(command -v lxterminal)" ] ; then
  lxterminal -e $EDITOR $FILE
elif [ -x "$(command -v konsole)" ] ; then
  konsole -e "$EDITOR $FILE"
elif [ -x "$(command -v uxterm)" ] ; then
  uxterm -e "$EDITOR $FILE"
elif [ -x "$(command -v lxterm)" ] ; then
  lxterm -e "$EDITOR $FILE"
elif [ -x "$(command -v xfce4-terminal)" ] ; then
  xfce4-terminal -e "$EDITOR $FILE"
elif [ -x "$(command -v koi8rxterm)" ] ; then
  koi8rxterm -e "$EDITOR $FILE"
fi
