# hadoop-practice

> hadoop2 环境搭建参看我的博客 http://blog.appcity.vip

- hdfs - 基本操作 增删改查
- MapReduce- 统计个数 wordcount
- MapReduce - 二度人脉 connections
- MapReduce - 词频 TF-IDF

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