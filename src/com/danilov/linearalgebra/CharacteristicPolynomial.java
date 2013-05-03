package com.danilov.linearalgebra;

import java.util.ArrayList;

public class CharacteristicPolynomial {
	
	Fraction[][] matrix;
	Fraction[] polynom;
	
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
		polynom = coeffs;
		return coeffs;
	}
	
	public ArrayList<Fraction> getEigenValues(){
		ArrayList<Fraction> eigenValues = new ArrayList<Fraction>();
		ArrayList<Fraction> possibleRoots = new ArrayList<Fraction>();
		ArrayList<Fraction> p = getDividers(polynom[polynom.length - 1]);
		ArrayList<Fraction> q = getDividers(polynom[0]);
		for(int i = 0; i < p.size(); i++){
			for(int j = 0; j < q.size(); j++){
				Fraction tmp = p.get(i).divide(q.get(j));
				possibleRoots.add(tmp);
				possibleRoots.add(tmp.inverse());
			}
		}
		possibleRoots = deleteRepetitive(possibleRoots);
		int size = polynom.length;
		for(int i = 0; i < possibleRoots.size(); i++){
			Fraction sum = new Fraction(0);
			for(int j = 0; j < size; j++){
				Fraction coef = polynom[size - j - 1];
				Fraction xInPow = possibleRoots.get(i).pow(j);
				sum = sum.plus((coef).multiply(xInPow));
			}
			if(sum.getNumerator() == 0){
				eigenValues.add(possibleRoots.get(i));
			}
		}
		return eigenValues;
	}
	
	private ArrayList<Fraction> deleteRepetitive(ArrayList<Fraction> list){
		ArrayList<Fraction> result = new ArrayList<Fraction>();
		for(int i = 0; i < list.size(); i++){
			Fraction tmp = list.get(i);
			if(!contains(result, tmp)){
				result.add(tmp);
			}
		}
		return result;
	}
	
	private boolean contains(ArrayList<Fraction> list, Fraction obj){
		boolean contains = false;
		for(int i = 0; i < list.size(); i++){
			Fraction tmp = list.get(i);
			if(tmp.getDenominator() == obj.getDenominator() && tmp.getNumerator() == obj.getNumerator()){
				contains = true;
				break;
			}
		}
		return contains;
	}
	
	private ArrayList<Fraction> getDividers(Fraction num){
		ArrayList<Fraction> dividers = new ArrayList<Fraction>();
		dividers.add(new Fraction(1));
		dividers.add(num);
		int tmp = Math.abs(num.getNumerator());
		for(int i = 2; i < tmp/2; i++){
			if(tmp%i == 0){
				dividers.add(new Fraction(i));
			}
		}
		return dividers;
	}
	
//	private ArrayList<Fraction> getDividers(Fraction num){
//		ArrayList<Fraction> dividers = new ArrayList<Fraction>();
//		dividers.add(new Fraction(1));
//		if(Math.abs(num.getDenominator()) != 1){
//			dividers.add(num);
//		}else{
//			int number = num.getNumerator();
//			if(Math.abs(number) != 1){
//				dividers.add(num);
//			}
//			if(number % 2 ==0)
//			{
//				dividers.add(new Fraction(2));
//			    while(number %2 == 0){
//			    	dividers.add(new Fraction(number/=2));
//			    }
//			}
//			for(int k = 3; k*k <= number/2; k+=2)
//			    if (number%k==0)
//			    {
//			        dividers.add(new Fraction(k));
//			        while(number%k==0){
//			        	dividers.add(new Fraction(number/=k));
//			        }
//			    }
//		}
//		return dividers;
//	}
	
	private void createEmptyMatrix(Fraction[][] matrix, int n){
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				matrix[i][j] = new Fraction(1);
			}
		}
	}
}
