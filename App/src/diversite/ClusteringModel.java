package diversite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;
import models.IRModel;
import models.Weighter;

public abstract class ClusteringModel extends IRModel {
	protected IRModel baseIRModel;
	private Instances dataset;
	protected HashMap<String, Integer> indexOfStem;
	protected HashMap<Integer, String> docsFromIndex;
	protected SimpleKMeans clusterer;
	private HashMap<String, Integer> oldQuery;
	protected int numberOfDocs;
	private FastVector fv;
	protected ArrayList<ArrayList<String>> clusters;
	
	public ClusteringModel(Weighter weighter, IRModel baseIRModel, SimpleKMeans clusterer, int numberOfDocs, boolean rankOrdering) {
		super(weighter);
		this.baseIRModel = baseIRModel;
		this.indexOfStem = new HashMap<String, Integer>();
		this.docsFromIndex = null;
		this.clusterer = clusterer;
		this.clusters = null;
		this.numberOfDocs = numberOfDocs;
		Integer i = 0;
		this.fv = new FastVector();
		for (String stem : weighter.getListStemsIds()){
			this.indexOfStem.put(stem, i);
			i = i + 1;
			fv.addElement(new Attribute(stem));
		}
	}
	
	public ClusteringModel(Weighter weighter, IRModel baseIRModel, SimpleKMeans clusterer, int numberOfDocs) {
		super(weighter);
		this.baseIRModel = baseIRModel;
		this.indexOfStem = new HashMap<String, Integer>();
		this.docsFromIndex = null;
		this.clusterer = clusterer;
		this.clusters = null;
		this.numberOfDocs = numberOfDocs;
		Integer i = 0;
		this.fv = new FastVector();
		for (String stem : weighter.getListStemsIds()){
			this.indexOfStem.put(stem, i);
			i = i + 1;
			fv.addElement(new Attribute(stem));
		}
	}
	
	public ArrayList<ArrayList<String>> getClusters(HashMap<String, Integer> query) throws Exception {
		if ((this.clusters == null) || (query != oldQuery)){
			int indexOfDoc = 0;
			this.docsFromIndex = new HashMap<Integer, String>();
			this.dataset = new Instances("Dataset Relevants", this.fv, 0);
			for (String doc : this.baseIRModel.getRanking(query).keySet()){
				this.docsFromIndex.put(indexOfDoc, doc);
				indexOfDoc += 1;
				Instance instance = new SparseInstance(this.indexOfStem.size());
				for (String stem : this.weighter.getDocWeightsForDoc(doc).keySet()){
					instance.setValue(this.indexOfStem.get(stem).intValue(), this.weighter.getDocWeightsForDoc(doc).get(stem).doubleValue());
				}
				this.dataset.add(instance);
				if (indexOfDoc >= this.numberOfDocs){
					break;
				}
			}
			//System.out.println("Clustering...");
			this.clusterer.buildClusterer(dataset);
			
			this.clusters = new ArrayList<ArrayList<String>>();
			for (int i = 0; i < this.clusterer.getNumClusters(); i++){
				this.clusters.add(new ArrayList<String>());
			}
			
			//for (int i = 0; i < dataset.numInstances(); i++){
			//	this.clusters.get(this.clusterer.clusterInstance(dataset.instance(i))).add(docsFromIndex.get(i));
			//}
			
			int[] assign = this.clusterer.getAssignments();
			for (int i = 0; i < assign.length; i++){
				this.clusters.get(assign[i]).add(docsFromIndex.get(i));
			}
		}
		//System.out.println("End getClusters");
		return this.clusters;
	}

	
	
	protected void processScores(HashMap<String, Integer> query) throws Exception {
		//System.out.println("Start processScores");
		this.queryForScores = query;
	
		LinkedHashMap<String, Double> ranking = new LinkedHashMap<String, Double>();
		
		boolean stop = false;
		ArrayList<ArrayList<String>> clusters = this.getClusters(query);
		
		ArrayList<Iterator<String>> iterators = new ArrayList<Iterator<String>>();
		for (Integer c : getClusterOrdering()){
			iterators.add(clusters.get(c).iterator());
		}
		
		double j = weighter.getListDocsIds().size();
		this.scores = new HashMap<String, Double>();
		while (stop == false)
		{
			stop = true;
			for (Iterator<String> it : iterators){ 
				if (it.hasNext()){
					String doc = it.next();
					this.scores.put(doc, j);
					ranking.put(doc, j);
					//ranking.put(doc, this.baseIRModel.getRanking(query).get(doc));
					//ranking.put(it.next(), j);
					j = j-1;
					stop = false;
				}
			}
		}
		//this.scores = this.baseIRModel.getScores(query);
		/*
		System.out.println("-----------------------");
		System.out.println(this.baseIRModel.getRanking(query).keySet());
		System.out.println(clusters.get(0));
		System.out.println(clusters.get(1));
		System.out.println(clusters.get(2));
		System.out.println(ranking.keySet());
		System.out.println("-----------------------");
		*/
		this.ranking = ranking;
		this.queryForRanking = query;
		//System.out.println("End processScores");
	}
	
	protected abstract int[] getClusterOrdering() throws ClassNotFoundException, IOException, Exception;

}

