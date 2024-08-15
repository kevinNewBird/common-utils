package com.common.utils.bitmap;

import com.common.utils.AESGCMUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * description: 位运算，等效于redis中的位图
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/3/5
 * version: 1.0
 */
public class BitSetUtils {

    private static final Logger logger = LoggerFactory.getLogger(BitSetUtils.class);

    public static void main(String[] args) {
//        testBitSet();
    }

    private static void testBitSet() {
        BitSet bitmap = new BitSet();

        int pos = Integer.MAX_VALUE;

        // 1.设置第5个位置为1，表示第5个元素存在
        bitmap.set(pos);

        // 2.检查第5个位置是否已设置
        boolean isExists = bitmap.get(pos);
        logger.info("element at position [{}] exists: {}", pos, isExists);
        logger.info("element string: {}", bitmap.toString());

        // 3.设置从索引10到20的所有位置为1
        bitmap.set(10, 21); // 参数是包含起始点不包含终点的区间

        // 4.计算bitmap中所有值为1的位的数量，相当于计算设置了的元素个数
        int count = bitmap.cardinality();
        logger.info("Number of set bits: {}", count);

        // 5.消除第5个位置
        bitmap.clear(pos);
        logger.info("element at position [{}] exists: {}", pos, bitmap.get(pos));

        // 6.判断位图是否为空
        boolean isEmpty = bitmap.isEmpty();
        logger.info("Is the bitmap empty after clearing position [{}] ? ret: {}", pos, isEmpty);


        // 7.清空位图后，判断位图是否为空
        bitmap.clear();
        logger.info("Is the bitmap empty after clearing some bits? ret: {}", bitmap.isEmpty());
    }
}
