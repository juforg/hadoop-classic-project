package vip.appcity.hadoopclassic.storm.workcount;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;

import java.util.Map;

public class WordCountSpout extends BaseRichSpout {
    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {

    }

    @Override
    public void nextTuple() {

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
