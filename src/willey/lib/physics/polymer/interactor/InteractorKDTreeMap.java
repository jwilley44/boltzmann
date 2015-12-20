package willey.lib.physics.polymer.interactor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import willey.lib.datastructures.CartesianPointSelector;
import willey.lib.datastructures.HyperCube;
import willey.lib.datastructures.KdTree;
import willey.lib.datastructures.KdTree.PointSelector;
import willey.lib.math.linearalgebra.CartesianVector;

public class InteractorKDTreeMap
{
	private final Lattice mLattice;
	private final KdTree<CartesianVector> mKdTree;
	private final Map<CartesianVector, Set<Interactor>> mPoint2Interactor = new HashMap<CartesianVector, Set<Interactor>>();
	private final PointSelector<CartesianVector> mPointSelector;
	private final double mMaxInteractionRadius;
	
	public InteractorKDTreeMap(Lattice pLattice, int pPartitions, double pMaxInteractionRadius)
	{
		mLattice = pLattice;
		List<CartesianVector> vSpacePartitions = partitionSpace(mLattice, pPartitions);
		mPointSelector = new CartesianPointSelector();
		mKdTree = new KdTree<CartesianVector>(mPointSelector, vSpacePartitions);
		vSpacePartitions.stream().forEach(pVector -> mPoint2Interactor.put(pVector, new HashSet<Interactor>()));
		mMaxInteractionRadius = pMaxInteractionRadius;
	}
	
	public Stream<Interactor> getNearest(Rod pRod)
	{
		return mKdTree.find(rodToSearchCube(pRod)).stream().flatMap(pPoint -> get(pPoint)).distinct();
	}
	
	public Stream<Interactor> getNearest(Monomer pMonomer)
	{
		return get(mKdTree.find(pMonomer.position())).distinct();
	}
	
	public void addMonomer(Monomer pMonomer)
	{
		add(mKdTree.find(pMonomer.position()), pMonomer);
	}
	
	public void addRod(Rod pRod)
	{
		mKdTree.find(rodToSearchCube(pRod)).stream().forEach(pPoint -> add(pPoint, pRod));
	}
	
	private Stream<Interactor> get(CartesianVector pPoint)
	{
		return mPoint2Interactor.get(pPoint).stream();
	}
	
	private void add(CartesianVector pKey, Interactor pInteractor)
	{
		mPoint2Interactor.get(pKey).add(pInteractor);
	}
	
	private static List<CartesianVector> partitionSpace(Lattice pLattice, int pPartitions)
	{
		return new ArrayList<CartesianVector>();
	}
	
	private HyperCube<CartesianVector> rodToSearchCube(Rod pRod)
	{
		CartesianVector vStart = pRod.position();
		CartesianVector vEnd = pRod.endPoint();
		HyperCube.Builder<CartesianVector> vBuilder = HyperCube.builder();
		for (int i=0; i < 3; i++)
		{
			double vSign = Math.signum(vStart.subtract(vEnd).getCoordinate(i));
			double[] vCoord = new double[3];
			vCoord[i] = mMaxInteractionRadius;
			vCoord[(i+1)%3] = 0;
			vCoord[(i-1)%3] = 0;
			vBuilder.add(
					vStart.subtract(CartesianVector.of(vCoord).scale(vSign)),
					vEnd.add(CartesianVector.of(vCoord)), i);
		}
		return vBuilder.build(mPointSelector);
	}
}
