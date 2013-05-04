package com.danilov.linearalgebra;

import java.util.ArrayList;

import android.provider.SyncStateContract.Columns;


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
		polynomial = cp.getPolynomial(matrixRows);
		eigenValues = cp.getEigenValues();
	}
	
	public ArrayList<Fraction> getEigenValues(){
		return eigenValues;
	}
	
	public String[] getDiagonalEigenMatrix(){
		String[] matrix = new String[matrixRows*matrixCols];
		int count = 0;
		int count2 = 0;
		int size = matrixRows*matrixCols;
		for(int i = 0; i <size; i++){
			matrix[i] = "0";
		}
		for(int i = 0; i <size; i++){
			if((i % matrixRows) == 0){
				matrix[i + count2] = eigenValues.get(count).toString();
				if(count < eigenValues.size() - 1){
					count++;
				}
				count2++;
			}
		}
		return matrix;
	}
	
	public String[] getEigenVectorMatrix(){
		String[] eigenVectorMatrix = new String[matrixRows *matrixCols];
		int count = 0;
		for(int i = 0; i < eigenValues.size(); i++){
			ArrayList<String> matrix = getMatrixWithEigenNumbers(eigenValues.get(i));
			Gauss g = new Gauss(matrix, matrixRows, matrixCols);
			g.getEchelon(0);
			String[] roots = g.getRoots();
			for(int j = 0; j < matrixCols; j++){
				eigenVectorMatrix[j*matrixCols + count] = roots[j];
			}
			count++;
		}
		return eigenVectorMatrix;
	}
	
	private ArrayList<String> getMatrixWithEigenNumbers(Fraction eigenValue){
		ArrayList<String> result = new ArrayList<String>();
		int count = 0;
		for(int i = 0; i < matrix.size(); i++){
			result.add(matrix.get(i));
		}
		for(int i = 0; i < result.size(); i++){
			if(i % matrixCols == 0){
				Fraction tmp = new Fraction(matrix.get(i + count)).plus(eigenValue.inverse());
				result.set(i + count, tmp.toString());
				count++;
			}
		}
		return result;
	}
	
}
