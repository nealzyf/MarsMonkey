#!/bin/bash


cd $(dirname $0)


#Make sure the logs dir exists under the module
mkdir -p ./logs
SERVICE_NAME=tool-service
MODULE_NAME=tool-service
VERSION=1.0.0-SNAPSHOT
SYS_ERR_LOG=./logs/syserr.log
LOG_PATH=/var/log/neal
mkdir -p ${LOG_PATH}

PID_FILE=${MODULE_NAME}.pid

PID="x"$(pgrep -f ${MODULE_NAME})
if [ ! ${PID} = "x" ]; then
        #Check if there's a PID file
        if [ -f ${PID_FILE} ]; then
                PID=`cat ${PID_FILE}`
                #Find out if there is a process with the pid found in the file
                #If not then the var only contains 'x' make sure grep dot get false positive by using -w flag
                STARTED="x"`ps -ef | grep -w "${PID}" | grep "${MODULE_NAME}"`
        else
                PID=$(pgrep -f ${MODULE_NAME})
                STARTED=${PID}
        fi
else
    STARTED="x"
fi


case $1 in
start)
    if [ "${STARTED}" = "x" ]; then
        java  -jar \
        -XX:NewRatio=2 \
        -Xms256G \
        -Xmx256G \
        -Xloggc:"./logs/gc.log" \
        -XX:+PrintGCTimeStamps \
        -XX:+PrintGCDetails \
        -XX:+UseParallelGC \
        -XX:-DisableExplicitGC \
        -XX:+UseGCLogFileRotation \
        -XX:NumberOfGCLogFiles=10 \
        -XX:GCLogFileSize=1M \
        -XX:+HeapDumpOnOutOfMemoryError \
        -XX:HeapDumpPath="${LOG_PATH}/${MODULE_NAME}.$$.hprof" \
        -XX:+CrashOnOutOfMemoryError \
        -XX:+ExitOnOutOfMemoryError \
        -XX:ErrorFile="${LOG_PATH}/${MODULE_NAME}.$$.hs_err.log" \
        -XX:+UnlockDiagnosticVMOptions \
        ${MODULE_NAME}-${VERSION}.jar & > ${LOG_PATH}/${MODULE_NAME}-console.log & echo $! > ${PID_FILE}
        #wait for the started_file to get created which signals that the process has finished starting
        time=0
        while [[ ${time} -lt 3 && ! -f ${LOG_PATH}/${MODULE_NAME}.log ]]
        do
           sleep 1
           time=$[$time+1]
        done
        if [ -f ${LOG_PATH}/${MODULE_NAME}.log ]; then
            tail -f ${LOG_PATH}/${MODULE_NAME}.log
            exit 0
        else
            pid=$(pgrep -f ${MODULE_NAME})
            kill -9 "${pid}" 2> /dev/null
            exit 124
        fi
    else
        echo "${MODULE_NAME} is already started (${PID})"
        exit 0
    fi
;;

stop)
    #if we find a running process, kill it and keep looping until it's dead
    if [ ! "${STARTED}" = "x" ] && rm -f "${PID_FILE}"; then
        echo "Stopping ${MODULE_NAME} (${PID})"
        while :
        do
            if kill "${PID}" 2> /dev/null; then
                sleep 1
            else
                echo "Stopped ${MODULE_NAME} (${PID})"
                exit 0
            fi
        done
    else
        echo "No ${MODULE_NAME} to stop (either could not find file ${PID_FILE} or no such process)"
    fi
    exit 0
;;

status)
    if [ ! "${STARTED}" = "x" ]; then
        echo "${MODULE_NAME} is running (${PID})"
        exit 0
    else
        echo "${MODULE_NAME} is not running"
        exit 3
    fi
;;

*)
    echo "Usage: $0 {start|stop|status}" >&2
    exit 1
;;

esac
