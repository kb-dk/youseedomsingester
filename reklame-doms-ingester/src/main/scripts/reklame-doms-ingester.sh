#!/bin/sh

TOPDIR=$(dirname $(readlink -f $0))/..

CLASSPATH="-classpath $TOPDIR/conf:$TOPDIR/lib/*"
MAINCLASS="dk.statsbiblioteket.doms.reklame.ReklameIngesterCLI"
java $CLASSPATH $MAINCLASS "$@"
