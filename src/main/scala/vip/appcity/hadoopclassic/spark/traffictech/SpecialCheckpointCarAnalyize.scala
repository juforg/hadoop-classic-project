package vip.appcity.hadoopclassic.spark.traffictech

import org.apache.spark.rdd.RDD
import org.apache.spark.{Accumulator, Partitioner, SparkConf, SparkContext}

case class FlowAction(date:String,
                      monitorId:String,
                      cameraId:String,
                      car:String,
                      actionTime:String,
                      speed:String,
                      roadId:String,
                      areaId:String
                     )
class CustomPartitioner(partitions: Int) extends Partitioner {

  require(partitions > 0, s"Number of partitions ($partitions) cannot be negative.")

  def numPartitions: Int = partitions

  def getPartition(key: Any): Int = key match {
    case (k: String, v: Int) => math.abs(k.hashCode % numPartitions)
    case f:FlowAction => math.abs(f.car.hashCode % numPartitions)
    case null => 0
    case _ => math.abs(key.hashCode % numPartitions)
  }

  override def equals(other: Any): Boolean = other match {
    case h: CustomPartitioner => h.numPartitions == numPartitions
    case _ => false
  }

  override def hashCode: Int = numPartitions
}
object FlowActionKey {
  implicit def orderingByGradeStudentScore[A <: FlowAction] : Ordering[A] = {
    //    Ordering.by(fk => (fk.grade, fk.student, fk.score * -1))
    Ordering.by(fk => (fk.car, fk.actionTime))
  }
}

object SpecialCheckpointCarAnalyize {
  /**
    * 求指定卡口下车辆的行车轨迹
    * @param args
    */
  def main(args: Array[String]): Unit = {
    System.setProperty("HADOOP_USER_NAME", "root")
    val specialCheckPoint = "0001"
    val conf = new SparkConf()
    conf.setMaster("local")
    conf.setAppName("SpecialCheckpointCarAnalyize")
    val sparkContext = new SparkContext(conf)
    val lineRdd = sparkContext.textFile("data/spark/monitor_flow_action",3)

    val rddcache = lineRdd.map(_.split("\t")).map(x=>((x(3),FlowAction(x(0),x(1),x(2),x(3),x(4),x(5),x(6),x(7))))).cache()
    val filteredcars = rddcache.filter(_._2.monitorId.equals(specialCheckPoint)).groupByKey().map(x=>(x._1,x._1)).cache()
    val l = filteredcars.count()
    println(l)
//    val joined: RDD[(String, (FlowAction, String))] = rddcache.join(filteredcars)
//    joined.map(x=>(x._2._1,x._2._1)).sortBy(_._1).map(x=>(x._1.car,x._1)).reduceByKey((x,y)=>(x.car,y))
    val joined: RDD[(String, (Iterable[FlowAction], String))] = rddcache.groupByKey().join(filteredcars)
    val flated: RDD[FlowAction] = joined.map(x=>(x._1,x._2._1)).values.flatMap(y=>y)
    implicit def FlowActionOrderingDesc = new Ordering[FlowAction] {
      override def compare(x: FlowAction, y: FlowAction): Int = {
        if (y.car.compare(x.car) == 0) -y.actionTime.compare(x.actionTime)
        else y.car.compare(x.car)
      }
    }
    val result: RDD[(FlowAction, FlowAction)] = flated.map(x=>(x,x)).repartitionAndSortWithinPartitions(new CustomPartitioner(l.toInt))
    val ac: Accumulator[Int] = sparkContext.accumulator(0)
    result.foreachPartition(x=>{
      var t:FlowAction = null
      x.foreach(y=>{
        t=y._1
        print(y._1.monitorId +":")
        print(y._2.actionTime+"->")

      }
      )
      ac.add(1)
      if(t != null) println(t.car)
    })
    println(ac.value)
  }
}
