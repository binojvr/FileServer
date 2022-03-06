package com.binoj.fileserver.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.binoj.fileserver.service.FileService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Validated
@RestController
@RequestMapping("/files")
public class FileController {

	private static final Logger logger = LoggerFactory.getLogger(FileController.class);
	
	@Autowired
	FileService fileService;

	@Operation(summary = "Upload a file")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "The file has been successfully uploaded", content = @Content),
			@ApiResponse(responseCode = "500", description = "Failed to Upload File", content = @Content) })
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public void uploadFile(@RequestParam("file") MultipartFile file) {
		fileService.saveFile(file);
		logger.debug("The file {} has been successfully uploaded", file.getName());
	}

	@Operation(summary = "Get uploaded file list (Name only) ")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "List of files served", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = List.class)) }),
			@ApiResponse(responseCode = "404", description = "No files found", content = @Content) })
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<String> getFiles() {
		return fileService.getFiles();
		
	}

	@Operation(summary = "Delete a file")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Deleted successfully", content = @Content),
			@ApiResponse(responseCode = "404", description = "File doesnt exist in the directory.", content = @Content),
			@ApiResponse(responseCode = "400", description = "Filename specified is a directory.", content = @Content),
			@ApiResponse(responseCode = "500", description = "Error while deleting file", content = @Content) })

	@DeleteMapping(value = "/{fileName}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteFile(@PathVariable("fileName") String fileName)
			throws IOException {
		fileService.delete(fileName);
		logger.debug("The file {} has been deleted successfully", fileName);
	}

}
