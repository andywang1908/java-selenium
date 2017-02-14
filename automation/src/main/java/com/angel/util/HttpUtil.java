package com.angel.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
	private final static HttpUtil instance = new HttpUtil();
	public static HttpUtil getInstance() {
		return instance;
	}
	
	public void save(String link) throws Exception {
		//http://ww4.sinaimg.cn/mw690/005BX16ijw1ej5mbmmkb7j30pr15o0zw.jpg
		int idx = link.indexOf("://");
		if ( idx<0 ) {
			throw new Exception("link can not be connected("+link+")");
		}
		
		String pathName = link.substring( idx+"://".length() );

		this.saveWithPathName(link, pathName);
		//just use try manually to skip those resource,otherwise no way to re download it.
		try {
		} catch (Exception e) {
			System.out.println("Error:can not download resouce:"+link+"");
			e.printStackTrace();
		}
	}

	// 是否覆盖
	public void saveWithPathName(String link, String pathName) throws IOException {
		// url =
		// "http://img2.moko.cc/users/3/1123/337162/post/ec/img2_src_9269083.jpg";
		URL url = new URL(link);

		FolderUtil folderUtil = FolderUtil.getInstance();

		// rootdictionary+path+filename
		String fullName = folderUtil.combineDic(folderUtil.calRootPath(), pathName);

		// System.out.println( fullName );
		if (FileUtil.getInstance().exist(fullName))
			return;

		// 打开链接
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 设置请求方式为"GET"
		conn.setRequestMethod("GET");
		// 超时响应时间为5秒
		conn.setConnectTimeout(5 * 1000);
		// 通过输入流获取图片数据
		InputStream inStream = conn.getInputStream();
		// 得到图片的二进制数据，以二进制封装得到数据，具有通用性
		byte[] data = readInputStream(inStream);
		// new一个文件对象用来保存图片，默认保存当前工程根目录
		File imageFile = new File(fullName);
		// 创建输出流
		FileOutputStream outStream = new FileOutputStream(imageFile);
		if (1 == 1) {
			BufferedOutputStream buff = new BufferedOutputStream(outStream);
			buff.write(data);
			buff.flush();
			outStream.close();
			buff.close();
		} else {
			// 写入数据
			outStream.write(data);
			// 关闭输出流
			outStream.close();
		}
	}

	private byte[] readInputStream(InputStream inStream) throws IOException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		// 创建一个Buffer字符串
		byte[] buffer = new byte[1024];
		// 每次读取的字符串长度，如果为-1，代表全部读取完毕
		int len = 0;
		// 使用一个输入流从buffer里把数据读取出来
		while ((len = inStream.read(buffer)) != -1) {
			// 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
			outStream.write(buffer, 0, len);
		}
		// 关闭输入流
		inStream.close();
		// 把outStream里的数据写入内存
		return outStream.toByteArray();
	}
}
