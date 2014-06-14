package Utilities;


import com.google.common.collect.Lists;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

/**
 * Created by von on 6/14/14.
 */
public class Indexer {

    public static Lists stringLists(StringReader reader) throws IOException{

        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);

        TokenStream ts = analyzer.tokenStream("content", reader);

        ts = new LowerCaseFilter(Version.LUCENE_46,ts);

        CharTermAttribute charTermAttribute = ts.addAttribute(CharTermAttribute.class);

        List<String> word = Lists.newArrayList();

        while (ts.incrementToken()) {
            char[] termbuffer = charTermAttribute.buffer();
            int termLength = charTermAttribute.length();
            String w = new String(termbuffer,0,termLength);
            word.add(w);
        }




    }

}
