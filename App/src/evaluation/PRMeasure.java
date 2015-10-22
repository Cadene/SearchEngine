package evaluation;

import java.util.ArrayList;

public class PRMeasure extends EvalMeasure {

	private int nbLevels;

	public PRMeasure(int nbLevels){
		this.nbLevels = nbLevels;
	}
	
	@Override
	public ArrayList<Double> eval(IRList l) {
		
		ArrayList<Double> precision = new ArrayList<Double>();
		ArrayList<Double> recall = new ArrayList<Double>();
		int nb_relevants = l.getQuery().relevantsSize();
		int nb_matchs = 0;
		
		if (nb_relevants == 0)
		{
			return null;
		}
		if (l.size() == 0)
		{
			return null;
		}
		
		for (int i = 0; i < l.size(); i++) {
			String id_doc = l.getDocs().get(i);
			int nb_retrieved = i + 1;
			if (l.getQuery().isRelevant(id_doc)) {
				nb_matchs++;
			}
			precision.add( nb_matchs * 1.0 / nb_retrieved );
			recall.add( nb_matchs * 1.0 / nb_relevants );
		}
		
		ArrayList<Double> k = new ArrayList<Double>();
		for (int i = 1; i <= this.nbLevels; i++) {
			k.add( i * 1.0 / this.nbLevels);
		}
		
		//optimisable
		ArrayList<Double> rslt = new ArrayList<Double>();
		for (int i = 0; i < k.size(); i++){
			double measure_max = 0.0;
			for (int j = 0; j < recall.size(); j++) {
				if (recall.get(j) >= k.get(i)) {
					if (precision.get(j) > measure_max) // max 
						measure_max = precision.get(j);
				}
			}
			rslt.add(measure_max);
		}
		return rslt;
	}
}
