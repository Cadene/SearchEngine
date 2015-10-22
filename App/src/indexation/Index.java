package indexation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

public class Index { 
	private String name;
	private RandomAccessFile index;
	private RandomAccessFile inverted;
	
	private Parser parser;
	private TextRepresenter textRepresenter;
	
	private HashMap<String, long[]> docs;
	private HashMap<String, long[]> stems;
	private HashMap<String, String[]> docFrom;
	
	private HashMap<String, Set<String>> pred;
	private HashMap<String, Set<String>> succ;
	
	
	public Index(String name, String path, Parser parser, TextRepresenter textRepresenter) throws FileNotFoundException{
		this.name = name; 
		this.index = new RandomAccessFile(path+"save/"+name+"/index", "rw");
		this.inverted = new RandomAccessFile(path+"save/"+name+"/inverted", "rw");

		this.parser = parser;
		this.textRepresenter = textRepresenter; 
		
		this.docs = new HashMap<String, long[]>();
		this.stems = new HashMap<String, long[]>();
		this.docFrom = new HashMap<String, String[]>();	
		
		this.pred = new HashMap<String, Set<String>>();
		this.succ = new HashMap<String, Set<String>>();
	}
	
	public Index(String name, String path) throws ClassNotFoundException, IOException{
		System.out.println("Loading index from "+ path + "save/" + name);
		
		this.name = name;
		this.index = new RandomAccessFile(path +"save/" + name +"/index", "rw");
		this.inverted = new RandomAccessFile(path +"save/" + name +"/inverted", "rw");
		
		this.docFrom = (HashMap<String, String[]>)Utility.loadObject(path+"save/" + name + "/docFrom");
		this.docs = (HashMap<String, long[]>)Utility.loadObject(path+"save/" + name + "/docs");
		this.stems = (HashMap<String, long[]>)Utility.loadObject(path+"save/" + name + "/stems");
		
		this.pred = (HashMap<String, Set<String>>)Utility.loadObject(path+"save/" + name + "/pred");
		this.succ = (HashMap<String, Set<String>>)Utility.loadObject(path+"save/" + name + "/succ");
		System.out.println("Index " + name + " Loaded");
	}
	
	public void indexation(String savepath, String sourcepath) throws IOException, ClassNotFoundException {

		System.out.println("Indexing from " + sourcepath + " ...");
		
		this.parser.init(sourcepath);
		Document doc = this.parser.nextDocument();
		
		String tmpPath = "/tmp/3000693_RI_tmp";
		File tmpFile = new File(tmpPath);
		tmpFile.mkdir();
		tmpFile.deleteOnExit();
		tmpPath = tmpPath + "/";
				
		long endDocFrom = this.index.getFilePointer();
		while (doc != null) {
			HashMap<String, Integer> representation = this.textRepresenter.getTextRepresentation(doc.getText());
			long startDocFrom = endDocFrom;
			this.index.write(Utility.serialize(representation));
			endDocFrom = this.index.getFilePointer();
			long[] docFromValue = {startDocFrom, endDocFrom - startDocFrom};
			startDocFrom = endDocFrom;
			this.docs.put(doc.getId(), docFromValue);
			this.docFrom.put(doc.getId(), doc.get("from").split(";"));
			
			HashMap<String, Integer> stem;
			for (Entry<String, Integer> entry : representation.entrySet()){
				long[] stemsValue = this.stems.get(entry.getKey());
				RandomAccessFile stemTmp = new RandomAccessFile(tmpPath+entry.getKey(), "rw");
				if (stemsValue == null) {
					this.stems.put(entry.getKey(), new long[2]);
					stem = new HashMap<String, Integer>();
					tmpFile = new File(tmpPath+entry.getKey());
					tmpFile.deleteOnExit();
				}
				else
				{
					stem = (HashMap<String, Integer>)Utility.loadObject(tmpPath+entry.getKey());
				}
				stem.put(doc.getId(), entry.getValue());
				stemTmp.write(Utility.serialize(stem));
				stemTmp.close();
			}
			
			this.succ.put(doc.getId(), new HashSet<String>());
			if (!this.pred.containsKey(doc.getId())){
				this.pred.put(doc.getId(), new HashSet<String>());
			}
			for (String successeur : doc.get("links").split(";")){
				if (successeur != ""){
					this.succ.get(doc.getId()).add(successeur);
					if (!this.pred.containsKey(successeur)){
						this.pred.put(successeur, new HashSet<String>());
					}
					this.pred.get(successeur).add(doc.getId());
				}
			}
			doc = this.parser.nextDocument();
		}
		
		long endStems = this.inverted.getFilePointer();
		for (Entry<String, long[]> entry : this.stems.entrySet()){
			RandomAccessFile infile = new RandomAccessFile(tmpPath+entry.getKey(), "r");
			byte[] b = new byte[(int)infile.length()];
			infile.read(b);
			infile.close();
			long startStems = this.inverted.getFilePointer();
			this.inverted.write(b);
			endStems = this.inverted.getFilePointer();
			long[] stemsValue = {startStems, endStems - startStems};
			this.stems.put(entry.getKey(), stemsValue); 
			startStems = endStems;
		}
		
		System.out.println("Indexing Done. Writing to " + savepath);
		
		RandomAccessFile docFromFile = new RandomAccessFile(savepath + "docFrom", "rw");
		docFromFile.write(Utility.serialize(docFrom));
		docFromFile.close();
		
		RandomAccessFile docsFile = new RandomAccessFile(savepath + "docs", "rw");
		docsFile.write(Utility.serialize(docs));
		docsFile.close();
		
		RandomAccessFile stemsFile = new RandomAccessFile(savepath + "stems", "rw");
		stemsFile.write(Utility.serialize(stems));
		stemsFile.close();
		
		RandomAccessFile succFile = new RandomAccessFile(savepath + "succ", "rw");
		succFile.write(Utility.serialize(this.succ));
		succFile.close();
		
		RandomAccessFile predFile = new RandomAccessFile(savepath + "pred", "rw");
		predFile.write(Utility.serialize(this.pred));
		predFile.close();
		
		System.out.println("Done.");
	}
	
