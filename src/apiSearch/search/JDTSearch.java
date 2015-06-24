package apiSearch.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import apiSearch.intermediate.Var;
import apiSearch.staticClass.Flag;

/**
 * 自定义的ASTVisitor基类
 * @author barry
 *
 */
class MyVisitor extends ASTVisitor {
	
	CompilationUnit cu;
	ArrayList<Var> data;
	String type;
	String name;
	
	public MyVisitor(CompilationUnit cu, String type, String name) {
		// TODO Auto-generated constructor stub
		this.cu = cu;
		this.type = type;
		this.name = name;
		data = new ArrayList<Var>();
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
			if (Flag.debug) {
				System.out.println("target !");
			}
			return true;
		}
		
		ITypeBinding p = t.getSuperclass();
		while(p != null) {
			String trimedPName = trimName(p.getQualifiedName());
			if (trimedPName.equals(type)) {
				if (Flag.debug) {
					System.out.println("superclass: " + trimedPName);
				}
				if (Flag.debug) {
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
				if (Flag.debug) {
					System.out.println("interface: " + trimedIName);
				}
				if (Flag.debug) {
					System.out.println("target !");
				}
				return true;
			}
		}
		
		return false;
	}
}

/**
 * 搜索并找到所有与API类型相同的变量
 * @author barry
 *
 */
class VarVisitor extends MyVisitor {
	
	public VarVisitor(CompilationUnit cu, String type, String name) {
		// TODO Auto-generated constructor stub
		super(cu, type, name);
	}
	
	public boolean visit(VariableDeclarationFragment node){
		IBinding binding = node.resolveBinding();
		if (binding == null) {
			return true;
		}
		ITypeBinding t = ((IVariableBinding) binding).getType();
		
		
		if (t != null) {
			if (Flag.debug) {
				System.out.println("+++");
				System.out.println(t.getQualifiedName());			
			}
		}
		
		if (t != null && isTarget(t)) {
			
			int pos = cu.getLineNumber(node.getStartPosition() - 1);

			if (Flag.debug) {
				
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
/**
 * 搜索并判定当前的函数调用的类型是否与该变量的类型一致，以及调用该函数的是否是该变量（通过绑定binding来判断）
 * @author barry
 *
 */
class NameVistor extends MyVisitor {
	
	String NodeName;
	boolean same;
	IBinding binding;

	public NameVistor(CompilationUnit cu, String type, String name, String NodeName, IBinding binding) {
		super(cu, type, name);
		this.NodeName = NodeName;
		// TODO Auto-generated constructor stub
		same = false;
		this.binding = binding;
	}
	
	public boolean visit(SimpleName node){
		
		if (node.getIdentifier().equals(NodeName) && node.resolveBinding() != null && node.resolveBinding().equals(binding)) {
			if (Flag.debug) {
				System.out.println("		expnode: " + node.getIdentifier());
			}
			same = true;
		}
		
		return true;		
	}
	
}
/**
 * 搜索该变量的方法调用情况
 * @author barry
 *
 */
class InvocationVisitor extends MyVisitor {
	
	Var input;
	
	public InvocationVisitor(CompilationUnit cu, String type, String name, Var input) {
		// TODO Auto-generated constructor stub
		super(cu, type, name);
		this.input = input;
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
		 
		 NameVistor nameVistor =  new NameVistor(cu, type, name, var.getName().getIdentifier(), var.getName().resolveBinding());
		 
		 exp.accept(nameVistor);
		 
		 if (typeBinding != null) {
			 if (Flag.debug) {
				 System.out.println("++++++");
				 System.out.println("method: " + node.getName().getIdentifier());
				 System.out.println("var: "+ var.getName().getIdentifier());
			}
		}

		 if (typeBinding != null  && nameVistor.same && var_type.getQualifiedName().equals(typeBinding.getQualifiedName()) && node.getName().toString().equals(name)) {

			 int pos = cu.getLineNumber(node.getStartPosition() - 1);

			 if (Flag.debug) {
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
	
}

/**
 * 采用JDT Core进行API搜索，实现了若干个Visitor用于进行API搜索
 * @author barry
 *
 */
public class JDTSearch extends Search {

	public JDTSearch() {
		// TODO Auto-generated constructor stub
	}
	
	public ArrayList<Var> search(Object root) {
		
		final CompilationUnit cu = (CompilationUnit)root;
		
		VarVisitor varVisitor = new VarVisitor(cu, type, name);
		
		
		cu.accept(varVisitor);
		
		for (int i = 0; i < varVisitor.data.size(); i++) {
			Var now = varVisitor.data.get(i);
			
			InvocationVisitor visitor = new InvocationVisitor(cu, type, name, now);	
			
			cu.accept(visitor);
			visitor.input.sort();
			
			varVisitor.data.set(i, visitor.input);
				
		}
		
		return varVisitor.data;
	}

}
