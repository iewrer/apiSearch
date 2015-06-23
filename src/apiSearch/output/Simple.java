package apiSearch.output;

import java.util.ArrayList;

import apiSearch.intermediate.InterRep;
import apiSearch.intermediate.Var;
import apiSearch.tool.Project;

public class Simple extends Output {

	public Simple() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void output(Project now) {
		// TODO Auto-generated method stub
		System.out.println("----------------output-----------------------");
		System.out.println("project name: " + now.name);
		System.out.println("project path: " + now.path);
		for (int i = 0; i < now.result.size(); i++) {
			InterRep inter = now.result.get(i);
			for (Var j : inter.getData()) {
				System.out.println("	no." + i);
				System.out.println("	" + j.toString());
			}
		}
	}



}
