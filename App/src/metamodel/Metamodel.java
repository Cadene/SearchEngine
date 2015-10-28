package metamodel;

import models.IRModel;
import models.Weighter;
import featurer.FeaturerList;

public abstract class Metamodel extends IRModel{
	
	protected FeaturerList featurerList;
	
	public Metamodel(Weighter weighter, FeaturerList featurerList) {
		super(weighter);
		this.featurerList = featurerList;
	}

}
