package vip.appcity.hadoopclassic.spark.sparkstreaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Durations, StreamingContext}

object SSonKafkaReceiver {
  def main(args: Array[String]): Unit = {
    System.setProperty("HADOOP_USER_NAME","root")
    val conf = new SparkConf()
    conf.setMaster("local[2]").setAppName("SSonKafkaReceiver")
    //开启wal机制
    val context = new StreamingContext(conf,Durations.seconds(5))
    context.checkpoint("data/checkpoint")
    var map: Map[String, Int] = Map()
    map += ("sk1" -> 1)
    map += ("sk2" -> 1)
    val lines = KafkaUtils.createStream(context,"sj-node1:2181","mygroup",map)
    val words = lines.flatMap(_._2.split(" "))
    val words2 = words.map((_,1))
    val result = words2.reduceByKey(_+_)
    result.print()
    context.start()
    context.awaitTermination()
    context.stop()

  }
}
