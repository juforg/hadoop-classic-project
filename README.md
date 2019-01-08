# hadoop-practice

> hadoop2 环境搭建参看我的博客 http://blog.appcity.vip

- hadoop
    - [] hdfs - 基本操作 增删改查
    - [] MapReduce- 统计个数 wordcount
    - [] MapReduce - 二度人脉 connections
    - [] MapReduce - 词频 TF-IDF
- hbase
- spark
    - [] traffictech 交通流量监控
    - MLlib
        - [] 预测 线性回归
        - [] 垃圾邮件过滤 朴素贝叶斯 80%
        - [] k-means 80%
- elastic search
- storm
    - [] wordcount 字数统计
    - [] logfilter 日志过滤
        -实例参考 https://github.com/apache/storm/tree/master/external/storm-kafka
    - [] Flume+kafka+storm整合
    - [] 移动通信掉话率统计 Planned
    - [] DRPC


## 打包发布

直接用gradle的 jar task 就好
![](http://wntc-1251220317.cossh.myqcloud.com/2018/11/15/1542241819272.png)

core-site.xml
hdfs-site.xml
yarn-site.xml
必须包含否则运行失败
打包时需把


## 参考
- https://github.com/trex-group/Big-Data/issues/19
- https://wiki.apache.org/hadoop/WordCount