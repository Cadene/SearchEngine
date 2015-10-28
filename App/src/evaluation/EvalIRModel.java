package evaluation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import indexation.TextRepresenter;
import models.IRModel;

public class EvalIRModel {

	private IRModel model;
	private EvalMeasure measure;
	private ArrayList<Query> queries;
	private TextRepresenter stemmer;
	private ArrayList<Double> mean;
	private ArrayList<Double> std;
	private double evalValue;
	
	public EvalIRModel(IRModel model, EvalMeasure measure, ArrayList<Query> queries, TextRepresenter stemmer) {
		this.model = model;
		this.measure = measure;
		this.queries = queries;
		this.stemmer = stemmer;
	}

	public double getEvalValue() {
		return this.evalValue;
	}
	
	public ArrayList<Double> getMean() {
		return this.mean;
	}
	
	public ArrayList<Double> getStd() {
		return this.std;
	}
	
	public double eval() throws Exception {
		ArrayList<ArrayList<Double>> rslt = new ArrayList<ArrayList<Double>>();
		for (int id_qu = 0; id_qu < this.queries.size(); id_qu++) {
			Query query = this.queries.get(id_qu);
			if (query.getRelevants().size() != 0){
				HashMap<String, Integer> quStems = this.stemmer.getTextRepresentation(this.queries.get(id_qu).get("text"));
				LinkedHashMap<String, Double> ranking = this.model.getRanking(quStems);
				ArrayList<String> ldocs   = new ArrayList<String>();
				ArrayList<Double> lscores = new ArrayList<Double>();
				for (HashMap.Entry<String, Double> entry : ranking.entrySet()) {
					ldocs.add(entry.getKey());
					lscores.add(entry.getValue());
				}
				rslt.add(this.measure.eval(new IRList(this.queries.get(id_qu), ldocs, lscores)));
			}
		}
		
		// Moyenne
		this.mean = new ArrayList<Double>();
		ArrayList<Double> res = rslt.get(0);
		for (int j = 0; j < res.size(); j++){
			this.mean.add(res.get(j));
		}
		for (int i = 1; i < rslt.size(); i++) {
			res = rslt.get(i);
			for (int j = 0; j < res.size(); j++) {
				this.mean.set(j, this.mean.get(j) + res.get(j));
			}
		}
		for (int j = 0; j < res.size(); j++){
			this.mean.set(j, this.mean.get(j) / rslt.size());
		}
				
		// Std
		this.std = new ArrayList<Double>();
		res = rslt.get(0);
		for (int j = 0; j < res.size(); j++) {
			double d = this.mean.get(j) - res.get(j);
			this.std.add(d*d);
		}
		for (int i = 1; i < rslt.size(); i++) {
			res = rslt.get(i);
			for (int j = 0; j < res.size(); j++) {
				double d = this.mean.get(j) - res.get(j);
				this.std.set(j, this.std.get(j) + d*d);
			}
		}
		for (int j = 0; j < res.size(); j++){
			this.std.set(j, Math.sqrt(this.std.get(j)));
		}
		
		this.evalValue = 0;
		for (double s : this.mean){
			this.evalValue += s;
		}
		this.evalValue /= this.mean.size();
		return this.evalValue;
	}
}
