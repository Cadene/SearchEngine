package models;

import indexation.Index;

import java.io.IOException;
import java.util.HashMap;

public class WeighterTfInd extends Weighter {

	public WeighterTfInd(Index index) {
		super(index);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HashMap<String, Double> getDocWeightsForDoc(String idDoc) throws ClassNotFoundException, IOException {
		HashMap<String, Integer> occ = index.getTfsForDoc(idDoc);
		HashMap<String, Double> tf = new HashMap<String,Double>();
		for (HashMap.Entry<String, Integer> entry : occ.entrySet()) {
			tf.put(entry.getKey(), new Double(entry.getValue()));
		}
		return tf;
	}

	@Override
	public HashMap<String, Double> getDocWeightsForStem(String stem) throws ClassNotFoundException, IOException {
		HashMap<String, Integer> occ = index.getTfsForStem(stem);
		if (occ == null)
			return null;
		HashMap<String, Double> tf = new HashMap<String,Double>();
		for (HashMap.Entry<String, Integer> entry : occ.entrySet()) {
			tf.put(entry.getKey(), new Double(entry.getValue()));
		}
		return tf;
	}

	@Override
	public HashMap<String, Double> getWeightsForQuery(HashMap<String, Integer> query) {
		HashMap<String, Double> tf = new HashMap<String,Double>();
		for (HashMap.Entry<String, Integer> entry : query.entrySet()) {
			Integer v = entry.getValue();
			if (v > 0)
				tf.put(entry.getKey(), 1.0);
		}
		return tf;
	}
}
