package apiSearch.tool;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import apiSearch.intermediate.InterRep;
import apiSearch.intermediate.Var;


/**
 * 用于存储单个项目的信息，包括：
 * 	1.项目地址
 * 	2.所有代码文件地址
 * 	3.API搜索结果
 * @author barry
 *
 */
public class Project implements Serializable{
	
	public String name;
	public String path;
	
	String extension;
	
	public ArrayList<String> codes;
	public Map<String, InterRep> result;

	public Project(String name, String path, String extension) {
		// TODO Auto-generated constructor stub
		this.name = name;
		this.path = path;
		this.extension = extension;
		codes = new ArrayList<>();
		result = new HashMap();
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
//				System.out.println("find file: " + f.getAbsolutePath());
			}
		}
	}

	public void findAll() {
		find(path);
	}
	public boolean isEmpty() {
		Iterator iterator = result.entrySet().iterator();
		boolean isEmpty = true;
		while (iterator.hasNext()) {
			Entry entry = (Entry) iterator.next();
			String file = (String)entry.getKey();
			InterRep inter = (InterRep)entry.getValue();
			
			
			if (inter.getData().isEmpty()) {
				continue;
			}
			
			for (Var j : inter.getData()) {
				if (!j.toString().isEmpty()) {
					isEmpty = false;
					break;
				}		
			}
			
			if (!isEmpty) {
				break;
			}
		}
		return isEmpty;
	}
}
