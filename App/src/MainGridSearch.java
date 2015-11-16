import indexation.Index;
import indexation.Stemmer;
import indexation.TextRepresenter;

import java.util.ArrayList;

import models.IRModel;
import models.LanguageModel;
import models.Okapi;
import models.Weighter;
import models.WeighterTfIdf;
import models.WeighterTfTf;
import evaluation.APMeasure;
import evaluation.EvalIRModel;
import evaluation.EvalMeasure;
import evaluation.GridSearch;
import evaluation.Query;
import evaluation.QueryParser;
import evaluation.QueryParserCISI_CACM;


public class MainGridSearch {

	public static void main(String[] args) throws Exception {
		String path = "/Vrac/3152691/RI/";
		//String path = "/users/nfs/Etu3/3000693/Documents/RI/SearchEngine/";
	
		//EvalMeasure measure = new PRMeasure(5);
		EvalMeasure measure = new APMeasure();
		QueryParser queryParser = new QueryParserCISI_CACM();
		TextRepresenter stemmer = new Stemmer();
		
		//Index index = new Index("cacm", path);
		//queryParser.init(path+"cacm/cacm.qry", path+"cacm/cacm.rel");
		Index index = new Index("cisi", path);
		queryParser.init(path+"cisi/cisi.qry", path+"cisi/cisi.rel");
		
		Query query = queryParser.nextQuery();
		ArrayList<Query> queries = new ArrayList<Query>();
		while (query != null) {
			queries.add(query);
			query = queryParser.nextQuery();
		}
		

		System.out.println("Modele de langue, lambda 0:0.01:1");
		Weighter weighterLangue = new WeighterTfTf(index);
		IRModel modelLangue = new LanguageModel(weighterLangue, .5);
		GridSearch gridSearchLangue = new GridSearch(modelLangue, measure, queries, stemmer);
		ArrayList<double[]> listParametersLangue = new ArrayList<double[]>();
		for (double i = 0.0; i <= 1; i+=0.01){
			double[] params = new double[1];
			params[0] = i;
			listParametersLangue.add(params);
		}
		gridSearchLangue.optimize(listParametersLangue);
		
		
		System.out.println("Modele Okapi, k=1:0.1:2  b=0.7:0.01:0.8");
		Weighter weighterOkapi = new WeighterTfIdf(index);
		IRModel modelOkapi = new Okapi(weighterOkapi); // parametres k et b
		GridSearch gridSearchOkapi = new GridSearch(modelOkapi, measure, queries, stemmer);
		ArrayList<double[]> listParametersOkapi = new ArrayList<double[]>();
		for (double k = 1.0; k <= 2.0; k+=0.1){
			for (double b = .7; b <= .8; b+=0.01){
				double[] params = new double[2];
				params[0] = k;
				params[1] = b;
				listParametersOkapi.add(params);
			}
		}
		gridSearchOkapi.optimize(listParametersOkapi);
		
		EvalIRModel evalModelOkapi = new EvalIRModel(modelOkapi, measure, queries, stemmer);
		evalModelOkapi.eval();
		System.out.println("modele Okapi");
		System.out.println(evalModelOkapi.getMean());
		//System.out.println(evalModelOkapi.getStd());
	}

}
