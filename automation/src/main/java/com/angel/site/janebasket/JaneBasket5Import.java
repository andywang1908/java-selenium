package com.angel.site.janebasket;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.angel.model.ClothItem;
import com.angel.site.janebasket.meta.MetaFactory;
import com.angel.util.FolderUtil;
import com.angel.util.SysUtil;
import com.angel.util.WorksheetUtil;

public class JaneBasket5Import extends GenerateFile {
	private int idxTitle = 1;

	private void doit(String resourceFile, String excelFile) throws Exception {
		Map<String, ClothItem> mapResource = readResourceJson(resourceFile);

		Workbook wb = null;
		Sheet sheet = null;

		try {
			sheet = WorksheetUtil.getInstance().getObjByName(excelFile, null);
			wb = sheet.getWorkbook();

			this.tabMain(wb, mapResource);

			List<String[]> subTabImage = new ArrayList<String[]>();
			List<String[]> subTabColorSize = new ArrayList<String[]>();
			List<String[]> subTabColorSizeTotal = new ArrayList<String[]>();
			// mapResource = readResourceJson(resourceFile);

			this.prepareTab(mapResource, subTabImage, subTabColorSize, subTabColorSizeTotal);

			this.tabAdditionalImages(wb, subTabImage);
			this.tabProductOptions(wb, subTabColorSizeTotal);
			this.tabProductOptionValues(wb, subTabColorSize);

			// save excel
			WorksheetUtil.getInstance().save(sheet, excelFile);
		} catch (Exception e) {
			throw e;
		} finally {
			WorksheetUtil.getInstance().releaseMem(sheet);
		}
	}

