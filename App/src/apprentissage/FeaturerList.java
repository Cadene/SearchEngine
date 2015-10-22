package apprentissage;

import java.util.ArrayList;

public class FeaturerList extends Featurer {

	private ArrayList<Featurer> featurers;

	public FeaturerList(ArrayList<Featurer> featurers) {
		super();
		this.featurers = featurers;
	}

	@Override
	public ArrayList<Double> getFeatures(String idDoc, String query) throws Exception {
		for (int i = 0; i < this.featurers.size(); i++) {
			this.features.addAll(this.featurers.get(i).getFeatures(idDoc, query));
		}
		return this.features;
	}

}
