package featurer;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class DocAndQueryFeaturer extends Featurer {

	protected HashMap<String, HashMap<HashMap<String, Integer>, ArrayList<Double>>> docAndQueryfeatures;
	
	public DocAndQueryFeaturer() {
		super();
		this.docAndQueryfeatures = new HashMap<String, HashMap<HashMap<String, Integer>, ArrayList<Double>>>();
	}

	public abstract void buildFeatures(String idDoc, HashMap<String, Integer> query) throws Exception;
	
	@Override
	public ArrayList<Double> getFeatures(String idDoc, HashMap<String, Integer> query) throws Exception{
		if ((!this.docAndQueryfeatures.containsKey(idDoc)) || (!this.docAndQueryfeatures.get(idDoc).containsKey(query))){
			this.buildFeatures(idDoc, query);
		}
		return this.docAndQueryfeatures.get(idDoc).get(query);
	}
}
