package vip.appcity.hadoopclassic;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;

/**
 * @author songjie
 */
public class HDFSOperations {
    static Configuration config ;
    static FileSystem fileSystem;
    static String resourcePath ;
    static String pathhdfsStr ;

    //ha模式调用
    public static void configure() throws Exception {
        config = new Configuration();
        config.set("fs.defaultFS", "hdfs://appcity");
        config.set("dfs.nameservices", "appcity");
        config.set("dfs.ha.namenodes.appcity", "nn1,nn2");
        config.set("dfs.namenode.rpc-address.appcity.nn1", "sj-node1:8020");
        config.set("dfs.namenode.rpc-address.appcity.nn2", "sj-node2:8020");
        config.set("dfs.client.failover.proxy.provider.appcity", "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");
//        fileSystem = FileSystem.get(URI.create("hdfs://192.168.199.101:9000"), config, "root");
        //文件系统、
        fileSystem = FileSystem.get(new URI("hdfs://appcity"), config,"root");

    }

    public static void main(String[] args) throws Exception {
        configure();
        // 不加此行 本地运行则可能会报权限问题
//        System.setProperty("HADOOP_USER_NAME", "root");
        //1. 初始化Configuration
//        config = new Configuration();
        // 提交jar包到服务器时 需把这里fs.defaultFS注释掉 本地运行则放开
//        config.set("fs.defaultFS","hdfs://sj-node1:8020");
//        fileSystem = FileSystem.get(config);
        pathhdfsStr= "/testoperation/testha";
        resourcePath=System.getProperty("user.dir")+"/src/main/resources";
        // 2.新建文件夹
        mkdir(pathhdfsStr);
        // 3. 上传文件 该文件存在resources目录下
        uploadfiles("qq.txt");
        // 4. 列出指定目录下所有文件
        listFiles(pathhdfsStr);

    }

    public static boolean mkdir(String pathstr) throws IOException {
        Path path = new Path(pathstr);
        return fileSystem.mkdirs(path);
    }
    public static void uploadfiles(String filename) throws IOException {
        File file = new File(resourcePath+"/"+filename);
        Path path = new Path(pathhdfsStr+"/"+filename);
        FSDataOutputStream fsDataOutputStream= fileSystem.create(path);
        IOUtils.copyBytes(new FileInputStream(file),fsDataOutputStream,config);
        System.out.println("上传结束");

    }
    public static void listFiles(String str) throws IOException {
        Path path = new Path(str);
        RemoteIterator<LocatedFileStatus> iterator = fileSystem.listFiles(path, true);

        while (iterator.hasNext()) {
            LocatedFileStatus status = iterator.next();
            System.out.println(status.getPath().getName());
        }
    }
}
