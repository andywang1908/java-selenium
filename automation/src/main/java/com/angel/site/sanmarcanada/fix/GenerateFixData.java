package com.angel.site.sanmarcanada.fix;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.angel.model.ClothItem;
import com.angel.site.sanmarcanada.GenerateFile;
import com.angel.site.sanmarcanada.meta.MetaFactory;
import com.angel.util.FileUtil;
import com.angel.util.FolderUtil;
import com.angel.util.GsonUtil;
import com.angel.util.SysUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GenerateFixData extends GenerateFile {
	public void checkUnionId(String resourceFile) throws Exception {
		Map<String, ClothItem> mapResource = readResourceJson(resourceFile);
		
		Map<String, String> mapCheck = new HashMap<String, String>();
		
		int idx = 0;
		
		for (ClothItem clothItem: mapResource.values() ) {
			idx++;
			
			if ( mapCheck.get(clothItem.getId())==null ) {
				//System.out.println(idx+":"+clothItem.getId());
				mapCheck.put(clothItem.getId(), clothItem.getModel());
			} else {
				System.out.println(idx+":"+clothItem.getId()+":in:"+mapCheck.get(clothItem.getId())+":out:"+clothItem.getModel());
			}
		}
		
		for(int i=0;i<400;i++) {
			if ( mapCheck.get(i+"")==null ) {
				System.out.println("missing i:"+i);
			}
		}
	}

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
	
	public void fixEmptyId(String resourceFile) throws Exception {
		Gson gson = new Gson();
		String json = null;
		FolderUtil folderUtil = new FolderUtil();
		
		Map<String, ClothItem> mapResource = readResourceJson(resourceFile);
		
		String unionFile = folderUtil.combineDic( folderUtil.calRootPath(), "wanglin", "union.json");

		Map<String, ClothItem> mapUnion = readResourceJson(unionFile);
		
		int idx = 0;
		
		for (ClothItem clothItem: mapResource.values() ) {
			idx++;
			

			if ( mapUnion.get( clothItem.getModel() )!=null ) {
				String unionId = mapUnion.get( clothItem.getModel() ).getId();

				if ( clothItem.getId()==null ) {
					//System.out.println(idx+":"+clothItem.getId());
					//mapCheck.put(clothItem.getId(), clothItem.getModel());
					System.out.println("empty id should be "+unionId+"");
					
					clothItem.setId( unionId );
				} else {
					if ( !clothItem.getId().equals(unionId) ) {
						System.out.println("different id should be "+unionId);
						throw new Exception("different id should be "+unionId);
					}
				}
			} else {
				System.out.println("Error:can not be found in mapUnion:"+clothItem.getModel()+"");
			}
			
		}
		
		//json = gson.toJson(mapResource);
		//FileUtil.getInstance().writeTxt(resourceFile, json);
	}
	
	//danger 
	//TODO backup automatically
	public void fixId(String resourceFile) throws Exception {
		Gson gson = new Gson();
		String json = null;
		
		Map<String, ClothItem> mapResource = readResourceJson(resourceFile);
		
		int idx = 0;
		
		for (ClothItem clothItem: mapResource.values() ) {
			idx++;
			
			clothItem.setId(idx+"");
		}
		
		json = gson.toJson(mapResource);
		FileUtil.getInstance().writeTxt(resourceFile, json);
	}
	
	//danger  never use
	public void fixUnion(String resourceFile) throws Exception {
		Gson gson = new Gson();
		String json = null;
		
		FolderUtil folderUtil = FolderUtil.getInstance();
		String unionFile = folderUtil.combineDic(folderUtil.calRootPath(), "wanglin", "union.json");
		
		Type listType = null;

		json = FileUtil.getInstance().readTxt(unionFile);
		Map<String, ClothItem> unionMap;
		if (json == null) {
			unionMap = new HashMap<String, ClothItem>();
		} else {
			listType = new TypeToken<Map<String, ClothItem>>() {}.getType();
			unionMap = gson.fromJson(json, listType);
		}

		Iterator<Map.Entry<String,ClothItem>> iter = unionMap.entrySet().iterator();
		ClothItem clothItem = null;
		while (iter.hasNext()) {
		    Map.Entry<String,ClothItem> entry = iter.next();
		    //Ladies'  T-Shirts
		    clothItem = new ClothItem();
		    clothItem.setId( entry.getValue().getId() );
		    clothItem.setCate( entry.getValue().getCate() );//must
		    
		    //not must
		    clothItem.setModel( entry.getValue().getModel() );
		    unionMap.put( entry.getKey() , clothItem);
		}
		System.out.println( unionMap.values().size() );

		json = gson.toJson(unionMap);
		FileUtil.getInstance().writeTxt(unionFile, json);
		
		/*
		Map<String, ClothItem> mapResource = readResourceJson(resourceFile);
		
		int idx = 0;

		System.out.println( mapResource.values().size() );
		Iterator<Map.Entry<String,ClothItem>> iter = mapResource.entrySet().iterator();
		while (iter.hasNext()) {
		    Map.Entry<String,ClothItem> entry = iter.next();
		    //Ladies'  T-Shirts
		    if( entry.getValue().getCate().indexOf("T-Shirts")<0 ){
	    			System.out.println(entry.getValue().getCate());
		        iter.remove();
		    }
		}
		System.out.println( mapResource.values().size() );
		
		json = gson.toJson(mapResource);
		FileUtil.getInstance().writeTxt(resourceFile, json);*/
	}

	public static void main(String[] args) throws Exception {
		SysUtil.getInstance().setProvinceProxy();
		
		String cateFolder = null;
		cateFolder = "wanglin/lady";
		cateFolder = "wanglin/youth";
		cateFolder = "wanglin/sport";

		GenerateFixData me = new GenerateFixData();
		FolderUtil folderUtil = FolderUtil.getInstance();
		String resourceFile = folderUtil.combineDic(folderUtil.calRootPath(), cateFolder, "resource.json");

		//danger
		me.checkUnionId(folderUtil.combineDic(folderUtil.calRootPath(), "wanglin", "union.json"));
		//me.fixEmptyId(resourceFile);
		//me.fixId(resourceFile);
		//me.fixUnion(resourceFile);
	}

}
