package willey.lib.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public class FileLineIterator implements Iterator<String>
{
		private final BufferedReader mReader;
		
		private String mNextLine;
		
		public FileLineIterator(File pFile) throws FileNotFoundException
		{
			mReader = new BufferedReader(new FileReader(pFile));
			mNextLine = safeReadLine();
		}

		@Override
		public boolean hasNext()
		{
			boolean vHasNext = mNextLine != null;
			if (!vHasNext)
			{
				try
				{
					mReader.close();
				} 
				catch (IOException e)
				{
					throw new RuntimeException(e);
				}
			}
			return vHasNext;
		}

		@Override
		public String next() 
		{
			String vNext = mNextLine;
			mNextLine = safeReadLine();
			return vNext;
		}
		
		private String safeReadLine()
		{
			String vLine = null;
			try
			{
				vLine = mReader.readLine();
			}
			catch(IOException e)
			{
				throw new RuntimeException(e);
			}
			return vLine;
		}
}