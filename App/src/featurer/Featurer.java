package featurer;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Featurer {
	
	protected int size;
	
	public Featurer() {
		this.size = 1;
	}
	
	public abstract ArrayList<Double> getFeatures(String idDoc, HashMap<String, Integer> query) throws Exception;
	
	public int getSize(){
		return this.size;
	};
	
	// Pour éviter de recalculer les features, on les sauvegarde en mémoire
	// On pourra aussi rendre la classe serializable
	
	
}
