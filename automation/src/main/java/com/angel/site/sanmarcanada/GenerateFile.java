package com.angel.site.sanmarcanada;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.angel.model.ClothItem;
import com.angel.site.sanmarcanada.meta.MetaFactory;
import com.angel.util.FileUtil;
import com.angel.util.FolderUtil;
import com.angel.util.GsonUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GenerateFile {
	//public String taskFold = "db/sanmarcanada";
	//public String taskFold = "db/sanmarcanada/lady";
	//public String taskFold = "db/sanmarcanada/youth";
	//public String taskFold = "db/sanmarcanada/sport";
	//public String taskFold = "db/sanmarcanada/woven";
	//public String taskFold = "db/sanmarcanada/workwear";  
	//public String taskFold = "db/sanmarcanada/activeware";
	//public String taskFold = "db/sanmarcanada/fleece";
	//public String taskFold = "db/sanmarcanada/outerwear";
	//public String taskFold = "db/sanmarcanada/headwear";
	//public String taskFold = "db/sanmarcanada/accessories";
	public String taskFold = "db/sanmarcanada/pants";//cur

	protected Map<String, ClothItem> readResourceJson(String metaFile) throws Exception {
		String json = null;
		Type jsonType = null;

		json = FileUtil.getInstance().readTxt(metaFile);
		Map<String, ClothItem> clothMeta;
		if (json == null) {
			clothMeta = new HashMap<String, ClothItem>();
		} else {
			jsonType = new TypeToken<Map<String, ClothItem>>() {
			}.getType();
			clothMeta = GsonUtil.getObj().fromJson(json, jsonType);
		}

		return clothMeta;
	}

	protected Map<String, ClothItem> readUnionJson() throws Exception {
		FolderUtil folderUtil = FolderUtil.getInstance();
		//setup wanglin
		String resourceFile = folderUtil.combineDic( folderUtil.calRootPath(), "wanglin/tone", "union.json");

		return readResourceJson(resourceFile);
	}

	protected void saveUnionJson(Map<String, ClothItem> mapUnion) throws Exception {
		FolderUtil folderUtil = FolderUtil.getInstance();
		//setup wanglin
		String resourceFile = folderUtil.combineDic( folderUtil.calRootPath(), "wanglin", "union.json");

		String json = GsonUtil.getObj().toJson(mapUnion);
		FileUtil.getInstance().writeTxt(resourceFile, json);
	}

	protected Map<String, String> readTaskJson(String taskFile) throws Exception {
		return GsonUtil.getInstance().parseMapStringString(taskFile);
	}

	protected String convertToId(String value, ClothItem clothItem) {
		String result = null;
		
		if ( value.equals("4XLT") ) {
			System.out.println("aaa");
		}

		result = MetaFactory.getInstance().getMetaColorSize().get(value);
		
		if ( result!=null ) {
			//System.out.println("ok:colorSize:("+color+":"+value+")");
		} else {
			//System.out.println("error:colorSize value("+value+")");
			MetaFactory.getInstance().getMetaMissingColor().put(value, "1");
			/*
			if ( value.equals("Pink") || value.equals("Ice Grey") || value.equals("Sand") || value.equals("Texas Orange") || value.equals("Olive") || value.equals("Stone Blue") || value.equals("Prairie Dust") || value.equals("Honey") || value.equals("Natural") || value.equals("Hthr Cardinal") || value.equals("Hthr Indigo") || value.equals("Sky") || value.equals("Paprika") || value.equals("Ash") || value.equals("Orchid") || value.equals("Blue Dusk") || value.equals("Chestnut") || value.equals("Tan") || value.equals("Serene Green") || value.equals("Tangerine") || value.equals("Iris") || value.equals("Royal Blue") || value.equals("Safety Green") || value.equals("Drk Chocolate") || value.equals("Safety Orange") || value.equals("Dark Grey Hth") || value.equals("Indigo") ) {
			} else {
			}*/
		}
		
		return result;
	}

}
