package strategy;

import java.io.File;
import java.io.IOException;


import apiSearch.intermediate.FileIO;
import apiSearch.intermediate.InterRep;
import apiSearch.intermediate.Var;
import apiSearch.parser.JDT;
import apiSearch.search.JDTSearch;
import apiSearch.staticClass.Flag;
import apiSearch.tool.Input;
import apiSearch.tool.Project;

public class JavaStrategy extends Strategy {

	long findprojectBegin;
	
	public JavaStrategy(Input in) {
		super(in);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void strategy(String extension) {
		// TODO Auto-generated method stub

		if (in.create) {
			System.out.println("-------------create search---------------");
			createSearch(extension);
		}		
		else {
			System.out.println("-------------read search---------------");
			readSearch();
		}

	}

	public void readSearch() {
		// TODO Auto-generated method stub
		findprojectBegin = System.currentTimeMillis();
		
		File results = new File(readPath);
		File[] files = results.listFiles();
		if (files != null && files.length > 1) {
			
			System.out.println("-------------intermediate data reading---------------");
			for(File project : files) {
				
				if (project.getName().startsWith(".")) {
					continue;
				}

				FileIO reader = new FileIO(project.getAbsolutePath());
				
				try {
					Project now = reader.read();
					projects.add(now);
//					out.output(now);
				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
			}
		}
		
		long findprojectEnd = System.currentTimeMillis();
//		
		System.out.println("project find last: " + (findprojectEnd - findprojectBegin));
		searchAPI();
		
		DoSomething();
	}

	//读取上次搜索的结果后可以继续做一些事儿...
	public void DoSomething() {
		// TODO Auto-generated method stub
		searchAPI();
	}

	public void createSearch(String extension) {
		// TODO Auto-generated method stub
		
		findprojectBegin = System.currentTimeMillis();
		
		
//		System.out.println("-------begin to find project-----");
		projects = findProject(extension);
//		System.out.println("-------end to find project-----");
//		
		long findprojectEnd = System.currentTimeMillis();
//		
		System.out.println("project find last: " + (findprojectEnd - findprojectBegin));
		parse();
	}

	private void searchAPI() {
		// TODO Auto-generated method stub
		for (int i = 0; i < projects.size(); i++) {
			Project now = projects.get(i);
			out.output(now);
		}
	}

	private void parse() {
		// TODO Auto-generated method stub
		JDT jdt = (JDT)parser;
		JDTSearch searcher = (JDTSearch)search;
		searcher.setAPI(in.api);
		
		int count = 0;
		
		
		for (int i = 0; i < projects.size(); i++) {
			Project now = projects.get(i);
	
			jdt.setSrc(now.path);		
			
//			System.out.println("-----project time test begin-----");
//			
//			long projectBegin = System.currentTimeMillis();
			
			for (int j = 0; j < now.codes.size(); j++) {
						
				String codePath = now.codes.get(j);
				
				jdt.SetFile(codePath);
				
				long parseBegin = System.currentTimeMillis();
				
				jdt.parse(searcher.name);	
				

				
				long parseEnd = System.currentTimeMillis();	
//				System.out.println("parse last: " + (parseEnd - parseBegin) + "Millis");
			
				if (Flag.debug) {
					System.out.println("--------search begin------------");
					System.out.println("search: " + codePath);
				}
				
				InterRep inter = new InterRep();
				
				long searchBegin = System.currentTimeMillis();
				
				if (!jdt.isEmpty()) {
					
					count++;
					
					inter.setData(searcher.search(jdt.getRoot()));
					
					boolean empty = true;
					
					for (Var var : inter.getData()) {
						if (!var.lineToCode.isEmpty()) {
							empty = false;
						}
					}
					
					if (!empty) {
						now.result.put(codePath, inter);
					}
				}	
				
				long searchEnd = System.currentTimeMillis();
//				
//				System.out.println("search last: " + (searchEnd - searchBegin) + "Millis");
				
				
				if (Flag.debug) {
					System.out.println("--------search end------------");
				}
				
				
			}
//			out.output(now);
			projects.set(i, now);
//			long last = System.currentTimeMillis() - projectBegin;
//			System.out.println("!!!this project use: " + last + " Millis!!!");
//			System.out.println("-----project time test end-----");
		}	
		System.out.println("------------all time counting---------\n"
				+ "all time using: " + (System.currentTimeMillis() - findprojectBegin) + " Millis");
		System.out.println("search files: " + count);
	}



}
