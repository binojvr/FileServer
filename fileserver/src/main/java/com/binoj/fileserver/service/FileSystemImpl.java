package com.binoj.fileserver.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * FileSystemImpl is wrapper on {@link Java.nio.Files} Note: Ignoring unit test
 * on this class
 * 
 * @author binoj
 *
 */

@Component
public class FileSystemImpl implements IFilesSystem {

	@Override
	public void copy(MultipartFile multipartFile, String txtPath, StandardCopyOption options)
			throws UnsupportedOperationException, IOException {

		InputStream is = multipartFile.getInputStream();
		Path path = Paths.get(txtPath);
		Files.copy(is, path, options);

	}

	@Override
	public List<String> listFiles(String txtPath) throws IOException {

		try (Stream<Path> stream = Files.list(Paths.get(txtPath))) {
			return stream.filter(file -> !Files.isDirectory(file)).map(Path::getFileName).map(Path::toString)
					.collect(Collectors.toList());
		}

	}

	@Override
	public void delete(String txtPath) throws NoSuchFileException, DirectoryNotEmptyException, IOException {
		Files.delete(Paths.get(txtPath));

	}

}
