package com.angel.site.janebasket.meta;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.angel.util.FolderUtil;

//this is not pojo
public class MetaFactory {
	private static final String FOLDER_SANMARCANADA = "db/janebasket";
	private static final String FOLDER_META = "0meta";
	
	private Map<String, String> metaCate;
	private Map<String, String> metaColorSize;
	private Map<String, String> metaMissingColor = new HashMap<String, String>();
	

	private final static MetaFactory instance = new MetaFactory();
	public static MetaFactory getInstance() {
		FolderUtil folderUtil = FolderUtil.getInstance();
		if ( instance.getMetaCate()==null ) {
			String filePath = folderUtil.combineDic(folderUtil.calRootPath(), FOLDER_SANMARCANADA, FOLDER_META, "paramCate.xlsx");
			try {
				instance.initMetaCate(filePath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if ( instance.getMetaColorSize()==null ) {
			String filePath = folderUtil.combineDic(folderUtil.calRootPath(), FOLDER_SANMARCANADA, FOLDER_META, "paramColorSize.xlsx");
			try {
				instance.initMetaColorSize(filePath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return instance;
	}
	
	public Map<String, String> getMetaCate() {
		return metaCate;
	}
	public void setMetaCate(Map<String, String> metaCate) {
		this.metaCate = metaCate;
	}
	public Map<String, String> getMetaColorSize() {
		return metaColorSize;
	}
	public void setMetaColorSize(Map<String, String> metaColorSize) {
		this.metaColorSize = metaColorSize;
	}
	public Map<String, String> getMetaMissingColor() {
		return metaMissingColor;
	}
	public void setMetaMissingColor(Map<String, String> metaMissingColor) {
		this.metaMissingColor = metaMissingColor;
	}

	//load and refresh
	private void initMetaColorSize(String colorSizeFile) throws Exception {

		Map<String, String> result = new HashMap<String, String>();

		FileInputStream fileInputStream = null;
		Workbook wb = null;
		Sheet sheet = null;
		int idx = 0;

		try {
			fileInputStream = new FileInputStream(colorSizeFile);

			wb = new XSSFWorkbook(fileInputStream);
			sheet = wb.getSheetAt(1);

			idx = 0;
			for (Row rowSingle : sheet) {
				if (idx == 0) {
					idx++;
					continue;
				}

				rowSingle.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
				rowSingle.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
				String optionName = rowSingle.getCell(4).toString().trim();
				//String optionType = rowSingle.getCell(1).toString();
				result.put(optionName, rowSingle.getCell(0).toString());

				idx++;
			}

			this.metaColorSize = result;
		} catch (Exception e) {
			throw e;
		} finally {
			if (wb != null) {
				wb.close();
			}
		}

	}
	
	//load and refresh
	private void initMetaCate(String cateFile) throws Exception {
		//clean old
		Map<String, String> result = new HashMap<String, String>();

		FileInputStream fileInputStream = null;
		Workbook wb = null;
		Sheet sheet = null;
		int idx = 0;

		try {
			fileInputStream = new FileInputStream(cateFile);

			wb = new XSSFWorkbook(fileInputStream);
			sheet = wb.getSheetAt(0);

			for (Row rowSingle : sheet) {
				if (idx == -1) {
					idx++;
					continue;
				}

				rowSingle.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
				rowSingle.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
				rowSingle.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
				String parentId = rowSingle.getCell(1).toString();
				if ( parentId==null || parentId.equals("0") ) {
					result.put(rowSingle.getCell(0).toString().trim(), rowSingle.getCell(2).toString());
				}

				idx++;
			}

			idx = 0;
			for (Row rowSingle : sheet) {
				if (idx == 0) {
					idx++;
					continue;
				}

				rowSingle.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
				rowSingle.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
				rowSingle.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
				String parentId = rowSingle.getCell(1).toString();
				if ( !parentId.equals("0") ) {
					String parentName = result.get(parentId);
					if ( parentName!=null ) {
						//System.out.println("ok:category mapping:("+parentId+":"+parentName+")");
						if ( parentName.equals("T-shirts") ) {
							parentName = "T-Shirts";
						} else if ( parentName.equals("Ladies Only") ) {//ours
							parentName = "Ladies'";//source
						} else if ( parentName.equals("Youth Only") ) {//ours
							parentName = "Youth";//source
						} else if ( parentName.equals("WorkWear") ) {//ours
							parentName = "Workwear";//source
						}
						String cateName = rowSingle.getCell(2).toString().trim();
						if ( cateName.equals("Parkas/Shells/Systems Youth") ) {
							cateName = "Parkas/Shells/Systems";
						}
						result.put(parentName+"----"+cateName, rowSingle.getCell(0).toString());
					} else {
						System.out.println("error:category mapping:("+parentId+")");
					}
				}

				idx++;
			}

			this.metaCate = result;
		} catch (Exception e) {
			throw e;
		} finally {
			if (wb != null) {
				wb.close();
			}
		}

	}
	
}
