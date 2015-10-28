package featurer;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class DocFeaturer extends Featurer {

	protected HashMap<String, ArrayList<Double>> featuresForDoc;
	
	public DocFeaturer() {
		super();
		this.featuresForDoc = new HashMap<String, ArrayList<Double>>();
	}

	public abstract void buildFeatures(String idDoc);
	
	@Override
	public ArrayList<Double> getFeatures(String idDoc, HashMap<String, Integer> query){
		if (! this.featuresForDoc.containsKey(idDoc)){
			this.buildFeatures(idDoc);
		}
		return this.featuresForDoc.get(idDoc);
	}
}
