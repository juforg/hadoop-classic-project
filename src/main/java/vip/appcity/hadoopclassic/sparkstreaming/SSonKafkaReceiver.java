package vip.appcity.hadoopclassic.sparkstreaming;

import org.apache.spark.SparkConf;

public class SSonKafkaReceiver {
    public static void main(String[] args) {
        final SparkConf conf = new SparkConf();
        conf.setMaster("local").setAppName("SSonKafkaReceiver");
    }
}
