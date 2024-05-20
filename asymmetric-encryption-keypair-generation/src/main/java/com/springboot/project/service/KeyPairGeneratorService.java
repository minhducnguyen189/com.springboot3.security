package com.springboot.project.service;

import com.springboot.project.config.ApplicationProperty;
import com.springboot.project.model.KeyPairResponse;
import org.apache.tomcat.util.buf.HexUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Service
public class KeyPairGeneratorService {

    private static final String RSA_ALGORITHM = "RSA";

    private final ApplicationProperty applicationProperty;

    @Autowired
    public KeyPairGeneratorService(ApplicationProperty applicationProperty) {
        this.applicationProperty = applicationProperty;
    }

    public KeyPairResponse generateKeyPair() {
        SecureRandom secureRandom = new SecureRandom();
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
            keyPairGenerator.initialize(this.applicationProperty.getKeySize(), secureRandom);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            KeyPairResponse keyPairResponse = new KeyPairResponse();

            keyPairResponse.setPrivateKey(Base64.encodeBase64String(keyPair.getPrivate().getEncoded()));
            keyPairResponse.setPublicKey(Base64.encodeBase64String((keyPair.getPublic().getEncoded())));

            return keyPairResponse;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
