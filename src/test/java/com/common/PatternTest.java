package com.common;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * description: com.common
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/3/20
 * version: 1.0
 */
public class PatternTest {

    
    @Test
    public void testPattern() {
        String[] inputArray = {"03b85d1", "9fbca90", "2.0.2"};
        Pattern pattern = Pattern.compile("(^[a-zA-Z\\d]{3,})");
        for (String input : inputArray) {
            Matcher matcher = pattern.matcher(input);
            if (matcher.find()) {
                System.out.println(matcher.group(1));
            }
        }

    }
}
