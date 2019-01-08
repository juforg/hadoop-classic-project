package vip.appcity.hadoopclassic.spark.ml

import org.apache.spark.ml.classification.{NaiveBayes, NaiveBayesModel}
import org.apache.spark.ml.feature.{CountVectorizer, CountVectorizerModel}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Row, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}

object SpamMailFilter_Bayes {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setMaster("local")
    conf.setAppName("SpamMailFilter_Bayes")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    val rawData: RDD[String] = sc.textFile("data/spark/ml/sms_spam.txt")
    val rawData2: RDD[Array[String]] = rawData.map(_.split(",")).cache()
    val dataRows: RDD[Row] = rawData2.map(x=>Row(if(x(0)=="ham") 1.0 else 0.0 ,x(1).split(" ").map(_.trim)))
    val structType = StructType(List(
      StructField("label", DoubleType, nullable = false),
      StructField("words", ArrayType(StringType, true), nullable = false)
    ))
    val dframe: DataFrame = sqlContext.createDataFrame(dataRows,structType)
    // 构建词汇表
    val countVectorizerModel: CountVectorizerModel = new CountVectorizer().setInputCol("words").setOutputCol("features").fit(dframe)

    // 查看词汇表
    countVectorizerModel.vocabulary.take(100).foreach(println)

    // 文本向量化

    val vframe: DataFrame = countVectorizerModel.transform(dframe)
    vframe.show(false)

    val examples: DataFrame = vframe.drop("words")
    examples.show(10)

    //切分
    val Array(trainingData,testData) = examples.randomSplit(Array(0.8,0.2),seed = 1234L)

    //训练朴素贝叶斯模型

    val bayesModel: NaiveBayesModel = new NaiveBayes().fit(trainingData)
    val predictions: DataFrame = bayesModel.transform(testData)
    predictions.show()

    predictions.registerTempTable("result")

    val accuracy: DataFrame = sqlContext.sql("select (1- (sum(abs(label-prediction)))/count(label)) as accuracy from result")
    accuracy.show()


  }
}
