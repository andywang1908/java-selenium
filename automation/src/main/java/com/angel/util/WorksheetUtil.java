package com.angel.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WorksheetUtil {
	private final static WorksheetUtil instance = new WorksheetUtil();
	public static WorksheetUtil getInstance() {
		return instance;
	}

	//multiple instance
	public Sheet getObjByName(String excelFile, String sheetName) throws Exception {
		FileInputStream fileInputStream = null;
		Workbook wb = null;
		Sheet sheet = null;
		
		fileInputStream = new FileInputStream(excelFile);
		if ( excelFile.endsWith(".xls") ) {
			wb = new HSSFWorkbook(fileInputStream);
		} else {
			wb = new XSSFWorkbook(fileInputStream);
		}
		if ( sheetName==null ) {
			sheet = wb.getSheetAt(0);
		} else {
			
		}
		
		//TODO finally
		if ( fileInputStream!=null ) {
			fileInputStream.close();
		}

		return sheet;
	}
	
	public Sheet initSheet(Workbook wb, String sheetName) {
		Sheet sheet = null;
		
		for (int i = 0; i < wb.getNumberOfSheets(); i++) {
			if ((sheetName).equalsIgnoreCase(wb.getSheetName(i))) {
				sheet = wb.getSheetAt(i);
				break;
			}
		}
		if (sheet == null) {
			sheet = wb.createSheet(sheetName);
		}
		
		return sheet;
	}
	
	public void save(Sheet sheet, String excelFile) throws Exception {
		FileOutputStream fileOut = new FileOutputStream(excelFile);
		sheet.getWorkbook().write(fileOut);

		//TODO finally
		if ( fileOut!=null ) {
			fileOut.close();
		}
	}
	
	public void releaseMem(Sheet sheet) throws Exception {
		if ( sheet!=null && sheet.getWorkbook()!=null ) {
			sheet.getWorkbook().close();
		}
	}

	// read excel,clean better than new excel,you can save some format
	// TODO clean bug    change "" to null
	public void cleanSheetContent(Sheet sheet, int start) {
		int i = 0;
		for (Row row : sheet) {
			if (i < start) {
				i++;
				continue;
			}

			for (Cell cell : row) {
				// break;
				cell.setCellValue("");
			}
			i++;
		}
	}
}
