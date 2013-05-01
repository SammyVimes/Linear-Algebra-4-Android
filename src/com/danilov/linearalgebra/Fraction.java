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
	
	public Fraction plus(Fraction addingValue){
		Fraction result = new Fraction(null);
		if(this.denominator == addingValue.denominator){
			int tmpNumerator = getNumerator() + addingValue.getNumerator();
			result = cutFraction(new Fraction(tmpNumerator, getDenominator()));
		}else{
			
		}
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
	
	private Fraction cutFraction(Fraction unCutFraction){
		Fraction fraction = unCutFraction;
		int tmpNumerator = fraction.getNumerator();
		int tmpDenominator = fraction.getDenominator();
		boolean numeratorPositive = tmpNumerator >= 0;
		boolean denominatorPositive = tmpDenominator >= 0;
		tmpNumerator = Math.abs(tmpNumerator);
		tmpDenominator = Math.abs(tmpDenominator);
		if(tmpDenominator == 1){
			return fraction;
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
			fraction = new Fraction(tmpNumerator, tmpDenominator);
		}
		
		return fraction;
	}
	
	private int getGcd(int a, int b){
		return BigInteger.valueOf(a).gcd(BigInteger.valueOf(b)).intValue();
	}
	
	public String toString(){
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
