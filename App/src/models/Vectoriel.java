package models;

import indexation.Index;

import java.util.HashMap;

public class Vectoriel extends IRmodel {
	
	public Vectoriel(Weighter weighter) {
		super(weighter);
	}

	@Override
	public HashMap<String, Double> getScores(HashMap<String, Integer> query) throws Exception {
		HashMap<String, Double> scores = new HashMap<String, Double>();
		HashMap<String, Double> wtq = this.weighter.getWeightsForQuery(query);
		for (HashMap.Entry<String, Double> entry : wtq.entrySet()){
			HashMap<String, Double> docWeightsForStem = this.weighter.getDocWeightsForStem(entry.getKey());
			for (HashMap.Entry<String, Double> entryDocs : docWeightsForStem.entrySet()){
				if (scores.containsKey(entryDocs.getKey()))
					scores.put(entryDocs.getKey(), scores.get(entryDocs.getKey()) + entryDocs.getValue() * entry.getValue());
				else 
					scores.put(entryDocs.getKey(), entryDocs.getValue() * entry.getValue());
			}
		}
		// TODO Auto-generated method stub
		return scores;
	}
	
	

	public static void main(String[] args) throws Exception {
		String path = "/users/nfs/Etu3/3000693/Documents/RI/SearchEngine/";

		Index index = new Index("cisi", path);
		Weighter weighter = new WeighterTfInd(index);
		IRmodel model = new Vectoriel(weighter); 
		
		HashMap<String, Integer> query = new HashMap<String, Integer>();
		query.put("librarianship", 2);
		model.getScores(query);
		System.out.println(model.getRanking(query));
		System.out.println(index.getStrDoc("407"));
		System.out.println(index.getTfsForDoc("407"));
		System.out.println(index.getTfsForDoc("353"));
	}	
}
