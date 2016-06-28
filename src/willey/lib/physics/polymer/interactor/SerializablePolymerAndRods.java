package willey.lib.physics.polymer.interactor;

import java.util.List;

import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.math.linearalgebra.SegmentUtil.Segment;

public class SerializablePolymerAndRods implements
		SerializableInteractors<PolymerAndRods, SerializablePolymerAndRods>
{
	private static final long serialVersionUID = 1L;

	private final List<CartesianVector> mMonomerPositions;
	private final double mMonomerRadius;

	private final List<Segment> mRods;
	private final double mRodRadius;
	private final long mLatticeVolume;

	private final int mStateId;
	
	public SerializablePolymerAndRods(SerializablePolymer pPolymer,
			SerializableRods pRods, int pStateId)
	{
		this(pPolymer.getMonomerPositions(), pPolymer.getMonomerRadius(), pRods
				.getLineSegments(), pRods.getRodRadius(), pRods
				.getLatticeVolume(), pStateId);
	}

	public SerializablePolymerAndRods(List<CartesianVector> pMonomerPositions,
			double pMonomerRadius, List<Segment> pRods, double pRodRadius,
			long pLatticeVolume, int pStateId)
	{
		mMonomerPositions = pMonomerPositions;
		mMonomerRadius = pMonomerRadius;
		mRods = pRods;
		mRodRadius = pRodRadius;
		mLatticeVolume = pLatticeVolume;
		mStateId = pStateId;
	}

	@Override
	public int getStateId()
	{
		return mStateId;
	}

	@Override
	public PolymerAndRods toMeasurable()
	{
		return new StaticPolymerAndRods(new StaticPolymer(mMonomerPositions,
				mMonomerRadius, mStateId), new StaticRods(mRods, mRodRadius,
				mStateId, mLatticeVolume), mStateId);
	}

}
