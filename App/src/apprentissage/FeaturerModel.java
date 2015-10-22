package apprentissage;

import indexation.Stemmer;

import java.util.ArrayList;
import java.util.HashMap;

import models.IRModel;

public class FeaturerModel extends Featurer {

	private IRModel model;
	private Stemmer stemmer;

	public FeaturerModel(IRModel model, Stemmer stemmer) {
		super();
		this.model = model;
		this.stemmer = stemmer;
	}

	@Override
	public ArrayList<Double> getFeatures(String idDoc, String query) throws Exception {
		HashMap<String, Integer> quStems = this.stemmer.getTextRepresentation(query);
		HashMap<String, Integer> ranking = this.model.processRanking(quStems);
		HashMap<String, Double> scores = this.model.getScores();
		
		// /!\ Est-ce toujours retourné dans le même ordre ?
		//     Sinon les features n'ont aucun sens
		for (HashMap.Entry<String, Double> entry : scores.entrySet()) { 
			this.features.add(entry.getValue());
		}
		return this.features;
	}

}