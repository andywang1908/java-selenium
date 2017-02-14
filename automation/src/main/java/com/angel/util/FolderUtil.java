package com.angel.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class FolderUtil {
	private String rootPath = null;

	private final static FolderUtil instance = new FolderUtil();
	public static FolderUtil getInstance() {
		return instance;
	}
	
	public String calRootPath() {
		if ( rootPath==null ) {
            // C:/cut/quality/real/realws
            // /Users/andy/Downloads/bug
		    // C:/green/dev/PortableGit/andy/java-selenium
			rootPath = "C:/green/dev/PortableGit/andy/java-selenium";
		}
		
		return rootPath;
	}
	
	/*
	public List<String> listFolder(String path, String suffix) {
		List<String> result = new ArrayList<String>();

		File curDir = new File(path);

		File[] filesList = curDir.listFiles();
		for (File f : filesList) {
			// if(f.isDirectory())
			if (f.isFile()) {
				if (suffix == null || f.getName().toUpperCase().endsWith(suffix.toUpperCase())) {
					// System.out.println(f.getName());
					result.add(path + f.getName());
				}
			}
		}

		return result;
	}*/

	public boolean cleanBackUpFolder(String path, String backUpPath, String suffix) throws Exception {
		this.backUpFolder(path, backUpPath, suffix);
		this.cleanFolder(path, suffix);

		return false;
	}

	//建立需要的目录
	public boolean prepareDic(String path) {
		String fullName = combineDic(rootPath,path);
		System.out.println("path:"+fullName);
		String[] parts = fullName.split("\\\\");//File.separator

		String dicName = "";
		String part;
		File theDir;
		for ( int i=0;i<parts.length;i++ ) {
			part = parts[i];
			dicName += part+File.separator;
			theDir = new File(dicName);
			if ( !theDir.exists() ) {
				if ( !theDir.mkdir() ) {
					System.out.println("mkdir failed:"+dicName);
				}
			}
		}
		
		return true;
	}


	private boolean cleanFolder(String path, String suffix) {
		File curDir = new File(path);
		if (suffix == null)
			return false;

		File[] filesList = curDir.listFiles();
		for (File f : filesList) {
			// if(f.isDirectory())
			if (f.isFile()) {
				if (f.getName().toUpperCase().endsWith(suffix.toUpperCase())) {
					String fName = f.getName();
					System.out.println(fName);
					f.delete();
				}
			}
		}

		return false;
	}

	private boolean backUpFolder(String path, String backUpPath, String suffix) throws Exception {
		File curDir = new File(path);
		if (suffix == null)
			return false;

		File[] filesList = curDir.listFiles();
		for (File f : filesList) {
			// if(f.isDirectory())
			if (f.isFile()) {
				if (f.getName().toUpperCase().endsWith(suffix.toUpperCase())) {
					String fName = f.getName();

					File source = new File(path + fName);
					File dest = new File(backUpPath + fName + "." + DateUtil.getInstance().getCurrentTimeFile());//

					copyFileUsingFileChannels(source, dest);

					System.out.println(fName);
					// f.delete();
				}
			}
		}

		return false;
	}

	private static void copyFileUsingFileChannels(File source, File dest) throws Exception {
		FileInputStream fileInputStream = null;
		FileOutputStream fileOutputStream = null;
		FileChannel inputChannel = null;
		FileChannel outputChannel = null;
		try {
			fileInputStream = new FileInputStream(source);
			fileOutputStream = new FileOutputStream(dest);
			inputChannel = fileInputStream.getChannel();
			outputChannel = fileOutputStream.getChannel();
			outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (inputChannel != null) {
				inputChannel.close();
			}
			if (outputChannel != null) {
				outputChannel.close();
			}

			if (fileInputStream != null) {
				fileInputStream.close();
			}

			if (fileOutputStream != null) {
				fileOutputStream.close();
			}

		}
	}

	//出去不一定是带\\  but进来有可能也带\\
	public String combineDic(String... paths) {
		String result = "";
		for (String path : paths) {
			if ( result.endsWith(File.separator) || "".equals(result) ) {
				result += path;
			} else {
				result += File.separator+path;
			}
		}
		//System.out.println("dic:"+result);
		return result;
	}
}
