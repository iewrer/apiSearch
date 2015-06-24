package apiSearch.search;

import java.util.ArrayList;

import apiSearch.intermediate.Var;

public abstract class Search {

	String type;
	String name;
	
	public Search() {
		// TODO Auto-generated constructor stub
		type = new String();
		name = new String();
	}
	
	//拆分API = ClassType + MethodName
	public void setAPI(String api) {
		String[] line = api.split("\\.");
		ArrayList<String> names = new ArrayList<>();
		for (int i = 0; i < line.length; i++) {
			names.add(line[i]);
		}
		name = names.get(names.size() - 1);
		for (int i = 0; i < names.size() - 2; i++) {
			type += names.get(i) + ".";
		}
		type += names.get(names.size() - 2);
		
	}
	
	public String getType() {
		return type;
	}
	public abstract ArrayList<Var> search(Object root, boolean debug);

}
