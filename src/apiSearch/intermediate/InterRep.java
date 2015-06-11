package apiSearch.intermediate;

import java.util.Map;

/**
 * 中间表示形式的基类，设计为抽象类，必须使用具体的子类来实现之。
 * 该类中存储了中间表示形式所需要的信息。
 * @author barry
 *
 */
public abstract class InterRep {
	String path; //中间表示形式的文件保存位置
	
	String project;
	String file;
	Map<Integer, String> lineToCode; //lineNumber -> Code
	public InterRep() {
		// TODO Auto-generated constructor stub
	}
	
	public abstract void read();
	public abstract void write();

}
