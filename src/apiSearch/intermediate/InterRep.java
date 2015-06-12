package apiSearch.intermediate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


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
	ArrayList<Var> data;
	public InterRep() {
		// TODO Auto-generated constructor stub
		data = new ArrayList<Var>();
	}
	
	public abstract void read();
	public abstract void write();
	public ArrayList<Var> getData() {
		return data;
	}
	public void setData(ArrayList<Var> data) {
		this.data = data;
	}
}

