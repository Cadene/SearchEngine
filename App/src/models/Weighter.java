package models;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import indexation.Index;

public abstract class Weighter {
	protected Index index;
	private Double sumWeightsInCorpus;
	private HashMap<String, Double> sumWeightsForDocs;
	private HashMap<String, Double> sumWeightsForStems;
	private HashMap<String, Double> normWeightsForDocs;
	
	public Weighter(Index index) {
		this.index = index;
		this.sumWeightsInCorpus = null;
		this.sumWeightsForDocs = new HashMap<String, Double>();
		this.sumWeightsForStems = new HashMap<String, Double>();
		this.normWeightsForDocs = new HashMap<String, Double>();
	}
	
	public abstract HashMap<String, Double> getDocWeightsForDoc(String idDoc) throws ClassNotFoundException, IOException;
	public abstract HashMap<String, Double> getDocWeightsForStem(String stem) throws ClassNotFoundException, IOException;
	public abstract HashMap<String, Double> getWeightsForQuery(HashMap< String, Integer> query) throws ClassNotFoundException, IOException;
	
	public Set<String> getListDocsIds() {
		return index.getListDocsIds();
	}
	
	public Set<String> getListStemsIds() {
		return index.getListStemsIds();
	}
	
	public Double getNormForDoc(String docId) throws ClassNotFoundException, IOException{
		if (!this.normWeightsForDocs.containsKey(docId)){
			HashMap<String, Double> weightsForDocs = this.getDocWeightsForDoc(docId);				
			double norm = 0;
			for (HashMap.Entry<String, Double> entry: weightsForDocs.entrySet()) {
					norm = norm + (entry.getValue()) * (entry.getValue());
				}
				this.normWeightsForDocs.put(docId, Math.sqrt(norm));
			}
		return this.normWeightsForDocs.get(docId);
	}
	
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
		if (!this.sumWeightsForDocs.containsKey(docId))
		{
			double sum = .0;
			for (HashMap.Entry<String, Double> entry : this.getDocWeightsForDoc(docId).entrySet())
				sum += entry.getValue();
			this.sumWeightsForDocs.put(docId, sum);
		}
		return this.sumWeightsForDocs.get(docId);
	}
	
	public double getSumWeightsForStemInCorpus(String stem) throws ClassNotFoundException, IOException {
		if (!this.sumWeightsForStems.containsKey(stem))
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
