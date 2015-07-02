package apiSearch.tool;

import org.apache.commons.lang.time.StopWatch;

import strategy.JavaStrategy;
import strategy.Strategy;
import apiSearch.api.JDK;
import apiSearch.intermediate.FileIO;
import apiSearch.staticClass.Language;



/**
 * API搜索的入口，若要增加新的支持，需要：
 * 	1.增加isOfficalAPI(...)中对应的语言官方API检测
 * 	2.增加makeStrategy(...)方法中对应的新搜索策略的对象创建
 * @author barry
 *
 */
public class Main {

	public Main() {
		// TODO Auto-generated constructor stub
		Language.set();
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Main main = new Main();
		main.begin(args);
	}

	private void begin(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Input in = new Input();
		in.read(args);
		
		String extension = Language.getExtension(in.language);
		isOfficalAPI(in);
		
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

	private void isOfficalAPI(Input in) {
		// TODO Auto-generated method stub
		if (in.language.contains("java")) {
			isJDKAPI(in);
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
		
		if (jdk.isInOfficialAPI(in.api)) {
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
