package com.danilov.linearalgebra;

import java.util.ArrayList;


public class Eigen {
	
	private ArrayList<String> matrix;
	private CharacteristicPolynomial cp;
	private int matrixRows;
	private int matrixCols;
	private Fraction[] polynomial;
	private ArrayList<Fraction> eigenValues;
	
	public Eigen(ArrayList<String> matrix, int matrixRows, int matrixCols){
		this.matrixRows = matrixRows;
		this.matrixCols = matrixCols;
		this.matrix = matrix;
	}
	
	public void solve(){
		cp = new CharacteristicPolynomial(matrix, matrixRows, matrixCols);
		polynomial = cp.getPolynomial(3);
		eigenValues = cp.getEigenValues();
	}
	
	public ArrayList<Fraction> getEigenValues(){
		return eigenValues;
	}
	
	public String[] getDiagonalEigenMatrix(){
		String[] matrix = new String[matrixRows*matrixCols];
		int count = 0;
		int size = matrixRows*matrixCols;
		for(int i = 0; i <size; i++){
			matrix[i] = "0";
		}
		for(int i = 0; i <size; i++){
			if((i % matrixRows) == 0){
				matrix[i + count] = eigenValues.get(count).toString();
				count++;
			}
		}
		return matrix;
	}
//	
//	public Matrix getEigenVectorMatrix(){
//		return decomposition.getV();
//	}
	
}
