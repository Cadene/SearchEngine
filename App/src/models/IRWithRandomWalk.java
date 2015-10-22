package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import walk.RandomWalk;

public class IRWithRandomWalk extends IRModel {

	private IRModel baseModel;
	private RandomWalk walker;
	HashMap<String, Set<String>> pred;
	HashMap<String, Set<String>> succ;
	private int nSeeds;
	private int nIn;
	
	public IRWithRandomWalk(Weighter weighter, IRModel baseModel, RandomWalk walker, 
				HashMap<String, Set<String>> pred, HashMap<String, Set<String>> succ, int nSeeds, int nIn) {
		super(weighter);
		this.baseModel = baseModel;
		this.walker = walker;
		this.pred = pred;
		this.succ = succ;
		this.nSeeds = nSeeds;
		this.nIn = nIn;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void processScores(HashMap<String, Integer> query)
			throws Exception {
		HashSet<String> seeds = new HashSet<String>();
		LinkedHashMap<String, Double> ranking = this.baseModel.getRanking(query);
		Iterator<String> iterator = ranking.keySet().iterator();
		int i = 0;
		while (i < nSeeds && iterator.hasNext()){
			String doc = iterator.next();
			seeds.add(doc);
			seeds.addAll(this.succ.get(doc));
			if (this.pred.get(doc) != null){
				ArrayList<String> predecesseurs = new ArrayList<String>();
				int k = 0;
				while (!predecesseurs.isEmpty() && k < this.nIn){
					int index = (int) Math.floor(Math.random() * predecesseurs.size());
					seeds.add(predecesseurs.get(index));
					predecesseurs.remove(index);
				}
			}
			i++;
		}
		HashMap<String, Set<String>> sousSucc = new HashMap<String, Set<String>>();
		HashMap<String, Set<String>> sousPred = new HashMap<String, Set<String>>();
		for (String doc : seeds){
			sousSucc.put(doc, new HashSet<String>());
			if (! sousPred.containsKey(doc))
				sousPred.put(doc, new HashSet<String>());
			for (String s : this.succ.get(doc)){
				if (seeds.contains(s)){
					sousSucc.get(doc).add(s);
					if (! sousPred.containsKey(s)){
						sousPred.put(s, new HashSet<String>());
					}
					sousPred.get(s).add(doc);
				}
			}
		}
		this.scores = this.walker.walk(sousPred, sousSucc);
	}

	@Override
	public void setParameters(double... parameters) {
		this.nSeeds = (int) parameters[0];
		this.nIn = (int) parameters[1];
	}
}
