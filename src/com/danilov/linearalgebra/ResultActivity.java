package com.danilov.linearalgebra;

import java.util.ArrayList;

import Jama.Matrix;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;


public class ResultActivity extends SherlockActivity {
	
	private int action = 0;
	private TableLayout table;
	private Spinner spinner;
	private ArrayList<TextView> matrixCells = new ArrayList<TextView>();
	ArrayList<String> eigenVectorMatrix = null;
	ArrayList<String> diagonalEigenvalueMatrix = null;
	Resources resources;
	private int matrixRows;
	private int matrixCols;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		resources = getResources();
		table = (TableLayout)findViewById(R.id.matrixTable);
		spinner = (Spinner)findViewById(R.id.spinnerShow);
		handleIntentAction(getIntent());
	}
	
	private void handleIntentAction(Intent intent){
		String intentAction = intent.getAction();
		String[] actions = resources.getStringArray(R.array.array_action);
		if(intentAction.equals(actions[MainActivity.ACTION_EIGEN])){
			action = MainActivity.ACTION_EIGEN;
			onEigenAction(intent);
		}
		if(intentAction.equals(actions[MainActivity.ACTION_GAUSS])){
			action = MainActivity.ACTION_GAUSS;
			onGaussAction(intent);
		}
		setSpinnerContent();
	}
	
	private void onGaussAction(Intent intent){
		String[] tmp = intent.getStringArrayExtra(MainActivity.MATRIX);
		int rows = intent.getIntExtra(MainActivity.ROWS, -1);
		int columns = intent.getIntExtra(MainActivity.COLUMNS, -1);
		ArrayList<String> matrix = new ArrayList<String>();
		for(int i = 0; i < tmp.length; i++){
			matrix.add(tmp[i]);
		}
		showStringMatrix(matrix, rows, columns);
	}
	
	private void onEigenAction(Intent intent){
		String[] tmpEigenVectorMatrix = intent.getStringArrayExtra(MainActivity.EIGEN_VECTOR_MATRIX);
		String[] tmpDiagonalEigenvalueMatrix = intent.getStringArrayExtra(MainActivity.DIAGONAL_EIGENVALUE_MATRIX);
		String[] eigenValues = intent.getStringArrayExtra(MainActivity.EIGEN_VALUES);
		matrixRows = intent.getIntExtra(MainActivity.ROWS, -1);
		matrixCols = matrixRows;
		diagonalEigenvalueMatrix = arrayToList(tmpEigenVectorMatrix);
		eigenVectorMatrix = arrayToList(tmpDiagonalEigenvalueMatrix);
		TextView tv = (TextView)findViewById(R.id.textView);
		Resources res = this.getResources();
		String text = res.getString(R.string.eigen_values) + " [";
		for(int i = 0; i < eigenValues.length; i++){
			String eigenValue = "" + eigenValues[i];
			eigenValue = handleLongNumber(eigenValue);
			text = text + eigenValue;
			if(i != eigenValues.length - 1){
				text = text + ", ";
			}
		}
		text = text + "]";
		tv.setText(text);
		tv.setVisibility(View.VISIBLE);
	}
	
	private String handleLongNumber(String number){
		String result = number;
		int dotPosition = number.indexOf(".");
		int length = number.length();
		int finish = length;
		if(dotPosition != -1){
			if(dotPosition + 3 < finish){
				finish = dotPosition + 3;
			}
			result = number.substring(0, finish);
		}
		return result;
	}
	
	private void showStringMatrix(ArrayList<String> matrix, int rows, int columns){
		eraseTable();
		matrixCells.clear();
		for(int i = 0; i < rows*columns; i++){
			TextView tv = new TextView(this);
			tv.setWidth(50);
			tv.setHeight(50);
			String element = matrix.get(i);
			tv.setText(element);
			matrixCells.add(tv);
		}
		for(int i = 0; i < rows; i++){
			TableRow rowTitle = new TableRow(this);
			for(int j = 0; j < columns; j++){
				rowTitle.addView(matrixCells.get(i*columns + j));
			}
			table.addView(rowTitle);
		}
	}
	
	private void eraseTable(){
		table.removeAllViews();
	}
	
	private ArrayList<String> arrayToList(String[] array){
		ArrayList<String> list = new ArrayList<String>();
		for(int i = 0; i < array.length; i++){
			list.add(array[i]);
		}
		return list;
	}
	
	private void setSpinnerContent(){
		ArrayAdapter<String> adapter = null;
		if(action == MainActivity.ACTION_EIGEN){
			String[] eigen = resources.getStringArray(R.array.array_spinner_eigen);
			adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, eigen);
			spinner.setVisibility(View.VISIBLE);
		}
		if(adapter != null){
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
			spinner.setPrompt("Action"); 
			spinner.setSelection(0);
			spinner.setOnItemSelectedListener(new ItemSelectedListener());
		}
	}
	
	private class ItemSelectedListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			if(action == MainActivity.ACTION_EIGEN){
				if(position == 0){
					showStringMatrix(eigenVectorMatrix, matrixRows, matrixCols);
				}else{
					showStringMatrix(diagonalEigenvalueMatrix, matrixRows, matrixCols);
				}
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
