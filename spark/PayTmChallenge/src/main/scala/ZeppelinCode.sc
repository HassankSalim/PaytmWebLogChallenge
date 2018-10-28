import com.mongodb.hadoop.BSONFileInputFormat
import org.bson.BSONObject
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.functions._

val conf = new SparkConf()
conf.setMaster("local[*]")
conf.setAppName("Zeppelin")

val sc = new SparkContext(conf)
val sqlContext = new org.apache.spark.sql.SQLContext(sc)
import sqlContext.implicits._

val mongoRDD = sc.newAPIHadoopFile("file:///home/hasali/personal/PaytmWebLogChallenge/data/paytm_log.bson",classOf[BSONFileInputFormat].asSubclass(classOf[org.apache.hadoop.mapreduce.lib.input.FileInputFormat[Object,BSONObject]]), classOf[Object], classOf[BSONObject])

val castedRdd = mongoRDD.map {
  case(id, bsonObject) => (
    id.toString,
    try {
      bsonObject.get("alb-name").asInstanceOf[String]
    } catch {
      case e: Exception => "N/A"
    },
    try {
      bsonObject.get("domain").asInstanceOf[String]
    } catch {
      case e: Exception => "N/A"
    },
    try {
      bsonObject.get("request_uri").asInstanceOf[String]
    } catch {
      case e: Exception => "N/A"
    },
    try {
      bsonObject.get("http_method").asInstanceOf[String]
    } catch {
      case e: Exception => "N/A"
    },
    try {
      bsonObject.get("log_timestamp").asInstanceOf[String]
    } catch {
      case e: Exception => "N/A"
    },
    try {
      bsonObject.get("target_processing_time").asInstanceOf[Double]
    } catch {
      case e: Exception => 0.0
    },
    try {
      bsonObject.get("request_processing_time").asInstanceOf[Double]
    } catch {
      case e: Exception => 0.0
    },
    try {
      bsonObject.get("response_processing_time").asInstanceOf[Double]
    } catch {
      case e: Exception => 0.0
    },
    try {
      bsonObject.get("received_bytes").asInstanceOf[Int]
    } catch {
      case e: Exception => -1
    },
    try {
      bsonObject.get("target_status_code").asInstanceOf[Int]
    } catch {
      case e: Exception => -1
    },
    try {
      bsonObject.get("elb_status_code").asInstanceOf[Int]
    } catch {
      case e: Exception => -1
    },
    try {
      bsonObject.get("geoip").asInstanceOf[BSONObject].get("ip").asInstanceOf[String]
    } catch {
      case e: Exception => "N/A"
    },
    try {
      bsonObject.get("geoip").asInstanceOf[BSONObject].get("city_name").asInstanceOf[String]
    } catch {
      case e: Exception => "N/A"
    },
    try {
      bsonObject.get("geoip").asInstanceOf[BSONObject].get("region_name").asInstanceOf[String]
    } catch {
      case e: Exception => "N/A"
    },
    try {
      bsonObject.get("geoip").asInstanceOf[BSONObject].get("longitude").asInstanceOf[Double]
    } catch {
      case e: Exception => 0.0
    },
    try {
      bsonObject.get("geoip").asInstanceOf[BSONObject].get("latitude").asInstanceOf[Double]
    } catch {
      case e: Exception => 0.0
    },
    try {
      bsonObject.get("user_agent_exists").asInstanceOf[Boolean]
    } catch {
      case e: Exception => false
    },
    try {
      if(bsonObject.get("user_agent_exists").asInstanceOf[Boolean])
        bsonObject.get("agent_details").asInstanceOf[BSONObject].get("os").asInstanceOf[String]
      else
        "N/A"
    } catch {
      case e: Exception => "N/A"
    },
    try {
      if(bsonObject.get("user_agent_exists").asInstanceOf[Boolean])
        bsonObject.get("agent_details").asInstanceOf[BSONObject].get("name").asInstanceOf[String]
      else
        "N/A"
    } catch {
      case e: Exception => "N/A"
    },
    try {
      if(bsonObject.get("user_agent_exists").asInstanceOf[Boolean])
        bsonObject.get("agent_details").asInstanceOf[BSONObject].get("device").asInstanceOf[String]
      else
        "N/A"
    } catch {
      case e: Exception => "N/A"
    }
  )
}

val columnNames = Seq("_id", "alb-name", "domain", "request_uri", "http_method", "log_timestamp", "target_processing_time", "request_processing_time", "response_processing_time", "received_bytes", "target_status_code", "elb_status_code", "ip",
  "city_name", "region_name", "longitude", "latitude", "user_agent_exists", "os", "browser", "device")

val df = castedRdd.toDF(columnNames: _*)

val getReqs = df.filter("http_method = 'GET'")
val avgGetRespTime = getReqs.select(avg($"response_processing_time"))
avgGetRespTime.show()

import org.apache.spark.sql.types.IntegerType
val dfWithDate = df.withColumn("date", df("log_timestamp").substr(0, 10)).withColumn("year", df("log_timestamp").substr(0, 4).cast(IntegerType)).withColumn("month", df("log_timestamp").substr(6, 2).cast(IntegerType)).withColumn("day", df("log_timestamp").substr(9, 2).cast(IntegerType))

val groupedByOs = dfWithDate.filter("os != 'N/A'").groupBy("os").count()
val totalReqFromValidOs = groupedByOs.select("count").rdd.map(_(0).asInstanceOf[Long]).reduce(_ + _)
val osReqPercent = groupedByOs.withColumn("percentage", (groupedByOs("count") / totalReqFromValidOs) * 100)

osReqPercent.sort($"percentage".desc).show(10)