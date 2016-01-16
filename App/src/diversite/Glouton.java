package diversite;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import models.IRModel;
import models.Weighter;

public class Glouton extends IRModel {

	private IRModel baseIRModel;
	private int nbDocs;
	private double alpha;

	public Glouton(Weighter weighter, IRModel baseIRModel, int nbDocs, double alpha) {
		super(weighter);
		this.baseIRModel = baseIRModel;
		this.nbDocs = nbDocs;
		this.alpha = alpha;
	}

	@Override
	public void setParameters(double... parameters) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processScores(HashMap<String, Integer> query) throws Exception {
		this.queryForScores = query;
		this.scores = new HashMap<String, Double>();
		LinkedHashMap<String, Double> ranking = new LinkedHashMap<String, Double>();
		HashMap<String, HashMap<String,Double>> U = new HashMap<String, HashMap<String,Double>>();
		HashMap<String, HashMap<String,Double>> R = new HashMap<String, HashMap<String,Double>>();
		int indexOfDoc = 0;
		for (String idDoc : this.baseIRModel.getRanking(query).keySet()){
			U.put(idDoc, weighter.getDocWeightsForDoc(idDoc));
			indexOfDoc++;
			if (indexOfDoc >= this.nbDocs){
				break;
			}
		}
		
		HashMap<String, Double> q = weighter.getWeightsForQuery(query);
		double j = weighter.getListDocsIds().size();
		for (int i = 0; i < U.size(); i++){
			String idDoc = argmax_value(q, U, R);
			if (idDoc != null){
				R.put(idDoc, U.get(idDoc));
				U.remove(idDoc);
				this.scores.put(idDoc, j);
				ranking.put(idDoc, j);
				j = j-1;
			}
		}
		//this.ranking = ranking;
		//this.queryForRanking = query;
	}
	
	private String argmax_value(HashMap<String, Double> q, HashMap<String, HashMap<String, Double>> U, HashMap<String, HashMap<String, Double>> R) {
		String argmax = null;
		double vmax = -Double.MAX_VALUE;
		for (String idDoc : U.keySet()) {
			double nmax = alpha * sim_phi(U.get(idDoc), q) + (1 - alpha) * sim_psi(U.get(idDoc), R);
			if (nmax > vmax){
				argmax = idDoc;
			}
		}
		return argmax;
	}
	
	private double sim_phi(HashMap<String,Double> doc1, HashMap<String,Double> doc2) {
		return 1 - dissimilarity(doc1, doc2);
	}
	
	private double sim_psi(HashMap<String,Double> doc1, HashMap<String, HashMap<String,Double>> R) {
		double rslt = 0.0;
		if (R.isEmpty()){
			return 0;
		}
		for(HashMap<String,Double> doc2 : R.values()) {
			if (doc2.isEmpty() == false){
				rslt += dissimilarity(doc1, doc2);
			}
		}
		return rslt / R.size();
	}
	
	private double dissimilarity(HashMap<String,Double> doc1, HashMap<String,Double> doc2) {
		double rslt = 0.0;
		double norm1 = 0.0;
		double norm2 = 0.0;
		
		for (Double value1 : doc1.values()){
			norm1 += value1 * value1;
		}
		for (Double value2 : doc2.values()){
			norm2 += value2 * value2;
		}
		for (Entry<String, Double> entry1 : doc1.entrySet()){
			if (doc2.containsKey(entry1.getKey())) {
				rslt += entry1.getValue() * doc2.get(entry1.getKey());
			}
		}
		rslt /= Math.sqrt(norm1) * Math.sqrt(norm2);
		return 1 - rslt;
	}

}
