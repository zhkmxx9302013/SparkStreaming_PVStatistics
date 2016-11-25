# SparkStreaming_PVStatistics
A simulation of real-time log files collection system, and make a statistic on the logs, to show the information of PV reduced by several situation. 

#Environment of the project
* This project is a Spark Streaming test program, so the main environment of the Spark-Core is the version of [ spark-1.6.2-bin-hadoop2.6](http://d3kbcqa49mib13.cloudfront.net/spark-1.6.2-bin-hadoop2.6.tgz).
* The scala version the Spark-Core depends on is the `2.10.4`.
* The HDFS is provided by the `2.7.1` version of the `Hadoop`.
* The python environment is the version of the 2.7.13
* The `*.scala` files and related jar files were built in the IDEA IDE, you can also use the Eclipse IDE, to rebuild this jar file.

#How to use the project
1. Copy the `sample_web_log.py` and the `generate_logs.sh` files into the same directory on the Linux File System.
2. Then copy the jar file into Linux File System, here the jar file name is `FirstSpark.jar`.
3. Before starting the `generate_logs.sh`, I recommend you to make two directories on your HDFS first, here according to the bash file, this directory is used for store the log files, and the two directories are `/user/hadoop/spark/web_logs` and `/user/hadoop/spark/web_logs/tmp`
4. Now you can run the `generate_logs.sh` file.
5. Then, use `spark-submit` command to analyze the PV data.
