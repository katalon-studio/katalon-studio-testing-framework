package com.kms.katalon.core.webui.common;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ExecuteCommandAction {
	private String command;
	private Process process;
	private List<String> outputs = new ArrayList<String>();
	private List<String> errors = new ArrayList<String>();

	public ExecuteCommandAction(String command) {
		this.command = command;
	}

	public Result execute() {
		// command = "cmd.exe /s /c " + command;
		buildProcess(command);
		Result result = new Result();
		if (errors.size() > 0) {
			result.setMessage(getErrorMessage());
		}
		result.setReturnValue(outputs);
		return result;
	}

	private String getErrorMessage() {
		String error = "";
		for (String err : errors) {
			error += err + " ";
		}
		return error;
	}

	private void buildProcess(String command) {
		try {
			String workingDirectory = this.getWorkingDirectory();
			if (workingDirectory != null) {
				process = Runtime.getRuntime().exec(command, null,
						new File(workingDirectory));
			} else {
				process = Runtime.getRuntime().exec(command);
			}

			// any error message?
			StreamGobbler errorGobbler = new StreamGobbler(
					process.getErrorStream(), "ERROR");
			// any output?
			StreamGobbler outputGobbler = new StreamGobbler(
					process.getInputStream(), "OUTPUT");

			// kick them off
			errorGobbler.start();
			outputGobbler.start();

			System.out.println("START : " + command);
			process.waitFor();

		} catch (Exception e) {
			errors.add(e.getMessage());
		}
	}

	private String getWorkingDirectory() {
		return null;
	}

	private class StreamGobbler extends Thread {
		InputStream in;
		String type;

		public StreamGobbler(InputStream in, String type) {
			super();
			this.in = in;
			this.type = type;
		}

		public void run() {
			try {
				InputStreamReader isr = new InputStreamReader(in);
				BufferedReader br = new BufferedReader(isr);
				String line = null;

				while ((line = br.readLine()) != null) {
					if ("output".equalsIgnoreCase(type)) {
						outputs.add(line);
					} else if ("error".equalsIgnoreCase(type)) {
						errors.add(line);
					} else {
						System.out.println(type + ">" + line);
					}

				}
			} catch (Exception e) {
				{
					e.printStackTrace();
				}
			}
		}
	}
}
