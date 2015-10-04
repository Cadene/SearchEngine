package evaluation;

import java.util.ArrayList;

public class APMeasure extends EvalMeasure{

	public APMeasure(){
	}
	
	@Override
	public ArrayList<Double> eval(IRList l) {
		
		Double rslt = 0.0;
		
		int nb_relevants = l.getQuery().relevantsSize(); // nb_pertinants
		int nb_matchs = 0;
		for (int i = 0; i < l.size(); i++) {
			String id_doc = l.getDocs().get(i);
			boolean docIsRel = l.getQuery().isRelevant(id_doc);
			if (docIsRel) {
				nb_matchs++;
			}
			int nb_retrieved = i + 1;
			
			double precision_i = 0.0;
			if (nb_retrieved != 0){
				precision_i = nb_matchs * 1.0 / nb_retrieved;
			}
			
			if (docIsRel) {
				rslt += precision_i;
			}
		}
		
		if (nb_relevants != 0){
			rslt /= nb_relevants;
		}
		
		ArrayList<Double> toRet = new ArrayList<Double>();
		toRet.add(rslt);
		return toRet;
	}

}
