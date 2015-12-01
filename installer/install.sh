#!/usr/bin/env bash

installpath=$HOME/.softserve
mkdir $installpath
tar xzvf scrummachine.tar.gz -C $installpath
echo "Icon=$HOME/.softserve/softServeLogo_small.png" >> $installpath/scrummachine.desktop
echo "Exec=java -jar $HOME/.softserve/project-6-1.0.0.jar" >> $installpath/scrummachine.desktop
cp $installpath/scrummachine.desktop $HOME/Desktop/
mv $installpath/scrummachine.desktop $HOME/.local/share/applications/
chmod +x $HOME/.local/share/applications/
chmod +x $HOME/Desktop/scrummachine.desktop
chmod +x $installpath/project-6-1.0.0.jar
