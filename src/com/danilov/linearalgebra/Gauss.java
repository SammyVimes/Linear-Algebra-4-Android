package com.danilov.linearalgebra;

import java.util.ArrayList;

public class Gauss {
	
	private ArrayList<Fraction> matrix = new ArrayList<Fraction>();
	private int rows;
	private int columns;
	
	
	public Gauss(ArrayList<String> matrix, int rows, int columns){
		this.columns = columns;
		this.rows = rows;
		for(int i = 0; i < matrix.size(); i++){
			this.matrix.add(new Fraction(matrix.get(i)));
		}
	}
	
	@SuppressWarnings("unchecked")
	public void solve(){
		ArrayList<Fraction> tmpMatrix = (ArrayList<Fraction>)matrix.clone();
		int count;
		for (int i = 0; i < columns; i++){
	        if (matrix.get(i/columns + i).toString().equals("0") && i < rows-1){
	        count = i;
	        Fraction tmp = null;
	            do{
	            count++;
	            tmp = matrix.get(count/columns);
	            }while(tmp.toString().equals("0"));
	        matrix = swapLine(matrix, count, i);
	        } 
	        
	        for (int x = 0; x < rows; x++){
	            for (int y = 0; y < columns; y++){
	            	tmpMatrix.set(x * columns + y, matrix.get(x * columns + y));
	            }
	        }
        
	        for (int j = i + 1; j < rows; j++){
	            for (int k = i; k < columns; k++){
	                Fraction tmp1 = matrix.get(j * columns + k);
	                Fraction tmp2 = matrix.get(i * columns + k).multiply(tmpMatrix.get(j * columns + i));
	                Fraction tmp3 = tmp2.divide(matrix.get(i * columns + i));
	                Fraction res = tmp1.plus(tmp3.multiply(new Fraction(-1,1)));
	                matrix.set(j * columns + k, res);
	            }                
	        }
	    }
	}
	
	public ArrayList<String> getMatrix(){
		ArrayList<String> matrix = new ArrayList<String>();
		for(int i = 0; i < this.matrix.size(); i++){
			matrix.add(this.matrix.get(i).toString());
		}
		return matrix;
	}
	
	
	
	private ArrayList<Fraction> swapLine(ArrayList<Fraction> matrix, int line1, int line2){
		ArrayList<Fraction> result = new ArrayList<Fraction>();
		if(line2 < line1){
			int tmp = line1;
			line1 = line2;
			line2 = tmp;
		}
		for(int i = 0; i < matrix.size(); i++){
			Fraction tmp = null;
			if(i/rows == line1){
				tmp = matrix.get(line2 + i%rows);
			}else if(i/rows == line2){
				tmp = matrix.get(line1 + i%rows);
			}
			result.add(tmp);
		}
		return result;
	}

}
