#!/usr/bin/env bash
cp src/main/resources/softServeLogo_small.png target/
cp src/main/resources/scrummachine.desktop target/
cp installer/install.sh target/
cd target
tar zcf scrummachine.tar.gz project-6-1.0.0.jar softServeLogo_small.png scrummachine.desktop
tar zcf scrummachine-linux.tar.gz  install.sh scrummachine.tar.gz
rm install.sh scrummachine.desktop softServeLogo_small.png