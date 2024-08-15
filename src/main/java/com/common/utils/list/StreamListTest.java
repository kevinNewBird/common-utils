package com.common.utils.list;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.IntStream;

/**
 * description: com.common.utils.list
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/3/20
 * version: 1.0
 */
public class StreamListTest {

    private static List<User> userList = new ArrayList<>(8);

    static {
        User bob = new User(2, "bob", "chengdu");
        User michale = new User(1, "michale", "guangyuan");
        User alice = new User(3, "alice", "chengdu");
        User tom = new User(6, "tom", "guangzhou");
        User tommy = new User(4, "tommy", "taiwan");
        User jack = new User(5, "jack", "shenzhen");
        userList = Arrays.asList(bob, michale, alice, tom, tommy, jack);
    }

    /**
     * 通过stream流的reduce进行分组
     * description:
     * create by: zhaosong 2024/3/20 9:23
     */
    private static void groupByReduce() {
        /**
         * 参数1： 初始化一个map
         * 参数2： 对初始化map按规则进行分组，本例为基于地址分组
         * 参数3： 归并处理。仅在并行时将结果进行累加处理，当前的样例中是没有生效的。
         */
        Map<String, List<User>> groupMap = userList.stream().reduce(new HashMap<String, List<User>>(), (stringMapList, user) -> {
            List<User> groupList = stringMapList.getOrDefault(user.getAddress(), new ArrayList<>());
            groupList.add(user);
            stringMapList.put(user.getAddress(), groupList);
            return stringMapList;
        }, (stringMapList, stringMapList2) -> {
//            stringMapList.putAll(stringMapList2);// 用于处理并行时的数据归并，当前是不生效的，可以通过注释掉该行代码来验证
            return stringMapList;
        });
        int expected = 5; // 期待分组后的组大小，应为5
        if (groupMap.size() != expected) {
            System.out.println(MessageFormat.format("串行处理：\n大小：{0}， 输出结果：\n{1}", groupMap.size(), groupMap));
        } else {
            System.out.println("串行处理成功！");
        }
    }

    /**
     * 通过stream流的reduce进行分组(并行)
     * description:
     * create by: zhaosong 2024/3/20 9:23
     */
    public static void parallelGroupByReduce() {
        /**
         * 参数1： 初始化一个map
         * 参数2： 对初始化map按规则进行分组，本例为基于地址分组
         * 参数3： 归并处理。仅在并行时将结果进行累加处理，当前的样例中是没有生效的。
         */
        /**
         * 注意事项：使用stream的reduce并发处理集合时，应尽量避免修改集合
         * 解决方案：
         * 1.更改为顺序流：  list.stream().sequential().reduce()
         * 2.加锁(降低性能)：List<Object> synList = </>Collections.synchronizedList(list); synList.parallelStream().reduce();
         */

        // 以下方式会报错
        /*Map<String, List<User>> groupMap = userList.stream().parallel().reduce(new HashMap<String, List<User>>(), (stringMapList, user) -> {
            List<User> groupList = stringMapList.getOrDefault(user.getAddress(), new ArrayList<>());
            groupList.add(user);
            stringMapList.put(user.getAddress(), groupList);
            return stringMapList;
        }, (stringMapList, stringMapList2) -> {
            stringMapList.putAll(stringMapList2);// 用于处理并行时的数据归并，当前生效，可以通过注释掉该行代码来验证
            return stringMapList;
        });*/

        // TIP: 使用顺序流，和groupByReduce是一样的
        Map<String, List<User>> groupMap = userList.stream().sequential().reduce(new HashMap<String, List<User>>(), (stringMapList, user) -> {
            List<User> groupList = stringMapList.getOrDefault(user.getAddress(), new ArrayList<>());
            groupList.add(user);
            stringMapList.put(user.getAddress(), groupList);
            return stringMapList;
        }, (stringMapList, stringMapList2) -> {
            stringMapList.putAll(stringMapList2);// 用于处理并行时的数据归并，当前生效，可以通过注释掉该行代码来验证
            return stringMapList;
        });

        int expected = 5; // 期待分组后的组大小，应为5
        if (groupMap.size() != expected) {
            System.err.println(MessageFormat.format("并行处理:\n大小：{0}， 输出结果：\n{1}", groupMap.size(), groupMap));
        } else {
            System.out.println("并行处理成功！");
        }

    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    private static class User {

        private Integer id;
        private String name;

        private String address;
    }

    public static void main(String[] args) {
        IntStream.range(0, 50).forEach(index -> {
            groupByReduce();
            parallelGroupByReduce();
            System.out.println(MessageFormat.format("--------------------------------{0}----------------------------------", index + 1));
        });
    }
}
