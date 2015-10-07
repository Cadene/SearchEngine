package evaluation;

import java.util.ArrayList;
import java.util.HashMap;

import indexation.TextRepresenter;
import models.IRModel;

public class EvalIRModel {

	private IRModel model;
	private EvalMeasure measure;
	private ArrayList<Query> queries;
	private TextRepresenter stemmer;
	private ArrayList<Double> mean = new ArrayList<Double>();
	private ArrayList<Double> std = new ArrayList<Double>();

	public EvalIRModel(IRModel model, EvalMeasure measure, ArrayList<Query> queries, TextRepresenter stemmer) {
		this.model = model;
		this.measure = measure;
		this.queries = queries;
		this.stemmer = stemmer;
	}

	public ArrayList<Double> getMean() {
		return this.mean;
	}
	
	public ArrayList<Double> getStd() {
		return this.std;
	}
	
	
	public void process() throws Exception {
		ArrayList<ArrayList<Double>> rslt = new ArrayList<ArrayList<Double>>();
		for (int id_qu = 0; id_qu < this.queries.size(); id_qu++) {
			HashMap<String, Integer> quStems = this.stemmer.getTextRepresentation(this.queries.get(id_qu).get("text"));
			HashMap<String, Integer> ranking = this.model.processRanking(quStems);
			HashMap<String, Double> scores = this.model.getScores();
			ArrayList<String> ldocs   = new ArrayList<String>();
			ArrayList<Double> lscores = new ArrayList<Double>();
			for (HashMap.Entry<String, Integer> entry : ranking.entrySet()) {
				ldocs.add(entry.getKey());
				lscores.add(scores.get(entry.getKey()));
			}
			rslt.add(this.measure.eval(new IRList(this.queries.get(id_qu), ldocs, lscores)));
			for (int i = 0; i < rslt.size(); i++) {
				ArrayList<Double> res = rslt.get(i);
				for (int j = 0; j < res.size(); j++) {
					if (this.mean.size() <= j)
						this.mean.add(res.get(j));
					else
						this.mean.set(j, this.mean.get(j) + res.get(j));
					if (i == rslt.size() - 1)
						this.mean.set(j, this.mean.get(j) / rslt.size());
				}
			}
			for (int i = 0; i < rslt.size(); i++) {
				ArrayList<Double> res = rslt.get(i);
				for (int j = 0; j < res.size(); j++) {
					if (this.std.size() <= j)
					{
						double d = this.mean.get(j) - res.get(j);
						this.std.add(d*d);
					}
					else
					{
						double d = this.mean.get(j) - res.get(j);
						this.std.set(j, this.std.get(j) + d*d);
					}
					if (i == rslt.size() - 1)
						this.std.set(j, Math.sqrt(this.std.get(j)));
				}
			}
		}
		
		
		
		
	}
}
