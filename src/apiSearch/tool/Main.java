package apiSearch.tool;

import java.io.IOException;

import apiSearch.intermediate.FileIO;

public class Main {

	public Main() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Input in = new Input();
		in.read(args);
		if (in.language.contains("java")) {
			JavaStrategy strategy = new JavaStrategy(in);
			strategy.strategy(".java");
			for (int i = 0; i < strategy.projects.size(); i++) {
				
				Project now = strategy.projects.get(i);
				
				FileIO io = new FileIO(makePath(in.savaPath, now.name));
				io.write(now.result);
				
			}
		}
		
	}

	private static String makePath(String path, String name) {
		// TODO Auto-generated method stub
		return path + "/" + name;
	}

}
