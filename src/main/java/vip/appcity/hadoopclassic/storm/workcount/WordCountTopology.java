package vip.appcity.hadoopclassic.storm.workcount;

import backtype.storm.topology.TopologyBuilder;

public class WordCountTopology {
    public static void main(String[] args) {
        final TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout("wcspout", new WordCountSpout());


    }

}
