/**  
* @Title: TreeUtils.java
* @Package: cn.wywk.mypay.comm.base
* @Description: TREE 对象 ：主要功能生成对象初始化所生成的JSON
* @author: kevin
* @date: 2014-3-25
* @version: V1.0  
*/
package com.eb.admin.utils.jqgrid;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eb.admin.utils.jqgrid.exception.TreeUtilsException;



/**
* @ClassName: TreeUtils
* @Description: TREE 对象 ：主要功能生成对象初始化所生成的JSON
* @author kevin
* @date 2014-4-9 下午4:12:57
*
*/
@SuppressWarnings({"rawtypes","unchecked","static-access"})
public class TreeUtils {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory.getLogger(TreeUtils.class);	
	
	/**
	 * @author: kevin
	 * @date: 2014-4-9 下午4:16:51
	 * @Description: 返回fuelux.tree的JSON
	 * @param options
	 * @return String
	 */
	public static String getTree2String(TreeOptions options) throws TreeUtilsException{
		//处理异常情况
		if (StringUtils.isBlank(options.getId())
				|| StringUtils.isBlank(options.getText())
				|| StringUtils.isBlank(options.getNodeLevel())
				|| StringUtils.isBlank(options.getParentId())
				|| StringUtils.isBlank(options.getHasChild())
				){
			throw new TreeUtilsException("请确保 map 中有 id,text,hasChild,nodeLevel,parentId 参数");
		}
		StringBuffer treeJson = new StringBuffer("");
		LinkedMap rootMap = buildTree(options);
		treeJson.append("{");
		for (int i = 0 ; i < rootMap.size(); i++) {
			JSONObject jsonObj = new JSONObject();
			String nodeKey = (String) rootMap.get(i);
			jsonObj.put(nodeKey, rootMap.get(nodeKey));
			
			if (i != 0){
				treeJson.append(",");
			}
			String json = jsonObj.toJSONString();
			json = json.substring(1,json.length() - 1);
			treeJson.append(json);
		}
		treeJson.append("}");
		
		if (!options.isShowRoot()){
			return treeJson.toString();
		}else{
			String rsStr = "{\"all\" : {\"name\": \"全部\", \"type\": \"folder\",\"additionalParameters\":{\"children\":"
					+ treeJson.toString() + "}}}";
			return rsStr;
		}
	}
	
	/**
	 * @author: kevin
	 * @date: 2014-4-9 下午4:20:30
	 * @Description: List中的数据要求，先将所有有子节点的数据放在最上面，然后按级别排序，最后按所要显示的数据排序
	 * @param options
	 * @return LinkedMap
	 * @throws TreeUtilsException 
	 */
	private static LinkedMap buildTree(TreeOptions options) throws TreeUtilsException{
		/*
		 * 1、将所有有子节点的分别放入各级别的MAP中，其实最高级节点放在List中
		 * 2、将子节点放入父节点中。
		 */
		LinkedMap rootMap = new LinkedMap();
		Map<String,Map> allChild = new HashMap<String,Map>();
		for (int i = 0; i < options.getDataList().size(); i++) {
			Object obj = options.getDataList().get(i);
			Map nodeMap = buildNode(obj,options);
			if ("1".equals(nodeMap.get("level")) || nodeMap.get("superId") == null){
				rootMap.put(nodeMap.get("name"), nodeMap);
			}else{
				//1、先将每个节点分别放入Map中所对应的对象中
				Map levelMap = allChild.get(nodeMap.get("level"));
				if (levelMap == null){
					levelMap = new HashMap();
					allChild.put((String) nodeMap.get("level"), levelMap);
				}
				levelMap.put(nodeMap.get("id"), nodeMap);
				if (nodeMap.get("level") == null || "null".equals(nodeMap.get("level"))){
					throw new TreeUtilsException("data error : (name :" + nodeMap.get("name") + ") level is null");
				}
				String superLevel = String.valueOf((Integer.valueOf((String) nodeMap.get("level")) - 1));
				//2、将2级节点加入头节点中
				Set<String> rootKeySet = rootMap.keySet();
				if (!"0".equals(superLevel)){
					if ("1".equals(superLevel)){
						for (String key : rootKeySet) {
							Map superMap = (Map) rootMap.get(key);
							if (superMap.get("id").equals(nodeMap.get("superId"))){
								Map addMap = (Map) superMap.get("additionalParameters");
								LinkedMap link = (LinkedMap) addMap.get("children");
								link.put(nodeMap.get("name"),nodeMap);
							}
						}
					}else{
						Map<String,Map> superLevelMap = allChild.get(superLevel);
						Map superNodeMap = (Map) superLevelMap.get(nodeMap.get("superId"));
						Map addMap = (Map) superNodeMap.get("additionalParameters");
						LinkedMap link = (LinkedMap) addMap.get("children");
						link.put(nodeMap.get("name"),nodeMap);
					}
				}
			}
		}
		
		return rootMap;
	}
	
