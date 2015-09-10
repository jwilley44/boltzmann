package willey.lib.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;

public class Version
{
	private static final String kFile = "Version.txt";
	
	private Version()
	{
		
	}
	
	public static String get() throws IOException
	{
		BufferedReader vReader = new BufferedReader(new FileReader(Version.class.getResource(kFile).getFile()));
		String vReturn = vReader.lines().collect(Collectors.joining("\n", "Build Version Info\n", "\n"));
		vReader.close();
		return vReturn;
	}
}
