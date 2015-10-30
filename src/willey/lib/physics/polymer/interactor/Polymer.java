package willey.lib.physics.polymer.interactor;

import java.util.stream.Stream;

import willey.lib.math.linearalgebra.CartesianVector;

public interface Polymer extends Measurable
{
	public int getSize();
	
	public double getEndToEndDistance();
	
	public double monomerRadius();
	
	public Stream<Monomer> getMonomers();
	
	public CartesianVector getDirection();
	
	public Stream<CartesianVector> getDirections();
}
