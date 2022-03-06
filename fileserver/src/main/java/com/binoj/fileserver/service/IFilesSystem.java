package com.binoj.fileserver.service;

import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.NoSuchFileException;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface IFilesSystem {

	void copy(MultipartFile multipartFile, String txtPath, StandardCopyOption options)
			throws UnsupportedOperationException, IOException;

	void delete(String path) throws NoSuchFileException, DirectoryNotEmptyException, IOException;

	List<String> listFiles(String txtPath) throws IOException;
}
