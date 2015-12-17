package evaluation;

import java.util.ArrayList;

public class PMeasure extends EvalMeasure {

	private int n;

	public PMeasure(int n) {
		this.n = n;
	}

	@Override
	public ArrayList<Double> eval(IRList l) {
		
		ArrayList<Double> rslt = new ArrayList<Double>();
		int nb_relevent = 0;
		
		// if l.size() < this.n bug
		for (int i = 0; i < this.n; i++) {
			String id_doc = l.getDocs().get(i);
			if (l.getQuery().isRelevant(id_doc)) {
				nb_relevent ++;
			}
		}
		
		rslt.add(nb_relevent * 1.0 / this.n);
		return rslt;
	}

}
