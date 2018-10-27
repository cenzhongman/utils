package com.cenzhongman.util.nlp;

import com.hankcs.hanlp.utility.SentencesUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author 岑忠满
 * @date 2018/10/26 11:57
 */
class SentenceUtilTest {

    @Test
    void toCnSentence() {
        for (String sentence:SentenceUtil.toCnSentence("我爱我家，我的国家？阿瑟东")){
            System.out.println(sentence);
            System.out.println("**********");
        };
    }

    @Test
    void toEnSentence() {
        for (String sentence:SentenceUtil.toEnSentence("President Donald Trump on Tuesday admitted he had “no proof” that “unknown Middle Easterners” were attempting to enter the United States as part of a migrant caravan moving towards the southern border, but alluded that such evidence was immaterial because “they very well could be.”\n" +
                "“I have very good information,” the president told reporters in the Oval Office after he was asked about a growing migrant caravan of more than 7,000 people moving toward the border with Mexico. Trump has slammed the movement in recent days and threatened to militarize the region amid his own baseless claims that “criminals” were among the group.\n" +
                "When pressed repeatedly by CNN’s Jim Acosta on Tuesday if he had any proof that people from the Middle East had joined the caravan, Trump later admitted: “There’s no proof of anything. There’s no proof of anything, but there very well could be.”")){
            System.out.println(sentence);
            System.out.println("**********");
        };
    }
}