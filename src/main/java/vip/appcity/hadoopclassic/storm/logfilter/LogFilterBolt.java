package vip.appcity.hadoopclassic.storm.logfilter;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

public class LogFilterBolt extends BaseBasicBolt {
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
