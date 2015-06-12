package apiSearch.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;


public class JDT extends Parser{
	
	CompilationUnit root;
	String src;

	public JDT(String language) {
		// TODO Auto-generated constructor stub
		super(language);
	}

	@Override
	public void parse() {
		// TODO Auto-generated method stub
		String str;
		
		try {
			str = readFileToString(path);
			ASTParser parser = ASTParser.newParser(AST.JLS8);
			parser.setSource(str.toCharArray());
			parser.setEnvironment( 
					// classpath，绝对路径
		            null,
		            // sourcepath，绝对路径
					new String[] { "/Users/barry/Documents/workspace/apiSearch/codehouse" }, 
		            null, 
		            true);
			parser.setUnitName("AST");
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			//建立binding
			parser.setResolveBindings(true);
			parser.setBindingsRecovery(true);
			final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
			
			if (cu.getAST().hasResolvedBindings()) {
				System.out.println("Binding activated.");
			}
			root = cu;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public String readFileToString(String filePath) throws IOException {
		StringBuilder fileData = new StringBuilder(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
 
		char[] buf = new char[10];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
 
		reader.close();
 
		return  fileData.toString();	
	}
	public void SetFile(String path) {
		this.path = path;
	}
	public CompilationUnit getRoot() {
		return root;
	}
}