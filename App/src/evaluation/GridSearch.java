package evaluation;

import indexation.Index;
import indexation.Stemmer;
import indexation.TextRepresenter;

import java.util.ArrayList;

import models.IRModel;
import models.IRWithRandomWalk;
import models.LanguageModel;
import models.Okapi;
import models.Vectoriel;
import models.Weighter;
import models.WeighterTfIdf;
import models.WeighterTfInd;
import models.WeighterTfTf;
import walk.HITS;
import walk.PageRank;

public class GridSearch {
	private IRModel model;
	private EvalMeasure mesure;
	private ArrayList<Query> queries;
	private TextRepresenter stemmer;
	
	GridSearch(IRModel model, EvalMeasure mesure, ArrayList<Query> queries, TextRepresenter stemmer){
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

	public ArrayList<Double> optimize(ArrayList<double[]> parameters){
		
	}

	String path = "/Vrac/3152691/RI/";
	//String path = "/users/nfs/Etu3/3000693/Documents/RI/SearchEngine/";

	EvalMeasure measure = new PRMeasure(5);
	//EvalMeasure measure = new APMeasure();
	
	Index index = new Index("cacm", path);
	//Index index = new Index("cisi", path);
	

	EvalIRModel evalModel = new EvalIRModel(model, measure, queries, stemmer);
	evalModel.eval();
	System.out.println("modele Vectoriel");
	System.out.println(evalModel.getMean());
	//System.out.println(evalModel.getStd());

	Weighter weighterLangue = new WeighterTfTf(index);
	IRModel modelLangue = new LanguageModel(weighterLangue, .25);
	EvalIRModel evalModelLangue = new EvalIRModel(modelLangue, measure, queries, stemmer);
	evalModelLangue.eval();
	System.out.println("modele de Langue");
	System.out.println(evalModelLangue.getMean());
	//System.out.println(evalModelLangue.getStd());
	
	Weighter weighterOkapi = new WeighterTfIdf(index);
	IRModel modelOkapi = new Okapi(weighterOkapi);
	EvalIRModel evalModelOkapi = new EvalIRModel(modelOkapi, measure, queries, stemmer);
	evalModelOkapi.eval();
	System.out.println("modele Okapi");
	System.out.println(evalModelOkapi.getMean());
	//System.out.println(evalModelLangue.getStd());

	IRModel pageRank = new IRWithRandomWalk(weighterOkapi, modelOkapi, new PageRank(.2), index.getPred(), index.getSucc(), 10, 5);
	EvalIRModel evalModelPageRank = new EvalIRModel(pageRank, measure, queries, stemmer);
	evalModelPageRank.eval();
	System.out.println("modele PageRank");
	System.out.println(evalModelPageRank.getMean());
	//System.out.println(evalModelPageRank.getStd());
	
	IRModel modelHITS = new IRWithRandomWalk(weighterOkapi, modelOkapi, new HITS(), index.getPred(), index.getSucc(), 10, 5);
	EvalIRModel evalModelHITS = new EvalIRModel(modelHITS, measure, queries, stemmer);
	evalModelHITS.eval();
	System.out.println("modele HITS");
	System.out.println(evalModelHITS.getMean());
	//System.out.println(evalModelPageRank.getStd());
	
}
