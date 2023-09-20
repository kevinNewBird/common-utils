package com.common.utils;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.common.exceptions.PasswordEncryptException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * description: com.common.utils
 * company: 北京海量数据有限公司
 * create by: zhaosong 2023/9/20
 * version: 1.0
 */
public class AESUtils {

    private static final Logger logger = LoggerFactory.getLogger(AESUtils.class);

    private AESUtils() {
    }

    private static final String PRI_KEY = "Va[st71I.YB4]OH<";

    private static final String ENCRYPT_ALGORITHM = "AES/CBC/NoPadding";

    private static final Random RANDOM = new Random();


    private static byte[] generateIV() {
        StringBuilder ret = new StringBuilder();
        IntStream.range(0, 16).forEach(index -> {
            // 生成随机ASCII字符
            char ch = (char) (RANDOM.nextInt(93) + 33);
            ret.append(ch);
        });
        logger.info(ret.toString());
        return ret.toString().getBytes();
    }

    public static String encrypt(String content) {
        byte[] ivBytes = generateIV();
        JSONObject ret = new JSONObject();
        ret.put("iv", base64ToString(ivBytes));
        String encrypted = base64ToString(aesCBCEncrypt(content.getBytes(), ivBytes, PRI_KEY.getBytes()));
        ret.put("data", base64ToString(encrypted.getBytes()));
        return base64ToString(JSON.toJSONBytes(ret));
    }

    public static String decrypt(String content) {
        byte[] base64 = stringToBase64(content);
        String base64Str = new String(base64);
        JSONObject jsonObj = JSON.parseObject(base64Str);
        byte[] ivBytes = stringToBase64(jsonObj.getString("iv"));
        byte[] encryptedBytes = stringToBase64(jsonObj.getString("data"));

        byte[] decryptedPwd = aesCBCDecrypt(stringToBase64(new String(encryptedBytes)), ivBytes, PRI_KEY.getBytes());
        return new String(decryptedPwd).trim().replace("\"", "");
    }

    private static byte[] aesCBCEncrypt(byte[] content, byte[] ivBytes, byte[] keyBytes) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

            Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORITHM);
            int blockSize = cipher.getBlockSize();

            int plaintextLength = content.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(content, 0, plaintext, 0, content.length);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            return cipher.doFinal(plaintext);
        } catch (Exception ex) {
            throw new PasswordEncryptException("AES加密发生错误！", ex);
        }
    }

    private static byte[] aesCBCDecrypt(byte[] content, byte[] ivBytes, byte[] keyBytes) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

            Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            return cipher.doFinal(content);
        } catch (Exception ex) {
            throw new PasswordEncryptException("AES解密发生错误！", ex);
        }

    }

    private static byte[] stringToBase64(String src) {
        return Base64.getDecoder().decode(src.getBytes());
    }

    private static String base64ToString(byte[] src) {
        return Base64.getEncoder().encodeToString(src);
    }

    public static void main(String[] args) {
        String encrypt = encrypt("Vbase@1234");
        System.out.println("加密：" + encrypt);
        System.out.println("解密：" + decrypt(encrypt));

        AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, "1234567812345678".getBytes(), "1234567812345678".getBytes());
        String encryptHutool = aes.encryptBase64("helloworld");
        System.out.println("hutool加密：" + encryptHutool);
        System.out.println("hutool解密：" + aes.decryptStr(encryptHutool));

    }

}
