package willey.lib.physics.polymer.experiment;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class ParameterCombinerTest
{
	private static final String kFileName = "parameterTest.param";
	
	private File getFile(String pFilename)
	{
		return new File(getClass().getResource(pFilename).getFile());
	}
	
	@Test
	public void test() throws IOException
	{
		ParameterCombiner vParameterCombiner = new ParameterCombiner(getFile(kFileName));
		Assert.assertEquals(6, vParameterCombiner.getParameterCombinations().distinct().count());
	}
}
