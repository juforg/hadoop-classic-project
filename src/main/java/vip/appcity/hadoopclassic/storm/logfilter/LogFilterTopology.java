package vip.appcity.hadoopclassic.storm.logfilter;

import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;

import java.util.ArrayList;
import java.util.List;

public class LogFilterTopology {
    public static void main(String[] args) {
        final TopologyBuilder topologyBuilder = new TopologyBuilder();
        String topic = "kafkatest";
        final ZkHosts zkHosts = new ZkHosts("sj-node1:2181,sj-node2:2181");
        final SpoutConfig spoutConfig = new SpoutConfig(zkHosts, topic, "/kafka_storm", "test_id");
        List<String> zkServers = new ArrayList<String>();
        for (String host : zkHosts.brokerZkStr.split(",")) {
            zkServers.add(host.split(":")[0]);
        }
        spoutConfig.zkServers = zkServers;
        spoutConfig.zkPort = 2181;
        spoutConfig.forceFromStart= true;
        spoutConfig.socketTimeoutMs = 60 * 1000;
        spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
        final KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);
        topologyBuilder.setSpout("kafka_spout", kafkaSpout,3);
        topologyBuilder.setBolt("filter", new LogFilterBolt(),3);

    }
}
