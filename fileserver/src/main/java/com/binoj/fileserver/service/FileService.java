package com.binoj.fileserver.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.NoSuchFileException;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.binoj.fileserver.util.ExcludeFromJacocoGeneratedReport;
import com.binoj.fileserver.util.FileServerProperties;

@Service
public class FileService {

	private static final Logger logger = LoggerFactory.getLogger(FileService.class);

	@Autowired
	FileServerProperties properties;

	@Autowired
	IFilesSystem fileSystem;

	public void saveFile(MultipartFile multipartFile) {

		String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));

		String filePath = properties.getFolder() + File.separator + fileName;
		logger.debug(filePath);

		try {
			fileSystem.copy(multipartFile, filePath, StandardCopyOption.REPLACE_EXISTING);
		}

		catch (UnsupportedOperationException | IOException e) {

			logger.warn(e.getMessage());

			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}

	}

	public List<String> getFiles() {
		String filePath = properties.getFolder() + File.separator;
		List<String> files;
		try {
			files = fileSystem.listFiles(filePath);
		} catch (IOException e) {
			logger.warn(e.getMessage());
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		if (files.isEmpty()) {

			logger.warn("No files found");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		}
		logger.debug(files.toString());
		return files;
	}

	public void delete(String fileName) {
		String filePath = properties.getFolder() + File.separator + fileName;
		try {
			logger.debug(filePath);
			fileSystem.delete(filePath);
		} catch (NoSuchFileException e) {
			logger.warn(e.getMessage());
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (DirectoryNotEmptyException e) {
			logger.warn(e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (IOException e) {
			logger.warn(e.getMessage());
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@ExcludeFromJacocoGeneratedReport
	@ExceptionHandler(NullPointerException.class)
	public String nullPointerException(Exception e) {
		throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
	}

}
