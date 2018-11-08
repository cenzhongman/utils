package com.cenzhongman.util.nlp;

import com.hankcs.hanlp.mining.word2vec.DocVectorModel;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;

import java.io.IOException;

/**
 * @author 岑忠满
 * @date 2018/11/5 18:10
 */
public class Word2vecUtil {
    private static DocVectorModel cnDocVectorModel;
    private static DocVectorModel enDocVectorModel;

    static {
        try {
            cnDocVectorModel = new DocVectorModel(new WordVectorModel("/home/czm/data/nlpData/hanlp-wiki-vec-zh.txt"));
            enDocVectorModel = new DocVectorModel(new WordVectorModel("/home/czm/data/nlpData/wiki-news-300d-1M.vec"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static float cnDocSimilarity(String a, String b){
        return cnDocVectorModel.similarity(a, b);
    }

    public static float enDocSimilarity(String a, String b){
        return enDocVectorModel.similarity(a, b);
    }
}
