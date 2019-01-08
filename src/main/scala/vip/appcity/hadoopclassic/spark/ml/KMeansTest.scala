package vip.appcity.hadoopclassic.spark.ml

import org.apache.spark.mllib.clustering.{KMeans, KMeansModel}
import org.apache.spark.mllib.linalg
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object KMeansTest {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setAppName("KMeans")
    conf.setMaster("local")
    val sc = new SparkContext(conf)
    val rawdata: RDD[String] = sc.textFile("data/spark/ml/kmeans_data.txt")
    val parsedData: RDD[linalg.Vector] = rawdata.map(s => Vectors.dense(s.split(" ").map(_.toDouble))).cache()
    val numCluster = 4
    val numInteraction = 100
    val meansModel: KMeansModel = new KMeans()
      .setK(numCluster)
      .setMaxIterations(numInteraction)
      .run(parsedData)
    val centers: Array[linalg.Vector] = meansModel.clusterCenters
    println("centers")
    centers.foreach(x=>
    println(x(0)+"\t"+x(1)+"\t"+x(2))
    )
    //计算误差
    val wss: Double = meansModel.computeCost(parsedData)
    println("wwssse:" + wss)


  }
}
