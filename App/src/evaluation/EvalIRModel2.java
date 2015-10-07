package evaluation;

import java.util.ArrayList;
import java.util.HashMap;

import indexation.TextRepresenter;
import models.IRModel;

public class EvalIRModel2 {
	
	private ArrayList<IRModel> models;
	private ArrayList<EvalMeasure> measures;
	private ArrayList<Query> queries;
	private TextRepresenter stemmer;

	public EvalIRModel2(ArrayList<IRModel> models, ArrayList<EvalMeasure> measures, ArrayList<Query> queries, TextRepresenter stemmer) {
		this.models = models;
		this.measures = measures;
		this.queries = queries;
		this.stemmer = stemmer;
	}
	
	public void process() throws Exception {
		ArrayList<Double> rslt = new ArrayList<Double>();
		for (int id_qu = 0; id_qu < this.queries.size(); id_qu++) {		
			for (int id_me = 0; id_me < this.measures.size(); id_me++) {
				for (int id_mo = 0; id_mo < this.models.size(); id_mo++) {
					HashMap<String, Integer> quStems = this.stemmer.getTextRepresentation(this.queries.get(id_qu).getText());
					HashMap<String, Integer> ranking = this.models.get(id_mo).processRanking(quStems);
					HashMap<String, Double> scores = this.models.get(id_mo).getScores();
					ArrayList<String> ldocs   = new ArrayList<String>();
					ArrayList<Double> lscores = new ArrayList<Double>();
					for (HashMap.Entry<String, Integer> entry : ranking.entrySet()) {
						ldocs.add(entry.getKey());
						lscores.add(scores.get(entry.getKey()));
					}
					
					//rslt.add(this.measures.get(id_me).eval(new IRList(this.queries.get(id_qu), ldocs, lscores)));
				}
			}
		}
	}

}
