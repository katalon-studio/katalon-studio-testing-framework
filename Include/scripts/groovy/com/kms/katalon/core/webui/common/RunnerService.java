package com.kms.katalon.core.webui.common;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class RunnerService {
	private static RunnerService runnerService;

	public static RunnerService getInstance() {
		if (runnerService == null) {
			runnerService = new RunnerService();
		}
		return runnerService;
	}

	/**
	 * create new file if not exist
	 * 
	 * @param filePath
	 * @return
	 */
	public File createFileIfNotExist(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			try {
				if (!file.isDirectory()) {
					File fFolder = new File(file.getParent());
					if (!fFolder.exists()) {
						fFolder.mkdirs();
					}
					file.createNewFile();
				} else {
					file.mkdirs();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * create new folder if not exist
	 * 
	 * @param folderPath
	 * @return
	 */
	public File createFolderIfNotExist(String folderPath) {
		File file = new File(folderPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}

	/**
	 * get current time by format
	 * 
	 * @return
	 */
	public String getCurrentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy_hh:mm:ss.SSS a");
		Date date = Calendar.getInstance().getTime();
		return sdf.format(date);
	}

	public static String removeSpecialCharacters(String input) {
		return input.replaceAll("[^a-zA-Z0-9]", "");
	}

	public static HashMap<String, String> parseText(String tagText,
			String delimitor) {
		try {
			String[] token = tagText.split(delimitor);
			HashMap<String, String> cache = new HashMap<String, String>();
			String temp_delims = "[=]";
			for (int i = 0; i < token.length; i++) {
				String[] temp_tokens = token[i].split(temp_delims);
				String key = "";
				String value = "";
				try {
					key = temp_tokens[0];
					value = temp_tokens[1];
				} catch (Exception e) {

				}

				cache.put(key, value);
			}
			return cache;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

}