	/**
	 * @author: kevin
	 * @date: 2014-4-9 下午4:20:46
	 * @Description: for @getTree2String
	 * @param obj
	 * @param options
	 * @return
	 * @throws TreeUtilsException 
	 */
	private static Map buildNode(Object obj,TreeOptions options) throws TreeUtilsException{
		Map node = new HashMap();
		node.put("id", getValue(obj,options.getId()));
		node.put("name", getValue(obj,options.getText()));
		node.put("type", getValue(obj,options.getHasChild()));
		node.put("level", getValue(obj,options.getNodeLevel()));
		node.put("superId", getValue(obj,options.getParentId()));
		String[] fields = options.getExtendedFields();
		for (String str : fields) {
			node.put(str, getValue(obj,str));
		}
		Map child = new HashMap();
		child.put("children", new LinkedMap());
		node.put("additionalParameters", child);
		if ("1".equals(node.get("type"))){
			node.put("type","folder");
		}else{
			node.put("type", "item");
		}
		return node;
	}
	
	/**
	 * @author: kevin
	 * @date: 2014-4-9 下午4:21:15
	 * @Description: 对对象内的属性进行取值，支持BaseEntity和Map
	 * @param obj
	 * @param property
	 * @return String
	 * @throws TreeUtilsException 
	 */
	private static String getValue(Object obj,String property) throws TreeUtilsException{
		if("undefined".equals(property.toLowerCase())){
			return "";
		}
		try {
			if (obj instanceof Map){
				return (String) ((Map) obj).get(property);
			}else{
				Class c = obj.getClass();
				Method m = c.getMethod("get" + toUpperCaseFirstOne(property), new Class[]{});
				String rsValue = String.valueOf(m.invoke(obj, new Object[]{}));
				if (!StringUtils.isBlank(rsValue)){
					if("null".equals(rsValue.toLowerCase())){
						return "";
					}
				}
				return rsValue;
			}
		} catch (Exception e) {
			throw new TreeUtilsException(e.toString());
		}
	}
	
	/**
	 * @author: kevin
	 * @date: 2014-4-9 下午4:21:30
	 * @Description: 首字母转大写
	 * @param s
	 * @return String
	 */
	private static String toUpperCaseFirstOne(String s){
        if(Character.isUpperCase(s.charAt(0))){
            return s;
        }else{
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
        }
	}
	
	/**
	 * @author: chenxiaopei
	 * @date: 2014-4-9 下午4:21:30
	 * @Description: 返回城市下拉列表双级联动的JSON
	 * @param cityData
	 * @param map
	 * @return String
	 * @throws TreeUtilsException 
	 */
	public static String getCity2String(List cityData, Map map) throws TreeUtilsException{
		//处理异常情况
		if (StringUtils.isBlank((String) map.get("id"))
				|| StringUtils.isBlank((String) map.get("name"))
				|| StringUtils.isBlank((String) map.get("superId"))
				|| StringUtils.isBlank((String) map.get("type"))
				){
			throw new TreeUtilsException("请确保 map 中有 id,name,superId,type 参数");
		}
		List rootList = buildCity(cityData,map);
		JSONArray j = new JSONArray();
		String jj = j.toJSONString(rootList);
		
		return jj;
	}
	
