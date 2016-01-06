package diversite;

import java.util.HashMap;

import utils.ArrayUtils;
import models.IRModel;
import models.Weighter;
import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.core.Dataset;

public class SizeClustering extends ClusteringModel {

	public SizeClustering(Weighter weighter, IRModel baseIRModel, Clusterer clusterer) {
		super(weighter, baseIRModel, clusterer);
	}

	@Override
	protected int[] getClusterOrdering(HashMap<String, Integer> query) throws Exception {
		Dataset[] clusters = this.getClusters(query);
		int nbClusters = clusters.length;
		float[] sizeClusters = new float[nbClusters];
		for (int i = 0; i < nbClusters; i++){
			sizeClusters[i] = clusters[i].size();
		}
		return ArrayUtils.argsort(sizeClusters);
	}

	@Override
	public void setParameters(double... parameters) {
		// TODO Auto-generated method stub
		
	}

}
