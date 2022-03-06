package com.binoj.fileserver.controller;

import com.binoj.fileserver.service.FileService;
import com.binoj.fileserver.util.FileServerProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({FileController.class, FileServerProperties.class})
class FileControllerTest {
	@MockBean
	FileService fileService;
	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper objectMapper;

	MockMultipartFile mockMultipartFile;

	@BeforeEach
	void setup() {
		mockMultipartFile = new MockMultipartFile("fileThatDoesNotExists", "fileThatDoesNotExists.txt", "text/plain",
				"This is a dummy file content".getBytes(StandardCharsets.UTF_8));
	}

	@Test
	void testUploadAPIWorks() throws Exception {
		fileService.saveFile(mockMultipartFile);

		mockMvc.perform(multipart("/files").file("file", mockMultipartFile.getBytes())
				.contentType(MediaType.MULTIPART_FORM_DATA))

				.andExpect(status().isCreated());

		then(fileService).should().saveFile(mockMultipartFile);
	}

	@Test
	void testGetFileNameAPIWorks() throws Exception {
		mockMvc.perform(get("/files").contentType(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isOk());

		then(fileService).should().getFiles();
	}

	@Test
	void testDeleteAPIWorks() throws Exception {
		mockMvc.perform(delete("/files/{fileName}","fileName", "fileThatDoesNotExists.txt"))
				.andExpect(status().isNoContent());
	}
}