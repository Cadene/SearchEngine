package walk;

import java.util.HashMap;
import java.util.Set;

public class HITS implements RandomWalk {
	private int nIter;
	private HashMap<String, Double> auth;
	private HashMap<String, Double> hubs;
	
	public HITS(int nIter) {
		this.nIter = nIter;
	}
	
	public HITS(){
		this(100);
	}

	@Override
	public HashMap<String, Double> walk(HashMap<String, Set<String>> pred, HashMap<String, Set<String>> succ) {
		int nbNodes = pred.size();
		HashMap<String, Double> newAuth = new HashMap<String, Double>();
		HashMap<String, Double> newHubs = new HashMap<String, Double>(); 
		double normAuth;
		double normHubs;
		this.auth = new HashMap<String, Double>();
		this.hubs = new HashMap<String, Double>();

		for (int i = 0; i < nbNodes; i++ ){
			double tmp = Math.sqrt(1/nbNodes);
			for (String key : pred.keySet()){
				this.auth.put(key, tmp);
				this.hubs.put(key, tmp);
				newAuth.put(key, 1.0);
				newHubs.put(key, 1.0);
			}
		}

		for (int iter = 0; iter < this.nIter; iter++){
			normHubs = 0;
			normAuth = 0;
			for (String nodeI : pred.keySet()){
				double sum = 0;
				for (String nodeJ : pred.get(nodeI)){
					sum += this.hubs.get(nodeJ);
				}
				newHubs.put(nodeI, sum);
				normHubs += sum*sum;
				sum = 0;
				for (String nodeJ : succ.get(nodeI)){
					sum += this.auth.get(nodeJ);
				}
				newAuth.put(nodeI, sum);
				normAuth += sum*sum;
			}
			normAuth = Math.sqrt(normAuth);
			normHubs = Math.sqrt(normHubs);		
			for (String node: pred.keySet()){
				this.auth.put(node, newAuth.get(node) / normAuth);
				this.hubs.put(node, newHubs.get(node) / normHubs);
			}
				
		}
		return this.auth;
	}
}
