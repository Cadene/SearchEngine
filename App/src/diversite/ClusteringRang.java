package diversite;

import utils.ArrayUtils;
import weka.clusterers.SimpleKMeans;
import models.IRModel;
import models.Weighter;

public class ClusteringRang extends ClusteringModel {

	public ClusteringRang(Weighter weighter, IRModel baseIRModel, SimpleKMeans clusterer, int numberOfDocs) {
		super(weighter, baseIRModel, clusterer, numberOfDocs);
	}

	@Override
	protected int[] getClusterOrdering() throws Exception {
		int nbClusters = this.clusters.size();
		double[] scoreClusters = new double[nbClusters];
		for (int i = 0; i < nbClusters; i++){
			scoreClusters[i] = this.baseIRModel.getScores(this.queryForScores).get(this.clusters.get(i).get(0));
		}
		return ArrayUtils.argsort(scoreClusters, false);
	}

	@Override
	public void setParameters(double... parameters) {
		// TODO Auto-generated method stub
		
	}

}
