package strategy;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import apiSearch.intermediate.FileIO;
import apiSearch.output.Output;
import apiSearch.output.Simple;
import apiSearch.parser.JDT;
import apiSearch.parser.Parser;
import apiSearch.search.JDTSearch;
import apiSearch.search.Search;
import apiSearch.tool.Flag;
import apiSearch.tool.Input;
import apiSearch.tool.Project;

/**
 * 根据工具中读取的输入，存储并设置相应的apiSearch策略
 * @author barry
 *
 */

public abstract class Strategy {
	
	Input in;
	Output out;
	
	FileIO interIO;
	Search search;
	Parser parser;
	
	Set<String> validParser;
	Set<String> validSearch;
	Set<String> validInter;
	Set<String> validOutput;
	
	public ArrayList<Project> projects;
	
	String readPath;

	public Strategy(Input in) {
		// TODO Auto-generated constructor stub
		this.in = in;
		setvalidParameter();
		setStrategy();
		projects = new ArrayList<Project>();
		readPath = in.savaPath;
		mkSaveDir();
	}

	private void mkSaveDir() {
		// TODO Auto-generated method stub
		File save = new File(in.savaPath);
		if (!save.exists()) {
			save.mkdirs();
		}
	}

	private void setvalidParameter() {
		// TODO Auto-generated method stub
		setvalidPaser();
		setvalidSearch();
		setvalidInter();
		setvalidOutput();
		
	}

	private void setDebug() {
		// TODO Auto-generated method stub
		if (in.debug) {
			Flag.debug = true;
		}
		else {
			Flag.debug = false;
		}
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
		setDebug();
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
			interIO = new FileIO();
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
	
	public ArrayList<Project> findProject(String extension) {
		// TODO Auto-generated method stub
		File codehouse = new File(in.path);
		File[] projectDirectories = codehouse.listFiles();
		ArrayList<Project> projects = new ArrayList<Project>();
		
		for (File project : projectDirectories) {
			if (project.isDirectory()) {
				String projectName = project.getName();
				String projectPath = project.getAbsolutePath();
				if (in.debug) {
					System.out.println("---------------");
					System.out.println(projectName);					
				}
				Project now = new Project(projectName, projectPath, extension);
				projects.add(now);
			}
		}
		
		return projects;
	}
	
	
	public abstract void strategy(String extension);
	public abstract void createSearch(String extension);
	public abstract void readSearch();
	public abstract void DoSomething();

}
