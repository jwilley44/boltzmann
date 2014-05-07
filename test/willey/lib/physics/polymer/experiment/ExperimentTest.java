package willey.lib.physics.polymer.experiment;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class ExperimentTest
{
	@Test
	public void testPolymerTerminating() throws IOException
	{
		PolymerExperiment vExperiment = new PolymerExperiment(getFile("polymer.t.param"));
		vExperiment.run();
	}
	
	@Test
	public void testPolymerContinuous() throws IOException
	{
		PolymerExperiment vExperiment = new PolymerExperiment(getFile("polymer.c.param"));
		vExperiment.run();
	}
	
	@Test
	public void testRodsTerminating() throws IOException
	{
		RodsExperiment vExperiment = new RodsExperiment(getFile("rods.t.param"));
		vExperiment.run();
	}
	
	@Test
	public void testRodsContinous() throws IOException
	{
		RodsExperiment vExperiment = new RodsExperiment(getFile("rods.c.param"));
		vExperiment.run();
	}
	
	@Test
	public void testPolymerAndRodsTerminating() throws IOException
	{
		PolymerAndRodsExperiment vExperiment = new PolymerAndRodsExperiment(getFile("polymer.rods.t.param"));
		vExperiment.run();
	}
	
	@Test
	public void testPolymerAndRodsContinous() throws IOException
	{
		PolymerAndRodsExperiment vExperiment = new PolymerAndRodsExperiment(getFile("polymer.rods.c.param"));
		vExperiment.run();
	}
	
	private File getFile(String pFilename)
	{
		return new File(getClass().getResource(pFilename).getFile());
	}
}