	/**
	 * @author: chenxiaopei
	 * @date: 2014-4-03
	 * @Description: List存放省级，list中存放map，map包含其属性和items，然后items中存放该省的城市的list，这个list下是城市的属性map
	 * @param treeData
	 * @param map
	 * @return
	 * @throws TreeUtilsException 
	 */
	private static List buildCity(List<Object> treeData, Map<String,String> map) throws TreeUtilsException{
		/*
		 * 1、将所有有子节点的分别放入各级别的MAP中，其实最高级节点放在List中
		 * 2、将子节点放入父节点中。
		 */
		List rootList = new ArrayList();
		Map<String,Map> allChild = new HashMap<String,Map>();
		for (int i = 0; i < treeData.size(); i++) {
			Object obj = treeData.get(i);
			Map nodeMap = buildCityNode(obj,map);
			if ("1".equals(nodeMap.get("type")) || nodeMap.get("superId") == null){
				rootList.add(nodeMap);
			}else{
				//1、先将每个节点分别放入Map中所对应的对象中
				Map levelMap = allChild.get(nodeMap.get("type"));
				if (levelMap == null){
					levelMap = new HashMap();
					allChild.put((String) nodeMap.get("type"), levelMap);
				}
				levelMap.put(nodeMap.get("id"), nodeMap);
				String superLevel = String.valueOf((Integer.valueOf((String) nodeMap.get("type")) - 1));
				//2、将2级节点加入头节点中
				
				if (!"0".equals(superLevel)){
					if ("1".equals(superLevel)){
						for (int j=0;j<rootList.size();j++) {
							Map superMap = (Map) rootList.get(j);
							if (superMap.get("id").equals(nodeMap.get("superId"))){
								List link = (List) superMap.get("items");
								if(link == null){
									link = new ArrayList();
									superMap.put("items", link);
								}
								nodeMap.remove("items");
								link.add(nodeMap);
							}
						}
					}
				}
			}
		}
		
		return rootList;
	}
	
	/**
	 * @author: chenxiaopei
	 * @date: 2014-4-9 下午4:22:31
	 * @Description: for @getCity2String
	 * @param obj
	 * @param map
	 * @return
	 * @throws TreeUtilsException 
	 */
	private static Map buildCityNode(Object obj, Map<String,String> map) throws TreeUtilsException{
		Map node = new HashMap();
		Set<String> keySet = map.keySet();
		for (String key : keySet) {
			node.put(key, getValue(obj,map.get(key)));
		}
		node.put("items", new ArrayList());
		return node;
	}

