package evaluation;

import java.util.ArrayList;

public class APMeasure extends EvalMeasure{

	public APMeasure(){
	}
	
	@Override
	public ArrayList<Double> eval(IRList l) {
		
		ArrayList<Double> toRet = new ArrayList<Double>();
		int nb_relevants = l.getQuery().relevantsSize(); // nb_pertinants
		if (nb_relevants == 0){
			return toRet;
		}
		
		int nb_matchs = 0;
		Double rslt = 0.0;
		
		for (int i = 0; i < l.size(); i++) {
			String id_doc = l.getDocs().get(i);
			boolean docIsRel = l.getQuery().isRelevant(id_doc);
			if (docIsRel) {
				nb_matchs++;
				double precision_i = nb_matchs * 1. / (i+1);
				rslt += precision_i;
			}
		}
		toRet.add(rslt/nb_relevants);
		
		return toRet;
	}

}
