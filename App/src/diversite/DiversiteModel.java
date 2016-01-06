package diversite;

import models.IRModel;
import models.Weighter;

public abstract class DiversiteModel extends IRModel {

	protected IRModel baseIRModel;
	
	public DiversiteModel(Weighter weighter, IRModel baseIRModel) {
		super(weighter);
		this.baseIRModel = baseIRModel;
	}
}
