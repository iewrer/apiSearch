package apiSearch.tool;

import java.util.HashMap;
import java.util.Map;

public class Language {
	
	
	static Map<String, String> languages = new HashMap<String, String>();

	public Language() {
		// TODO Auto-generated constructor stub	
	}

	/**
	 * 待添加其他语言的扩展名
	 */
	public static void set() {
		// TODO Auto-generated method stub
		languages.put("java", ".java");
	}

	public static String getExtension(String language) {
		// TODO Auto-generated method stub
		return languages.get(language);
	}

}
