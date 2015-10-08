package models;

import java.io.IOException;
import java.util.HashMap;

import indexation.Index;

public abstract class Weighter {
	protected Index index;
	private Double sumWeightsInCorpus;
	private HashMap<String, Double> sumWeightsForDocs;
	private HashMap<String, Double> sumWeightsForStems;
	
	public Weighter(Index index) {
		this.index = index;
		this.sumWeightsInCorpus = null;
		this.sumWeightsForDocs = new HashMap<String, Double>();
		this.sumWeightsForStems = new HashMap<String, Double>();
	}
	
	public abstract HashMap<String, Double> getDocWeightsForDoc(String idDoc) throws ClassNotFoundException, IOException;
	public abstract HashMap<String, Double> getDocWeightsForStem(String stem) throws ClassNotFoundException, IOException;
	public abstract HashMap<String, Double> getWeightsForQuery(HashMap< String, Integer> query) throws ClassNotFoundException, IOException;
	
	public double getSumWeightsInCorpus() throws ClassNotFoundException, IOException {
		if (this.sumWeightsInCorpus == null){
			this.sumWeightsInCorpus = 0.;
			for (String docId : this.index.getListDocsIds()){
				for (HashMap.Entry<String, Double> entry : this.getDocWeightsForDoc(docId).entrySet())
					this.sumWeightsInCorpus += entry.getValue();
			}
		}
		return this.sumWeightsInCorpus;
	}
	
	public double getSumWeightsForDocInCorpus(String docId) throws ClassNotFoundException, IOException {
		if (this.sumWeightsForDocs.get(docId) == null)
		{
			double sum = .0;
			for (HashMap.Entry<String, Double> entry : this.getDocWeightsForDoc(docId).entrySet())
				sum += entry.getValue();
			this.sumWeightsForDocs.put(docId, sum);
		}
		return this.sumWeightsForDocs.get(docId);
	}
	
	public double getSumWeightsForStemInCorpus(String stem) throws ClassNotFoundException, IOException {
		if (this.sumWeightsForStems.get(stem) == null)
		{
			double sum = .0;
			HashMap<String, Double> docWeightsForStem = this.getDocWeightsForStem(stem);
			if (docWeightsForStem == null) {
				this.sumWeightsForStems.put(stem, null);
			}
			else {
				for (HashMap.Entry<String, Double> entry : docWeightsForStem.entrySet()){
					sum += entry.getValue();
				}
				this.sumWeightsForStems.put(stem, sum);
			}
		}
		return this.sumWeightsForStems.get(stem);
	}
}
