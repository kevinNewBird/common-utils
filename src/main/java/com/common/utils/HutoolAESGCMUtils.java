package com.common.utils;


import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * description: com.common.utils
 * company: 北京海量数据有限公司
 * create by: zhaosong 2023/11/24
 * version: 1.0
 */
public class HutoolAESGCMUtils {
    private static final Logger logger = LoggerFactory.getLogger(HutoolAESGCMUtils.class);

    private static String publicKey;

    private static String privateKey;

    static {
        generateKeyPair();
    }


    private static synchronized void generateKeyPair() {
        try {
            KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
            gen.initialize(512);
            KeyPair keyPair = gen.generateKeyPair();
            PrivateKey priKey = keyPair.getPrivate();
            PublicKey pubKey = keyPair.getPublic();
            privateKey = Base64.encodeBase64String(priKey.getEncoded());
            publicKey = Base64.encodeBase64String(pubKey.getEncoded());
        } catch (Exception ex) {
            logger.error("gen key pair fail!", ex);
        }
    }

    public static String encrypt(String plainText) {
        RSA rsa = new RSA((String) null, publicKey);
        byte[] encrypt = rsa.encrypt(plainText.getBytes(StandardCharsets.UTF_8), KeyType.PublicKey);
        return new String(Base64.encodeBase64(encrypt), StandardCharsets.UTF_8);
    }

    public static String decrypt(String cipherText) {
        RSA rsa = new RSA(privateKey, (String) null);
        byte[] decrypt = rsa.decrypt(Base64.decodeBase64(cipherText), KeyType.PrivateKey);
        return new String(decrypt, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) {
        String encrypt = encrypt("Vbase@1234");
        System.out.println(encrypt);
        System.out.println(decrypt(encrypt));
    }
}
