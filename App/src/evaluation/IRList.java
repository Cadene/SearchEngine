package evaluation;

import java.util.ArrayList;

public class IRList {

	private Query query;
	private ArrayList<String> docs;
	private ArrayList<Double> scores;

	public IRList(Query query, ArrayList<String> docs, ArrayList<Double> scores) {
		this.setQuery(query);
		this.setDocs(docs);
		this.setScores(scores);
	}

	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}

	public ArrayList<String> getDocs() {
		return docs;
	}

	public void setDocs(ArrayList<String> docs) {
		this.docs = docs;
	}

	public ArrayList<Double> getScores() {
		return scores;
	}

	public void setScores(ArrayList<Double> scores) {
		this.scores = scores;
	}
	
	public int size(){
		return this.docs.size();
	}
	
	
}