	private void prepareTab(Map<String, ClothItem> mapResource, List<String[]> subTabImage,
			List<String[]> subTabColorSize, List<String[]> subTabColorSizeTotal) {
		ClothItem clothItem = null;

		Iterator<?> it = null;
		it = mapResource.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			clothItem = (ClothItem) pair.getValue();

			if (clothItem.getImageSrcList() != null && clothItem.getImageSrcList().size() >= 0) {
				for (int k = 1; k < clothItem.getImageSrcList().size(); k++) {
					// save to sub tab;
					String[] arrImage = new String[2];
					arrImage[0] = clothItem.getId();
					arrImage[1] = clothItem.getImageSrcList().get(k);
					subTabImage.add(arrImage);
				}
			}
			if (clothItem.getColorList() != null && clothItem.getColorList().size() > 0) {
				for (String color:clothItem.getColorList()) {
					color = SysUtil.getInstance().formatColor(color, clothItem);
					
					String[] arrColorSize = new String[3];
					arrColorSize[0] = clothItem.getId();
					arrColorSize[1] = "Color";
					arrColorSize[2] = color;
					subTabColorSize.add(arrColorSize);
				}
				
				String[] arrTotal = new String[2];
				arrTotal[0] = clothItem.getId();
				arrTotal[1] = "Color";
				subTabColorSizeTotal.add(arrTotal);
			}
			//sizeline is useless
			/*
			if (clothItem.getSizeLine() != null) {
				// convert to array
			}*/
			if ( clothItem.getSizeList()!=null ) {
				for (String size:clothItem.getSizeList()) {
					String[] arrColorSize = new String[3];
					arrColorSize[0] = clothItem.getId();
					arrColorSize[1] = "Size";
					arrColorSize[2] = size.toUpperCase();
					subTabColorSize.add(arrColorSize);
				}
				
				String[] arrTotal = new String[2];
				arrTotal[0] = clothItem.getId();
				arrTotal[1] = "Size";
				subTabColorSizeTotal.add(arrTotal);
			} else {
				System.out.println("this has no size actually.("+clothItem.getModel()+")");
				String[] arrColorSize = new String[3];
				arrColorSize[0] = clothItem.getId();
				arrColorSize[1] = "Size";
				arrColorSize[2] = "L";
				subTabColorSize.add(arrColorSize);
				arrColorSize = new String[3];
				arrColorSize[0] = clothItem.getId();
				arrColorSize[1] = "Size";
				arrColorSize[2] = "XL";
				subTabColorSize.add(arrColorSize);

				String[] arrTotal = new String[2];
				arrTotal[0] = clothItem.getId();
				arrTotal[1] = "Size";
				subTabColorSizeTotal.add(arrTotal);
			}

			// it.remove(); // avoids a ConcurrentModificationException
		}

	}

	private void tabMain(Workbook wb, Map<String, ClothItem> mapResource) throws Exception {
		int i, j;
		String value;

		Sheet sheet = null;
		Row row = null;
		Cell cell;
		CellStyle cs;

		ClothItem clothItem = null;

		sheet = WorksheetUtil.getInstance().initSheet(wb, "Products");
		WorksheetUtil.getInstance().cleanSheetContent(sheet, idxTitle);

		i = idxTitle;
		Iterator<?> it = null;
		it = mapResource.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			clothItem = (ClothItem) pair.getValue();

			row = sheet.createRow(i++);
			j = 0;
			cell = row.createCell(j++);
			cell.setCellValue(clothItem.getId());

			cell = row.createCell(j++);
			cs = wb.createCellStyle();
			cs.setWrapText(true);
			cell.setCellStyle(cs);
			cell.setCellValue(clothItem.getName());

			cell = row.createCell(j++);
			value = clothItem.getCate();
			String[] arr = value.split(",");
			value = "";
			for (String strSingle : arr) {
				String ttt = MetaFactory.getInstance().getMetaCate().get(strSingle);
				if ( ttt!= null ) {
					value += ttt + ",";
				} else {
					System.out.println("error:can not get category id:("+strSingle+")");
					//value += strSingle + ",";
				}
			}
			if (value.length()>1) {
				value = value.substring(0, value.length() - 1);
			}
			cell.setCellValue(value);

			cell = row.createCell(j++);
			cell.setCellValue(clothItem.getModel());

			j = j + 6;

			cell = row.createCell(j++);
			cell.setCellValue(100000);

			cell = row.createCell(j++);
			cell.setCellValue(clothItem.getModel());

			cell = row.createCell(j++);
			value = clothItem.getName();
			if (value.indexOf("®") > 0) {
				value = value.substring(0, value.indexOf("®"));
			} else if (value.indexOf("™") > 0) {
				value = value.substring(0, value.indexOf("™"));
			} else {
				value = "";
			}
			value = value.replaceAll("NEW!", "").trim();
			cell.setCellValue(value);

			cell = row.createCell(j++);
			// faceimage

			value = clothItem.getFaceImg();
			if ( value==null ) {
				if ( clothItem.getImageSrcList()!=null ) {
					value = clothItem.getImageSrcList().get(0);
				}
			}
			if (value == null) {
				throw new Exception("The cloth has no face image(" + clothItem + ")");
			}
			if (value.indexOf(".com/") > 0) {
				value = value.substring(value.indexOf(".com/") + 5, value.length());
			}
			cell.setCellValue(value);

			cell = row.createCell(j++);// shipping
			cell.setCellValue("yes");

			String price = this.lowestPrice(clothItem);
			cell = row.createCell(j++);// price
			cell.setCellValue(price);

			cell = row.createCell(j++);// point
			cell.setCellValue(0);

			cell = row.createCell(j++);// shipping
			cell.setCellValue("2016-01-15");
			cell = row.createCell(j++);// shipping
			cell.setCellValue("2016-01-15");
			cell = row.createCell(j++);// shipping
			cell.setCellValue("2016-01-15");

			cell = row.createCell(j++);// point
			cell.setCellValue(0.00);
			cell = row.createCell(j++);// point
			cell.setCellValue("kg");
			cell = row.createCell(j++);// point
			cell.setCellValue(0);
			cell = row.createCell(j++);// point
			cell.setCellValue(0);
			cell = row.createCell(j++);// point
			cell.setCellValue(0);
			cell = row.createCell(j++);// point
			cell.setCellValue("cm");
			cell = row.createCell(j++);// point
			cell.setCellValue("true");
			cell = row.createCell(j++);// tax_class_id
			cell.setCellValue("9");
			cell = row.createCell(j++);// seo_keyword
			cell.setCellValue("");

			cell = row.createCell(j++);// desc
			value = clothItem.getDesc();
			if ( value==null ) value="";
			arr = value.split("\\|");
			if ( arr.length>1 ) {
				value = "";
				String logo = clothItem.getLogoImg();
				if ( logo==null || logo.equals("") ) {
				} else {
					if (logo.indexOf(".com/") > 0) {
						logo = "/image/"+logo.substring(logo.indexOf(".com/") + 5, logo.length());
					}
					value += "<img src='"+logo+"' /><br/>";
				}
				value += "<ul>";
				for (String str: arr) {
					if ( str.indexOf("2016 Catalogue")>=0 || str.trim().equals("") ) {
					} else {
						value += "<li>"+str+"</li>";
					}
				}
				//TODO util
				String pdf = clothItem.getPdf();
				if ( pdf==null || pdf.equals("") ) {
				} else {
					if (pdf.indexOf(".com/") > 0) {
						pdf = "/image/"+pdf.substring(pdf.indexOf(".com/") + 5, pdf.length());
					}
					value += "<li>Click <a href='"+pdf+"' target='_blank'>here</a> to download specification PDF</li>";
				}
				value += "</ul>";
			}
			cs = wb.createCellStyle();
			cs.setWrapText(true);
			cell.setCellStyle(cs);
			cell.setCellValue(value);

			cell = row.createCell(j++);//meta
			cell.setCellValue(clothItem.getName());
			cell = row.createCell(j++);//meta
			cell.setCellValue(clothItem.getName());
			cell = row.createCell(j++);//meta
			cell.setCellValue(clothItem.getName());

			cell = row.createCell(j++);
			cell.setCellValue(6);
			cell = row.createCell(j++);
			cell.setCellValue(0);

			j = j + 3;

			cell = row.createCell(j++);
			cell.setCellValue(1);
			cell = row.createCell(j++);
			cell.setCellValue("true");
			cell = row.createCell(j++);
			cell.setCellValue(1);

			// it.remove(); // avoids a ConcurrentModificationException
			//break;
		}
	}

	private String lowestPrice(ClothItem clothItem) {
		String result = "99.99";
		
		String colorSize;
		String price;
		
		if ( clothItem!=null && clothItem.getPriceMap()!=null ) {
			Iterator<?> it = null;
			it = clothItem.getPriceMap().entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				colorSize = (String) pair.getKey();
				if ( colorSize.startsWith(clothItem.getModel()) ) {
					continue;
				}
				price = (String) pair.getValue();
				if ( price.startsWith("$") ) {
					price = price.substring(1);
				}
				try {
					if ( Float.parseFloat(result)>Float.parseFloat(price) ) {
						result = price;
					}
				} catch (Exception e) {
					System.out.println("Error:lowestPrice");
					e.printStackTrace();
				}
			}
		}

		
		return result;
	}

	private void tabProductOptionValues(Workbook wb, List<String[]> subTabColorSize) {
		int i, j;
		String value;

		Sheet sheet = null;
		Row row = null;
		Cell cell;
		CellStyle cs;

		sheet = WorksheetUtil.getInstance().initSheet(wb, "ProductOptionValues");
		WorksheetUtil.getInstance().cleanSheetContent(sheet, idxTitle);
		
		i = idxTitle;
		for (String[] arr : subTabColorSize) {
			row = sheet.createRow(i++);
			j = 0;
			cell = row.createCell(j++);
			cell.setCellValue(arr[0]);
			cell = row.createCell(j++);
			cell.setCellValue(arr[1]);
			cell = row.createCell(j++);
			cell.setCellValue(arr[2].trim());
			cell = row.createCell(j++);
			cell.setCellValue(100000);
			cell = row.createCell(j++);
			cell.setCellValue("true");
			cell = row.createCell(j++);
			cell.setCellValue("0.00");
			cell = row.createCell(j++);
			cell.setCellValue("+");
			cell = row.createCell(j++);
			cell.setCellValue(0);
			cell = row.createCell(j++);
			cell.setCellValue("+");
			cell = row.createCell(j++);
			cell.setCellValue("0.00");
			cell = row.createCell(j++);
			cell.setCellValue("+");
		}
	}

	private void tabProductOptions(Workbook wb, List<String[]> subTabColorSizeTotal) {
		int i, j;
		String value;

		Sheet sheet = null;
		Row row = null;
		Cell cell;
		CellStyle cs;

		sheet = WorksheetUtil.getInstance().initSheet(wb, "ProductOptions");
		WorksheetUtil.getInstance().cleanSheetContent(sheet, idxTitle);
		
		i = idxTitle;
		for (String[] arr : subTabColorSizeTotal) {
			row = sheet.createRow(i++);
			j = 0;
			cell = row.createCell(j++);
			cell.setCellValue(arr[0]);
			cell = row.createCell(j++);
			cell.setCellValue(arr[1]);
			j++;
			cell = row.createCell(j++);
			cell.setCellValue("true");
		}
	}

	private void tabAdditionalImages(Workbook wb, List<String[]> subTabImage) {
		int i, j;
		String value;

		Sheet sheet = null;
		Row row = null;
		Cell cell;
		CellStyle cs;

		sheet = WorksheetUtil.getInstance().initSheet(wb, "AdditionalImages");
		WorksheetUtil.getInstance().cleanSheetContent(sheet, idxTitle);
		
		i = idxTitle;
		for (String[] arr : subTabImage) {
			row = sheet.createRow(i++);
			j = 0;
			cell = row.createCell(j++);
			cell.setCellValue(arr[0]);
			cell = row.createCell(j++);
			value = arr[1];
			if (value.indexOf(".com/") > 0) {
				value = value.substring(value.indexOf(".com/") + 5, value.length());
			}
			cell.setCellValue(value);
			cell = row.createCell(j++);
			cell.setCellValue("0");
		}
	}

	public static void main(String[] args) throws Exception {
		SysUtil.getInstance().setProvinceProxy();
		
		/*
		String cateFolder = null;
		cateFolder = "wanglin";
		//cateFolder = "wanglin/lady";*/

		JaneBasket5Import me = new JaneBasket5Import();
		FolderUtil folderUtil = FolderUtil.getInstance();
		String resourceFile = folderUtil.combineDic(folderUtil.calRootPath(), me.taskFold, "resource.json");
		String excelFile = folderUtil.combineDic(folderUtil.calRootPath(), me.taskFold, "product.xlsx");

		me.doit(resourceFile, excelFile);
	}

}
