package com.springboot.project.controller;

import com.springboot.project.model.DecryptionDataRequest;
import com.springboot.project.model.EncryptionDataRequest;
import com.springboot.project.service.AsymmetricEncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AsymmetricEncryptionController {

    private final AsymmetricEncryptionService asymmetricEncryptionService;

    @Autowired
    public AsymmetricEncryptionController(AsymmetricEncryptionService asymmetricEncryptionService) {
        this.asymmetricEncryptionService = asymmetricEncryptionService;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/v1/asymmetric-encryption/actions/encrypt")
    public ResponseEntity<String> encryptDataWithPrivateKey(@RequestBody EncryptionDataRequest encryptionDataRequest) {
        return new ResponseEntity<>(this.asymmetricEncryptionService
                .encryptDataWithPrivateKey(encryptionDataRequest.getData()), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/v1/asymmetric-encryption/actions/decrypt")
    public ResponseEntity<String> encryptDataWithPrivateKey(@RequestBody DecryptionDataRequest decryptionDataRequest) {
        return new ResponseEntity<>(this.asymmetricEncryptionService
                .decryptDataWithPublicKey(decryptionDataRequest.getEncryptionData()), HttpStatus.OK);
    }

}
