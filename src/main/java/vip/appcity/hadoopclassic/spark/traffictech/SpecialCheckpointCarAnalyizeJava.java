package vip.appcity.hadoopclassic.spark.traffictech;

import com.alibaba.fastjson.JSONObject;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;

import java.util.Iterator;
import java.util.LinkedList;

public class SpecialCheckpointCarAnalyizeJava {
    public static void main(String[] args) {
        final SparkConf conf = new SparkConf();
        conf.setMaster("local");
        conf.setAppName("test");
        final JavaSparkContext sc = new JavaSparkContext(conf);
        final JavaRDD<String> factinfo = sc.textFile("data/spark/monitor_flow_action", 3);
        final JavaPairRDD<String, JSONObject> mapToPairCache = factinfo.mapToPair(new PairFunction<String, String, JSONObject>() {
            @Override
            public Tuple2<String, JSONObject> call(String s) throws Exception {
                String[] a = s.split("\t");
                final JSONObject jsonObject = new JSONObject();
                jsonObject.put("date", a[0]);
                jsonObject.put("monitId", a[1]);
                jsonObject.put("cameriaId", a[2]);
                jsonObject.put("carId", a[3]);
                jsonObject.put("time", a[4]);
                jsonObject.put("speed", a[5]);
                jsonObject.put("b", a[6]);
                jsonObject.put("type", a[7]);
                return new Tuple2<>(a[3], jsonObject);
            }
        }).cache();

        final JavaPairRDD<String, JSONObject> filtered = mapToPairCache.filter(new Function<Tuple2<String, JSONObject>, Boolean>() {
            @Override
            public Boolean call(Tuple2<String, JSONObject> stringJSONObjectTuple2) throws Exception {
                return "0001".equals(stringJSONObjectTuple2._2.getString("monitId"));
            }
        });
        final JavaPairRDD<String, Iterable<JSONObject>> grouped = filtered.groupByKey();
        final JavaPairRDD<String, Iterable<JSONObject>> allcars = mapToPairCache.groupByKey();
        final JavaPairRDD<String, Tuple2<Iterable<JSONObject>, Iterable<JSONObject>>> joined = allcars.join(grouped);
        joined.foreach(new VoidFunction<Tuple2<String, Tuple2<Iterable<JSONObject>, Iterable<JSONObject>>>>() {
            @Override
            public void call(Tuple2<String, Tuple2<Iterable<JSONObject>, Iterable<JSONObject>>> stringTuple2Tuple2) throws Exception {
                final String carid = stringTuple2Tuple2._1;
                final Iterable<JSONObject> jsonObjects = stringTuple2Tuple2._2._1;
                final Iterator<JSONObject> iterator = jsonObjects.iterator();
                final LinkedList<JSONObject> jsonObjects1 = new LinkedList<>();

                while(iterator.hasNext()){
                    JSONObject row = iterator.next();
                    final String time1 = row.getString("time");
                    for (int i = 0; i < jsonObjects1.size(); i++) {
                        JSONObject tmp = jsonObjects1.get(i);
                        final String time = tmp.getString("time");
                        if(time1.compareTo(time)<0){
                            jsonObjects1.add(i, row);
                            break;
                        }

                    }
                    if(jsonObjects1.size()==0){
                        jsonObjects1.add( row);
                    }
                }
                System.out.println("carid:"+carid);
                StringBuffer sb = new StringBuffer();
                StringBuffer sb2 = new StringBuffer();
                for (int i = 0; i < jsonObjects1.size(); i++) {
                    sb.append(jsonObjects1.get(i).getString("monitId"));

                    sb.append("->");
                    sb2.append(jsonObjects1.get(i).getString("time"));
                    sb2.append("->");
                }
                System.out.println("monitids:"+sb.toString());
                System.out.println("monitids time:"+sb2.toString());
            }
        });
        sc.close();


    }
}
