package Hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobStatus;
import org.apache.hadoop.mapreduce.JobID;
import sun.security.krb5.Config;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;

/**
 * Created by von on 6/14/14.
 */
public class Connection_Driver {

    public static void Connection() throws IOException{
        String uri = "hdfs://192.168.3.193:9000/";
        Configuration hadoop_conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(uri), hadoop_conf);
        fs.printStatistics();
        System.out.println( fs.getHomeDirectory().toString());
        System.out.println(fs.getUri().getHost());
        System.out.println(fs.getWorkingDirectory().toString());
    }



    public static void main(String[] args) throws IOException{
        Connection();
    }


}
