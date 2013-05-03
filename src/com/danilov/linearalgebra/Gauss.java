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
		int val = columns;
		if(rows < columns){
			val = rows;
		}
		int max = columns;
		if(rows > columns){
			max = rows;
		}
		for (int i = 0; i < columns; i++){
			if(i < val){
		        if (matrix.get(i*columns + i).toString().equals("0") && i < rows-1){
			        count = i;
			        Fraction tmp = null;
			            do{
				            count++;
				            if(count*columns < columns*rows){
				            	tmp = matrix.get(count*columns);
				            }
			            }while(tmp.toString().equals("0") && count < max);
			            if(count < rows){
			            	matrix = swapLine(matrix, count, i);
			            }
		        } 
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
	
	public String[] getRoots(){
		String[] roots = new String[columns];
		Fraction[] tmpRoots = new Fraction[columns];
		boolean[] presented = new boolean[columns];
		Fraction[][] m = new Fraction[rows][columns];
		for(int i = 0; i < matrix.size(); i++){
			m[i/columns][i%columns] = matrix.get(i); 		
		}
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < columns; j++){
				if(m[i][j].getDenominator() != 0){
					presented[j] = true;
				}
			}
			if(getZeros(m[i]) == columns - 1 && presented[i]){
				tmpRoots[i] = new Fraction(0); 
			}
		}
		for(int i = m.length - 1; i >= 0; i--){
			if(getZeros(m[i]) == m.length){
				continue;
			}
			int pos = notNullPosition(m[i]);
			Fraction sum = new Fraction(0);
			for(int j = m.length - 1; j > pos; j--){
				if(tmpRoots[j] == null){
					tmpRoots[j] = new Fraction(1);
				}
				sum = sum.plus(tmpRoots[j].multiply(m[i][j]).inverse());
			}
			tmpRoots[pos] = sum.divide(m[i][pos]);
		}
		for(int i = 0; i < tmpRoots.length; i++){
			roots[i] = tmpRoots[i].toString();
		}
		return roots;
	}
	
	private int notNullPosition(Fraction m[]){
		int pos = 0;
		for(int i = 0; i < m.length; i++){
			if(m[i].getNumerator() != 0){
				pos = i;
				break;
			}
		}
		return pos;
	}
	
	private int getZeros(Fraction m[]){
		int count = 0;
		for(int i = 0; i < m.length; i++){
			if(m[i].getDenominator() == 0){
				count++;
			}
		}
		return count;
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
			if(i/columns == line1){
				tmp = matrix.get(line2*columns + i%columns);
			}else if(i/columns == line2){
				tmp = matrix.get(line1*columns + i%columns);
			}else{
				tmp = matrix.get(i);
			}
			result.add(tmp);
		}
		return result;
	}

}
