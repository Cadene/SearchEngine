package indexation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map.Entry;

public class Index { 
	private String name;
	private RandomAccessFile index;
	private RandomAccessFile inverted;
	private HashMap<String, long[]> docs;
	private HashMap<String, long[]> stems;
	private HashMap<Document, String> docFrom;
	private Parser parser;
	private TextRepresenter textRepresenter;
	
	public Index(String name, String path, Parser parser, TextRepresenter textRepresenter) throws FileNotFoundException{
		this.name = name; 
		this.index = new RandomAccessFile(path+"save/"+name+"_index", "rw");
		this.inverted = new RandomAccessFile(path+"save/"+name+"_inverted", "rw");
		this.parser = parser;
		this.docs = new HashMap<String, long[]>();
		this.stems = new HashMap<String, long[]>();
		this.docFrom = new HashMap<Document, String>();
		this.textRepresenter = textRepresenter; 
	}
	
	public void indexation(String filepath) throws IOException{
		this.parser.init(filepath);
		Document doc = this.parser.nextDocument();
		long end = this.index.getFilePointer();
		while (doc != null) {
			String text = doc.getText();
			HashMap<String, Integer> representation = this.textRepresenter.getTextRepresentation(text);
			long start = end;
			/* comment stocker une hashmap dans un fichier ? */
			for (Entry<String,Integer> entry : representation.entrySet()) {
				this.index.writeChars(entry.getKey());
				this.index.write(entry.getValue());
			}
			end = this.index.getFilePointer();
			long[] docValue = {start, end - start};
			start = end;
			docs.put(doc.get("title"), docValue);
			doc = this.parser.nextDocument();
		}
	}
	
	public static void main(String[] args) throws IOException {
		// String path = "/users/nfs/Etu3/3000693/Documents/RI/TME/";
		String path = "/Users/remicadene/Documents/DAC/RI/SearchEngine/";
		Index index = new Index("cisicacm", path, new ParserCISI_CACM(), new Stemmer());
		String p = path + "cisi/cisi.txt";
		index.indexation(p);
		p = path + "cacm/cacm.txt";
		index.indexation(p);
	}	
}
