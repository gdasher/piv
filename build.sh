#!/bin/bash

set -e

rm -f src/us/dashernet/piv/*.class
rm -f clientauth.jar

cd src
javac us/dashernet/piv/*
jar cvfm ../clientauth.jar ../Manfiest.txt us/dashernet/piv/*.class
