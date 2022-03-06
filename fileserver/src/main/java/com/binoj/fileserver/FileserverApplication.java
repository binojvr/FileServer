package com.binoj.fileserver;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.binoj.fileserver.util.FileServerProperties;

@SpringBootApplication
public class FileserverApplication {
	
	@Autowired
	FileServerProperties properties;

	private static final Logger logger = LoggerFactory.getLogger(FileserverApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(FileserverApplication.class, args);
	}
	
	@Bean
	public SmartInitializingSingleton importProcessor() {
	    return () -> {
	    	initialize();
	    };

	}
	
	public void initialize() {
		logger.info(properties.getFolder());
		File folderPath = new File(properties.getFolder());
		if (!Files.exists(Paths.get(properties.getFolder()))) {
			folderPath.mkdir();
		}

	}

}
