package com.ishaia.security;

public interface CryptoStrategy {
    String encrypt(String body) throws Exception;

    String decrypt(String data) throws Exception;

}
