import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.jobcontrol.Job;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.IOException;
import java.net.URI;
import java.security.PrivilegedAction;

/**
 * Created by von on 6/18/14.
 */
public class Sample2 {

    public static void main(String a[]) {
        UserGroupInformation ugi
                = UserGroupInformation.createRemoteUser("acteam");

        try {
            ugi.doAs(new PrivilegedAction<Object>()  {

                public Void run(){
                    try {
                        String uri = "hdfs://192.168.3.193:9000/";
                        Configuration hadoop_conf = new Configuration();
                        FileSystem fs = FileSystem.get(URI.create(uri), hadoop_conf);
                        fs.printStatistics();
                        fs.mkdirs(new Path("testdata/data"));
                        System.out.println(fs.getWorkingDirectory());
//
//                        FileStatus[] status = fs.listStatus(new Path("/testdata/data"));
//                        for(int i=0;i<status.length;i++){//                            System.out.println(status[i].getPath());
//                        }

                    }
                    catch (IOException e){
                        throw new IllegalArgumentException(e);
                    }

                    // write your remaining piece of code here.

                    return null;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
