package com.angel.model;

public class DownloadTask {
	private String id;
	private String desc;
	private int status;//0 todo 1 finshed

	private String url;
	private String f1;
	private String f2;
	private String f3 = "";
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getF1() {
		return f1;
	}
	public void setF1(String f1) {
		this.f1 = f1;
	}
	public String getF2() {
		return f2;
	}
	public void setF2(String f2) {
		this.f2 = f2;
	}
	public String getF3() {
		return f3;
	}
	public void setF3(String f3) {
		this.f3 = f3;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return "DownloadTask [id=" + id + ", desc=" + desc + ", status=" + status + ", url=" + url + ", f1=" + f1
				+ ", f2=" + f2 + ", f3=" + f3 + "]";
	}

	
	
}
