package com.danilov.linearalgebra;

import java.math.BigInteger;

import android.annotation.SuppressLint;

@SuppressLint("UseValueOf")
public class Fraction {
	
	private int numerator;
	private int denominator;
	
	public Fraction(String number){
		FractionPair pair = stringToFractionPair(number);
		this.numerator = pair.getNumerator();
		this.denominator = pair.getDenominator();
	}
	
	public Fraction(int numerator, int denominator){
		this.numerator = numerator;
		this.denominator = denominator;
	}
	
	public Fraction(int number){
		this.numerator = number;
		this.denominator = 1;
	}
	
	public Fraction multiply(Fraction multiplyValue){
		Fraction result = null;
		result = new Fraction(getNumerator()*multiplyValue.getNumerator(),
				getDenominator()*multiplyValue.getDenominator());
		result.cutFraction();
		return result;
	}
	
	public Fraction divide(Fraction multiplyValue){
		Fraction result = null;
		result = new Fraction(getNumerator()*multiplyValue.getDenominator(),
				getDenominator()*multiplyValue.getNumerator());
		result.cutFraction();
		return result;
	}
	
	public Fraction plus(Fraction addingValue){
		Fraction result = null;
		int tmpNumerator;
		if(this.denominator == addingValue.denominator){
			tmpNumerator = getNumerator() + addingValue.getNumerator();
			result =new Fraction(tmpNumerator, getDenominator());
		}else{
			int addingNumerator = addingValue.getNumerator();
			int addingDenominator = addingValue.getDenominator();
			int gcd = getGcd(getDenominator(), addingDenominator);
			int newDenominator = (getDenominator() * addingDenominator)/gcd;
			tmpNumerator = ((newDenominator / getDenominator())*getNumerator()) + ((newDenominator/addingDenominator) * addingNumerator);
			result = new Fraction(tmpNumerator, newDenominator);
		}
		result.cutFraction();
		return result;
	}
	

	private FractionPair stringToFractionPair(String number){
		int slashSymbolPosition = number.indexOf("/");
		int numerator;
		int denominator;
		if(slashSymbolPosition != -1){
			int length = number.length();
			numerator = new Integer(number.substring(0, slashSymbolPosition));
			denominator = new Integer(number.substring(slashSymbolPosition + 1, length));
		}else{
			numerator = new Integer(number);
			denominator = 1;
		}
		return new FractionPair(numerator, denominator);
	}
	
	private void cutFraction(){
		int tmpNumerator = this.getNumerator();
		int tmpDenominator = this.getDenominator();
		boolean numeratorPositive = tmpNumerator >= 0;
		boolean denominatorPositive = tmpDenominator >= 0;
		tmpNumerator = Math.abs(tmpNumerator);
		tmpDenominator = Math.abs(tmpDenominator);
		if(tmpDenominator == 1){
			return;
		}else if(tmpDenominator == 0){
			this.numerator = 0;
		}else{			
			int gcd = getGcd(tmpNumerator, tmpDenominator);
			while(gcd != 1){
				tmpNumerator = tmpNumerator / gcd;
				tmpDenominator = tmpDenominator / gcd;
				gcd = getGcd(tmpNumerator, tmpDenominator);
			}
			if(!numeratorPositive){
				tmpNumerator = tmpNumerator * (-1);
			}
			if(!denominatorPositive){
				tmpDenominator = tmpDenominator * (-1);
			}
			this.numerator = tmpNumerator;
			this.denominator = tmpDenominator;
		}
	}
	
	private int getGcd(int a, int b){
		return BigInteger.valueOf(a).gcd(BigInteger.valueOf(b)).intValue();
	}
	
	public String toString(){
		if(Math.abs(getDenominator()) == 1){
			return new String(getNumerator() + "");
		}
		if(getNumerator() == 0){
			return new String("0");
		}
		return new String(getNumerator() + "/" + getDenominator());
	}
	
	public int getNumerator(){
		return numerator;
	}
	
	public int getDenominator(){
		return denominator;
	}
	
	private class FractionPair{
		
		private int numerator;
		private int denominator;
		
		public FractionPair(int numerator, int denominator){
			this.numerator = numerator;
			this.denominator = denominator;
		}
		
		public int getNumerator(){
			return numerator;
		}
		
		public int getDenominator(){
			return denominator;
		}
	}
}
