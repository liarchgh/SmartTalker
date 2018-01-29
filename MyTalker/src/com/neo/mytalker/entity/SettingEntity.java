package com.neo.mytalker.entity;

public class SettingEntity {
	private String name;
	private String title;
	private int id;
	private int imgId;
	private int titleId;
	public int getTitleId() {
		return titleId;
	}
	public void setTitleId(int titleId) {
		this.titleId = titleId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getImgId() {
		return imgId;
	}
	public void setImgId(int imgId) {
		this.imgId = imgId;
	}
	public SettingEntity(String name, String title, int id, int imgId, int titleId) {
		super();
		this.name = name;
		this.title = title;
		this.id = id;
		this.imgId = imgId;
		this.titleId = titleId;
	}
	public SettingEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "SettingEntity [name=" + name + ", title=" + title + ", id=" + id + ", imgId=" + imgId + "]";
	}
	
}