	/**
	 * @author: kevin
	 * @date: 2014-4-9 下午4:23:19
	 * @Description: 返回JQgrid用的树型JSON
	 * @param data
	 * @param map
	 * @return
	 * @throws TreeUtilsException 
	 */
	public static List getListForJqgridTree(TreeOptions options) throws TreeUtilsException{
		if (StringUtils.isBlank(options.getId())
				|| StringUtils.isBlank(options.getText())
				|| StringUtils.isBlank(options.getNodeLevel())
				|| StringUtils.isBlank(options.getParentId())
				|| StringUtils.isBlank(options.getHasChild())
				){
			throw new TreeUtilsException("请确保 map 中有 id,text,hasChild,nodeLevel,parentId 参数");
		}
		List<Map<String,Object>> rootList = new ArrayList<Map<String,Object>>();
		Map<String,List<Map<String,Object>>> childMap = new HashMap<String,List<Map<String,Object>>>();
		for (Object obj : options.getDataList()) {
			Map<String,Object> nodeMap = new HashMap<String,Object>();
			nodeMap.put("id", getValue(obj,options.getId()));
			nodeMap.put("text", getValue(obj,options.getText()));
			
			String nodeLevel = getValue(obj,options.getNodeLevel());
			if (StringUtils.isBlank(nodeLevel)){
				nodeMap.put("level", "0");
			}else{
				nodeMap.put("level", nodeLevel);
			}
			
			String parentId = getValue(obj,options.getParentId());
			if (StringUtils.isBlank(parentId)){
				nodeMap.put("parent", "");
			}else{
				nodeMap.put("parent", parentId);
			}
			
			String hasChild = getValue(obj,options.getHasChild());
			if ("1".equals(hasChild)){
				nodeMap.put("isLeaf", false);
			}else{
				nodeMap.put("isLeaf", true);
			}

			String[] fields = options.getExtendedFields();
			if (fields != null && fields.length > 0){
				for (String str : fields) {
					nodeMap.put(str, getValue(obj,str));
				}
			}
			
			if (options.isOpenAll()){
				nodeMap.put("expanded", true);
			}else{
				nodeMap.put("expanded", false);
			}
			nodeMap.put("loaded", true);
			
			if (StringUtils.isBlank((String) nodeMap.get("parent"))){
				//添加根节点
				if (options.isShowRoot()){
					nodeMap.put("parent", "0");
				}else{
					nodeMap.put("parent", "");
				}
				rootList.add(nodeMap);
			}else{
				List<Map<String,Object>> childList = childMap.get(nodeMap.get("parent"));
				if (childList == null){
					childList = new ArrayList<Map<String,Object>>();
					childMap.put((String)nodeMap.get("parent"), childList);
				}
				childList.add(nodeMap);
			}
		}
		
		List<Map<String,Object>> rsList = new ArrayList<Map<String,Object>>();
		//添加根节点
		if (options.isShowRoot()){
			Map<String,Object> fristMap = new HashMap<String,Object>();
			fristMap.put("id","0");
			fristMap.put("parent","");
			if (StringUtils.isBlank(options.getRootText())){
				fristMap.put("text","根节点");
			}else{
				fristMap.put("text",options.getRootText());
			}
			fristMap.put("level","0");
			fristMap.put("isLeaf",false);
			if (options.isOpenAll()){
				fristMap.put("expanded", true);
			}else{
				fristMap.put("expanded", false);
			}
			fristMap.put("loaded",true);
			rsList.add(fristMap);
		}
		
		for (Map<String, Object> rootMap : rootList) {
			rsList.add(rootMap);
			if (!(Boolean) rootMap.get("isLeaf")){
				rsList = putJQgridTree(rsList, (String)rootMap.get("id"), childMap);
			}
		}
		
		return rsList;
	}
	
	/**
	 * @author: kevin
	 * @date: 2014-4-9 下午4:23:28
	 * @Description: 整理出jqgrid 所要的结构
	 * @param rsList
	 * @param parentId
	 * @param childMap
	 * @return
	 */
	private static List<Map<String,Object>> putJQgridTree(List<Map<String,Object>> rsList, String parentId, Map<String,List<Map<String,Object>>> childMap){
		List<Map<String,Object>> putList = childMap.get(parentId);
		for (Map<String, Object> map : putList) {
			rsList.add(map);
			if (!(Boolean) map.get("isLeaf")){
				rsList = putJQgridTree(rsList, (String)map.get("id"), childMap);
			}
		}
		return rsList;
	}
	
