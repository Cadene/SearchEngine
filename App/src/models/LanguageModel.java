package models;

import java.util.HashMap;

public class LanguageModel extends IRModel {

	private double lamb;
	
	public LanguageModel(Weighter weighter, double lamb) {
		super(weighter);
		this.lamb = lamb;
	}

	@Override
	public HashMap<String, Double> processScores(HashMap<String, Integer> query)
			throws Exception {
		HashMap<String, Double> scores = new HashMap<String, Double>();
		HashMap<String, Integer> countScores = new HashMap<String, Integer>();
		HashMap<String, Double> wtq = this.weighter.getWeightsForQuery(query);
				
		// pour chaque document
			// somme tf(terme, query) + log(tf(terme, document) / longueur(document)
		
		// pour chaque terme de la query
		for (HashMap.Entry<String, Double> entry : wtq.entrySet()) {
			HashMap<String, Double> docWeightsForStem = this.weighter.getDocWeightsForStem(entry.getKey()); // poids du stem par doc
			if (docWeightsForStem != null) {
				double pct = this.weighter.getSumWeightsForStemInCorpus(entry.getKey()) / weighter.getSumWeightsInCorpus();
				// pour tous les documents qui contiennent le terme
				for (HashMap.Entry<String, Double> entryDocs : docWeightsForStem.entrySet()) { 
					if (!scores.containsKey(entryDocs.getKey())) { // Initialisation des scores
						scores.put(entryDocs.getKey(), 0.0);
						countScores.put(entryDocs.getKey(), 0);
					}
					double pdt;
					if (docWeightsForStem.get(entryDocs.getKey()) == null)
						pdt = 0;
					else
						pdt = docWeightsForStem.get(entryDocs.getKey()) / this.weighter.getSumWeightsForDocInCorpus(entryDocs.getKey());
					scores.put(entryDocs.getKey(), scores.get(entryDocs.getKey()) + entry.getValue() * Math.log(this.lamb * pdt + (1-this.lamb) * pct));
					countScores.put(entryDocs.getKey(), countScores.get(entryDocs.getKey()) + 1);
				}
				for (HashMap.Entry<String, Integer> entryCountScores : countScores.entrySet()) {
					scores.put(entryCountScores.getKey(), scores.get(entryCountScores.getKey()) + 
							(wtq.size() - entryCountScores.getValue()) * (entry.getValue() * Math.log((1-this.lamb) * pct)));
				}
			}
		}
		this.scores = scores;
		//System.out.println(scores);
		return this.scores;
	}
}
