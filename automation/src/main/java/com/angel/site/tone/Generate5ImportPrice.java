package com.angel.site.tone;

import java.util.Iterator;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.angel.model.ClothItem;
import com.angel.site.sanmarcanada.meta.MetaFactory;
import com.angel.util.FolderUtil;
import com.angel.util.SysUtil;
import com.angel.util.WorksheetUtil;

public class Generate5ImportPrice extends GenerateFile {
	private int idxTitle = 1;

	private void doit(String resourceFile, String excelFile) throws Exception {
		Map<String, ClothItem> mapResource = readResourceJson(resourceFile);

		Map<String, String> productParamMap = null;
		Iterator<Map.Entry<String,String>> iter;
		String[] arr = null;
		String value = null;

		String color = null;
		String size = null;
		String price = null;
		String inventory = null;

	    String key = null;
	    String colorName = null;
	    String sizeName = null;

		Sheet sheet = null;
		Row row = null;
		Cell cell;
		CellStyle cs;
		
		try {
			sheet = WorksheetUtil.getInstance().getObjByName(excelFile, null);
			
			WorksheetUtil.getInstance().cleanSheetContent(sheet, idxTitle+1);
			
			int i=idxTitle;//+1
			int j=0;
			for (ClothItem clothItem: mapResource.values()) {
				productParamMap = clothItem.getPriceMap();
				iter = productParamMap.entrySet().iterator();
				while (iter.hasNext()) {
				    Map.Entry<String,String> entry = iter.next();
				    //System.out.println(entry.getKey()+":"+entry.getValue());
				    
				    key = entry.getKey();
				    arr = key.split(",");
				    
				    if ( arr.length==2 ) {
				    		colorName = arr[0];
				    		colorName = SysUtil.getInstance().formatColor(colorName, clothItem);
			    			color = convertToId(colorName, clothItem);
			    			sizeName = arr[1];
			    			size = convertToId(sizeName, clothItem);
			    			price = entry.getValue();
			    			//remove $
			    			if ( price.startsWith("$") ) {
			    				price = price.substring(1);
			    			}
			    			
			    			inventory = clothItem.getInventoryMap().get(key);
			    			if ( inventory==null ) {
					    		System.out.println("error:can not get inventory by key("+entry.getKey()+")");
			    			} else if ( color == null) {
					    		//System.out.println("error:can not get price by key("+entry.getKey()+")");
			    			} else {

			    				row = sheet.createRow(i++);
			    				j = 1;
			    				cell = row.createCell(j++);
			    				cell.setCellValue(clothItem.getId());
			    				cell = row.createCell(j++);
			    				cell.setCellValue(clothItem.getModel());
			    				
			    				j = j+4;
			    				cell = row.createCell(j++);
			    				cell.setCellValue(0);
			    				j++;
			    				cell = row.createCell(j++);
			    				cell.setCellValue(0);
			    				cell = row.createCell(j++);
			    				cell.setCellValue(inventory);
			    				cell = row.createCell(j++);
			    				cell.setCellValue(price);

			    				cell = row.createCell(j++);
			    				cell.setCellValue("13");
			    				cell = row.createCell(j++);
			    				cell.setCellValue("Color");
			    				cell = row.createCell(j++);
			    				cell.setCellValue(color);
			    				cell = row.createCell(j++);
			    				cell.setCellValue(colorName);

			    				cell = row.createCell(j++);
			    				cell.setCellValue("14");
			    				cell = row.createCell(j++);
			    				cell.setCellValue("Size");
			    				cell = row.createCell(j++);
			    				cell.setCellValue(size);
			    				cell = row.createCell(j++);
			    				cell.setCellValue(sizeName);
			    				
			    			}
				    } else {
				    		System.out.println("error:wrong key("+entry.getKey()+")");
				    }
				    
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

		Generate5ImportPrice me = new Generate5ImportPrice();
		FolderUtil folderUtil = FolderUtil.getInstance();
		String resourceFile = folderUtil.combineDic(folderUtil.calRootPath(), me.taskFold, "resource.json");
		String excelFile = folderUtil.combineDic(folderUtil.calRootPath(), me.taskFold, "price.xls");//price.xls product.xlsx 

		me.doit(resourceFile, excelFile);
	}

}
