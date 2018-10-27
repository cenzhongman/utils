package com.cenzhongman.util.nlp;

import com.hankcs.hanlp.utility.SentencesUtil;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author 岑忠满
 * @date 2018/10/26 11:51
 */
public class SentenceUtil {
    public static List<String> toCnSentence(String doc){
        return SentencesUtil.toSentenceList(doc);
    }

    public static List<String> toCnSentence(String doc,boolean shortest){
        return SentencesUtil.toSentenceList(doc,shortest);
    }

    public static List<String> toEnSentence(String doc){
        List<String> sentences = new ArrayList<>();

        Properties properties = new Properties();
        // 配置处理器
        properties.setProperty("annotators", "tokenize,ssplit");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(properties);
        // 创建文档
        CoreDocument coreDocument = new CoreDocument(doc);
        // 使用处理器处理文档
        pipeline.annotate(coreDocument);

        for (CoreSentence coreSentence : coreDocument.sentences()) {
            sentences.add(coreSentence.toString());
        }

        return sentences;
    }
}
