package com.coggiri.main.configuration;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.jasypt.salt.RandomSaltGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableEncryptableProperties
public class JasyptConfig {
    @Value("${jasypt.encryptor.password}")
    private String password;


    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor(){
        log.info("===========================================");
        log.info("Jasypt Configuration is initialized");
        log.info("Jasypt Password: {}", password);
        log.info("===========================================");
        PooledPBEStringEncryptor pooledPBEStringEncryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig simpleConfig = new SimpleStringPBEConfig();
        simpleConfig.setPassword(password);
        simpleConfig.setAlgorithm("PBEWITHMD5ANDDES");
        simpleConfig.setKeyObtentionIterations(1000);
        simpleConfig.setPoolSize("1");
        simpleConfig.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        simpleConfig.setStringOutputType("base64");
        pooledPBEStringEncryptor.setConfig(simpleConfig);
        return pooledPBEStringEncryptor;
    }
}