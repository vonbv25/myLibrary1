package Hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;

/**
 * Created by von on 6/16/14.
 */
public class HdfsConnectionTest {

    public static void main(String[] args) throws Exception{

        Configuration conf = new Configuration();
        conf.set("fs.deafault.name","hdfs://0.0.0.0:50070");
        conf.addResource(new Path("conf/core-site.xml"));
        FileSystem fs = FileSystem.get(conf);

        fs.printStatistics();
    }
}
