package apiSearch.intermediate;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;

public class Var {

	int dec;
	String code;
	ASTNode node;
	Map<Integer, String> lineToCode; //lineNumber -> Code
	
	public Var() {
		// TODO Auto-generated constructor stub
		lineToCode = new HashMap<Integer, String>();
	}
	public void setNode(ASTNode node) {
		this.node = node;
	}
	public ASTNode getNode() {
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
		for (Map.Entry<Integer, String> entry : lineToCode.entrySet()) {  
			methodinvoke += "	line" + entry.getKey() + ": " + entry.getValue() + "\n";
		}
		return define + methodinvoke;
	}
	

}
