import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import org.apache.mahout.math.NamedVector;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.SequentialAccessSparseVector;
import org.apache.mahout.vectorizer.encoders.AdaptiveWordValueEncoder;
import org.apache.mahout.vectorizer.encoders.FeatureVectorEncoder;
import org.apache.mahout.vectorizer.encoders.StaticWordValueEncoder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by von on 6/10/14.
 */
public class sample {

    public static Vector encode_text(String document) throws IOException{
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
        return new NamedVector(v1, "ham");

    }

    public static void main(String[] args) throws IOException{

        BufferedReader reader = new BufferedReader(new FileReader(
                sample.class.getClassLoader().getResource("SMSSpamCollection").getFile()
        ));
        List<Vector> data = new ArrayList<Vector>();
        String line;
        while ((line = reader.readLine())!= null) {
            data.add(encode_text(line));
        }
        System.out.printf("%s\n", new SequentialAccessSparseVector(data.get(90)));
        System.out.print(data.size());


    }


}
