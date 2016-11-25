#!/bin/bash

HDFS="hdfs dfs"

streaming_dir="/user/hadoop/spark/web_logs"
$HDFS -rm "${streaming_dir}"
$HDFS -mkdir "${streaming_dir}"
$HDFS -rm "${streaming_dir}"'/tmp/*' > /dev/null 2>&1
$HDFS -rm "${streaming_dir}"'/*' 	 > /dev/null 2>&1
$HDFS -mkdir "${streaming_dir}/tmp"

while [ 1 ]; do
	echo "[]========================STRAT BASH==========================[]"
	./sample_web_log.py > test.log
	chmod 777 test.log
	echo "[]=====>test.log generated locally<====="
	tmplog="access.`date +'%s'`.log"
	echo "[]=====>$tmplog<====="
	$HDFS -put test.log ${streaming_dir}/tmp/$tmplog
	echo "[]=====>put test.log to ${streaming_dir}/tmp/$tmplog<====="
	$HDFS -mv ${streaming_dir}/tmp/$tmplog ${streaming_dir}/

	echo "[]=====>`date +"%F %T"` put $tmplog to HDFS succeed<====="
	echo "[]========================END BASH==========================[]"
	echo " "
	sleep 1
	
done