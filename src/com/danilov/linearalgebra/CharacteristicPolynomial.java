package com.danilov.linearalgebra;

import java.util.ArrayList;

public class CharacteristicPolynomial {
	
	Fraction[][] matrix;
	
	public CharacteristicPolynomial(ArrayList<String> matrix, int rows, int cols){
		this.matrix = new Fraction[rows][cols];
		for(int i = 0; i < matrix.size(); i++){
			this.matrix[i/cols][i%cols] = new Fraction(matrix.get(i));
		}
	}
	
	private Fraction getTrace(int n, Fraction[][] matrix){
		int i;
		Fraction trace;
		trace = new Fraction(0);
		for (i = 0; i < n; i++){
			  trace = trace.plus(matrix[i][i]);
		}
		return trace;
	}
	
	public Fraction[] getPolynomial(int n){
		Fraction[] coeffs = new Fraction[n + 1];
		int i,j,k,l;
		Fraction s;
		Fraction trace;
		Fraction[][] tmpB = new Fraction[n][n];
		createEmptyMatrix(tmpB, n);
		Fraction[][] tmpC = new Fraction[n][n];
		createEmptyMatrix(tmpC, n);
		if ((n % 2)!=0){ 
			coeffs[0]= new Fraction(-1);
		}else{ 
			coeffs[0]= new Fraction(1);
		}
		for (l = 1; l <= n; l++){
		    if (l == 1){
		      for (i = 0; i < n; i++){
		        for (j = 0; j < n; j++){
		        	tmpC[i][j] = matrix[i][j];
		        }
		      }
		    }else{
		      for (i = 0; i < n; i++){
		        for (j = 0; j < n; j++){
		          s= new Fraction(0);
		          for (k = 0; k < n; k++){
		            s = s.plus(tmpB[i][k].multiply(matrix[k][j]));
		          }
		          tmpC[i][j]=s;
		        }
		      }
		    }
		    trace = getTrace(n, tmpC).divide(new Fraction(l));
		    coeffs[l]=trace.multiply(coeffs[0]).multiply(new Fraction(-1, 1));
		    if (l - 1 < n){ 
		      for (i=0; i<n; i++){
		        for (j=0; j<n; j++){
		          if (j==i) { 
		        	  tmpB[i][j] = tmpC[i][j].plus(trace.multiply(new Fraction(-1, 1)));
		          }else{ 
		        	  tmpB[i][j] = tmpC[i][j];
		          }
		        }
		      }
		    }
		  }
		return coeffs;
	}
	
	private void createEmptyMatrix(Fraction[][] matrix, int n){
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				matrix[i][j] = new Fraction(1);
			}
		}
	}
}
