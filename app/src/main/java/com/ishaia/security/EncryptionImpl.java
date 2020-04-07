package com.ishaia.security;

public class EncryptionImpl implements CryptoStrategy {

    @Override
    public String encrypt(String body) throws Exception {
        return CryptoUtil.encrypt(body);
    }

    @Override
    public String decrypt(String data) {
        return null;
    }
}