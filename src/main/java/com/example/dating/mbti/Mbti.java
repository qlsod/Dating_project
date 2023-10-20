package com.example.dating.mbti;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Map.*;

public class Mbti {

    static Map<String, List<String>> map = ofEntries(
            entry("INFP", Arrays.asList("INFP", "ENFP", "INFJ", "ENFJ", "INTJ", "ENTJ", "INTP", "ENTP")),
            entry("ENFP", Arrays.asList("INFP", "ENFP", "INFJ", "ENFJ", "INTJ", "ENTJ", "INTP", "ENTP")),
            entry("INFJ", Arrays.asList("INFP", "ENFP", "INFJ", "ENFJ", "INTJ", "ENTJ", "INTP", "ENTP")),
            entry("ENFJ", Arrays.asList("INFP", "ENFP", "INFJ", "ENFJ", "INTJ", "ENTJ", "INTP", "ENTP", "ISFP")),
            entry("INTJ", Arrays.asList("INFP", "ENFP", "INFJ", "ENFJ", "INTJ", "ENTJ", "INTP", "ENTP")),
            entry("ENTJ", Arrays.asList("INFP", "ENFP", "INFJ", "ENFJ", "INTJ", "ENTJ", "INTP", "ENTP")),
            entry("INTP", Arrays.asList("INFP", "ENFP", "INFJ", "ENFJ", "INTJ", "ENTJ", "INTP", "ENTP", "ESTJ")),
            entry("ENTP", Arrays.asList("INFP", "ENFP", "INFJ", "ENFJ", "INTJ", "ENTJ", "INTP", "ENTP")),
            entry("ISFP", Arrays.asList("ENFJ", "ESFJ", "ESTJ")),
            entry("ESFP", Arrays.asList("ISFJ", "ISTJ")),
            entry("ISTP", Arrays.asList("ESFJ", "ESTJ")),
            entry("ESTP", Arrays.asList("ISFJ", "ISTJ")),
            entry("ISFJ", Arrays.asList("ESFP", "ESTP", "ESFJ", "ESTJ", "ISFJ", "ISTJ")),
            entry("ESFJ", Arrays.asList("ISFP", "ISTP", "ESFJ", "ESTJ", "ISFJ", "ISTJ")),
            entry("ISTJ", Arrays.asList("ESFP", "ESTP", "ESFJ", "ESTJ", "ISFJ", "ISTJ")),
            entry("ESTJ", Arrays.asList("INTP", "ISFP", "ISTP", "ESFJ", "ESTJ", "ISFJ", "ISTJ"))
    );

    public static List<String> getGoodPartner(String mbti) {
        return map.get(mbti);
    }
}
