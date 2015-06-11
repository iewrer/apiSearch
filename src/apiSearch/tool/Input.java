package apiSearch.tool;

import java.util.Scanner;

public class Input {
	
	boolean create;//重新创建搜索还是从中间表示形式中读取
	String inter;
	String output;
	String search;
	String parser;
	
	String path;//整个代码库的存放位置

	public Input() {
		// TODO Auto-generated constructor stub
		create = true;
		inter = new String();
		output = new String();
		search = new String();
		path = new String();
		parser = new String();
	}
	
	void read() throws Exception {
		Scanner in = new Scanner(System.in);
		while(in.hasNext()) {
			String now = in.next();
			if (now.startsWith("-create")) {
				create = in.nextBoolean();
			}
			else if (now.startsWith("-intermediate")) {
				inter = in.next();
			}
			else if (now.startsWith("-output")) {
				output = in.next();
			}
			else if (now.startsWith("-search")) {
				search = in.next();
			}
			else if (now.startsWith("-path")) {
				path = in.next();
			}
			else if (now.startsWith("-paser")){
				parser = in.next();
			}
			else {
				throw new Exception("invalid option exist!");
			}
		}
		if (create && !path.isEmpty()) {
			throw new Exception("create new search but intermediate path given!");
		}
	}

}
