package com.github.manerajona.auth.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.security.InvalidParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class JwtRandomKeyPair {

    private final KeyPair keyPair;

    /*
     * private constructor restricted to this class itself
     */
    private JwtRandomKeyPair(final String algorithmName, final int KeyLength) {
        try {
            SignatureAlgorithm algorithm = SignatureAlgorithm.forName(algorithmName);
            if (algorithm.getMinKeyLength() > KeyLength) {
                throw new InvalidParameterException("Key Length must be greater than or equal to %d"
                        .formatted(algorithm.getMinKeyLength()));
            }

            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm.getFamilyName());
            keyPairGenerator.initialize(KeyLength);
            this.keyPair = keyPairGenerator.generateKeyPair();
            log.info("JWT KeyPair generated with algorithm {}", keyPair.getPrivate().getAlgorithm());
        } catch (NoSuchAlgorithmException | InvalidParameterException e) {
            log.error("An error occurred generating rsa keys", e);
            throw new RuntimeException(e);
        }
    }

    public static KeyPair of(String algorithm, int KeyLength) {
        JwtRandomKeyPair instance = new JwtRandomKeyPair(algorithm, KeyLength);
        return instance.keyPair;
    }
}
