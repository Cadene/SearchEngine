package diversite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import models.IRModel;
import models.Weighter;
import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;

public abstract class ClusteringModel extends DiversiteModel {
	private Dataset dataset;
	private HashMap<String, Integer> indexOfStem;
	private HashMap<Integer, String> docsFromIndex;
	private Clusterer clusterer;
	Dataset [] clusters;
	
	public ClusteringModel(Weighter weighter, IRModel baseIRModel, Clusterer clusterer) {
		super(weighter, baseIRModel);
		this.dataset = new DefaultDataset();
		this.indexOfStem = new HashMap<String, Integer>();
		this.docsFromIndex = new HashMap<Integer, String>();
		this.clusterer = clusterer;
		this.clusters = null;
		Integer i = 0;
		for (String stem : weighter.getListStemsIds()){
			this.indexOfStem.put(stem, i);
			i = i + 1;
		}
	}

	public Dataset[] getClusters(HashMap<String, Integer> query) throws Exception {
		System.out.println("Start getClusters");
		if (this.clusters == null){
			int indexOfDoc = 0;
			for (String doc : this.baseIRModel.getRanking(query).keySet()){
				this.docsFromIndex.put(indexOfDoc, doc);
				indexOfDoc += 1;
				Instance instance = new SparseInstance(this.weighter.getListStemsIds().size());
				for (String stem : this.weighter.getDocWeightsForDoc(doc).keySet()){
					instance.put(this.indexOfStem.get(stem), this.weighter.getDocWeightsForDoc(doc).get(stem));
				}
				this.dataset.add(instance);
			}
			System.out.println("Clustering...");
			this.clusters = this.clusterer.cluster(dataset);
		}
		System.out.println("End getClusters");
		return this.clusters;
	}

	protected void processScores(HashMap<String, Integer> query) throws Exception {
		System.out.println("Start processScores");
		this.queryForScores = query;
	
		LinkedHashMap<String, Double> ranking = new LinkedHashMap<String, Double>();
		boolean stop = false;
		Dataset [] clusters = this.getClusters(query);
		ArrayList<Iterator<Instance>> iterators = new ArrayList<Iterator<Instance>>();
		for (Integer c : getClusterOrdering(query)){
			iterators.add(clusters[c].iterator());
		}
		
		while (stop == false)
		{
			stop = true;
			for (Iterator<Instance> it : iterators){ 
				if (it.hasNext()){
					String doc = this.docsFromIndex.get(it.next().getID());
					ranking.put(doc, this.baseIRModel.getRanking(query).get(doc));
					stop = false;
				}
			}
		}
		this.ranking = ranking;
		this.scores = ranking;
		System.out.println("End processScores");
	}
	
	protected abstract int[] getClusterOrdering(HashMap<String, Integer> query) throws ClassNotFoundException, IOException, Exception;

}
