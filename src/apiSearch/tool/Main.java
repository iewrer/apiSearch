package apiSearch.tool;

import strategy.JavaStrategy;
import strategy.Strategy;
import apiSearch.intermediate.FileIO;

public class Main {

	public Main() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Main main = new Main();
		Language.set();
		main.begin(args);
	}

	private void begin(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Input in = new Input();
		in.read(args);
		
		String extension = Language.getExtension(in.language);
		isJDKAPI(in);
		
		Strategy strategy = makeStrategy(in);
		
		
		strategy.strategy(extension);
		
		if (in.create) {
			
			System.out.println("-------------intermediate data saving---------------");
			
			for (int i = 0; i < strategy.projects.size(); i++) {
				
				Project now = strategy.projects.get(i);
				
				FileIO io = new FileIO(makePath(in.savaPath, now.name));
				io.write(now);
				
			}				
		}
	}

	private Strategy makeStrategy(Input in) {
		// TODO Auto-generated method stub
		
		Strategy strategy = null;
		
		if (in.language.contains("java")) {
			JavaStrategy now = new JavaStrategy(in);
			return now;
		}
		return strategy;
	}

	private void isJDKAPI(Input in) {
		// TODO Auto-generated method stub
		JDK jdk;
		if (in.jdkPath.isEmpty()) {
			jdk = new JDK();
		}
		else {
			jdk = new JDK(in.jdkPath);
		}
		
		if (jdk.isInJDK(in.api)) {
			System.out.println("trying to find JDK API: " + in.api + "");
		}
		else {
			System.out.println("trying to find Third Party API: " + in.api + "");
		}	
	}

	private String makePath(String path, String name) {
		// TODO Auto-generated method stub
		return path + "/" + name;
	}

}
