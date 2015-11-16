package metamodel;

import models.IRModel;
import models.Weighter;
import featurer.Featurer;

public abstract class Metamodel extends IRModel{
	
	protected Featurer featurer;
	
	public Metamodel(Weighter weighter, Featurer featurer) {
		super(weighter);
		this.featurer = featurer;
	}

}
