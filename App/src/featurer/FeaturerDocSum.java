package featurer;

import java.io.IOException;
import java.util.ArrayList;

import models.Weighter;

public class FeaturerDocSum extends DocFeaturer {

	private Weighter weighter;
	
	public FeaturerDocSum(Weighter weighter){
		super();
		this.weighter = weighter;
	}
	
	@Override
	public void buildFeatures(String idDoc) throws ClassNotFoundException, IOException {
		if (! this.featuresForDoc.containsKey(idDoc)){
			ArrayList<Double> feat = new ArrayList<Double>();
			feat.add(this.weighter.getSumWeightsForDocInCorpus(idDoc));
			this.featuresForDoc.put(idDoc, feat);
		}
	}
}
