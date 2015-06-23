package apiSearch.tool;

import java.io.File;
import java.util.ArrayList;

import apiSearch.intermediate.InterRep;
import apiSearch.intermediate.Var;
import apiSearch.parser.JDT;
import apiSearch.search.JDTSearch;

public class JavaStrategy extends Strategy {

	public JavaStrategy(Input in) {
		super(in);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void strategy(String extension) {
		// TODO Auto-generated method stub
		projects = findProject(extension);
		for (int i = 0; i < projects.size(); i++) {
			Project now = projects.get(i);
			for (int j = 0; j < now.codes.size(); j++) {
				String codePath = now.codes.get(j);
				
				JDT jdt = (JDT)parser;
				jdt.SetFile(codePath);
				jdt.setSrc(now.path);
				jdt.parse();				
			
				JDTSearch jdtsearch = (JDTSearch)search;
				jdtsearch.setAPI(in.api);
				
				System.out.println("--------search begin------------");
				System.out.println("search: " + codePath);
				
				inter.setData(jdtsearch.search(jdt.getRoot(), true));
				now.result.add(inter);
				
				for (Var var : inter.getData()) {
					System.out.println("----------");
					System.out.println(var.toString());
				}
				
				System.out.println("--------search end------------");
				
			}
			out.output(now);
			projects.set(i, now);
		}
	}

	private ArrayList<Project> findProject(String extension) {
		// TODO Auto-generated method stub
		File codehouse = new File(in.path);
		File[] projectDirectories = codehouse.listFiles();
		ArrayList<Project> projects = new ArrayList<Project>();
		
		for (File project : projectDirectories) {
			if (project.isDirectory()) {
				String projectName = project.getName();
				String projectPath = project.getAbsolutePath();
				System.out.println("---------------");
				System.out.println(projectName);
				Project now = new Project(projectName, projectPath, extension);
				projects.add(now);
			}
		}
		
		return projects;
	}

}