	public HashMap<String, Integer> getTfsForDoc(String id) throws IOException, ClassNotFoundException{
		this.index.seek(this.docs.get(id)[0]);
		byte[] b = new byte[(int)this.docs.get(id)[1]];
		this.index.read(b);
		return (HashMap<String, Integer>)Utility.deserialize(b) ; 
	}
	
	public HashMap<String, Integer> getTfsForStem(String id) throws IOException, ClassNotFoundException{
		long[] stem = this.stems.get(id);
		if (stem == null)
			return new HashMap<String, Integer>();
		this.inverted.seek(stem[0]);
		byte[] b = new byte[(int)stem[1]];
		this.inverted.read(b);
		return (HashMap<String, Integer>)Utility.deserialize(b) ; 
	}
	
	public String getStrDoc(String id) throws IOException, ClassNotFoundException{
		RandomAccessFile file = new RandomAccessFile(this.docFrom.get(id)[0], "r");
		file.seek(Integer.parseInt(this.docFrom.get(id)[1]));
		byte[] b = new byte[Integer.parseInt(this.docFrom.get(id)[2])];
		file.read(b);
		file.close();
		return new String(b);
	}
	
	public Set<String> getListDocsIds()
	{
		return this.docs.keySet();
	}
	
	public HashMap<String, Set<String>> getSucc(){
		return this.succ;
	}
	
	public HashMap<String, Set<String>> getPred(){
		return this.pred;
	}	
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		String id = "0";
		String path = "/Vrac/3152691/RI/";
		//String path = "/users/nfs/Etu3/3000693/Documents/RI/SearchEngine/";
		
		System.out.println("Main Index.");
		
		if (id.equals("0")) {
			Index indexCisi = new Index("cisi", path, new ParserCISI_CACM(), new Stemmer());
			String sourcepathCisi = path + "cisi/cisi.txt";
			String savepathCisi = path + "save/cisi/";
			indexCisi.indexation(savepathCisi, sourcepathCisi);
			
			Index indexCacm = new Index("cacm", path, new ParserCISI_CACM(), new Stemmer());
			String sourcepathCacm = path + "cacm/cacm.txt";
			String savepathCacm = path + "save/cacm/";
			indexCacm.indexation(savepathCacm, sourcepathCacm);
		}
		else
		{
			Index index = new Index("cisi", path);
			System.out.println(index.getTfsForDoc(id));
			System.out.println(index.getStrDoc(id));
			System.out.println(index.getTfsForStem("librarianship").get("909"));
			System.out.println(index.getTfsForStem("librarianship").get("1"));
			System.out.println(index.getTfsForStem("west"));
		}
	}
}
