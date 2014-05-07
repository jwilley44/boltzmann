package thiel.two;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MaxPathApp
{
	public static void main(String[] pArgs)
	{
		if (pArgs.length != 3)
		{
			throw new IllegalArgumentException(
					"Incorect number of arguments.\nUsage MaxPathApp [Height of triangle] [minimum integer] [maximum integer]");
		}
		int vHeight = Integer.parseInt(pArgs[0]);
		int vMin = Integer.parseInt(pArgs[1]);
		int vMax = Integer.parseInt(pArgs[2]);

		TriangleArray<Integer> vArray = createRandomIntegerTriangleArray(
				vHeight, vMin, vMax);
		System.out.println(vArray.toString());
		System.out.println(getMaxPath(vArray));
	}

	private static TriangleArray<Integer> createRandomIntegerTriangleArray(
			int pHeight, int pMin, int pMax)
	{
		Random vRandomGen = new Random();
		TriangleArray<Integer> vArray = new TriangleArray<Integer>(pHeight);
		int vRange = pMax - pMin;
		int vSize = vArray.getSize();
		for (int i = 0; i < vSize; i++)
		{
			vArray.add(Integer.valueOf(vRandomGen.nextInt(vRange) + pMin));
		}
		return vArray;
	}

	private static String getMaxPath(
			TriangleArray<Integer> pIntegerTriangleArray)
	{
		int vHeight = pIntegerTriangleArray.getHeight();
		TriangleArray<TriangleArrayPathElement> vArray = new TriangleArray<TriangleArrayPathElement>(
				vHeight);
		vArray.add(new TriangleArrayPathElement(pIntegerTriangleArray.get(0, 0)));
		for (int i = 1; i < pIntegerTriangleArray.getHeight(); i++)
		{
			int vIndex = 0;
			for (Integer vValue : pIntegerTriangleArray.getRow(i))
			{
				TriangleArrayPathElement vLeftAbove = vArray.get(i - 1,
						vIndex - 1);
				TriangleArrayPathElement vRightAbove = vArray
						.get(i - 1, vIndex);

				int vLeftMax;
				if (vLeftAbove == null)
				{
					vLeftMax = Integer.MIN_VALUE;
				}
				else
				{
					vLeftMax = vLeftAbove.getTotal();
				}

				int vRightMax;
				if (vRightAbove == null)
				{
					vRightMax = Integer.MIN_VALUE;
				}
				else
				{
					vRightMax = vRightAbove.getTotal();
				}

				if (vLeftMax > vRightMax)
				{
					vArray.add(new TriangleArrayPathElement(vValue.intValue(),
							vLeftAbove));
				}
				else
				{
					vArray.add(new TriangleArrayPathElement(vValue.intValue(),
							vRightAbove));
				}
				vIndex++;
			}
		}

		TriangleArrayPathElement vBottom = null;
		int vMax = Integer.MIN_VALUE;
		for (TriangleArrayPathElement vElement : vArray.getRow(vHeight - 1))
		{
			if (vElement.getTotal() > vMax)
			{
				vBottom = vElement;
				vMax = vBottom.getTotal();
			}
		}

		List<TriangleArrayPathElement> vPath = new ArrayList<TriangleArrayPathElement>(
				vHeight - 1);
		vPath.add(vBottom);
		TriangleArrayPathElement vNext = vBottom.getAbove();
		while (vNext != null)
		{
			vPath.add(vNext);
			vNext = vNext.getAbove();
		}

		StringBuilder vBuilder = new StringBuilder();
		vBuilder.append("The path: ");
		for (int i = vPath.size(); i > 0; i--)
		{
			vBuilder.append(vPath.get(i - 1).getValue() + " -> ");
		}
		String vAnswer = vBuilder.toString();
		return vAnswer.substring(0, vAnswer.lastIndexOf(" ->"))
				+ " gives the maximum sum, " + vMax;
	}
}
