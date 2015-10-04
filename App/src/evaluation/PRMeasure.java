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
		for (int i = 0; i < l.size(); i++) {
			String id_doc = l.getDocs().get(i);
			if (l.getQuery().isRelevant(id_doc)) {
				nb_matchs++;
			}
			int nb_retrieved = i + 1;
			
			if (nb_retrieved == 0) {
				precision.add( 0.0 );
			} else {
				precision.add( nb_matchs * 1.0 / nb_retrieved );
			}
			
			if (nb_relevants == 0) {
				recall.add( 1.0 );
			} else {
				recall.add( nb_matchs * 1.0 / nb_relevants );
			}
		}
		
		ArrayList<Double> k = new ArrayList<Double>();
		for (int i = 0; i < this.nbLevels; i++) {
			k.add( i * 1.0 / (this.nbLevels - 1) ); // -1 pour avoir 0 et 1
		}
		
		ArrayList<Double> rslt = new ArrayList<Double>();
		double measure_max = 0.0;
		for (int i = 0; i < k.size(); i++){
			if (recall.get(i) >= k.get(i)){
				if (precision.get(i) > measure_max) { // max 
					measure_max = precision.get(i);
				}
			}
			rslt.add(measure_max);
		}
		
		return rslt;
	}
	

}
