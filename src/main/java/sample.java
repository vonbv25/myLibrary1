import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import org.apache.mahout.math.*;
import org.apache.mahout.vectorizer.encoders.AdaptiveWordValueEncoder;
import org.apache.mahout.vectorizer.encoders.FeatureVectorEncoder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by von on 6/10/14.
 */
public class sample {

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
    }

    public static Vector encode_text(String document,String label) throws IOException{
        FeatureVectorEncoder encoder= new AdaptiveWordValueEncoder("text");
        encoder.setProbes(2);
        Analyzer analyzer= new StandardAnalyzer(Version.LUCENE_46);
        StringReader in = new StringReader(document);
        TokenStream ts= analyzer.tokenStream("content",in);
        CharTermAttribute termAtt = ts.addAttribute(CharTermAttribute.class);
        ts.reset();
        Vector v1= new RandomAccessSparseVector(100);
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

    public static void main(String[] args) throws IOException{

        BufferedReader reader = new BufferedReader(new FileReader(
                sample.class.getClassLoader().getResource("SMSSpamCollection").getFile()
        ));
        List<Vector> data = new ArrayList<Vector>();
        String line;
        while ((line = reader.readLine())!= null) {
            if(line.startsWith("ham")) {
                data.add(encode_text(line,"ham"));
            }
            else
            {
                data.add(encode_text(line,"spam"));
            }
        }
//        System.out.printf("%s\n", new SequentialAccessSparseVector(data.get(90)));
//        System.out.print(data.size());
        //write data to file

        File testData = new File("testdata");

        if (!testData.exists()) {
            testData.mkdir();
        }
        testData = new File("testdata/data");
        if (testData.exists()) {
            testData.mkdir();
        }

        Configuration conf= new Configuration();
        FileSystem fs = FileSystem.get(conf);
        writeDataToFile(data,"testdata/data/vsm",fs,conf);
        conf.a











    }


}
