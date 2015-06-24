package apiSearch.api;

import java.io.File;
import java.util.ArrayList;

import apiSearch.staticClass.Flag;



/**
 * 用于寻找JDK官方API集合的类
 * @author barry
 *
 */
public class JDK extends Official{
	
	String path;
	
	public JDK() {
		// TODO Auto-generated constructor stub
		this.path = "./java/src";
		findOfficialAPI();
	}
	

	public JDK(String path) {
		// TODO Auto-generated constructor stub
		this.path = path;
		findOfficialAPI();
	}
	
	public void readTop() {
		File f = new File(path);
		File[] files = f.listFiles();
		if (files != null) {
			for(File file : files) {
				if (file.isDirectory()) {
					findPackage(file.getAbsolutePath(), file.getName());
				}
			}
		}
		if(Flag.debug) {
			System.out.println(toString());
		}
	}

	private void findPackage(String path, String packageName) {
		// TODO Auto-generated method stub
		File f = new File(path);
		File[] files = f.listFiles();
		if (files != null) {
			for(File file : files) {
				if (file.isDirectory()) {
					String nextPackageName = packageName + "." + file.getName();
					apis.add(nextPackageName);
					findPackage(file.getAbsolutePath(), nextPackageName);
					
				}
			}
		}
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String result = "JDK API listing:\n";
		for(String pacakgeName : apis) {
			result += pacakgeName + "\n";
		}
		return result;
	}
	
	public boolean isInJDK(String api) {
		
		String[] line = api.split("\\.");
		ArrayList<String> names = new ArrayList<>();
		for (int i = 0; i < line.length; i++) {
			names.add(line[i]);
		}
		String name = names.get(names.size() - 1);
		String type = "";
		for (int i = 0; i < names.size() - 2; i++) {
			type += names.get(i) + ".";
		}
		type += names.get(names.size() - 2);
		
		if (apis.contains(type)) {
			return true;
		}
		else if (type.contains(".")){
			return isInJDK(type);
		}

		return false;
	}


	@Override
	public void findOfficialAPI() {
		// TODO Auto-generated method stub
		readTop();
	}


	@Override
	public boolean isInOfficialAPI(String api) {
		// TODO Auto-generated method stub
		return isInJDK(api);
	}

}
