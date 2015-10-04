package models;

import indexation.Index;

import java.io.IOException;
import java.util.HashMap;

public class Vectoriel extends IRModel {
	
	boolean normalized;
	HashMap<String, Double> docsNorm;
	HashMap<String, Double> scores;
	HashMap<String, Double> ranks;
	
	public Vectoriel(Weighter weighter) {
		super(weighter);
		this.normalized = false;
		this.scores = null;
		this.ranks = null;
	}
	
	public Vectoriel(Weighter weighter, Index index, boolean normalized) throws ClassNotFoundException, IOException {
		super(weighter);
		if (normalized == true) {
			this.normalized = true;
			this.docsNorm = new HashMap<String, Double>();
			for (String docId : index.getListDocsIds()) {
				HashMap<String, Integer> tfsForDocs = index.getTfsForDoc(docId);				
				double norm = 0;
				for (HashMap.Entry<String, Integer> entry: tfsForDocs.entrySet()) {
					norm = norm + (entry.getValue()) * (entry.getValue());
				}
				docsNorm.put(docId, Math.sqrt(norm));
			}
		}
		else
			this.normalized = false;
	}

	@Override
	public HashMap<String, Double> processScores(HashMap<String, Integer> query) throws Exception {
		HashMap<String, Double> scores = new HashMap<String, Double>();
		HashMap<String, Double> wtq = this.weighter.getWeightsForQuery(query);
		double queryNorm = 0; 
		if (this.normalized)
		{
			for (HashMap.Entry<String, Double> entry : wtq.entrySet()){
				queryNorm = queryNorm + entry.getValue() * entry.getValue();
			}
			queryNorm = Math.sqrt(queryNorm);
		}
		for (HashMap.Entry<String, Double> entry : wtq.entrySet()){
			HashMap<String, Double> docWeightsForStem = this.weighter.getDocWeightsForStem(entry.getKey());
			for (HashMap.Entry<String, Double> entryDocs : docWeightsForStem.entrySet()){
				if (scores.containsKey(entryDocs.getKey())) {
					double scoreUpdate = entryDocs.getValue() * entry.getValue();
					if (this.normalized)
						scoreUpdate = scoreUpdate / (this.docsNorm.get(entryDocs.getKey()) * queryNorm);
					scores.put(entryDocs.getKey(), scores.get(entryDocs.getKey()) + scoreUpdate);
				}
				else {
					double scoreUpdate = entryDocs.getValue() * entry.getValue();
					if (this.normalized)
						scoreUpdate = scoreUpdate / (this.docsNorm.get(entryDocs.getKey()) * queryNorm);
					scores.put(entryDocs.getKey(), scoreUpdate);
				}
			}
		}
		// TODO Auto-generated method stub
		this.scores = scores;
		return scores;
	}
	
	public HashMap<String, Double> getScores(){
		return this.scores;
	}

	public static void main(String[] args) throws Exception {
		String path = "/users/nfs/Etu3/3000693/Documents/RI/SearchEngine/";

		Index index = new Index("cisi", path);
		Weighter weighter = new WeighterTfInd(index);
		IRModel model = new Vectoriel(weighter, index, true); 
		
		HashMap<String, Integer> query = new HashMap<String, Integer>();
		query.put("librarianship", 2);
		model.processScores(query);
		System.out.println(model.processScores(query));
		System.out.println(model.processRanking(query));
		System.out.println(index.getStrDoc("407"));
		System.out.println(index.getTfsForDoc("407"));
		System.out.println(index.getTfsForDoc("353"));
	}	
}
