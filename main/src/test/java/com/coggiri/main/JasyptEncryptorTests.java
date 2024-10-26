package com.coggiri.main;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.event.annotation.BeforeTestClass;

public class JasyptEncryptorTests {
    private static String stringKey = "test";
    private static String username = "test";
    private static String password = "test";
    private static String url = "test";

    private static StandardPBEStringEncryptor pbeEnc;

    public StandardPBEStringEncryptor setEncryptor(){
        StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
        pbeEnc.setAlgorithm("PBEWITHMD5ANDDES");
        pbeEnc.setPassword(stringKey);
        return pbeEnc;
    }

    @Test
    void stringEncrytor(){
        pbeEnc = setEncryptor();
        System.out.println("key= " + jasyptEncoding(stringKey));
        System.out.println("username= " + jasyptEncoding(username));
        System.out.println("password= " + jasyptEncoding(password));
        System.out.println("url= " + jasyptEncoding(url));
    }

    public String jasyptEncoding(String value){
        return pbeEnc.encrypt(value);
    }

    public String jasyptDecoding(String value){
        return pbeEnc.decrypt(value);
    }
}