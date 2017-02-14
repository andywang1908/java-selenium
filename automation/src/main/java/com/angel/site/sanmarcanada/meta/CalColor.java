package com.angel.site.sanmarcanada.meta;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.angel.model.ClothItem;
import com.angel.model.DownloadTask;
import com.angel.util.FileUtil;
import com.angel.util.FolderUtil;
import com.angel.util.GsonUtil;
import com.angel.util.HttpUtil;
import com.angel.util.SysUtil;
import com.google.gson.reflect.TypeToken;

public class CalColor {
	//private String metaFile;

	private void doit() throws Exception {
		FolderUtil folderUtil = FolderUtil.getInstance();
		String resourceFile = folderUtil.combineDic( folderUtil.calRootPath(), "wanglin", "resource.json" );
		Map<String, String> colorParam = dealSingle(resourceFile);
		
		resourceFile = folderUtil.combineDic( folderUtil.calRootPath(), "wanglin/lady", "resource.json" );
		colorParam.putAll( dealSingle(resourceFile) );
		
		resourceFile = folderUtil.combineDic( folderUtil.calRootPath(), "wanglin/youth", "resource.json" );
		colorParam.putAll( dealSingle(resourceFile) );
		
		resourceFile = folderUtil.combineDic( folderUtil.calRootPath(), "wanglin/sport", "resource.json" );
		colorParam.putAll( dealSingle(resourceFile) );
		
		resourceFile = folderUtil.combineDic( folderUtil.calRootPath(), "wanglin/woven", "resource.json" );
		colorParam.putAll( dealSingle(resourceFile) );
		
		resourceFile = folderUtil.combineDic( folderUtil.calRootPath(), "wanglin/workwear", "resource.json" );
		colorParam.putAll( dealSingle(resourceFile) );
		
		resourceFile = folderUtil.combineDic( folderUtil.calRootPath(), "wanglin/activeware", "resource.json" );
		colorParam.putAll( dealSingle(resourceFile) );
		
		resourceFile = folderUtil.combineDic( folderUtil.calRootPath(), "wanglin/fleece", "resource.json" );
		colorParam.putAll( dealSingle(resourceFile) );
		
		resourceFile = folderUtil.combineDic( folderUtil.calRootPath(), "wanglin/outerwear", "resource.json" );
		colorParam.putAll( dealSingle(resourceFile) );
		
		resourceFile = folderUtil.combineDic( folderUtil.calRootPath(), "wanglin/headwear", "resource.json" );
		colorParam.putAll( dealSingle(resourceFile) );
		
		resourceFile = folderUtil.combineDic( folderUtil.calRootPath(), "wanglin/accessories", "resource.json" );
		colorParam.putAll( dealSingle(resourceFile) );
		
		resourceFile = folderUtil.combineDic( folderUtil.calRootPath(), "wanglin/pants", "resource.json" );
		colorParam.putAll( dealSingle(resourceFile) );

	    System.out.println("size:"+colorParam.keySet().size());
	    
		Iterator it = colorParam.entrySet().iterator();
		Map<String, String> mapColor = MetaFactory.getInstance().getMetaColorSize();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        //System.out.println( SysUtil.getInstance().formatColor(pair.getKey().toString(), null)  + "	" + pair.getValue());
	        //HttpUtil.getInstance().save(pair.getValue().toString());
	        //it.remove(); // avoids a ConcurrentModificationException
	        
	        if ( mapColor.get(SysUtil.getInstance().formatColor(pair.getKey().toString(), null))==null ) {
		        System.out.println( "missing:"+SysUtil.getInstance().formatColor(pair.getKey().toString(), null)  + "	" + pair.getValue());
	        }
	        
	    }
	}
	private Map<String, String> dealSingle(String resourceFile) throws Exception {
		String json = null;
		
		json = FileUtil.getInstance().readTxt(resourceFile);
		Map<String, ClothItem> clothMeta;
		ClothItem clothItem = null;
		if ( json==null ) {
			clothMeta = new HashMap<String, ClothItem>();
		} else {
			Type listType = new TypeToken<Map<String, ClothItem>>() {}.getType();
			clothMeta = GsonUtil.getObj().fromJson(json, listType);
		}

		List<String> imageThumbList = null;
		Iterator it = clothMeta.entrySet().iterator();
		Map<String, String> colorParam = new HashMap<String, String>();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        
	        clothItem = (ClothItem)pair.getValue();
	        imageThumbList = clothItem.getColorImageList();
	        for ( String ss:imageThumbList ) {
	        		String[] arr = ss.split("\\|");
	        		colorParam.put(arr[0], arr[1]);
	        }
	        
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    
	    return colorParam;
	}

	public static void main(String[] args) throws Exception {
		SysUtil.getInstance().setProvinceProxy();
		
		String cateFolder = null;
		cateFolder = "wanglin";
		cateFolder = "wanglin/lady";

		CalColor me = new CalColor();
		
		me.doit();
	}

}
