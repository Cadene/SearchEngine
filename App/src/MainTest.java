

import java.util.ArrayList;

import walk.HITS;
import walk.PageRank;
import evaluation.APMeasure;
import evaluation.EvalIRModel;
import evaluation.EvalMeasure;
import evaluation.PRMeasure;
import evaluation.Query;
import evaluation.QueryParser;
import evaluation.QueryParserCISI_CACM;
import featurer.Featurer;
import featurer.FeaturerDocSum;
import featurer.FeaturerDocWalkRank;
import featurer.FeaturerList;
import featurer.FeaturerModel;
import featurer.FeaturerQuerySum;
import metamodel.MetamodelLineaire;
import models.IRModel;
import models.IRWithRandomWalk;
import models.LanguageModel;
import models.Okapi;
import models.Vectoriel;
import models.Weighter;
import models.WeighterLogtfIdf;
import models.WeighterLogtfidfLogtfidf;
import models.WeighterTfIdf;
import models.WeighterTfInd;
import models.WeighterTfTf;
import indexation.Index;
import indexation.TextRepresenter;
import indexation.Stemmer;

public class MainTest {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String path = "/Vrac/3152691/RI/";
	
		//EvalMeasure measure = new PRMeasure(10);
		EvalMeasure measure = new APMeasure();
		
		Index index = new Index("cacm", path);
		//Index index = new Index("cisi", path);
		
		double paramLangueCACM = 0.8;
		double paramOkapi_k_CACM = 0;
		double paramOkapi_b_CACM = 0;
		
		double paramOkapi_k_CISI = 0;
		double paramOkapi_b_CISI = 0;
		double paramLangueCISI = 0.8;
		
		double paramLangue = paramLangueCACM;
		double paramOkapi_k = paramOkapi_k_CACM;
		double paramOkapi_b = paramOkapi_b_CACM;
		/*
		double paramLangue = paramLangueCISI;
		double paramOkapi_k = paramOkapi_k_CISI;
		double paramOkapi_b = paramOkapi_b_CISI;
		*/
		
		QueryParser queryParser = new QueryParserCISI_CACM();
		TextRepresenter stemmer = new Stemmer();
		
		queryParser.init(path+"cacm/cacm.qry", path+"cacm/cacm.rel");
		//queryParser.init(path+"cisi/cisi.qry", path+"cisi/cisi.rel");
		
		Query query = queryParser.nextQuery();
		ArrayList<Query> queries = new ArrayList<Query>();
		while (query != null) {
			queries.add(query);
			query = queryParser.nextQuery();
		}
		
		Weighter weighterVectTfInd1 = new WeighterTfInd(index);
		IRModel modelVectTfInd1 = new Vectoriel(weighterVectTfInd1, false);
		EvalIRModel evalModelVectTfInd1 = new EvalIRModel(modelVectTfInd1, measure, queries, stemmer);
		evalModelVectTfInd1.eval();
		System.out.println("modele Vectoriel Tf-presence");
		System.out.println(evalModelVectTfInd1.getMean());
		//System.out.println(evalModel.getStd());
		
		Weighter weighterVectTfTf1 = new WeighterTfTf(index);
		IRModel modelVectTfTf1 = new Vectoriel(weighterVectTfTf1, false);
		EvalIRModel evalModelVectTfTf1 = new EvalIRModel(modelVectTfTf1, measure, queries, stemmer);
		evalModelVectTfTf1.eval();
		System.out.println("modele Vectoriel Tf-Tf");
		System.out.println(evalModelVectTfTf1.getMean());
		//System.out.println(evalModel.getStd());

		Weighter weighterVectTfIdf1 = new WeighterTfIdf(index);
		IRModel modelVectTfIdf1 = new Vectoriel(weighterVectTfIdf1, false);
		EvalIRModel evalModelVectTfIdf1 = new EvalIRModel(modelVectTfIdf1, measure, queries, stemmer);
		evalModelVectTfIdf1.eval();
		System.out.println("modele Vectoriel Tf-Idf");
		System.out.println(evalModelVectTfIdf1.getMean());
		//System.out.println(evalModel.getStd());
		
		Weighter weighterVectLogtfIdf1 = new WeighterLogtfIdf(index);
		IRModel modelVectLogtfIdf1 = new Vectoriel(weighterVectLogtfIdf1, false);
		EvalIRModel evalModelVectLogtfIdf1 = new EvalIRModel(modelVectLogtfIdf1, measure, queries, stemmer);
		evalModelVectLogtfIdf1.eval();
		System.out.println("modele Vectoriel LogTf-Idf");
		System.out.println(evalModelVectLogtfIdf1.getMean());
		//System.out.println(evalModel.getStd());
		
		Weighter weighterVectLogtfidfLogtfidf1 = new WeighterLogtfidfLogtfidf(index);
		IRModel modelVectLogtfidfLogtfidf1 = new Vectoriel(weighterVectLogtfidfLogtfidf1, false);
		EvalIRModel evalModelVectLogtfidfLogtfidf1 = new EvalIRModel(modelVectLogtfidfLogtfidf1, measure, queries, stemmer);
		evalModelVectLogtfidfLogtfidf1.eval();
		System.out.println("modele Vectoriel LogTfIdf-LogTfIdf");
		System.out.println(evalModelVectLogtfidfLogtfidf1.getMean());
		//System.out.println(evalModel.getStd());
		
		
		Weighter weighterVectTfInd2 = new WeighterTfInd(index);
		IRModel modelVectTfInd2 = new Vectoriel(weighterVectTfInd2, true);
		EvalIRModel evalModelVectTfInd2 = new EvalIRModel(modelVectTfInd2, measure, queries, stemmer);
		evalModelVectTfInd2.eval();
		System.out.println("modele Vectoriel Tf-presence normalized");
		System.out.println(evalModelVectTfInd2.getMean());
		//System.out.println(evalModel.getStd());
		
