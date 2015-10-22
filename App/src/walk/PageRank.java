package walk;

import java.util.HashMap;
import java.util.Set;

public class PageRank implements RandomWalk {
	
	private double d;
	private int nIter;
	private HashMap<String, Double> mu;

	public PageRank(double d, int nIter) {
		this.d = d;
		this.nIter = nIter;
		this.mu = new HashMap<String, Double>();
	}
	
	public PageRank(double d) {
		this(d, 100);
	}
	
	@Override
	public HashMap<String, Double> walk(HashMap<String, Set<String>> pred, HashMap<String, Set<String>> succ) {
		int nbNodes = pred.size();
		HashMap<String, Double> newMu = new HashMap<String, Double>();
		for (String key : pred.keySet()){
			this.mu.put(key, 1.0/nbNodes);
			newMu.put(key, 1.0);
		}
		for (int iter = 0; iter < this.nIter; iter++){
			double tmp = (1.-this.d)/nbNodes;
			for (String nodeI : pred.keySet()){
				double sum = 0;
				for (String nodeJ : pred.get(nodeI)){
					sum += this.mu.get(nodeI) / succ.get(nodeJ).size();
				}
				sum *= this.d;
				newMu.put(nodeI, tmp + sum);
			}
			HashMap<String, Double> tmpMu = this.mu;
			this.mu = newMu;
			newMu = tmpMu;
		}
		return mu;
	}

}
