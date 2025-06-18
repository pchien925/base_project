package com.phamchien.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

@Component
public class JwtKeyProvider {
    private final KeyPair keyPair;

    public JwtKeyProvider(@Value("${jwt.rsa.private-key}") Resource privateKeyResource) throws Exception {
        this.keyPair = createKeyPair(privateKeyResource);
    }

    private KeyPair createKeyPair(Resource privateKeyResource) throws Exception {
        byte[] privateKeyBytes = privateKeyResource.getInputStream().readAllBytes();
        String privateKeyPEM = new String(privateKeyBytes)
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PRIVATE KEY-----", "");
        byte[] decodedPrivateKey = Base64.getDecoder().decode(privateKeyPEM);
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(decodedPrivateKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        RSAPrivateCrtKey rsaPrivateKey = (RSAPrivateCrtKey) privateKey;
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(
                rsaPrivateKey.getModulus(),
                rsaPrivateKey.getPublicExponent()
        );
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        return new KeyPair(publicKey, privateKey);
    }

    public KeyPair getKeyPair() {
        return keyPair;
    }
}
