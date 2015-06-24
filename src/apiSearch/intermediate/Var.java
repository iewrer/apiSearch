package apiSearch.intermediate;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 用于保存具体的API信息的类，每个Var类保存了一个变量和其对应的API调用情况
 * @author barry
 *
 */
public class Var implements Serializable{

	int dec;
	String code;
	transient Object node;
	public Map<Integer, String> lineToCode; //lineNumber -> Code
	
	public Var() {
		// TODO Auto-generated constructor stub
		lineToCode = new HashMap<Integer, String>();
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
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		if (lineToCode.isEmpty()) {
			return "";
		}
		String define = "line" + dec + ": " + code + "\n";
		String methodinvoke = "";
		
		Map<Integer, String> treeMap = new TreeMap<Integer, String>();
		
		treeMap.putAll(lineToCode);
		
		for (Map.Entry<Integer, String> entry : treeMap.entrySet()) {  
			methodinvoke += "	line" + entry.getKey() + ": " + entry.getValue() + "\n";
		}
		return define + methodinvoke;
	}
	public void sort() {
		// TODO Auto-generated method stub
		
	}
	

}
