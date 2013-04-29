package com.danilov.linearalgebra;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

public class Eigen {
	
	private Matrix matrix;
	private EigenvalueDecomposition decomposition;
	
	public Eigen(Matrix matrix){
		this.matrix = matrix;
	}
	
	public void solve(){
		decomposition = new EigenvalueDecomposition(matrix);
	}
	
	public double[] getEigenValues(){
		return decomposition.getRealEigenvalues();
	}
	
	public Matrix getDiagonalEigenMatrix(){
		return decomposition.getD();
	}
	
	public Matrix getEigenVectorMatrix(){
		return decomposition.getV();
	}
	
}
