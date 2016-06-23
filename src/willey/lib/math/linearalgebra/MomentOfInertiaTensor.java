package willey.lib.math.linearalgebra;

import java.util.Arrays;
import java.util.List;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

public class MomentOfInertiaTensor
{
	private final Matrix mTensor;
	
	public static MomentOfInertiaTensor get(List<CartesianVector> pVectors)
	{
		return new MomentOfInertiaTensor(pVectors);
	}
	
	public MomentOfInertiaTensor(CartesianVector...pVectors)
	{
		this(Arrays.asList(pVectors));
	}
	
	public MomentOfInertiaTensor(List<CartesianVector> pVectors)
	{
		this(toMomentOfInertiaTensor(pVectors));
	}
	
	private MomentOfInertiaTensor(Matrix pTensor)
	{
		mTensor = pTensor;
	}
	
	public MomentOfInertiaTensor usingCenterOfMass(CartesianVector pCenterOfMass)
	{
		return new MomentOfInertiaTensor(mTensor.plus(toMomentOfInertiaTensor(Arrays.asList(pCenterOfMass))));
	}
	
	public CartesianVector dominantDirection()
	{
		EigenvalueDecomposition vEigen = mTensor.eig();
		return getColumn(vEigen.getV(), getMinimumEigenValueIndex(vEigen));
	}
	
	private CartesianVector getRow(Matrix pMatrix, int pRow)
	{
		return CartesianVector.of(pMatrix.get(pRow, 0), pMatrix.get(pRow, 1), pMatrix.get(pRow, 2));
	}
	
	private CartesianVector getColumn(Matrix pMatrix, int pColumn)
	{
		Matrix vMatrix = pMatrix.transpose();
		return getRow(vMatrix, pColumn);
	}
	
	private int getMinimumEigenValueIndex(EigenvalueDecomposition pEigen)
	{
		double[] vValues = diagonal(pEigen.getD());
		int vIndex = 0;
		double vMin = Double.POSITIVE_INFINITY;
		for (int i=0; i < vValues.length; i++)
		{
			double vValue = Math.abs(vValues[i]);
			if (vValue < vMin)
			{
				vMin = vValue;
				vIndex = i;
			}
		}
		return vIndex;
	}
	
	private double[] diagonal(Matrix pMatrix)
	{
		return new double[]{pMatrix.get(0, 0), pMatrix.get(1, 1), pMatrix.get(2, 2)};
	}
	
	private static double ixx(CartesianVector pVector)
	{
		return pVector.y()*pVector.y() + pVector.z()*pVector.z();
	}
	
	private static double iyy(CartesianVector pVector)
	{
		return pVector.x()*pVector.x() + pVector.z()*pVector.z();
	}
	
	private static double izz(CartesianVector pVector)
	{
		return pVector.x()*pVector.x() + pVector.y()*pVector.y();
	}
	
	private static double ixy(CartesianVector pVector)
	{
		return -pVector.x()*pVector.y();
	}
	
	private static double ixz(CartesianVector pVector)
	{
		return -pVector.x()*pVector.z();
	}
	
	private static double iyz(CartesianVector pVector)
	{
		return -pVector.y()*pVector.z();
	}
	
	private static Matrix toMomentOfInertiaTensor(List<CartesianVector> pVectors)
	{
		double vIxx = pVectors.stream().mapToDouble(MomentOfInertiaTensor::ixx).sum();
		double vIyy = pVectors.stream().mapToDouble(MomentOfInertiaTensor::iyy).sum();
		double vIzz = pVectors.stream().mapToDouble(MomentOfInertiaTensor::izz).sum();
		double vIxy = pVectors.stream().mapToDouble(MomentOfInertiaTensor::ixy).sum();
		double vIxz = pVectors.stream().mapToDouble(MomentOfInertiaTensor::ixz).sum();
		double vIyz = pVectors.stream().mapToDouble(MomentOfInertiaTensor::iyz).sum();
		return new Matrix(toMatrix(vIxx, vIxy, vIxz, vIxy, vIyy, vIyz, vIxz, vIyz, vIzz));
	}
	
	private static double[][] toMatrix(double...pValues)
	{
		double[][] vValues = new double[3][3];
		vValues[0][0] = pValues[0]; vValues[1][0] = pValues[1]; vValues[2][0] = pValues[2];
		vValues[0][1] = pValues[3]; vValues[1][1] = pValues[4]; vValues[2][1] = pValues[5];
		vValues[0][2] = pValues[6]; vValues[1][2] = pValues[7]; vValues[2][2] = pValues[8];
		return vValues;
	}
}
