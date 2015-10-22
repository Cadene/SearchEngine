package walk;

import java.util.HashMap;
import java.util.Set;

public interface RandomWalk {
	public HashMap<String, Double> walk(HashMap<String, Set<String>> pred, HashMap<String, Set<String>> succ); 
}
