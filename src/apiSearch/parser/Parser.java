package apiSearch.parser;


public abstract class Parser {
	
	String language;//parser适用的语言
	String path;//单个源代码文件的存放位置
	
	Object root;

	public Parser(String language) {
		// TODO Auto-generated constructor stub
		this.language = language;
		this.path = new String();
	}
	
	public abstract void parse();
	public abstract void SetFile(String path);
	public abstract void setSrc(String src);
	public abstract Object getRoot();

}
