package evaluation;

import java.util.HashMap;

import indexation.Document;

public class Query extends Document{
	private HashMap<String, Double> relevants;
	
	Query(String id,String text,HashMap<String,String> other, HashMap<String, Double> relevants){
		super(id, text, other);
		this.relevants = relevants; 
	}
	
	Query(Document doc){
		this(doc.getId(), doc.getText(), doc.getOther(), new HashMap<String, Double>());
	}
	
	Query(String id,String text,HashMap<String,String> other){
		super(id, text, other);
		this.relevants = new HashMap<String, Double>(); 
	}
	Query(String id,String text){
		this(id,text,new HashMap<String,String>());
	}
	Query(String id){
		this(id,"");
	}
	
	public HashMap<String, Double> getRelevants(){
		return this.relevants;
	}
	
	public boolean isRelevant(String id_doc){
		return this.relevants.containsKey(id_doc);
	}
	
	public int relevantsSize(){
		return this.relevants.size();
	}
	
	public void putRelevants(String doc, Double score){
		this.relevants.put(doc, score);
	}
	
	public void setRelevants(HashMap<String, Double> relevants){
		this.relevants = relevants;
	}
}
