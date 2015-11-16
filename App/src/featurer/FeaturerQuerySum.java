package featurer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import models.Weighter;

public class FeaturerQuerySum extends QueryFeaturer {

	private Weighter weighter;
	
	public FeaturerQuerySum(Weighter weighter){
		super();
		this.weighter = weighter;
	}
	
	@Override
	public void buildFeatures(HashMap<String, Integer> query) throws ClassNotFoundException, IOException {
		if (! this.featuresForQuery.containsKey(query)){
			double sum = 0;
			for (String stem : query.keySet()){
				sum += this.weighter.getSumWeightsForStemInCorpus(stem) * query.get(stem);
			}
			ArrayList<Double> feat = new ArrayList<Double>();
			feat.add(sum);
			this.featuresForQuery.put(query, feat);
		}
	}
}
