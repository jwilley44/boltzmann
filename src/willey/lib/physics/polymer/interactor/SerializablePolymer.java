package willey.lib.physics.polymer.interactor;

import java.util.List;

import willey.lib.math.linearalgebra.CartesianVector;

public class SerializablePolymer implements SerializableInteractors<Polymer, SerializablePolymer>
{
	private static final long serialVersionUID = 1L;
	
	List<CartesianVector> mMonomerPositions;
	double mMonomerRadius;
	int mStateId;
	
	public SerializablePolymer(List<CartesianVector> pMonomerPositions, double pMonomerRadius, int pStateId)
	{
		mMonomerPositions = pMonomerPositions;
		mMonomerRadius = pMonomerRadius;
		mStateId = pStateId;
	}
	
	public List<CartesianVector> getMonomerPositions()
	{
		return mMonomerPositions;
	}
	
	public double getMonomerRadius()
	{
		return mMonomerRadius;
	}
	
	@Override
	public int getStateId()
	{
		return mStateId;
	}
	
	@Override
	public Polymer toMeasurable()
	{
		return new StaticPolymer(mMonomerPositions, mMonomerRadius, mStateId);
	}
}
