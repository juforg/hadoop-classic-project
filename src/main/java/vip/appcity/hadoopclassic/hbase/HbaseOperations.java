package vip.appcity.hadoopclassic.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;


public class HbaseOperations {
    static Configuration config ;
    static HBaseAdmin hBaseAdmin;
    static String TN = null;

    public static void main(String[] args) throws IOException {
        config= new Configuration();
        config.set("hbase.zookeeper.quorum","sj-node1,sj-node2,sj-node3");
        hBaseAdmin = new HBaseAdmin(config);
        TN = "testhbase2";

        //1. 创建表
        createDateBase(TN);
        //2. put 数据
        putData();

    }
    public static void putData() throws IOException {
        HTable hTable = new HTable(config,TN);
        Put put = new Put(Bytes.toBytes("1102"));

    }

    public static void createDateBase(String tbname) throws IOException {
        if(hBaseAdmin.tableExists(tbname)){
            hBaseAdmin.disableTable(tbname);
            hBaseAdmin.deleteTable(tbname);
            System.out.println("删除表"+tbname);
        }
        HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf(tbname));
        HColumnDescriptor columnDescriptor = new HColumnDescriptor("cf1");
        columnDescriptor.setBlockCacheEnabled(true);
        columnDescriptor.setInMemory(true);
        columnDescriptor.setMaxVersions(3);
        tableDescriptor.addFamily(columnDescriptor);
        hBaseAdmin.createTable(tableDescriptor);
    }
}
