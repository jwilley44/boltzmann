package willey.lib.math;


public interface SequenceElement<T>
{
	boolean greaterThan(T pElement);
	
	SequenceElement<T> next();
	
	T getElement();
}
