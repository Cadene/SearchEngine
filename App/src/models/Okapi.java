package models;

import java.io.IOException;
import java.util.HashMap;

public class Okapi extends IRModel {

	private double k;
	private double b;
	private long n;    // nb de documents dans le corpus
	private double l;  // longueur moyenne des documents

	public Okapi(Weighter weighter, double k, double b) throws ClassNotFoundException, IOException {
		super(weighter);
		this.k = k;
		this.b = b;
		this.n = weighter.getListDocsIds().size();
		this.l = weighter.getSumWeightsInCorpus() / n;
	}

	public Okapi(Weighter weighter, double k) throws ClassNotFoundException, IOException {
		this(weighter, k, 0.75);
		this.k = k;
	}

	public Okapi(Weighter weighter) throws ClassNotFoundException, IOException {
		this(weighter, 1.5, 0.75);
	}
	
	@Override
	public void processScores(HashMap<String, Integer> query)
			throws Exception {
		double idf;
		double pIdf;
		double tf;
		this.scores = new HashMap<String, Double>();

		for (HashMap.Entry<String, Integer> q : query.entrySet()){
			String stem = q.getKey();
			HashMap<String, Double> docsForStem = weighter.getDocWeightsForStem(q.getKey());
			for (HashMap.Entry<String, Double> d : docsForStem.entrySet()){
				String doc = d.getKey();
				tf = weighter.getDocWeightsForDoc(doc).get(stem);
				idf = docsForStem.get(doc);
				pIdf = Math.max(0, Math.log((this.n - idf + 0.5) / (idf + 0.5)));
				if (! this.scores.containsKey(doc))
					this.scores.put(doc, 0.);
				this.scores.put(doc, this.scores.get(doc) + pIdf * ((this.k + 1) * tf) / (this.k * ((1-this.b) + this.b * weighter.getSumWeightsForDocInCorpus(doc) / this.l) + tf));
			}
		}
	}

	@Override
	public void setParameters(double... parameters) {
		this.k = parameters[0];
		this.b = parameters[1];
	}
}
