
import com.google.common.collect.Lists;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.vectorizer.encoders.AdaptiveWordValueEncoder;
import org.apache.mahout.vectorizer.encoders.FeatureVectorEncoder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Created by von on 6/12/14.
 */


public class Indexer {

    static int VECTOR_WIDTH = 4000;

    public static void main(String[] args) throws IOException {

        FeatureVectorEncoder encoder = new AdaptiveWordValueEncoder("message");

        encoder.setProbes(2);
//        BufferedReader reader = new BufferedReader(new FileReader(""));

        String pathname = new File(Indexer.class.getClassLoader().
                getResource("SMSSpamCollection").getFile()).getAbsolutePath();
//        System.out.print(pathname);
        Vector v = new RandomAccessSparseVector(VECTOR_WIDTH);

        BufferedReader reader = new BufferedReader(new FileReader(pathname));

        List<String> doc = Lists.newArrayList();

        //Add documents to storage.
        List<String>targets = Lists.newArrayList();
        String line;
        while ((line=reader.readLine())!= null) {
            doc.add(line);
        }

    }

}