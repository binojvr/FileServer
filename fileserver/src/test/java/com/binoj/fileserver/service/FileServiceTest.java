package com.binoj.fileserver.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.binoj.fileserver.model.FileInfo;
import com.binoj.fileserver.util.FileServerProperties;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

	@InjectMocks
	FileService fileservice;

	MultipartFile multipartFile;
	FileInfo fileInfo;

	@Mock
	IFilesSystem fileSystem;

	@Mock
	FileServerProperties properties;

	@BeforeEach
	void setup() throws IOException {
		multipartFile = new MockMultipartFile("file1.txt", "file2.txt", "text/plain",
				"This is a test file".getBytes(StandardCharsets.UTF_8));
		fileInfo = new FileInfo(multipartFile.getName(), null);
		when(properties.getFolder()).thenReturn("TestFolder");
	}

	@Test
	void testFileSave() throws IOException {

		doNothing().when(fileSystem).copy(any(), anyString(), eq(StandardCopyOption.REPLACE_EXISTING));
		fileservice.saveFile(multipartFile);
		verify(fileSystem, times(1)).copy(any(), anyString(), eq(StandardCopyOption.REPLACE_EXISTING));
	}

	@Test
	void testFileSaveThrowsException() throws IOException {
		doThrow(new IOException("Exception occured")).when(fileSystem).copy(any(), anyString(),
				eq(StandardCopyOption.REPLACE_EXISTING));
		Assertions.assertThrows(ResponseStatusException.class, () -> fileservice.saveFile(multipartFile));
	}

	@Test
	void testGetFiles() throws IOException {
		List<String> files = new ArrayList<>();
		files.add("file1.txt");
		files.add("file2.txt");
		when(fileSystem.listFiles(anyString())).thenReturn(files);
		Assertions.assertFalse(fileservice.getFiles().isEmpty());

	}

	@Test
	void testGetFilesEmpty() throws IOException {
		List<String> files = new ArrayList<>();
		when(fileSystem.listFiles(anyString())).thenReturn(files);
		Assertions.assertThrows(ResponseStatusException.class, () -> fileservice.getFiles());

	}

	@Test
	void testGetFilesFileException() throws IOException {
		doThrow(new IOException("Exception occured")).when(fileSystem).listFiles(anyString());
		Assertions.assertThrows(ResponseStatusException.class, () -> fileservice.getFiles());

	}

	@Test
	void testFileDelete() throws IOException {

		doNothing().when(fileSystem).delete(anyString());
		fileservice.delete("filename");
		verify(fileSystem, times(1)).delete(anyString());
	}

	@Test
	void testFileDeleteThrowsException() throws IOException {
		doThrow(new IOException("Exception occured")).when(fileSystem).delete(anyString());
		Assertions.assertThrows(ResponseStatusException.class, () -> fileservice.delete("FileNameNotExisting"));
	}

}
