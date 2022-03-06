package com.binoj.fileserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import com.binoj.fileserver.controller.FileController;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EnableConfigurationProperties
class FileserverApplicationTest {

	@Autowired
	private FileController fileController;

	@Test
	public void contextLoads() {
		assertThat(fileController).isNotNull();
	}

}
