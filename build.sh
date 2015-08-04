#!/bin/bash

set -e

rm -f src/us/dashernet/piv/*.class
rm -f clientauth.jar

cd src
javac us/dashernet/piv/*.java
jar cvfe ../clientauth.jar us.dashernet.piv.SslClientAuth us/dashernet/piv/{*.class,piv.cfg}
jar cvfm ../mysqltoy.jar ../MysqlManifest.txt us/dashernet/piv/{*.class,piv.cfg}
