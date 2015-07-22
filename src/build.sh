#!/bin/bash

javac us/dashernet/piv/*
jar cvfm clientauth.jar Manfiest.txt us/dashernet/piv/*.class
