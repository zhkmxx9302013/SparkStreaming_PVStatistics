package com.zhkmxx.scala.app

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by zhaozihe on 2016/11/25.
  */
object SparkStreamPv {
  def main(args: Array[String]): Unit = {
    val batch = 10
    //215.55.156.214 - - [2016-11-25 14:54:07] "GET /zhkmxx.jsp HTTP/1.1" 200 0 "http://www.baidu.com/s?wd=news" "Mozilla / 4.0(MAC) AppleWebKit / 537.36(KHTML, likeGecko) Chrome / 53.0.2785.143Safari / 537.36" "-"
    //{63.187.215.124 - - [2016-11-25 14:54:07] "GET /list.php HTTP/1.1" 200 0 "-" "Mozilla / 5.0(Android) AppleWebKit / 537.36(KHTML, likeGecko) Chrome / 53.0.2785.143Safari / 537.36" "-"}
    val conf = new SparkConf().setMaster("spark://localhost.localdomain:7077").setAppName("PV Statistic Streaming")
    val ssc = new StreamingContext(conf,Seconds(batch))

    val lines = ssc.textFileStream("hdfs://localhost.localdomain:8020/user/hadoop/spark/web_logs")
    println("[]===============SUM PV=================[]")
    lines.count().print()

    println("[]===========PV REDUCE BY IP============[]")
    lines.map(line => (line.split(" ")(0), 1)).reduceByKey(_+_).transform(rdd => {
      rdd.map(ip_pv => (ip_pv._2, ip_pv._1)).sortByKey(false).map(ip_pv => (ip_pv._2, ip_pv._1))
    }).print()

    println("[]======PV REDUCE BY SEARCH ENGINE======[]")
    val refer = lines.map(line => line.split("\"")(3)) //200 0后面"http://www.baidu.com/s?wd=news&wd=today"或"-"
    val searchEngineInfo = refer.map(r =>{
      val f = r.split('/')
      val searchEngines = Map(
        "www.google.cn" -> "q",
        "www.baidu.com" -> "wd",
        "www.sogou.com" -> "query",
        "www.yahoo.com" -> "p",
        "cn.bing.com" -> "1"
      )

      if(f.length > 2){//非"-"
        val host = f(2)
        if(searchEngines.contains(host)) {
          val query = r.split('?')(1) //query=[wd=news&wd=today]
          if(query.length > 0) {//有路由参数
            val arr_search_q = query.split('&').filter(_.indexOf(searchEngines(host)+"=") == 0)
            if(arr_search_q.length > 0)
              (host,arr_search_q(0).split("=")(1))
            else
              (host, "")
          }else{
            (host,"")
          }
        }else{
          ("","")
        }
      }else{
        ("","")
      }
    })
    searchEngineInfo.filter(_._1.length > 0).map(p => {(p._1, 1)}).reduceByKey(_+_).print()

    println("[]========PV REDUCE BY KEY WORDS========[]")
    searchEngineInfo.filter(_._2.length > 0).map(p => {(p._1, 1)}).reduceByKey(_+_).print()

    println("[]======PV REDUCE BY TERMINAL TYPE======[]")
    lines.map(_.split("\"")(5)).map(agent => {//Mozilla / 4.0(MAC) AppleWebKit / 537.36(KHTML, likeGecko) Chrome / 53.0.2785.143Safari / 537.36
      val types = Seq("Iphone","Android","MAC","Linux")
      var r = "Default"
      for (t <- types){
        if(agent.indexOf(t) != -1)
          r = t
      }
      (r,1)
    }).reduceByKey(_+_).print()

    println("[]========PV REDUCE BY WEB PAGES========[]")
    lines.map(line => {
      (line.split("\"")(1).split(" ")(1),1)
    }).reduceByKey(_+_).print()

    ssc.start()
    ssc.awaitTermination()
  }//main
}//object
