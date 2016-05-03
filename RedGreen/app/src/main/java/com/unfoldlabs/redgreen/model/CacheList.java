package com.unfoldlabs.redgreen.model;

public class CacheList {

	private int id;
	private int cacheClean;
	private String updatedDate;
	private int ramClean;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCacheClean() {
		return cacheClean;
	}
	public void setCacheClean(int cacheClean) {
		this.cacheClean = cacheClean;
	}
	public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	public int getRamClean() {
		return ramClean;
	}
	public void setRamClean(int ramClean) {
		this.ramClean = ramClean;
	}

}
