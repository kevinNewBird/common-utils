package com.common.introspector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * description: com.common.introspector
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/3/19
 * version: 1.0
 */
public class OrderlyListTests {

    private static final List<Address> addressList = new ArrayList<>();

    static {
        Address cd = new Address("2.0.2", "01", "sichuan", "chengdu");
        Address gy = new Address("2.0.2", "02", "sichuan", "guangyuan");
        Address ya = new Address("3.0.2", "01", "sichuan", "yaan");
        Address ya2 = new Address("3.0.2", "02", "sichuan", "yaan2");
        Collections.addAll(addressList, gy, cd, ya, ya2);
    }

    public static void main(String[] args) {
        Address address = addressList.stream().max(Comparator.comparing(Address::getCommitId).thenComparing(Address::getBuildNum)).orElse(null);
        System.out.println("最大版本的地址：" + address);


        System.out.println("a".compareTo("b"));
        System.out.println("排序前：" + addressList);
        // 倒序
        addressList.sort(Comparator.comparing(Address::getCity).reversed());
        // 正序
//        addressList.sort(Comparator.comparing(Address::getCity));
        System.out.println("排序后：" + addressList);
    }
}
