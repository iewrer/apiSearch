package apiSearch.search;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import apiSearch.intermediate.Var;


class MyVisitor extends ASTVisitor {
	
	CompilationUnit cu;
	ArrayList<Var> data;
	String type;
	String name;
	boolean debug;
	
	public MyVisitor(CompilationUnit cu, String type, String name) {
		// TODO Auto-generated constructor stub
		this.cu = cu;
		this.type = type;
		this.name = name;
		data = new ArrayList<Var>();
		debug = false;
	}	
	
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	//用于处理java.util.Collection<java.lang.String>这样的情况，即带有类型参数的那些类型
	public String trimName(String name) {
		if (name.contains("<")) {
			String[] names = name.split("<");
			return names[0];
		}
		return name;
	}

	public boolean isTarget(ITypeBinding t) {
		String trimedName = trimName(t.getQualifiedName());
		if (trimedName.equals(type)) {
			if (debug) {
				System.out.println("target !");
			}
			return true;
		}
		
		ITypeBinding p = t.getSuperclass();
		while(p != null) {
			String trimedPName = trimName(p.getQualifiedName());
			if (trimedPName.equals(type)) {
				if (debug) {
					System.out.println("superclass: " + trimedPName);
				}
				if (debug) {
					System.out.println("target !");
				}
				return true;
			}
			p = p.getSuperclass();
		}
		ITypeBinding[] in = t.getInterfaces();
		for (int i = 0; i < in.length; i++) {
			String trimedIName = trimName(in[i].getQualifiedName());

			if (trimedIName.equals(type)) {
				if (debug) {
					System.out.println("interface: " + trimedIName);
				}
				if (debug) {
					System.out.println("target !");
				}
				return true;
			}
		}
		
		return false;
	}
}

class VarVisitor extends MyVisitor {
	
	public VarVisitor(CompilationUnit cu, String type, String name) {
		// TODO Auto-generated constructor stub
		super(cu, type, name);
		debug = false;
	}
	
	public boolean visit(VariableDeclarationFragment node){
		IBinding binding = node.resolveBinding();
		if (binding == null) {
			return true;
		}
		ITypeBinding t = ((IVariableBinding) binding).getType();
		
		
		if (t != null) {
			if (debug) {
				System.out.println("+++");
				System.out.println(t.getQualifiedName());			
			}
		}
		
		if (t != null && isTarget(t)) {
			
			int pos = cu.getLineNumber(node.getStartPosition() - 1);

			if (debug) {
				
				System.out.println("binding variable declaration: " +((IVariableBinding) binding).getVariableDeclaration());
				System.out.println(node.toString());
				System.out.println("pos: " + pos);
			}
			
			Var now = new Var();
			now.setNode(node);
			now.setDefinition(pos, node.toString());
			data.add(now);
			
		}
		return true;
	}
}

class NameVistor extends MyVisitor {
	
	String NodeName;
	boolean same;

	public NameVistor(CompilationUnit cu, String type, String name, String NodeName) {
		super(cu, type, name);
		this.NodeName = NodeName;
		// TODO Auto-generated constructor stub
		debug = false;
		same = false;
	}
	
	public boolean visit(SimpleName node){
		
		if (node.getIdentifier().equals(NodeName)) {
			if (debug) {
				System.out.println("		expnode: " + node.getIdentifier());
			}
			same = true;
		}
		
		return true;		
	}
	
}

class InvocationVisitor extends MyVisitor {
	
	Var input;
	
	public InvocationVisitor(CompilationUnit cu, String type, String name, Var input) {
		// TODO Auto-generated constructor stub
		super(cu, type, name);
		this.input = input;
		debug = false;
	}
	

	
	 public boolean visit(MethodInvocation node) {
		 Expression exp = node.getExpression();
		 if (exp == null) {
			return true;
		 }
		 
		 ITypeBinding typeBinding = exp.resolveTypeBinding();
		 
		 VariableDeclarationFragment var = (VariableDeclarationFragment) input.getNode();
		 
		 //用于确认当前的函数调用的类型是否与该变量的类型一致
		 ITypeBinding var_type = var.resolveBinding().getType();
		 
		 NameVistor nameVistor =  new NameVistor(cu, type, name, var.getName().getIdentifier());
		 
		 if (debug) {
			nameVistor.setDebug(debug);
		}
		 
		 exp.accept(nameVistor);
		 
		 if (typeBinding != null) {
			 if (debug) {
				 System.out.println("++++++");
				 System.out.println("method: " + node.getName().getIdentifier());
				 System.out.println("var: "+ var.getName().getIdentifier());
			}
		}

		 if (typeBinding != null  && nameVistor.same && var_type.getQualifiedName().equals(typeBinding.getQualifiedName()) && node.getName().toString().equals(name)) {

			 int pos = cu.getLineNumber(node.getStartPosition() - 1);

			 if (debug) {
				 System.out.println("	exp:" + exp.toString());
				 System.out.println("	methodinvocation: " + node.toString());
				 System.out.println("	" + node.toString());
				 System.out.println("	pos: " + pos);
				 System.out.println("	" + typeBinding.getQualifiedName());
			}
			 
			 input.addMap(pos, node.toString());
			 
		}
		return true;
	}
	 


	

	
	
//	public boolean visit(VariableDeclarationStatement node){
//		for (Iterator iter = node.fragments().iterator(); iter.hasNext();) {
//			
//			VariableDeclarationFragment fragment = (VariableDeclarationFragment) iter.next();
//			IVariableBinding binding = fragment.resolveBinding();
//			if (binding == null) {
//				return true;
//			}
//			
//			if (debug) {
//				System.out.println("binding variable declaration: " +binding.getVariableDeclaration());
//			}
//			
//			ITypeBinding t = binding.getType();
//			if (t != null && isTarget(t)) {
//
//				int pos = cu.getLineNumber(node.getStartPosition() - 1);
//
//				if (debug) {
//					System.out.println("------------------");
//					System.out.println("binding variable declaration: " +binding.getVariableDeclaration());
//					System.out.println(node.toString());
//					System.out.println("pos: " + pos);
//				}
//				
//				Var now = new Var();
//				now.setDefinition(pos, node.toString());
//				data.add(now);
//				
//			}
//		}
//		return true;
//	}
	
}


public class JDTSearch extends Search {

	public JDTSearch() {
		// TODO Auto-generated constructor stub
	}
	
	public ArrayList<Var> search(final CompilationUnit cu, boolean debug) {
		
		
		VarVisitor varVisitor = new VarVisitor(cu, type, name);
		
		if (debug) {
			varVisitor.setDebug(debug);
		}
		
		cu.accept(varVisitor);
		
		for (int i = 0; i < varVisitor.data.size(); i++) {
			Var now = varVisitor.data.get(i);
			
			InvocationVisitor visitor = new InvocationVisitor(cu, type, name, now);	
			
			if (debug) {
				visitor.setDebug(debug);
			}
			
			cu.accept(visitor);
			
			varVisitor.data.set(i, visitor.input);
			
			
		}
		

		
		return varVisitor.data;
	}

}
