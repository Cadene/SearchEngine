package models;

import java.io.IOException;
import java.util.HashMap;

import indexation.Index;

public abstract class Weighter {
	protected Index index;
	
	public Weighter(Index index) {
		this.index = index;
	}
	public abstract HashMap<String, Double> getDocWeightsForDoc(String idDoc) throws ClassNotFoundException, IOException;
	public abstract HashMap<String, Double> getDocWeightsForStem(String stem) throws ClassNotFoundException, IOException;
	public abstract HashMap<String, Double> getWeightsForQuery(HashMap< String, Integer> query) throws ClassNotFoundException, IOException;
}
