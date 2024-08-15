package com.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.StringJoiner;
import java.util.regex.Pattern;

/**
 * description: com.common.utils
 * company: 北京海量数据有限公司
 * create by: zhaosong 2023/12/21
 * version: 1.0
 */
public final class NetUtils {

    private static final String NET_MASK_VALIDATOR = "^((128|192)|2(24|4[08]|5[245]))(\\.(0|(128|192)|2((24)|(4[08])|(5[245])))){3}$";

    public static int calcMaskBit(String ipMask) {
        if (StringUtils.isBlank(ipMask) || !Pattern.matches(NET_MASK_VALIDATOR, ipMask)) {
            throw new IllegalArgumentException();
        }

        int[] ipArray = Arrays.stream(ipMask.split("\\."))
                .mapToInt(Integer::parseInt).toArray();

        int maskBit = 0;
        for (int maskSegment : ipArray) {
            while (maskSegment != 0) {
                maskSegment = maskSegment & (maskSegment - 1);
                maskBit++;
            }
        }
        return maskBit;
    }

    public static String calcIpMask(int maskBit) {
        if (maskBit < 0 || maskBit > 32) {
            throw new IllegalArgumentException();
        }

        int ipMaskSegment = 0;
        int pow = 8;
        StringJoiner ipMask = new StringJoiner(".", "", "");
        for (int i = 0; i < maskBit; i++) {
            ipMaskSegment += Math.pow(2, --pow);
            if (pow == 0) {
                ipMask.add(ipMaskSegment + "");
                pow = 8;
                ipMaskSegment = 0;
                continue;
            }
            if (i == maskBit - 1) {
                ipMask.add(ipMaskSegment + "");
            }
        }
        for (int i = ipMask.toString().split("\\.").length; i < 4; i++) {
            ipMask.add(0 + "");
        }
        return ipMask.toString();
    }
}
