package evaluation;

import java.util.ArrayList;
import java.util.HashMap;

import indexation.TextRepresenter;
import models.IRModel;
import models.Vectoriel;
import models.WeighterTfTf;

public class EvalIRModel {

	private IRModel model;
	private EvalMeasure measure;
	private ArrayList<Query> queries;
	private TextRepresenter stemmer;

	public EvalIRModel(IRModel model, EvalMeasure measure, ArrayList<Query> queries, TextRepresenter stemmer) {
		this.model = model;
		this.measure = measure;
		this.queries = queries;
		this.stemmer = stemmer;
	}

	public void process() throws Exception {
		ArrayList<ArrayList<Double>> rslt = new ArrayList<ArrayList<Double>>();
		ArrayList<Double> mean = new ArrayList<Double>();
		ArrayList<Double> std = new ArrayList<Double>();
		for (int id_qu = 0; id_qu < this.queries.size(); id_qu++) {
			HashMap<String, Integer> quStems = this.stemmer.getTextRepresentation(this.queries.get(id_qu).getText());
			HashMap<String, Integer> ranking = this.model.processRanking(quStems);
			HashMap<String, Double> scores = this.model.getScores();
			ArrayList<String> ldocs   = new ArrayList<String>();
			ArrayList<Double> lscores = new ArrayList<Double>();
			for (HashMap.Entry<String, Integer> entry : ranking.entrySet()) {
				System.out.println(entry.getKey());
				ldocs.add(entry.getKey());
				lscores.add(scores.get(entry.getKey()));
			}
			rslt.add(this.measure.eval(new IRList(this.queries.get(id_qu), ldocs, lscores)));
			for (int i = 0; i < rslt.size(); i++) {
				ArrayList<Double> res = rslt.get(i);
				for (int j = 0; j < res.size(); j++) {
					if (mean.size() <= j)
						mean.add(res.get(j));
					else
						mean.set(j, mean.get(j) + res.get(j));
					if (i == rslt.size() - 1)
						mean.set(j, mean.get(j) / rslt.size());
				}
			}
			for (int i = 0; i < rslt.size(); i++) {
				ArrayList<Double> res = rslt.get(i);
				for (int j = 0; j < res.size(); j++) {
					if (std.size() <= j)
					{
						double d = mean.get(j) - res.get(j);
						std.add(d*d);
					}
					else
					{
						double d = mean.get(j) - res.get(j);
						std.set(j, std.get(j) + d*d);
					}
					if (i == rslt.size() - 1)
						std.set(j, Math.sqrt(std.get(j)));
				}
			}
		}
			
	}

	public static void main(String[] args) {
		Index index = new Index();
		Weighter weighter = new WeighterTfTf();
		IRModel model = new Vectoriel();
		EvalMeasure measure = new EvalMeasure();
		ArrayList<Query> queries = new ArrayList<Query>;
		TextRepresenter stemmer = new Stemmer();
		
		EvalIRModel evalModel = new EvalIRModel(model, measure, queries, stemmer);
	}
}
