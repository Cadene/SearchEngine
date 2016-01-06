package evaluation;

import java.util.ArrayList;

public class PrAtN extends EvalMeasure {

	int n;
	
	public PrAtN(int n) {
		this.n = n;
	}
	
	@Override
	public ArrayList<Double> eval(IRList l) {
		
		ArrayList<Double> toRet = new ArrayList<Double>();
		
		double nb_matchs = 0;
		
		for (int i = 0; i < this.n && i < l.size(); i++) {
			String id_doc = l.getDocs().get(i);
			if (l.getQuery().isRelevant(id_doc)) {
				nb_matchs++;
			}
		}
		toRet.add(nb_matchs/this.n);
		
		return toRet;
	}
	
}
