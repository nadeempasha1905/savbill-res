package com.savbill.user;
import java.util.List;


public class MenuStructureBO {
	
	private String title;
	private String name;
	private String previlage;
	List<MenuStructureBO> SubMenuList ;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrevilage() {
		return previlage;
	}
	public void setPrevilage(String previlage) {
		this.previlage = previlage;
	}
	public List<MenuStructureBO> getSubMenuList() {
		return SubMenuList;
	}
	public void setSubMenuList(List<MenuStructureBO> subMenuList) {
		SubMenuList = subMenuList;
	}
}


