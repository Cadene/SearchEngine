
import java.util.ArrayList;

import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.clustering.KMeans;
import diversite.SizeClustering;
import walk.HITS;
import walk.PageRank;
import evaluation.APMeasure;
import evaluation.CRAtN;
import evaluation.EvalIRModel;
import evaluation.EvalMeasure;
import evaluation.PRMeasure;
import evaluation.PrAtN;
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

public class MainEasyCLEFDiv {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String path = "/Vrac/3152691/RI/";
	
		//EvalMeasure measure = new PRMeasure(10);
		//EvalMeasure measure = new APMeasure();
		EvalMeasure measure = new PrAtN(20);
		//EvalMeasure measure = new CRAtN(20);
		
		Index index = new Index("easyCLEF08", path);
		//Index index = new Index("cisi", path);
		
		double paramLangueEasyCLEF08 = 0.6;
		double paramOkapi_k_EasyCLEF08 = 1.6;
		double paramOkapi_b_EasyCLEF08 = 0.71;
		
		double paramLangue = paramLangueEasyCLEF08;
		double paramOkapi_k = paramOkapi_k_EasyCLEF08;
		double paramOkapi_b = paramOkapi_b_EasyCLEF08;
		
		//QueryParser queryParser = new QueryParserCISI_CACM();
		QueryParser queryParser = new QueryParserCISI_CACM();
		TextRepresenter stemmer = new Stemmer();
		
		queryParser.init(path+"easyCLEF08/easyCLEF08_query.txt", path+"easyCLEF08/easyCLEF08.rel");
		//queryParser.init(path+"cisi/cisi.qry", path+"cisi/cisi.rel");
		
		Query query = queryParser.nextQuery();
		ArrayList<Query> queries = new ArrayList<Query>();
		while (query != null) {
			queries.add(query);
			query = queryParser.nextQuery();
		}

		Weighter weighterVectTfInd1 = new WeighterTfInd(index);
		IRModel modelVectTfInd1 = new Vectoriel(weighterVectTfInd1, true);
		Clusterer kmeans = new KMeans();
		IRModel modelDiversite = new SizeClustering(weighterVectTfInd1, modelVectTfInd1, kmeans);
		//EvalIRModel evalModelVectTfInd1 = new EvalIRModel(modelVectTfInd1, measure, queries, stemmer);
		EvalIRModel evalModelVectTfInd1 = new EvalIRModel(modelDiversite, measure, queries, stemmer);
		evalModelVectTfInd1.eval();
		System.out.println("modele Vectoriel Tf-presence avec diversite");
		System.out.println(evalModelVectTfInd1.getMean());
		//System.out.println(evalModel.getStd());
		
		
	}
}