		Weighter weighterVectTfTf2 = new WeighterTfTf(index);
		IRModel modelVectTfTf2 = new Vectoriel(weighterVectTfTf2, true);
		EvalIRModel evalModelVectTfTf2 = new EvalIRModel(modelVectTfTf2, measure, queries, stemmer);
		evalModelVectTfTf2.eval();
		System.out.println("modele Vectoriel Tf-Tf normalized");
		System.out.println(evalModelVectTfTf2.getMean());
		//System.out.println(evalModel.getStd());
		
		Weighter weighterVectTfIdf2 = new WeighterTfIdf(index);
		IRModel modelVectTfIdf2 = new Vectoriel(weighterVectTfIdf2, true);
		EvalIRModel evalModelVectTfIdf2 = new EvalIRModel(modelVectTfIdf2, measure, queries, stemmer);
		evalModelVectTfIdf2.eval();
		System.out.println("modele Vectoriel Tf-Idf normalized");
		System.out.println(evalModelVectTfIdf2.getMean());
		//System.out.println(evalModel.getStd());
		
		Weighter weighterVectLogtfIdf2 = new WeighterLogtfIdf(index);
		IRModel modelVectLogtfIdf2 = new Vectoriel(weighterVectLogtfIdf2, true);
		EvalIRModel evalModelVectLogtfIdf2 = new EvalIRModel(modelVectLogtfIdf2, measure, queries, stemmer);
		evalModelVectLogtfIdf2.eval();
		System.out.println("modele Vectoriel LogTf-Idf normalized");
		System.out.println(evalModelVectLogtfIdf2.getMean());
		//System.out.println(evalModel.getStd());
		
		Weighter weighterVectLogtfidfLogtfidf2 = new WeighterLogtfidfLogtfidf(index);
		IRModel modelVectLogtfidfLogtfidf2 = new Vectoriel(weighterVectLogtfidfLogtfidf2, true);
		EvalIRModel evalModelVectLogtfidfLogtfidf2 = new EvalIRModel(modelVectLogtfidfLogtfidf2, measure, queries, stemmer);
		evalModelVectLogtfidfLogtfidf2.eval();
		System.out.println("modele Vectoriel LogTfIdf-LogTfIdf normalized");
		System.out.println(evalModelVectLogtfidfLogtfidf2.getMean());
		//System.out.println(evalModel.getStd());

		Weighter weighterLangue = new WeighterTfTf(index);
		IRModel modelLangue = new LanguageModel(weighterLangue, paramLangue);
		EvalIRModel evalModelLangue = new EvalIRModel(modelLangue, measure, queries, stemmer);
		evalModelLangue.eval();
		System.out.println("modele de Langue");
		System.out.println(evalModelLangue.getMean());
		//System.out.println(evalModelLangue.getStd());
		
		Weighter weighterOkapi = new WeighterTfIdf(index);
		IRModel modelOkapi = new Okapi(weighterOkapi, 1.9, 0.73); // parametres k et b
		EvalIRModel evalModelOkapi = new EvalIRModel(modelOkapi, measure, queries, stemmer);
		evalModelOkapi.eval();
		System.out.println("modele Okapi");
		System.out.println(evalModelOkapi.getMean());
		//System.out.println(evalModelOkapi.getStd());

		IRModel pageRank = new IRWithRandomWalk(weighterOkapi, modelOkapi, new PageRank(.2, 1000), index.getPred(), index.getSucc(), 10, 5);
		EvalIRModel evalModelPageRank = new EvalIRModel(pageRank, measure, queries, stemmer);
		evalModelPageRank.eval();
		System.out.println("modele PageRank");
		System.out.println(evalModelPageRank.getMean());
		//System.out.println(evalModelPageRank.getStd());
		
		IRModel modelHITS = new IRWithRandomWalk(weighterOkapi, modelOkapi, new HITS(1000), index.getPred(), index.getSucc(), 10, 5);
		EvalIRModel evalModelHITS = new EvalIRModel(modelHITS, measure, queries, stemmer);
		evalModelHITS.eval();
		System.out.println("modele HITS");
		System.out.println(evalModelHITS.getMean());
		//System.out.println(evalModelPageRank.getStd());

		ArrayList<Featurer> list = new ArrayList<Featurer>();
		list.add(new FeaturerModel(modelVectTfIdf2));
		list.add(new FeaturerModel(modelVectTfTf2));
		list.add(new FeaturerModel(modelLangue));
		list.add(new FeaturerModel(modelOkapi));
		list.add(new FeaturerDocSum(weighterVectTfInd1));
		list.add(new FeaturerDocWalkRank(new PageRank(.2, 1000), index.getPred(), index.getSucc()));
		list.add(new FeaturerQuerySum(weighterVectTfInd1));
		list.add(new FeaturerQuerySum(weighterVectTfIdf1));
		Featurer featurer = new FeaturerList(list);
		Weighter weighterLineaire = new WeighterTfIdf(index);
		MetamodelLineaire modelLineaire = new MetamodelLineaire(weighterLineaire, stemmer, featurer, 1000000, 0.0000001, 0);
		modelLineaire.train(queries);
		EvalIRModel evalModelLineaire = new EvalIRModel(modelLineaire, measure, queries, stemmer);
		evalModelLineaire.eval();
		System.out.println("modele Lineaire");
		System.out.println(evalModelLineaire.getMean());
	}

}

