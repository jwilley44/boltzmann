package willey.lib.physics.polymer.interactor;

import java.util.stream.Stream;

public interface Rods extends Measurable
{
	Stream<Rod> getRods();
	
	int rodCount();
	
	double rodsVolume();
	
	double rodRotation();
	
	double rodTranslation();
}
