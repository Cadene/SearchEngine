

import java.util.ArrayList;

import evaluation.EvalIRModel;
import evaluation.EvalMeasure;
import evaluation.PRMeasure;
import evaluation.Query;
import evaluation.QueryParser;
import evaluation.QueryParserCISI_CACM;
import models.IRModel;
import models.LanguageModel;
import models.Vectoriel;
import models.Weighter;
import models.WeighterTfInd;
import models.WeighterTfTf;
import indexation.Index;
import indexation.TextRepresenter;
import indexation.Stemmer;

public class MainTest {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String path = "/Vrac/3152691/RI/";
		//String path = "/users/nfs/Etu3/3000693/Documents/RI/SearchEngine/";
		Index index = new Index("cacm", path);
		Weighter weighter = new WeighterTfTf(index);
		IRModel model = new Vectoriel(weighter, index, true);
		// 
		EvalMeasure measure = new PRMeasure(5);
		
		QueryParser queryParser = new QueryParserCISI_CACM();
		TextRepresenter stemmer = new Stemmer();
		queryParser.init(path+"cacm/cacm.qry", path+"cacm/cacm.rel");
		Query query = queryParser.nextQuery();
		ArrayList<Query> queries = new ArrayList<Query>();
		
		while (query != null) {
			queries.add(query);
			query = queryParser.nextQuery();
		}

		EvalIRModel evalModel = new EvalIRModel(model, measure, queries, stemmer);
		evalModel.process();
		System.out.println("modele Vectoriel");
		System.out.println(evalModel.getMean());
		//System.out.println(evalModel.getStd());
		
		IRModel modelLangue = new LanguageModel(weighter, .2);
		EvalIRModel evalModelLangue = new EvalIRModel(modelLangue, measure, queries, stemmer);
		evalModelLangue.process();
		System.out.println("modele de Langue");
		System.out.println(evalModelLangue.getMean());
		//System.out.println(evalModelLangue.getStd());
	}

}
