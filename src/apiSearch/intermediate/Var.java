package apiSearch.intermediate;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;


/**
 * 用于保存具体的API信息的类，每个Var类保存了一个变量和其对应的全部API调用情况
 * @author barry
 *
 */
public class Var implements Serializable{

	int dec;
	String code;
	
	transient Object node;
	public Map<Integer, String> lineToCode; //lineNumber -> Code
	public Map<Integer, String> lineToParent;
	
	public Map<String, Set<Integer>> apiToLine; // api name->line number
	public Map<String, String> apiToTypes;// superclass/interface api name -> api name 用于反查待搜索API的子类的情况
 	
	public Var() {
		// TODO Auto-generated constructor stub
		lineToCode = new HashMap<Integer, String>();
		lineToParent = new HashMap<>();
		apiToLine = new HashMap<>();
		apiToTypes = new HashMap<>();
	}
	public void setNode(Object node) {
		this.node = node;
	}
	public Object getNode() {
		return this.node;
	}
	public void setDefinition(int dec, String code) {
		this.dec = dec;
		this.code = code;
	}
	public void addMap(int pos, String code) {
		lineToCode.put(pos, code);
	}
	public void addAPI(String api, int pos) {
		if (apiToLine.containsKey(api)) {
			apiToLine.get(api).add(pos);
		}
		else {
			Set<Integer> tmp = new HashSet<>();
			tmp.add(pos);
			apiToLine.put(api, tmp);
		}
		
	}
	public boolean hasAPI(String api) {
		if (apiToLine.containsKey(api) || apiToTypes.containsKey(api)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 输出全部信息
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder s = new StringBuilder();
		
//		return s.toString();
		
		if (lineToCode.isEmpty()) {
			return "";
		}
		
		String define = "line" + dec + ": " + code + "\n";
		
		
		Map<Integer, String> treeMap = new TreeMap<Integer, String>();
		
		treeMap.putAll(lineToCode);
		
//		StringBuilder s = new StringBuilder();
		
		for (Map.Entry<Integer, String> entry : treeMap.entrySet()) {  
			s.append("	line").append(entry.getKey()).append(": ").append(entry.getValue()).append(lineToParent.get(entry.getKey())).append("\n");
		}
		
		s.append("-------apiToLine-------\n");
		
		for (Entry<String, Set<Integer>> entry : apiToLine.entrySet()) { 
			s.append(entry.getKey() + "\n");
		}
		
		s.append("-------apiToTypes-------\n");
		
		for (Entry<String, String> entry : apiToTypes.entrySet()) { 
			s.append(entry.getKey() + " " + entry.getValue() +"\n");	
		}
		
		return define + s;
	}
	
	/**
	 * 传递参数api用于过滤，得到指定api的使用情况
	 * @param api
	 * @return
	 */
	public String toString(String api) {
		if (lineToCode.isEmpty()) {
			return "";
		}
		
		String define = "line" + dec + ": " + code + "\n";
		
		Map<Integer, String> treeMap = new TreeMap<Integer, String>();
		
		treeMap.putAll(lineToCode);
		
		StringBuilder s = new StringBuilder();
		
		for (Map.Entry<Integer, String> entry : treeMap.entrySet()) {  
			if ((apiToLine.containsKey(api) && apiToLine.get(api).contains(entry.getKey()) || (apiToLine.containsKey(apiToTypes.get(api)) && apiToLine.get(apiToTypes.get(api)).contains(entry.getKey())))) {
				s.append("	line").append(entry.getKey()).append(": ").append(entry.getValue()).append(lineToParent.get(entry.getKey())).append("\n");
			}
			
		}
		return define + s;
	}
	public void sort() {
		// TODO Auto-generated method stub
		
	}
	public void addType(String type, String name) {
		// TODO Auto-generated method stub
		apiToTypes.put(type, name);
	}
	

}
