package com.binoj.fileserver.cli.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import picocli.CommandLine.Command;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.IExitCodeExceptionMapper;
import picocli.CommandLine.IVersionProvider;
import picocli.CommandLine.Parameters;

@Component
@Command(name = "fs-store", mixinStandardHelpOptions = true, version = "1.0.0", versionProvider = FileServerClientCommand.class, description = "File Server Client Command - Upload file, Delete File, List files", subcommands = {
		FileServerClientCommand.UploadCommand.class, FileServerClientCommand.DeleteCommand.class,
		FileServerClientCommand.ListFilesCommand.class })
public class FileServerClientCommand implements Callable<Integer>, IExitCodeExceptionMapper, IVersionProvider {
	
	
	//@TODO: to read from application.yaml file. Similar one already done in Server project
	static String serverUrl = "http://localhost:8080/files";

	@Override
	public Integer call() {
		return ExitCode.OK;
	}

	@Override
	public int getExitCode(Throwable exception) {
		Throwable cause = exception.getCause();
		if(cause != null)
			System.out.println(cause.getMessage());
		return 12;
	}

	@Override
	public String[] getVersion() {
		return new String[] { "1.0.0" };
	}

	@Component
	@Command(name = "upload-file", mixinStandardHelpOptions = true, versionProvider = FileServerClientCommand.class, description = "fs-store upload-file <file>. <file> Please provide full path with file name to upload.")
	static class UploadCommand implements Callable<Integer> {

		@Parameters(index = "0",  arity = "1", description = "File to upload.")
		private String fileNameWithPath;

		@Override
		public Integer call() throws IOException {
			
			 File file = new File(fileNameWithPath);

			final RestTemplate restTemplate = new RestTemplate();

			final HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();

			ContentDisposition contentDisposition = ContentDisposition.builder("form-data").name("file")
					.filename(file.getName()).build();
			fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());

			HttpEntity<byte[]> fileEntity = new HttpEntity<>(convertFileIntoBytes(file), fileMap);

			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("file", fileEntity);

			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

			ResponseEntity<String> response = restTemplate.postForEntity(serverUrl, requestEntity, String.class);

			
			if (response.getStatusCode().equals(HttpStatus.CREATED)) {
				System.out.println("File Uploaded Successfully.");
				return ExitCode.OK;
			} else
				return 12;
		}

		private static byte[] convertFileIntoBytes(File file) {
			FileInputStream fileInputStream = null;
			byte[] bFile = new byte[(int) file.length()];
			try {
				fileInputStream = new FileInputStream(file);
				fileInputStream.read(bFile);
				fileInputStream.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
			return bFile;
		}

	}

	@Component
	@Command(name = "delete-file", mixinStandardHelpOptions = true, versionProvider = FileServerClientCommand.class, description = "fs-store delete-file <file>. <file> Please provide the File Name to be deleted")
	static class DeleteCommand implements Callable<Integer> {

		@Parameters(index = "0", arity = "1", description = "Name of the file to be deleted.")
		private String fileName;

		final RestTemplate restTemplate = new RestTemplate();

		@Override
		public Integer call() {

			String deletUrl = serverUrl +"/" +fileName;

			restTemplate.delete(deletUrl);

			return ExitCode.OK;
		}
	}

	@Component
	@Command(name = "list-files", mixinStandardHelpOptions = true, versionProvider = FileServerClientCommand.class, description = " fs-store list-files. List already uploaded files")
	static class ListFilesCommand implements Callable<Integer> {

		final RestTemplate restTemplate = new RestTemplate();

		@Override
		public Integer call() {

			ResponseEntity<List<String>> responseEntity = restTemplate.exchange(serverUrl, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<String>>() {
					});
			if (responseEntity.getBody() != null) {
				List<String> listOfFiles = responseEntity.getBody();
				listOfFiles.forEach(System.out::println);
				return ExitCode.OK;
			} else
				return 12;

		}

	}

}