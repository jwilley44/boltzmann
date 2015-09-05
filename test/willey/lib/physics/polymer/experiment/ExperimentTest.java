package willey.lib.physics.polymer.experiment;

import java.io.File;

import org.junit.Test;

public class ExperimentTest
{
	@Test
	public void testPolymerTerminating() throws Exception
	{
		PolymerExperiment vExperiment = new PolymerExperiment(getFile("polymer.t.param"));
		vExperiment.run();
	}
	
	@Test
	public void testPolymerContinuous() throws Exception
	{
		PolymerExperiment vExperiment = new PolymerExperiment(getFile("polymer.c.param"));
		vExperiment.run();
	}
	
	@Test
	public void testRodsTerminating() throws Exception
	{
		RodsExperiment vExperiment = new RodsExperiment(getFile("rods.t.param"));
		vExperiment.run();
	}
	
	@Test
	public void testRodsContinous() throws Exception
	{
		RodsExperiment vExperiment = new RodsExperiment(getFile("rods.c.param"));
		vExperiment.run();
	}
	
	@Test
	public void testPolymerAndRodsTerminating() throws Exception
	{
		PolymerAndRodsExperiment vExperiment = new PolymerAndRodsExperiment(getFile("polymer.rods.t.param"));
		vExperiment.run();
	}
	
	@Test
	public void testPolymerAndRodsContinous() throws Exception
	{
		PolymerAndRodsExperiment vExperiment = new PolymerAndRodsExperiment(getFile("polymer.rods.c.param"));
		vExperiment.run();
	}
	
	private File getFile(String pFilename)
	{
		return new File(getClass().getResource(pFilename).getFile());
	}
}
