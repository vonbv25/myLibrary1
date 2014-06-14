package Hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

import java.io.IOException;
import java.net.URI;

/**
 * Created by von on 6/14/14.
 */
public class Connection_Driver {

    public static void Connection() throws IOException{
        String uri = "hftp://0.0.0.0:50070/";
        Configuration hadoop_conf = new Configuration();

        FileSystem fs = FileSystem.get(URI.create(uri), hadoop_conf);
        fs.printStatistics();
    }


}
