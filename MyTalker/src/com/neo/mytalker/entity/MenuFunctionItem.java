package com.neo.mytalker.entity;

public class MenuFunctionItem {
	private int icon;//图标
	private String name;//图标名称
	private int position;//位置 
	
	public int getIcon() {
		return icon;
	}
	public void setIcon(int icon) {
		this.icon = icon;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public MenuFunctionItem(int icon, String name, int position) {
		super();
		this.icon = icon;
		this.name = name;
		this.position = position;
	}
	public MenuFunctionItem() {
		super();
	}
	@Override
	public String toString() {
		return "MenuFunctionItem [icon=" + icon + ", name=" + name + ", position=" + position + "]";
	}
	
	
}
