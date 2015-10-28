package featurer;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Featurer {
	
	protected int size;
	
	public Featurer() {
	}
	
	public abstract ArrayList<Double> getFeatures(String idDoc, HashMap<String, Integer> query) throws Exception;
	
	public int getSize(){
		return size;
	}
	
	// Pour éviter de recalculer les features, on les sauvegarde en mémoire
	// On pourra aussi rendre la classe serializable
	
	
}
