package models;

import java.util.HashMap;

public class LanguageModel extends IRModel {

	private double lamb;
	
	public LanguageModel(Weighter weighter, double lamb) {
		super(weighter);
		this.lamb = lamb;
	}

	@Override
	public void processScores(HashMap<String, Integer> query)
			throws Exception {
		this.scores = new HashMap<String, Double>();
		HashMap<String, Double> wtq = this.weighter.getWeightsForQuery(query);
		
		for (String term : query.keySet()){
			double pct = this.weighter.getSumWeightsForStemInCorpus(term) / weighter.getSumWeightsInCorpus();
			HashMap<String, Double> docWeightsForStem = this.weighter.getDocWeightsForStem(term);
			for (String doc : weighter.getListDocsIds()){
				double pdt = 0;
				if (docWeightsForStem.containsKey(doc)){
					pdt = docWeightsForStem.get(doc) / this.weighter.getSumWeightsForDocInCorpus(doc);
				}
				if (!this.scores.containsKey(doc)){
					this.scores.put(doc, 0.0);
				}
				this.scores.put(doc, this.scores.get(doc) + wtq.get(term) * Math.log(this.lamb * pdt + (1-this.lamb) * pct));
			}
		}
		
		
		// pour chaque document
			// somme tf(terme, query) + log(tf(terme, document) / longueur(document)
		
		// pour chaque terme de la query
		/*
		for (HashMap.Entry<String, Double> entry : wtq.entrySet()) {
			HashMap<String, Double> docWeightsForStem = this.weighter.getDocWeightsForStem(entry.getKey()); // poids du stem par doc
			if (docWeightsForStem != null) {
				double pct = this.weighter.getSumWeightsForStemInCorpus(entry.getKey()) / weighter.getSumWeightsInCorpus();
				// pour tous les documents qui contiennent le terme
				for (HashMap.Entry<String, Double> entryDocs : docWeightsForStem.entrySet()) { 
					if (!this.scores.containsKey(entryDocs.getKey())) { // Initialisation des scores
						this.scores.put(entryDocs.getKey(), 0.0);
						countScores.put(entryDocs.getKey(), 0);
					}
					double pdt;
					if (docWeightsForStem.get(entryDocs.getKey()) == null)
						pdt = 0;
					else
						pdt = docWeightsForStem.get(entryDocs.getKey()) / this.weighter.getSumWeightsForDocInCorpus(entryDocs.getKey());
					this.scores.put(entryDocs.getKey(), this.scores.get(entryDocs.getKey()) + entry.getValue() * Math.log(this.lamb * pdt + (1-this.lamb) * pct));
					countScores.put(entryDocs.getKey(), countScores.get(entryDocs.getKey()) + 1);
				}
				for (HashMap.Entry<String, Integer> entryCountScores : countScores.entrySet()) {
					this.scores.put(entryCountScores.getKey(), this.scores.get(entryCountScores.getKey()) + 
							(wtq.size() - entryCountScores.getValue()) * (entry.getValue() * Math.log((1-this.lamb) * pct)));
				}
			}
		}
		*/
	}

	@Override
	public void setParameters(double... parameters) {
		this.lamb = parameters[0];
	}
}
