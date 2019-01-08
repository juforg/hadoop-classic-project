package vip.appcity.hadoopclassic.spark.ml

import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.{LabeledPoint, LinearRegressionModel, LinearRegressionWithSGD}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object LinearRegression {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setAppName("lr")
    conf.setMaster("local")
    val sc = new SparkContext(conf)
    val data_path = "data/spark/lpsa.data"
    val data: RDD[String] = sc.textFile(data_path)
    val examples: RDD[LabeledPoint] = data.map { line =>
      val strings: Array[String] = line.split(',')
      LabeledPoint(strings(0).toDouble, Vectors.dense(strings(1).split(' ').map(_.toDouble)))
    }
    examples.cache()
    val train2TestData: Array[RDD[LabeledPoint]] = examples.randomSplit(Array(0.8,0.2),1)
    
    //迭代次数
    val numIteration = 100
    //梯度下降算法步数
    val stepSize = 0.1
    val mineBatchFraction = 1.0
    val lrsgd: LinearRegressionWithSGD = new LinearRegressionWithSGD()

    //设置需不需要设置截距
    lrsgd.setIntercept(false)
    lrsgd.optimizer.setStepSize(stepSize)
    lrsgd.optimizer.setMiniBatchFraction(mineBatchFraction)
    lrsgd.optimizer.setNumIterations(numIteration)
    val regressionModel: LinearRegressionModel = lrsgd.run(train2TestData(0))
    println(regressionModel.intercept)
    println(regressionModel.weights)
  }
}
