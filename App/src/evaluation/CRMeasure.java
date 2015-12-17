package evaluation;

import java.util.ArrayList;

public class CRMeasure extends EvalMeasure {
	
	private int n;

	public CRMeasure(int n) {
		this.n = n;
	}

	@Override
	public ArrayList<Double> eval(IRList l) {
		
		ArrayList<Double> rslt = new ArrayList<Double>();
		int nb_thema_rtrnd = 0;
		int nb_thema_total = 0;
		
		// if l.size() < this.n bug
		for (int i = 0; i < this.n; i++) {
			String id_doc = l.getDocs().get(i);
			if (l.getQuery().isRelevant(id_doc)) {
				nb_thema_rtrnd ++;
			}
		}
		
		rslt.add(nb_thema_rtrnd * 1.0 / nb_thema_total);
		return rslt;
	}

}