	/**
	 * @author: kevin
	 * @date: 2014-4-9 下午4:23:19
	 * @Description: 返回jstree用的树型JSON
	 * @param data
	 * @param map
	 * @return
	 * @throws TreeUtilsException 
	 */
	public static List getListForJsTree(TreeOptions options) throws TreeUtilsException{
		//处理异常情况
		if (StringUtils.isBlank(options.getId())
				|| StringUtils.isBlank(options.getText())
				|| StringUtils.isBlank(options.getNodeLevel())
				|| StringUtils.isBlank(options.getParentId())
				|| StringUtils.isBlank(options.getHasChild())
				){
			throw new TreeUtilsException("请确保 map 中有 id,text,hasChild,nodeLevel,parentId 参数");
		}
		List<Map<String,Object>> rootList = new ArrayList<Map<String,Object>>();
		Map<String,List<Map<String,Object>>> childMap = new HashMap<String,List<Map<String,Object>>>();
		for (Object obj : options.getDataList()) {
			Map<String,Object> nodeMap = new HashMap<String,Object>();
			nodeMap.put("id", getValue(obj,options.getId()));
			nodeMap.put("text", getValue(obj,options.getText()));
			
			String nodeLevel = getValue(obj,options.getNodeLevel());
			if (StringUtils.isBlank(nodeLevel)){
				nodeMap.put("level", "0");
			}else{
				nodeMap.put("level", nodeLevel);
			}
			
			String parentId = getValue(obj,options.getParentId());
			if (StringUtils.isBlank(parentId)){
				nodeMap.put("parent", "");
			}else{
				nodeMap.put("parent", parentId);
			}
			
			String hasChild = getValue(obj,options.getHasChild());
			if ("1".equals(hasChild) && options.isOpenAll()){
				Map stateMap = new HashMap();
				stateMap.put("opened",true);
				nodeMap.put("state", stateMap);
			}
			nodeMap.put("hasChild", hasChild);

			String[] fields = options.getExtendedFields();
			if (fields != null && fields.length > 0){
				for (String str : fields) {
					nodeMap.put(str, getValue(obj,str));
				}
			}
			
			if (StringUtils.isBlank((String) nodeMap.get("parent"))){
				//判断是否要显示根节点
				if (options.isShowRoot()){
					nodeMap.put("parent", "0");
				}else{
					nodeMap.put("parent", "#");
				}
				rootList.add(nodeMap);
			}else{
				List<Map<String,Object>> childList = childMap.get(nodeMap.get("parent"));
				if (childList == null){
					childList = new ArrayList<Map<String,Object>>();
					childMap.put((String)nodeMap.get("parent"), childList);
				}
				childList.add(nodeMap);
			}
		}
		
		List<Map<String,Object>> rsList = new ArrayList<Map<String,Object>>();
		//判断是否要显示根节点
		if (options.isShowRoot()){
			Map<String,Object> fristMap = new HashMap<String,Object>();
			fristMap.put("id","0");
			fristMap.put("parent","#");
			if (StringUtils.isBlank(options.getRootText())){
				fristMap.put("text","根节点");
			}else{
				fristMap.put("text",options.getRootText());
			}
			fristMap.put("level","0");
			if(options.isOpenAll()){
				Map stateMap = new HashMap();
				stateMap.put("opened",true);
				fristMap.put("state", stateMap);
			}
			rsList.add(fristMap);
		}
		
		for (Map<String, Object> rootMap : rootList) {
			rsList.add(rootMap);
			if ("1".equals(rootMap.get("hasChild"))){
				rsList = putJsTree(rsList, (String)rootMap.get("id"), childMap);
			}
		}
		
		return rsList;
	}
	
	/**
	 * @author: kevin
	 * @date: 2014-4-9 下午4:23:28
	 * @Description: 整理出JsTree 所要的结构
	 * @param rsList
	 * @param parentId
	 * @param childMap
	 * @return
	 */
	private static List<Map<String,Object>> putJsTree(
			List<Map<String,Object>> rsList,
			String parentId,
			Map<String,List<Map<String,Object>>> childMap){
		List<Map<String,Object>> putList = childMap.get(parentId);
		for (Map<String, Object> map : putList) {
			rsList.add(map);
			if ("1".equals(map.get("hasChild"))){
				rsList = putJsTree(rsList, (String)map.get("id"), childMap);
			}
		}
		return rsList;
	}
}

