#!/bin/sh
if [ "$SHUTDOWN_TIMER" != "" ]; then
       echo "sleep $SHUTDOWN_TIMER"
       sleep $SHUTDOWN_TIMER
fi
echo "kill java process"
kill $(ps aux | grep java | grep -v grep | awk '{print $1}')
