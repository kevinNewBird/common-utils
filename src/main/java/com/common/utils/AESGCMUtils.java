package com.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

/**
 * description: com.common.utils
 * company: 北京海量数据有限公司
 * create by: zhaosong 2023/9/22
 * version: 1.0
 */
public final class AESGCMUtils {

    // https://blog.csdn.net/u012169821/article/details/121995033
    //java没有按照常规方式转为base64是因为编码发现 上述base64后，java和js不一致，js的base64相当于java这么处理之后:
    //       Base64.getEncoder().encodeToString(new String(encryptData).getBytes());

    // 前后端方案：https://www.cnblogs.com/shenjp/p/16423487.html

    // https://juejin.cn/post/6844903540440186888


    private static final Logger logger = LoggerFactory.getLogger(AESGCMUtils.class);

    private static final String KEY_ALGORITHM = "AES";

    private static final String CIPHER_ALGORITHM = "AES/GCM/PKCS5Padding";

    private static final int AES_KEY_SIZE = 256;

    private static final int GCM_IV_LENGTH = 12;

    private static final int GCM_TAG_LENGTH = 16;


    private AESGCMUtils() {
    }

    private static byte[] generateSecret() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
        keyGenerator.init(AES_KEY_SIZE);

        SecretKey key = keyGenerator.generateKey();
        return key.getEncoded();
    }

    public static String encrypt(String content) throws Exception {
        return Base64.getEncoder().encodeToString(aesGCMEncrypt(content));
    }

    public static String decrypt(String content) throws Exception {
        return new String(aesGCMDecrypt(content));
    }

    private static byte[] aesGCMEncrypt(String content) throws Exception {
        try {
            byte[] secretKey = generateSecret();
            byte[] iv = new byte[GCM_IV_LENGTH];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);

            SecretKeySpec keySpec = new SecretKeySpec(secretKey, KEY_ALGORITHM);
            GCMParameterSpec ivSpec = new GCMParameterSpec(128, iv);

            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] cipherText = cipher.doFinal(content.getBytes());
            // 加密串组成：iv+private+secret
            ByteBuffer byteBUffer = ByteBuffer.allocate(iv.length + cipherText.length + secretKey.length);
            byteBUffer.put(iv);
            byteBUffer.put(secretKey);
            byteBUffer.put(cipherText);
            return byteBUffer.array();
        } catch (Exception ex) {
            logger.error("加密发生错误！", ex);
            throw ex;
        }
    }


    private static byte[] aesGCMDecrypt(String content) throws Exception {
        try {
            byte[] encrypted = Base64.getDecoder().decode(content);
            byte[] iv = Arrays.copyOfRange(encrypted, 0, GCM_IV_LENGTH);
            byte[] secretKey = Arrays.copyOfRange(encrypted, GCM_IV_LENGTH, 44);
            SecretKeySpec keySpec = new SecretKeySpec(secretKey, KEY_ALGORITHM);
            GCMParameterSpec ivSpec = new GCMParameterSpec(128, iv);

            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            return cipher.doFinal(Arrays.copyOfRange(encrypted, 44, encrypted.length));
        } catch (Exception ex) {
            logger.error("解密发生错误！", ex);
            throw ex;
        }
    }

    public static void main(String[] args) throws Exception {
        String encrypted = encrypt("Vbase@1234");
        System.out.println(encrypted);
        System.out.println(decrypt(encrypted));
    }
}
