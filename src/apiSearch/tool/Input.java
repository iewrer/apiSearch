package apiSearch.tool;

import java.io.File;


/**
 * 读取输入参数
 * @author barry
 *
 */
public class Input {
	
	public boolean create;//重新创建搜索还是从中间表示形式中读取
	public String inter;
	public String output;
	public String search;
	public String parser;
	
	public String language;
	public String api;
	
	public String path;//整个代码库的存放位置
	public String savaPath;//整个结果的保存位置
	public String jdkPath;//jdk源代码路径，用于区分是否官方
	
	public boolean debug;
	public boolean delete;

	public Input() {
		// TODO Auto-generated constructor stub
		create = true;
		debug = false;
		delete = false;
		inter = new String();
		output = new String();
		search = new String();
		path = new String();
		parser = new String();
		language = new String();
		api = new String();
		savaPath = new String();
		jdkPath = new String();
	}
	
	public void read(String[] args) throws Exception {
		
		for (int i = 0; i < args.length; i++) {
			String now = args[i];
			if (now.startsWith("-read")) {
				create = false;
			}
			else if (now.startsWith("-intermediate")) {		
				if (i + 1 < args.length && !args[i + 1].startsWith("-")) {
					inter = args[i + 1];
					i++;
				}
				else {
					inter = "default";
				}
			}
			else if (now.startsWith("-output")) {
				output = args[i + 1];
				i++;
			}
			else if (now.startsWith("-search")) {
				search = args[i + 1];
				i++;
			}
			else if (now.startsWith("-codePath")) {
				path = args[i + 1];
				i++;
			}
			else if (now.startsWith("-parser")){
				parser = args[i + 1];
				i++;
			}
			else if (now.startsWith("-savePath")){
				savaPath = args[i + 1];
				i++;
			}
			else if (now.startsWith("-api")){
				api = args[i + 1];
				i++;
			}
			else if (now.startsWith("-language")){
				language = args[i + 1];
				i++;
			}
			else if (now.startsWith("-debug")) {
				debug = true;
			}
			else if (now.startsWith("-deleteResult")) {
				delete = true;
			}
			else {
				System.err.println(now);
				throw new Exception("invalid option exist!");
			}			
		}
		
		
		File f = new File(savaPath);
		File[] files = f.listFiles();
		if (delete) {
			for (int j = 0; j < files.length; j++) {
				files[j].delete();
			}
		}
		else {
			if (files != null && files.length > 1) {
				System.out.println("having results: " + files.length);
				create = false;
			}
		}
		
		if (output.isEmpty() || search.isEmpty() || parser.isEmpty() 
				|| path.isEmpty() || savaPath.isEmpty() || api.isEmpty() || language.isEmpty()) {
			throw new Exception("not enough option given! at least need: -output,-search,-parser,-codePath,-savePath,-intermediate,-api,-language");
		}
	}

}
