package models;

import indexation.Index;

import java.io.IOException;
import java.util.HashMap;

public class WeighterLogtfIdf extends Weighter {

	public WeighterLogtfIdf(Index index) {
		super(index);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HashMap<String, Double> getDocWeightsForDoc(String idDoc) throws ClassNotFoundException, IOException {
		HashMap<String, Integer> occ = index.getTfsForDoc(idDoc);
		HashMap<String, Double> tf = new HashMap<String,Double>();
		if (occ.size() == 0)
			return tf;
		for (HashMap.Entry<String, Integer> entry : occ.entrySet()) {
			tf.put(entry.getKey(), (1 + Math.log(entry.getValue())));
		}
		return tf;
	}

	@Override
	public HashMap<String, Double> getDocWeightsForStem(String stem) throws ClassNotFoundException, IOException {
		HashMap<String, Integer> occ = index.getTfsForStem(stem);
		HashMap<String, Double> tf = new HashMap<String,Double>();
		if (occ.size() == 0)
			return tf;
		for (HashMap.Entry<String, Integer> entry : occ.entrySet()) {
			tf.put(entry.getKey(), (1 + Math.log(entry.getValue())));
		}
		return tf;
	}

	@Override
	public HashMap<String, Double> getWeightsForQuery(HashMap<String, Integer> query) throws ClassNotFoundException, IOException {
		HashMap<String, Double> idf = new HashMap<String,Double>();
		for (String term : query.keySet()) {
			idf.put(term, Math.log(index.getListDocsIds().size() / (1 + index.getTfsForStem(term).size())) );
		}
		return idf;
	}
}
