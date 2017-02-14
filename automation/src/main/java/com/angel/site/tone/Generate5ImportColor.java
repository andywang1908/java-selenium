package com.angel.site.tone;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.angel.model.ClothItem;
import com.angel.site.sanmarcanada.meta.MetaFactory;
import com.angel.util.FolderUtil;
import com.angel.util.SysUtil;
import com.angel.util.WorksheetUtil;

public class Generate5ImportColor extends GenerateFile {
	private int idxTitle = 1;

	private void doit(String resourceFile, String excelFile) throws Exception {
		Map<String, ClothItem> mapResource = readResourceJson(resourceFile);
		
		Iterator<String> iter;
		String value = null;

		String color = null;

	    String key = null;
	    String colorName = null;
	    String sizeName = null;

		Sheet sheet = null;
		Row row = null;
		Cell cell;
		//CellStyle cs;
		
		try {
			sheet = WorksheetUtil.getInstance().getObjByName(excelFile, null);

			WorksheetUtil.getInstance().cleanSheetContent(sheet, idxTitle+1);
			
			int i=idxTitle;
			int j=0;
			for (ClothItem clothItem: mapResource.values()) {
				iter = clothItem.getColorList().iterator();
				int idx = 0;
				
				List<String> imageList = clothItem.getImageSrcList();
				if ( imageList.size()!=clothItem.getColorList().size() ) {
					throw new Exception("color and image are not even.");
				}
				while (iter.hasNext()) {
				    //System.out.println(entry.getKey()+":"+entry.getValue());
				    
				    key = iter.next();
		    			colorName = SysUtil.getInstance().formatColor(key, clothItem);
	    				color = convertToId(colorName, clothItem);
	    				
	    				if ( color==null ) {
	    					//System.out.println("missing:"+key);
	    					continue;
	    				}

	    				row = sheet.createRow(i++);
	    				j = 0;
	    				cell = row.createCell(j++);
	    				cell.setCellValue(clothItem.getId());
	
	    				cell = row.createCell(j++);
	    				cell.setCellValue(color);
	
					cell = row.createCell(j++);
					value = imageList.get(idx);
					if (value.indexOf(".com/") > 0) {
						value = value.substring(value.indexOf(".com/") + 5, value.length());
					}
					cell.setCellValue(value);
				    
	    				idx++;
				}
				
				//break;
			}
			
			for (String missingColor:MetaFactory.getInstance().getMetaMissingColor().keySet()) {
				System.out.println("missingColor	"+missingColor);
			}

			WorksheetUtil.getInstance().save(sheet, excelFile);
		} catch (Exception e) {
			throw e;
		} finally {
			WorksheetUtil.getInstance().releaseMem(sheet);
		}

	}

	public static void main(String[] args) throws Exception {
		SysUtil.getInstance().setProvinceProxy();
		
		String cateFolder = null;
		cateFolder = "wanglin";
		//cateFolder = "wanglin/lady";

		Generate5ImportColor me = new Generate5ImportColor();
		FolderUtil folderUtil = FolderUtil.getInstance();
		String resourceFile = folderUtil.combineDic(folderUtil.calRootPath(), me.taskFold, "resource.json");
		String excelFile = folderUtil.combineDic(folderUtil.calRootPath(), me.taskFold, "colorImage.xls");//price.xls product.xlsx 

		me.doit(resourceFile, excelFile);
		
	}

}
