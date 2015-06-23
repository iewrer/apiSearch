package apiSearch.tool;

import java.io.File;
import java.util.ArrayList;

import org.osgi.framework.hooks.service.FindHook;

import apiSearch.intermediate.InterRep;

public class Project {
	
	public String name;
	public String path;
	
	String extension;
	
	ArrayList<String> codes;
	public ArrayList<InterRep> result;

	public Project(String name, String path, String extension) {
		// TODO Auto-generated constructor stub
		this.name = name;
		this.path = path;
		this.extension = extension;
		codes = new ArrayList<>();
		result = new ArrayList<>();
		findAll();
	}
	public void find(String path) {
		File project = new File(path);
		File[] files = project.listFiles();
		
		for (File f : files) {
			if (f.isDirectory()) {
				find(f.getAbsolutePath());
			}
			else if (f.getName().endsWith(extension)) {
				codes.add(f.getAbsolutePath());
				System.out.println("find file: " + f.getAbsolutePath());
			}
		}
	}

	public void findAll() {
		find(path);
	}
}
