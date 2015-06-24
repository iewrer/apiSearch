package apiSearch.tool;

import java.io.File;
import java.io.IOException;

import apiSearch.intermediate.FileIO;
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

		if (in.create) {
			createSearch(extension);
		}		
		else {
			readSearch();
		}

	}

	public void readSearch() {
		// TODO Auto-generated method stub
		File results = new File(readPath);
		File[] files = results.listFiles();
		if (files != null) {
			
			System.out.println("-------------intermediate data reading---------------");
			for(File project : files) {
				
				if (project.getName().startsWith(".")) {
					continue;
				}

				FileIO reader = new FileIO(project.getAbsolutePath());
				
				try {
					Project now = reader.read();
					projects.add(now);
					out.output(now);
				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
			}
		}
		DoSomething();
	}

	//读取上次搜索的结果后可以继续做一些事儿...
	public void DoSomething() {
		// TODO Auto-generated method stub
		
	}

	public void createSearch(String extension) {
		// TODO Auto-generated method stub
		projects = findProject(extension);
		JDT jdt = (JDT)parser;
		JDTSearch searcher = (JDTSearch)search;
		searcher.setAPI(in.api);
		
		
		for (int i = 0; i < projects.size(); i++) {
			Project now = projects.get(i);
	
			jdt.setSrc(now.path);		
			
			for (int j = 0; j < now.codes.size(); j++) {
				String codePath = now.codes.get(j);
				
				jdt.SetFile(codePath);
				jdt.parse();				
			
				if (Flag.debug) {
					System.out.println("--------search begin------------");
					System.out.println("search: " + codePath);
				}
				
				InterRep inter = new InterRep();
				inter.setData(searcher.search(jdt.getRoot(), false));
				
				boolean empty = true;
				
				for (Var var : inter.getData()) {
					if (!var.lineToCode.isEmpty()) {
						empty = false;
					}
				}
				
				if (!empty) {
					now.result.put(codePath, inter);
				}
				
				if (Flag.debug) {
					System.out.println("--------search end------------");
				}
				
				
			}
			out.output(now);
			projects.set(i, now);
		}	
	}



}
