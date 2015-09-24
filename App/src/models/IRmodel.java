package models;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class IRmodel {
	protected Weighter weighter;
	
	public IRmodel(Weighter weighter) {
		this.weighter = weighter;
	}

	public abstract HashMap<String,Double> getScores(HashMap<String,Integer> query) throws Exception;

	public HashMap<String,Integer> getRanking(HashMap<String, Integer> query) throws Exception{
		List<Entry<String,Double>> list = new LinkedList<Entry<String,Double>>(getScores(query).entrySet());
		Collections.sort(list, new Comparator<Entry<String,Double>>() {
			@Override
			public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});

		LinkedHashMap<String, Integer> ranking = new LinkedHashMap<String,Integer>();
		int i = 0;
		for (Iterator<Entry<String, Double>> it = list.iterator(); it.hasNext();) {
			Map.Entry<String,Double> entry = (Entry<String, Double>) it.next();
			ranking.put(entry.getKey(), i);
			i = i + 1;
		}
		return ranking;
	}
}

