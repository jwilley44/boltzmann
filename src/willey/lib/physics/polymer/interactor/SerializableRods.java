package willey.lib.physics.polymer.interactor;

import java.util.List;

import willey.lib.math.linearalgebra.SegmentUtil.Segment;

public class SerializableRods implements  SerializableInteractors<Rods, SerializableRods>
{
	private static final long serialVersionUID = 1L;
	private final List<Segment> mRods;
	private final double mRodRadius;
	private final long mLatticeVolume;
	private final int mStateId;
	
	public SerializableRods(List<Segment> pRods, double pRodRadius, long pLatticeVolume, int pStateId)
	{
		mRods = pRods;
		mRodRadius = pRodRadius;
		mLatticeVolume = pLatticeVolume;
		mStateId = pStateId;
	}
	
	public List<Segment> getLineSegments()
	{
		return mRods;
	}
	
	public double getRodRadius()
	{
		return mRodRadius;
	}
	
	public long getLatticeVolume()
	{
		return mLatticeVolume;
	}
	
	@Override
	public int getStateId()
	{
		return mStateId;
	}

	@Override
	public Rods toMeasurable()
	{
		return new StaticRods(mRods, mRodRadius, mStateId, mLatticeVolume);
	}

}
