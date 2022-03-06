package com.binoj.fileserver.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@ConfigurationProperties(prefix="file.server")
@Component
public class FileServerProperties {

	@Override
	public String toString() {
		return "FileServerProperties [folder=" + folder + "]";
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	private String folder;

}
