package com.angel.util;

import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GsonUtil {
	private final static Gson gson = new Gson();
	private final static GsonUtil instance = new GsonUtil();

	public static GsonUtil getInstance() {
		return instance;
	}
	public static Gson getObj() {
		return gson;
	}

	public Map<String, String> parseMapStringString(String taskFile) throws Exception {
		Map<String, String> mapTask;
		Type listType = null;
		
		String json = FileUtil.getInstance().readTxt(taskFile);
		if ( json==null ) {
			throw new Exception("no task");
		} else {
			listType = new TypeToken<Map<String, String>>() {}.getType();
			mapTask = gson.fromJson(json, listType);
		}

		return mapTask;
	}
}
