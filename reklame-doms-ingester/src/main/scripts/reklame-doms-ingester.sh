#!/bin/sh

topdir=$(dirname $(readlink -f $0))/..

CLASSPATH="-classpath $topdir/conf:$topdir/lib/*"
MAINCLASS="dk.statsbiblioteket.doms.reklame.ReklameIngesterCLI"
java $CLASSPATH $MAINCLASS $*
