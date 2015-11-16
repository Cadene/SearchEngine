package featurer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import walk.RandomWalk;

public class FeaturerDocWalkRank extends DocFeaturer {

	private RandomWalk walker;
	private HashMap<String, Set<String>> pred;
	private HashMap<String, Set<String>> succ;
	
	public FeaturerDocWalkRank(RandomWalk walker, HashMap<String, Set<String>> pred, HashMap<String, Set<String>> succ){
		this.walker = walker;
		this.pred = pred;
		this.succ = succ;
	}
	
	@Override
	public void buildFeatures(String idDoc) throws ClassNotFoundException,
			IOException {
		if (!this.featuresForDoc.containsKey(idDoc)){
			HashMap<String, Double> scores = walker.walk(this.pred, this.succ);
			for (HashMap.Entry<String, Double> entry : scores.entrySet()){
				ArrayList<Double> feature = new ArrayList<Double>();
				feature.add(entry.getValue());
				this.featuresForDoc.put(entry.getKey(), feature);
			}
		}
	}
}
