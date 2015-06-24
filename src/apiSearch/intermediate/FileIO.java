package apiSearch.intermediate;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Map;

import apiSearch.tool.Project;


/**
 * 该类实现了基本的对数据进行序列化和反序列化的能力，可以进行继承以获得保存为其他数据格式的能力
 * @author barry
 *
 */
public class FileIO {

	String path;
	String extension;
	
	
	public FileIO() {
		
	}
	
	public FileIO(String path) {
		// TODO Auto-generated constructor stub
		this.path = path;
	}
	
	public void write(Project now) throws IOException {
		FileOutputStream fout = new FileOutputStream(path);
		ObjectOutputStream oos = new ObjectOutputStream(fout);
		oos.writeObject(now);
		oos.close();
		fout.close();
		
		System.out.println("Serialized data is saved in " + path);
	}
	
	public Project read() throws ClassNotFoundException, IOException {
		Project data;
		
		FileInputStream fileIn = new FileInputStream(path);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        data = (Project) in.readObject();
        in.close();
        fileIn.close();
        
        System.out.println("Serialized data is readed from " + path);
        
        return data;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
}
