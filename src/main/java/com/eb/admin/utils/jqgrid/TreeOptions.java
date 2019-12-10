/**
* 
*
*@author：
*@since： JDK1.6
*@version：1.0
*@date：2014-4-18 下午5:50:08
*/ 

package com.eb.admin.utils.jqgrid;

import java.util.List;

/**
 * @ClassName: TreeOptions
 * @Description: 树的配置类
 * @author 
 * @date 2014-4-18 下午5:50:08
 *
 */
@SuppressWarnings({"rawtypes","unused"})
public class TreeOptions {

	/**
	 * 是否显示头节点
	 */
	private boolean isShowRoot = false;
	
	/**
	 * 是否展开全部节点
	 */
	private boolean isOpenAll = false;
	
	/**
	 * 头节点名称
	 */
	private String RootText = "根节点";
	
	/**
	 * 节点ID
	 */
	private String id;
	
	/**
	 * 节点TEXT
	 */
	private String text;
	
	/**
	 * 父节点ID
	 */
	private String parentId;
	
	/**
	 * 节点级别
	 */
	private String nodeLevel;
	
	/**
	 * 是否有子级
	 */
	private String hasChild;
	
	/**
	 * 扩展字段
	 */
	private String[] extendedFields;
	
	/**
	 * 数据 
	 */
	private List dataList;
	
	private TreeOptions(){
		
	}
	
	public TreeOptions(List dataList){
		this.dataList = dataList;
	}

	public boolean isShowRoot() {
		return isShowRoot;
	}

	public void setShowRoot(boolean isShowRoot) {
		this.isShowRoot = isShowRoot;
	}

	public boolean isOpenAll() {
		return isOpenAll;
	}

	public void setOpenAll(boolean isOpenAll) {
		this.isOpenAll = isOpenAll;
	}

	public String getRootText() {
		return RootText;
	}

	public void setRootText(String rootText) {
		RootText = rootText;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getNodeLevel() {
		return nodeLevel;
	}

	public void setNodeLevel(String nodeLevel) {
		this.nodeLevel = nodeLevel;
	}

	public String getHasChild() {
		return hasChild;
	}

	public void setHasChild(String hasChild) {
		this.hasChild = hasChild;
	}

	public String[] getExtendedFields() {
		return extendedFields;
	}

	public void setExtendedFields(String[] extendedFields) {
		this.extendedFields = extendedFields;
	}

	public List getDataList() {
		return dataList;
	}

	public void setDataList(List dataList) {
		this.dataList = dataList;
	}
	
}
