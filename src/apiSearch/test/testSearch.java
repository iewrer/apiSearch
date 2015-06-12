package apiSearch.test;

import apiSearch.intermediate.JSON;
import apiSearch.intermediate.Var;
import apiSearch.parser.JDT;
import apiSearch.search.JDTSearch;

public class testSearch {

	public testSearch() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String path = "./codehouse/Orienteering.java";
		
		JDT jdt = new JDT("java");
		jdt.SetFile(path);
		jdt.parse();
		
		
		JDTSearch search = new JDTSearch();
//		search.setAPI("org.eclipse.jdt.core.dom.ASTParser.setSource");
		search.setAPI("java.util.Collection.size");
		
		JSON json = new JSON();
		
		json.setData(search.search(jdt.getRoot(), false));
		
		for (Var i : json.getData()) {
			System.out.println("----------");
			System.out.println(i.toString());
		}
		
	}

}
