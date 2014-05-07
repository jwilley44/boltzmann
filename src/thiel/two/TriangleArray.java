package thiel.two;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TriangleArray<E> implements Iterable<E>
{
	private final List<E> mTriangleArray;
	private final int mHeight;
	private final int mMaxElements;
	
	public TriangleArray(int pHeight)
	{
		mHeight = pHeight;
		mMaxElements = calculateMaxElements(mHeight);
		mTriangleArray = new ArrayList<E>(mMaxElements);
	}
	
	public int getSize()
	{
		return mMaxElements;
	}
	
	public int getHeight()
	{
		return mHeight;
	}
	
	public void add(E pElement)
	{
		if (mTriangleArray.size() < mMaxElements)
		{
			mTriangleArray.add(pElement);
		}
		else
		{
			throw new IllegalStateException("The triangle array is full. Cannot add anymore elements.");
		}
	}
	
	public E get(int pIndex)
	{
		return mTriangleArray.get(pIndex);
	}
	
	private List<E> getRowAsList(int pRow)
	{
		int vIndex = calculateMaxElements(pRow + 1);
		return pRow == 0 ? mTriangleArray.subList(0, 1) : mTriangleArray.subList(vIndex - pRow - 1, vIndex);
	}
	
	public Iterable<E> getRow(int pRow)
	{
		return getRowAsList(pRow);
	}
	
	public E get(int pRow, int pIndex)
	{
		E vElement = null;
		try
		{
			if (pIndex <= pRow && pIndex > -1)
			{
				
				vElement = getRowAsList(pRow).get(pIndex);
			}
			else
			{
				throw new IndexOutOfBoundsException("There is not an element at index " + pIndex + " in row " + pRow);
			}
		}
		catch(IndexOutOfBoundsException e)
		{
			//return null
		}
		return vElement;
	}
	
	@Override
	public String toString()
	{
		StringBuilder vBuilder = new StringBuilder();
		for (int i=0; i < mHeight; i++)
		{
			addSpaces(mHeight - i, vBuilder);
			for (E vElement : getRow(i))
			{
				vBuilder.append(vElement.toString() + " ");
			}
			vBuilder.append("\n");
		}
		return vBuilder.toString();
	}
	
	private void addSpaces(int pAmount, StringBuilder pBuilder)
	{
		for (int i=0; i < pAmount; i++)
		{
			pBuilder.append(" ");
		}
	}

	@Override
	public Iterator<E> iterator()
	{
		return mTriangleArray.iterator();
	}
	
	private int calculateMaxElements(int pHeight)
	{
		return pHeight*(pHeight + 1)/2;
	}
}
