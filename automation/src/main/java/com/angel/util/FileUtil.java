package com.angel.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class FileUtil {

	private final static FileUtil instance = new FileUtil();

	public static FileUtil getInstance() {
		/*
		 * if (instance==null) { instance = new FileUtil(); }
		 */

		return instance;
	}

	public String readTxt(String filePath) throws Exception {
		String result = null;

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filePath));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append("\r\n");
				line = br.readLine();
			}
			result = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				br.close();
			}
		}

		return result;
	}

	public void writeTxt(String filePath, String content) throws Exception {
		FileWriter fWriter = null;
		BufferedWriter writer = null;
		try {
			fWriter = new FileWriter(filePath);
			writer = new BufferedWriter(fWriter);
			writer.write(content);
			writer.close(); // make sure you close the writer object
		} catch (Exception e) {
			throw e;
		}
	}

	public boolean exist(String fullName) {
		File file=new File(fullName);
		if (file.exists()) {
			return true;
		}
		return false;
	}
}
