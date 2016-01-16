package diversite;

import utils.ArrayUtils;
import weka.clusterers.SimpleKMeans;
import models.IRModel;
import models.Weighter;

public class ClusteringNbDocsD extends ClusteringModel {

	public ClusteringNbDocsD(Weighter weighter, IRModel baseIRModel, SimpleKMeans clusterer, int numberOfDocs) {
		super(weighter, baseIRModel, clusterer, numberOfDocs);
	}

	@Override
	protected int[] getClusterOrdering() throws Exception {
		int nbClusters = this.clusters.size();
		float[] sizeClusters = new float[nbClusters];
		for (int i = 0; i < nbClusters; i++){
			sizeClusters[i] = this.clusters.get(i).size();
		}
		return ArrayUtils.argsort(sizeClusters, false);
	}

	@Override
	public void setParameters(double... parameters) {
		// TODO Auto-generated method stub
		
	}

}
