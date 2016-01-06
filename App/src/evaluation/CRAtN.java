package evaluation;

import java.util.ArrayList;
import java.util.HashSet;

public class CRAtN extends EvalMeasure {

	int n;
	
	public CRAtN(int n) {
		this.n = n;
	}
	
	@Override
	public ArrayList<Double> eval(IRList l) {
		
		ArrayList<Double> toRet = new ArrayList<Double>();
		HashSet<Integer> clustersRetrouves = new HashSet<Integer>();
		
		for (int i = 0; i < this.n && i < l.size(); i++) {
			String id_doc = l.getDocs().get(i);
			if (l.getQuery().isRelevant(id_doc)) {
				clustersRetrouves.add(l.getQuery().getIdSubtopic(id_doc));
			}
		}
		toRet.add(new Double(clustersRetrouves.size()) / l.getNbSubtopics());
		
		return toRet;
	}
	
}
