package Utilities;


import com.google.common.collect.Lists;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import org.apache.mahout.math.NamedVector;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.vectorizer.encoders.AdaptiveWordValueEncoder;
import org.apache.mahout.vectorizer.encoders.FeatureVectorEncoder;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

/**
 * Created by von on 6/14/14.
 */
public class Indexer {

public static int VECTOR_WIDTH = 10000;

    public static List tokenized(Reader reader) throws IOException{

        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);
        TokenStream ts = analyzer.tokenStream("content", reader);
        ts = new LowerCaseFilter(Version.LUCENE_46,ts);
        ts = new PorterStemFilter(ts);
        CharTermAttribute charTermAttribute = ts.addAttribute(CharTermAttribute.class);
        List<String> words = Lists.newArrayList();

        while (ts.incrementToken()) {
            char[] termbuffer = charTermAttribute.buffer();
            int termLength = charTermAttribute.length();
            String w = new String(termbuffer,1,termLength);
            words.add(w);
        }
        return words;
     }


    public static Vector encoder(List<String> tokens, String label) {
        FeatureVectorEncoder encoder = new AdaptiveWordValueEncoder("content");
        encoder.setProbes(2);

        Vector v = new RandomAccessSparseVector(VECTOR_WIDTH);

        for (String word : tokens) {
            encoder.addToVector(word,1,v);
        }

        return new NamedVector(v,label);



    }









}
