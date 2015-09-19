package indexation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;

public class Utility {

	public static byte[] serialize(Object source) throws IOException {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
	    ObjectOutputStream out = new ObjectOutputStream(byteOut);
	    out.writeObject(source);
	    out.flush();
	    return byteOut.toByteArray();
	}
	
	public static Object deserialize(byte[] source) throws IOException, ClassNotFoundException {
	    ByteArrayInputStream byteIn = new ByteArrayInputStream(source);
	    ObjectInputStream in = new ObjectInputStream(byteIn);
	    return in.readObject();
	}
	
	public static Object loadObject(String path) throws IOException, ClassNotFoundException {
		RandomAccessFile file = new RandomAccessFile(path, "r");
		byte[] b = new byte[(int)file.length()];
		file.read(b);
		file.close();
		return Utility.deserialize(b);
	}
}
