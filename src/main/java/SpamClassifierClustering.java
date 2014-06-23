import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import org.apache.mahout.clustering.classify.WeightedPropertyVectorWritable;
import org.apache.mahout.clustering.kmeans.KMeansDriver;
import org.apache.mahout.clustering.kmeans.Kluster;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.math.NamedVector;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;
import org.apache.mahout.vectorizer.encoders.AdaptiveWordValueEncoder;
import org.apache.mahout.vectorizer.encoders.FeatureVectorEncoder;

import javax.xml.soap.Text;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by von on 6/23/14.
 */
public class SpamClassifierClustering {

    //
    public static void writeDataToFile (List<Vector> docs,String fileName,
                                        FileSystem fs, Configuration conf) throws IOException
    {
        Path path = new Path(fileName);
        SequenceFile.Writer writer=  new SequenceFile.Writer(fs,conf,path,
                LongWritable.class, VectorWritable.class);
        long key = 0;
        VectorWritable vec = new VectorWritable();
        for (Vector data:docs) {
            vec.set(data);
            writer.append(new LongWritable(key++),vec);
        }
        writer.close();
    }

    public static Vector encode_text(String document,String label,int VECTOR_WIDTH) throws IOException{
        FeatureVectorEncoder encoder= new AdaptiveWordValueEncoder("text");
        encoder.setProbes(2);
        Analyzer analyzer= new StandardAnalyzer(Version.LUCENE_46);
        StringReader in = new StringReader(document);
        TokenStream ts= analyzer.tokenStream("content",in);
        CharTermAttribute termAtt = ts.addAttribute(CharTermAttribute.class);
        ts.reset();
        Vector v1= new RandomAccessSparseVector(VECTOR_WIDTH);
        while(ts.incrementToken()){
            char[] termBuff= termAtt.buffer();
            int termLen=termAtt.length();
            String w = new String(termBuff,0,termLen);
            encoder.addToVector(w,1,v1);
        }
        ts.end();
        ts.close();
        return new NamedVector(v1, label);

    }

    public static void main(String[] args) throws Exception{

        int k = 2;
        BufferedReader reader = new BufferedReader(new FileReader(
                SpamClassifierClustering.class.getClassLoader().getResource("SMSSpamCollection").getFile()
        ));
        List<Vector> data = new ArrayList<Vector>();
        String line;
        while ((line = reader.readLine())!= null) {
            if(line.startsWith("ham")) {
                data.add(encode_text(line,"ham",1000));
            }
            else
            {
                data.add(encode_text(line,"spam",1000));
            }
        }
        File testData = new File("testdata");
        if (!testData.exists()) {
            testData.mkdir();
        }
        testData = new File("testdata/points");
        if (!testData.exists()) {
            testData.mkdir();
        }

        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);
        writeDataToFile(data, "testdata/points/file1", fs, conf);

        Path path = new Path("testdata/clusters/part-00000");
        SequenceFile.Writer writer = new SequenceFile.Writer(fs, conf,
                path, org.apache.hadoop.io.Text.class, Kluster.class);

        for (int i = 0; i < k; i++) {
            Vector vec = data.get(i);
            Kluster cluster = new Kluster(vec, i, new EuclideanDistanceMeasure());
            writer.append(new org.apache.hadoop.io.Text(cluster.getIdentifier()), cluster);
        }
        writer.close();
        KMeansDriver.run(conf, new Path("testdata/points"), new Path("testdata/clusters"),
                new Path("output"),
                0.001, 10, true, 0, false);

        SequenceFile.Reader reader1 = new SequenceFile.Reader(fs,
                new Path("output/" + Kluster.CLUSTERED_POINTS_DIR
                        + "/part-m-00000"), conf);

        IntWritable key = new IntWritable();
        WeightedPropertyVectorWritable value = new WeightedPropertyVectorWritable();
        while (reader1.next(key, value)) {
            System.out.println(value.toString() + " belongs to cluster "
                    + key.toString());
        }
        reader.close();
    }


}

