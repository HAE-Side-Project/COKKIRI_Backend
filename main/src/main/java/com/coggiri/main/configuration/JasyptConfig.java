package com.coggiri.main.configuration;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
public class JasyptConfig {
    @Value("${encryptor.key")
    private String password;

    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor(){
        PooledPBEStringEncryptor pooledPBEStringEncryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig simpleConfig = new SimpleStringPBEConfig();
        simpleConfig.setPassword(password);
        simpleConfig.setAlgorithm("PBEWITHMD5ANDDES");
        simpleConfig.setKeyObtentionIterations(976);
        simpleConfig.setPoolSize("1");
        simpleConfig.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        simpleConfig.setStringOutputType("base64");
        pooledPBEStringEncryptor.setConfig(simpleConfig);
        return pooledPBEStringEncryptor;
    }
}