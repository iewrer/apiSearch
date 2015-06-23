package apiSearch.intermediate;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FileIO {

	String path;
	String extension;
	
	public FileIO(String path) {
		// TODO Auto-generated constructor stub
		this.path = path;
	}
	
	public void write(ArrayList<InterRep> data) throws IOException {
		FileOutputStream fout = new FileOutputStream(path);
		ObjectOutputStream oos = new ObjectOutputStream(fout);
		oos.writeObject(data);
		oos.close();
		fout.close();
		System.out.println("Serialized data is saved in " + path);
	}
	
	public ArrayList<InterRep> read() throws ClassNotFoundException, IOException {
		ArrayList<InterRep> data;
		
		FileInputStream fileIn = new FileInputStream(path);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        data = (ArrayList<InterRep>) in.readObject();
        in.close();
        fileIn.close();
        System.out.println("Serialized data is readed from " + path);
        
        return data;
	}
}
