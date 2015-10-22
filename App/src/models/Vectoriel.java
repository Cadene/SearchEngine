package models;

import indexation.Index;

import java.io.IOException;
import java.util.HashMap;

public class Vectoriel extends IRModel {
	
	boolean normalized;
	
	public Vectoriel(Weighter weighter) {
		super(weighter);
		this.normalized = false;
	}
	
	public Vectoriel(Weighter weighter, boolean normalized) throws ClassNotFoundException, IOException {
		super(weighter);
		this.normalized = normalized;
	}

	@Override
	protected void processScores(HashMap<String, Integer> query) throws Exception {
		this.scores = new HashMap<String, Double>();
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
			if (docWeightsForStem != null) {			
				for (HashMap.Entry<String, Double> entryDocs : docWeightsForStem.entrySet()){
					if (this.scores.containsKey(entryDocs.getKey())) {
						double scoreUpdate = entryDocs.getValue() * entry.getValue();
						if (this.normalized)
							scoreUpdate = scoreUpdate / (weighter.getNormForDoc(entryDocs.getKey()) * queryNorm);
						this.scores.put(entryDocs.getKey(), this.scores.get(entryDocs.getKey()) + scoreUpdate);
					}
					else {
						double scoreUpdate = entryDocs.getValue() * entry.getValue();
						if (this.normalized)
							scoreUpdate = scoreUpdate / (weighter.getNormForDoc(entryDocs.getKey()) * queryNorm);
						this.scores.put(entryDocs.getKey(), scoreUpdate);
					}
				}
			}
		}
		// TODO Auto-generated method stub
		return;
	}

	public static void main(String[] args) throws Exception {
		String path = "/users/nfs/Etu3/3000693/Documents/RI/SearchEngine/";

		Index index = new Index("cisi", path);
		Weighter weighter = new WeighterTfInd(index);
		IRModel model = new Vectoriel(weighter, true); 
		
		HashMap<String, Integer> query = new HashMap<String, Integer>();
		query.put("librarianship", 2);
		model.processScores(query);
		System.out.println(model.getScores(query));
		System.out.println(model.getRanking(query));
		System.out.println(index.getStrDoc("407"));
		System.out.println(index.getTfsForDoc("407"));
		System.out.println(index.getTfsForDoc("353"));
	}

	@Override
	public void setParameters(double... parameters) {
	}	
}
