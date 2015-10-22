package apprentissage;

import java.util.ArrayList;

public abstract class Featurer {

	protected ArrayList<Double> features;

	public Featurer() {
		this.features = new ArrayList<Double>();
	}
	
	public abstract ArrayList<Double> getFeatures(String idDoc, String query) throws Exception;

	// Pour éviter de recalculer les features, on les sauvegarde en mémoire
	// On pourra aussi rendre la classe serializable
	
	
}
