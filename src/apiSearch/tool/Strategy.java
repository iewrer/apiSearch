package apiSearch.tool;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import apiSearch.intermediate.InterRep;
import apiSearch.output.Output;
import apiSearch.output.Simple;
import apiSearch.parser.JDT;
import apiSearch.parser.Parser;
import apiSearch.search.JDTSearch;
import apiSearch.search.Search;

/**
 * 根据工具中读取的输入，存储并设置相应的apiSearch策略
 * @author barry
 *
 */

public abstract class Strategy {
	
	Input in;
	Output out;
	
	InterRep inter;
	Search search;
	Parser parser;
	
	Set<String> validParser;
	Set<String> validSearch;
	Set<String> validInter;
	Set<String> validOutput;
	
	ArrayList<Project> projects;

	public Strategy(Input in) {
		// TODO Auto-generated constructor stub
		this.in = in;
		setvalidParameter();
		setStrategy();
		projects = new ArrayList<Project>();
	}

	private void setvalidParameter() {
		// TODO Auto-generated method stub
		setvalidPaser();
		setvalidSearch();
		setvalidInter();
		setvalidOutput();
	}

	private void setvalidOutput() {
		// TODO Auto-generated method stub
		validOutput = new HashSet<>();
		validOutput.add("simple");
	}

	private void setvalidInter() {
		// TODO Auto-generated method stub
		validInter = new HashSet<>();
		validInter.add("default");
	}

	private void setvalidSearch() {
		// TODO Auto-generated method stub
		validSearch = new HashSet<>();
		validSearch.add("jdt");
	}

	private void setvalidPaser() {
		// TODO Auto-generated method stub
		validParser = new HashSet<>();
		validParser.add("jdt");
	}

	private void setStrategy() {
		// TODO Auto-generated method stub
		setInter();
		setSearch();
		setInter();
		setOutput();
		setParser();
	}

	private void setParser() {
		// TODO Auto-generated method stub
		if (validParser.contains(in.parser)) {
			parser = new JDT(in.language);
		}
		else {
			try {
				throw new Exception("no such parser");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void setOutput() {
		// TODO Auto-generated method stub
		if (validOutput.contains(in.output)) {
			out = new Simple();
		}
		else {
			try {
				throw new Exception("no such output format");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}

	private void setSearch() {
		// TODO Auto-generated method stub
		if (validSearch.contains(in.search)) {
			search = new JDTSearch();
		}
		else {
			try {
				throw new Exception("no such search algo");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void setInter() {
		// TODO Auto-generated method stub
		if (validInter.contains(in.inter) || in.inter.isEmpty()) {
			inter = new InterRep();
		}
		else {
			try {
				throw new Exception("no such intermediate representation");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public abstract void strategy(String extension);

}
