package featurer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import models.IRModel;

public class FeaturerModel extends DocAndQueryFeaturer {

	private IRModel model;

	public FeaturerModel(IRModel model) {
		super();
		this.model = model;
		this.size = 1;
	}

	@Override
	public void buildFeatures(String idDoc, HashMap<String, Integer> query) throws Exception {
		LinkedHashMap<String, Double> ranking = this.model.getRanking(query);
		for (String doc : ranking.keySet()){
			if (!this.docAndQueryfeatures.containsKey(doc)){
				this.docAndQueryfeatures.put(doc, new HashMap<HashMap<String, Integer>, ArrayList<Double>>());	
			}
			ArrayList<Double> feature = new ArrayList<Double>();
			feature.add(ranking.get(doc));
			this.docAndQueryfeatures.get(doc).put(query, feature);
		}
	}
}
