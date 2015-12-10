package metamodel;

import indexation.TextRepresenter;

import java.util.ArrayList;
import java.util.HashMap;

import models.Weighter;
import evaluation.Query;
import featurer.Featurer;

public class MetamodelLineaire extends Metamodel {

	private TextRepresenter stemmer;
	private int tmax;
	private double[] weights;
	private double learningRate;
	private double l2;
	
	public MetamodelLineaire(Weighter weighter, TextRepresenter stemmer, Featurer featurer, int tmax, double learningRate, double l2) {
		super(weighter, featurer);
		this.stemmer = stemmer;
		this.tmax = tmax;
		this.learningRate = learningRate;
		this.l2 = l2;
		this.weights = new double[featurer.getSize()];
		for (int i = 0; i < this.weights.length; i++){
			this.weights[i] = Math.random() * 2 - 1;
		}
	}
	
	public double[] getWeights(){
		return this.weights;
	}

	public void train(ArrayList<Query> queries) throws Exception{
		double loss = 0;
		for (int t = 0; t < tmax; t++){
			Query query = queries.get((int) Math.floor(Math.random() * queries.size()));
			if ((query.getRelevants().size() > 0) && query.getRelevants().size() != weighter.getListDocsIds().size()){
				String idRelevant = (String) query.getRelevants().keySet().toArray()[(int) Math.floor(Math.random() * query.getRelevants().size())];
				String idNotRelevant = (String) weighter.getListDocsIds().toArray()[(int) Math.floor(Math.random() * weighter.getListDocsIds().size())];
				while (query.isRelevant(idNotRelevant)){
					idNotRelevant = (String) weighter.getListDocsIds().toArray()[(int) Math.floor(Math.random() * weighter.getListDocsIds().size())];
				}
				HashMap<String, Integer> quStems = this.stemmer.getTextRepresentation(query.get("text"));
				ArrayList<Double> featuresRelevant    =  featurer.getFeatures(idRelevant,    quStems);
				ArrayList<Double> featuresNotRelevant =  featurer.getFeatures(idNotRelevant, quStems);
				double scoreRelevant    = 0;
				double scoreNotRelevant = 0;
				for (int i = 0; i < this.weights.length; i++){
					scoreRelevant    += this.weights[i] * featuresRelevant.get(i);
					scoreNotRelevant += this.weights[i] * featuresNotRelevant.get(i);
				}
				if (1 - scoreRelevant + scoreNotRelevant > 0){
					for (int i = 0; i < this.weights.length; i++){
						this.weights[i] += this.learningRate * (featuresRelevant.get(i) - featuresNotRelevant.get(i));
					}
				}
				double normW = 0;
				for (int i = 0; i < this.weights.length; i++){
					this.weights[i] *= (1 - 2 * this.l2 * this.learningRate);
					normW += this.weights[i] * this.weights[i];
				}
				loss += Math.max(0, 1 - scoreRelevant + scoreNotRelevant) + this.l2 * normW;
			}
			if (t % 10000 == 0){
				System.out.println(t + ":\t"+ loss);
				loss = 0;
			}	
		}
	}
	
	@Override
	public void setParameters(double... parameters) {
		this.learningRate = parameters[0];
		this.l2 = parameters[1];
	}

	@Override
	protected void processScores(HashMap<String, Integer> query)
			throws Exception {
		this.scores = new HashMap<String, Double>();
		for (String idDoc : weighter.getListDocsIds()){
			ArrayList<Double> features =  featurer.getFeatures(idDoc, query);
			double score = 0;
			for (int i = 0; i < this.weights.length; i++){
				score += this.weights[i] * features.get(i);
			}
			this.scores.put(idDoc, score);
		}
		
		// TODO Auto-generated method stub

	}

}
