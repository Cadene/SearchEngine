package featurer;

import java.util.ArrayList;
import java.util.HashMap;

public class FeaturerList extends Featurer {

	private ArrayList<Featurer> featurers;

	public FeaturerList(ArrayList<Featurer> featurers) {
		super();
		this.featurers = featurers;
		this.size = 0;
		for (Featurer featurer : featurers){
			this.size += featurer.getSize();
		}
	}

	@Override
	public ArrayList<Double> getFeatures(String idDoc, HashMap<String, Integer> query) throws Exception {
		ArrayList<Double> features = new ArrayList<Double>();
		for (Featurer featurer : featurers){
			features.addAll(featurer.getFeatures(idDoc, query));
		}
		return features;
	}
}
