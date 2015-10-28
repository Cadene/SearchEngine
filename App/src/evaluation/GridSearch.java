package evaluation;

import indexation.TextRepresenter;

import java.util.ArrayList;

import models.IRModel;

public class GridSearch {
	private IRModel model;
	private EvalMeasure mesure;
	private ArrayList<Query> queries;
	private TextRepresenter stemmer;
	
	public GridSearch(IRModel model, EvalMeasure mesure, ArrayList<Query> queries, TextRepresenter stemmer){
		this.model = model;
		this.mesure = mesure;
		this.queries = queries;
		this.stemmer = stemmer;
	}
	
	public void setModel(IRModel model) {
		this.model = model;
	}
	
	public void setMesure(EvalMeasure mesure) {
		this.mesure = mesure;
	}
	
	public void setQueries(ArrayList<Query> queries) {
		this.queries = queries;
	}
	
	public void setStemmer(TextRepresenter stemmer){
		this.stemmer = stemmer;
	}

	public double[] optimize(ArrayList<double[]> listParameters) throws Exception{
		double[] bestParameters = null;
		double bestScore = 0;
		for (double[] parameters : listParameters){
			this.model.setParameters(parameters);
			EvalIRModel evalModel = new EvalIRModel(this.model, this.mesure, this.queries, this.stemmer);
			double score = evalModel.eval();
			
			for (double s : evalModel.getMean()){
				score += s;
			}
			if (score > bestScore){
				bestParameters = parameters;
				bestScore = score;
			}
		}
		this.model.setParameters(bestParameters);
		System.out.println("Best Score: " + bestScore);
		System.out.println("Best Parameters: ");
		for (double p : bestParameters){
			System.out.println(p);
		}
		return bestParameters;
	}
}
