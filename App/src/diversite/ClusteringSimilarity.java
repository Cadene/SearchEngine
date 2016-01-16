package diversite;

import java.util.HashMap;
import java.util.Map.Entry;

import utils.ArrayUtils;
import weka.clusterers.SimpleKMeans;
import models.IRModel;
import models.Weighter;

public class ClusteringSimilarity extends ClusteringModel {

	public ClusteringSimilarity(Weighter weighter, IRModel baseIRModel, SimpleKMeans clusterer, int numberOfDocs) {
		super(weighter, baseIRModel, clusterer, numberOfDocs);
	}

	@Override
	protected int[] getClusterOrdering() throws Exception {
		int nbClusters = this.clusters.size();

		HashMap<String, Double> q = weighter.getWeightsForQuery(this.queryForScores);
		double[] queryArray = new double[this.weighter.getListStemsIds().size()];
		for (Entry<String,Double> entry : q.entrySet()){
			if (this.indexOfStem.containsKey(entry.getKey())){
				queryArray[this.indexOfStem.get(entry.getKey())] = entry.getValue();
			}	
		}
		
		
		float[] simClusters = new float[nbClusters];
		
		
		for (int i = 0; i < nbClusters; i++){
			simClusters[i] = (float) ArrayUtils.cosinus(queryArray, this.clusterer.getClusterCentroids().instance(i).toDoubleArray());
		}
		return ArrayUtils.argsort(simClusters, false);
	}

	@Override
	public void setParameters(double... parameters) {
		// TODO Auto-generated method stub
		
	}

}
