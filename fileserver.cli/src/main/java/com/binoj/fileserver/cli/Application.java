package com.binoj.fileserver.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.binoj.fileserver.cli.commands.FileServerClientCommand;

import picocli.CommandLine;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.IFactory;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.ParseResult;

@SpringBootApplication
public class Application implements CommandLineRunner, ExitCodeGenerator {

	static Logger LOG = LoggerFactory.getLogger(Application.class);

	private int exitCode;

	private final FileServerClientCommand fileServerCLICommand;

	private final IFactory factory;

	public Application(FileServerClientCommand fileServerCLICommand, IFactory factory) {
		this.fileServerCLICommand = fileServerCLICommand;
		this.factory = factory;
	}

	public static void main(String[] args) {
		System.exit(SpringApplication.exit(SpringApplication.run(Application.class, args)));
	}

	@Override
	public void run(String... args) {
		CommandLine commandLine = new CommandLine(fileServerCLICommand, factory);

		try {
			ParseResult parsed = commandLine.parseArgs(args);
			if (parsed.subcommand() == null && !parsed.isUsageHelpRequested() && !parsed.isVersionHelpRequested()) {
				System.err.println("Error: at least 1 command required. Refer details usage");
				commandLine.usage(System.out);
				exitCode = ExitCode.USAGE;
				return;
			}
		} catch (ParameterException e) {
			LOG.warn(e.getMessage());
		}

		exitCode = commandLine.setExitCodeExceptionMapper(fileServerCLICommand).execute(args);
		System.out.print(exitCode);
	}

	@Override
	public int getExitCode() {
		return exitCode;
	}

}