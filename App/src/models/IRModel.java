package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class IRModel {
	protected Weighter weighter;
	protected LinkedHashMap<String, Double> ranking;
	protected HashMap<String, Double> scores;
	protected HashMap<String, Integer> queryForRanking;
	protected HashMap<String, Integer> queryForScores;
	
	public IRModel(Weighter weighter) {
		this.weighter = weighter;
		this.ranking = null;
		this.scores = null;
		this.queryForRanking = null;
		this.queryForScores = null;
	}
	
	public abstract void setParameters(double... parameters);
	
	public LinkedHashMap<String, Double> getRanking(HashMap<String, Integer> query) throws Exception{
		if (this.queryForRanking == query && this.ranking != null){
			return this.ranking;
		}
		
		List<Entry<String,Double>> list = new LinkedList<Entry<String,Double>>(this.getScores(query).entrySet());
		Collections.sort(list, new Comparator<Entry<String,Double>>() {
			@Override
			public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});

		this.ranking = new LinkedHashMap<String, Double>();
		for (Iterator<Entry<String, Double>> it = list.iterator(); it.hasNext();) {
			Map.Entry<String,Double> entry = (Entry<String, Double>) it.next();
			this.ranking.put(entry.getKey(), entry.getValue());
		}
		
		for (String doc : weighter.getListDocsIds())
		{
			if (!this.ranking.containsKey(doc))
				this.ranking.put(doc, 0.0);
		}
		
		return this.ranking;
	}
	
	public HashMap<String,Double> getScores(HashMap<String, Integer> query) throws Exception
	{
		if (this.queryForScores != query || this.scores == null)
			processScores(query);
		return this.scores;
	};
	
	protected abstract void processScores(HashMap<String, Integer> query) throws Exception;
}
