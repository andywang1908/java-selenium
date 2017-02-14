package com.angel.model;

import java.util.List;
import java.util.Map;

public class ClothItem implements Cloneable {
	private String id;
	private String name;
	private String desc;
	private String memo;
	private String cate;

	private String faceImg;
	private String logoImg;
	private List<String> colorList;
	private List<String> colorImageList;//actually for setting    red|fda/fdas/ddd.jpg
	private List<String> sizeList;
	private List<String> imageSrcList;
	private String sizeLine;

	private Map<String, String> priceMap;
	private Map<String, String> inventoryMap;
	
	private String model;
	private String sku;
	private int quantity;
	private int price;//cent
	private String priceTxt;//cent
	private String pdf;
	private int checkPrice;
	
	private String lastUrl="";
	
	public int getCheckPrice() {
		return checkPrice;
	}
	public void setCheckPrice(int checkPrice) {
		this.checkPrice = checkPrice;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getCate() {
		return cate;
	}
	public void setCate(String cate) {
		this.cate = cate;
	}
	public String getFaceImg() {
		return faceImg;
	}
	public void setFaceImg(String faceImg) {
		this.faceImg = faceImg;
	}
	public List<String> getColorList() {
		return colorList;
	}
	public void setColorList(List<String> colorList) {
		this.colorList = colorList;
	}
	public List<String> getSizeList() {
		return sizeList;
	}
	public void setSizeList(List<String> sizeList) {
		this.sizeList = sizeList;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getSizeLine() {
		return sizeLine;
	}
	public void setSizeLine(String sizeLine) {
		this.sizeLine = sizeLine;
	}
	public List<String> getColorImageList() {
		return colorImageList;
	}
	public void setColorImageList(List<String> colorImageList) {
		this.colorImageList = colorImageList;
	}
	public String getPdf() {
		return pdf;
	}
	public void setPdf(String pdf) {
		this.pdf = pdf;
	}
	public List<String> getImageSrcList() {
		return imageSrcList;
	}
	public void setImageSrcList(List<String> imageSrcList) {
		this.imageSrcList = imageSrcList;
	}
	public Map<String, String> getPriceMap() {
		return priceMap;
	}
	public void setPriceMap(Map<String, String> priceMap) {
		this.priceMap = priceMap;
	}
	public Map<String, String> getInventoryMap() {
		return inventoryMap;
	}
	public void setInventoryMap(Map<String, String> inventoryMap) {
		this.inventoryMap = inventoryMap;
	}
	public String getLogoImg() {
		return logoImg;
	}
	public void setLogoImg(String logoImg) {
		this.logoImg = logoImg;
	}
	public String getLastUrl() {
		return lastUrl;
	}
	public void setLastUrl(String lastUrl) {
		this.lastUrl = lastUrl;
	}
	
	
	public String getPriceTxt() {
		return priceTxt;
	}
	public void setPriceTxt(String priceTxt) {
		this.priceTxt = priceTxt;
	}
	@Override
	public String toString() {
		return "ClothItem [id=" + id + ", name=" + name + ", desc=" + desc + ", memo=" + memo + ", cate=" + cate
				+ ", faceImg=" + faceImg + ", logoImg=" + logoImg + ", colorList=" + colorList + ", colorImageList="
				+ colorImageList + ", sizeList=" + sizeList + ", imageSrcList=" + imageSrcList + ", sizeLine="
				+ sizeLine + ", priceMap=" + priceMap + ", inventoryMap=" + inventoryMap + ", model=" + model + ", sku="
				+ sku + ", quantity=" + quantity + ", price=" + price + ", pdf=" + pdf + ", checkPrice=" + checkPrice
				+ ", lastUrl=" + lastUrl + "]";
	}
	
	
	
}
