package com.springboot.project.service;

import com.springboot.project.config.ApplicationProperty;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class AsymmetricEncryptionService {

    private static final String RSA_ALGORITHM = "RSA";

    private final ApplicationProperty applicationProperty;

    @Autowired
    public AsymmetricEncryptionService(ApplicationProperty applicationProperty) {
        this.applicationProperty = applicationProperty;
    }

    public String encryptDataWithPrivateKey(String data) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            byte[] pkcs8EncodedBytes = Base64
                    .getDecoder()
                    .decode(this.applicationProperty.getPrivateKey().getBytes(StandardCharsets.UTF_8));
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8EncodedBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] cipherBytes = cipher.doFinal(data.getBytes());

            return Base64
                    .getEncoder()
                    .encodeToString(cipherBytes);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public String decryptDataWithPublicKey(String encryptedData) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            byte[] pkcs8EncodedBytes = Base64
                    .getDecoder()
                    .decode(this.applicationProperty.getPublicKey());
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pkcs8EncodedBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            byte[] cipherBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData.getBytes()));

            return new String(cipherBytes);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
