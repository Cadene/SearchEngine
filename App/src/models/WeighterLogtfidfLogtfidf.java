package models;

import indexation.Index;

import java.io.IOException;
import java.util.HashMap;

public class WeighterLogtfidfLogtfidf extends Weighter {

	public WeighterLogtfidfLogtfidf(Index index) {
		super(index);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HashMap<String, Double> getDocWeightsForDoc(String idDoc) throws ClassNotFoundException, IOException {
		HashMap<String, Integer> occ = index.getTfsForDoc(idDoc);
		HashMap<String, Double> tfidf = new HashMap<String,Double>();
		if (occ.size() == 0)
			return tfidf;
		for (HashMap.Entry<String, Integer> entry : occ.entrySet()) {
			Integer idf = index.getTfsForStem(entry.getKey()).size();
			tfidf.put(entry.getKey(), (1 + Math.log(entry.getValue())) * idf);
		}
		return tfidf;
	}

	@Override
	public HashMap<String, Double> getDocWeightsForStem(String stem) throws ClassNotFoundException, IOException {
		HashMap<String, Integer> occ = index.getTfsForStem(stem);
		HashMap<String, Double> tfidf = new HashMap<String,Double>();
		if (occ.size() == 0)
			return tfidf;
		for (HashMap.Entry<String, Integer> entry : occ.entrySet()) {
			Integer idf = index.getTfsForStem(entry.getKey()).size();
			tfidf.put(entry.getKey(), (1 + Math.log(entry.getValue())) * idf);
		}
		return tfidf;
	}

	@Override
	public HashMap<String, Double> getWeightsForQuery(HashMap<String, Integer> query) throws ClassNotFoundException, IOException {
		HashMap<String, Double> tfidf = new HashMap<String,Double>();
		for (HashMap.Entry<String, Integer> entry : query.entrySet()) {
			Integer idf = index.getTfsForStem(entry.getKey()).size();
			tfidf.put(entry.getKey(), (1 + Math.log(entry.getValue())) * idf);
		}
		return tfidf;
	}
}
