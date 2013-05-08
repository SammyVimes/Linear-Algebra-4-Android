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
	
	public Gauss(int rows, int columns){
		this.columns = columns;
		this.rows = rows;
	}
	
	public boolean getInverse(){
		Fraction[][] ed = new Fraction[rows][rows];
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < rows; j++){
				ed[i][j] = (i == j? new Fraction(1):new Fraction(0));
			}
		}
		Fraction[][] m  = getAugmented(ed, getMatrixFromList(matrix));
		Gauss gauss = new Gauss(rows, columns*2);
		gauss.putMatrixToList(m);
		gauss.getEchelon(0);
		Fraction[][] m1 = gauss.getMatrixFromList(gauss.matrix);
	    int i,j;
	    Fraction t;
	    for(i=0;i < rows;i++) {
	      t = m1[i][i];
	      if (t.cmp(0)!=0) {
	        for(j=0;j < columns*2;j++)  //делим строчку на t
	          m1[i][j] = m1[i][j].divide(t);
	      }else{
	        // Опред = 0! Обратная не сущ.!
	        return false;
	      }	  
	    }
	    // "отделяем вторую половину"
	    Fraction[][] m2 = new Fraction[rows][columns];
	    for(i=0;i<rows;i++){
		      for(j=0;j<columns;j++){
		    	  m2[i][j]= m1[i][j + columns];
		      }
	    }
	    putMatrixToList(m2);
	    return true;
	}
	
	private Fraction[][] getAugmented(Fraction[][] unitary, Fraction[][] m){
		Fraction[][] n = new Fraction[rows][columns*2];
		    for(int i = 0; i < rows; i++){
			      for(int j= 0;j < columns*2;j++){
			    	  n[i][j] = (j < columns?m[i][j]:unitary[i][j-columns]);
			      }
		    }
		return n;
	}
	
	public Fraction[][] getMatrixFromList(ArrayList<Fraction> list){
		Fraction[][] m = new Fraction[rows][columns];
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < columns; j++){
				m[i][j] = matrix.get(i*columns + j);
			}
		}
		return m;
	}
	
	public void putMatrixToList(Fraction[][] m){
		ArrayList<Fraction> list = new ArrayList<Fraction>();
		for(int i = 0; i < rows; i++){
			for(int v = 0; v < columns; v++){
				list.add(m[i][v]);
			}
		}
		matrix = list;
	}
	
	
	public void getEchelon(int canswapcols) {
		Fraction[][] m = new Fraction[rows][columns];
		m = getMatrixFromList(matrix);
		canswapcols = 0;
		 int j;
		 int k;
		 int l;
		 int last=columns-1;
		 int z = Math.min(rows, columns);
		 for(int i=0;i<z;i++) {
			 k= i;
			 while (k!= rows && m[k][i].cmp(0)==0) { //ищем ненулевой элемент в столбце i
				 k++;
				 if (k == rows) {
					 if (last <= i){
						 break;
					 } else{
					   if (canswapcols!=0) {
				        //если в столбце все нули, то меняем его с последним...
				        swapCols(i,last, m);
				        last--;
				        k = i;
				       }
					 }
				 }
			 }	
		  
			 if (k!=i && k!=  rows) {
				 swapRows(k,i, m); 
			 }
			 if (k!= rows)
				 for (j = 0; j < rows;j++) { 
					 if (j == i){
						 continue;
					 }
					 for (l=i+1;l< columns;l++){
						 m[j][l] = m[j][l].plus(m[j][i].inverse().divide(m[i][i]).multiply(m[i][l]) );
					 }
					 m[j][i] = new Fraction(0,1);
				 }
		 	}
		 putMatrixToList(m);	
	}
	
	void swapCols(int i,int j, Fraction a[][]) {
		 Fraction t;
		 for(int k=0;k<this.rows;k++) {
			  t = a[k][i];
			  a[k][i] = a[k][j];
			  a[k][j] = t;
		 }
	}

	void swapRows(int i,int j, Fraction a[][]) {
		 Fraction t;
		 for(int k=0;k< columns; k++) {
			  t = a[i][k];
			  a[i][k] = a[j][k];
			  a[j][k] = t;
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
				if(m[i][j].getDenominator() != 0 && m[i][j].getNumerator() != 0){
					presented[j] = true;
				}
			}
			int pos = notNullPosition(m[i]);
			if(getZeros(m[i]) == columns - 1 && presented[pos]){
				tmpRoots[pos] = new Fraction(0); 
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
			if(tmpRoots[pos] != null){
				if(tmpRoots[pos].getNumerator() != 0 && tmpRoots[pos].getDenominator() != 0){
					tmpRoots[pos] = sum.divide(m[i][pos]);
				}else{
					for(int k = 0; k < m.length; k++){
						if(m[i][k].getNumerator() != 0 && m[i][k].getDenominator() != 0 && k != pos){
							tmpRoots[k] = new Fraction(0);
						}
					}
				}
			}else{
				tmpRoots[pos] = sum.divide(m[i][pos]);
			}
		}
		for(int i = 0; i < tmpRoots.length; i++){
			if(tmpRoots[i] == null){
				tmpRoots[i] = new Fraction(1);
			}
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
			if(m[i].getDenominator() == 0 || m[i].getNumerator() == 0){
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
