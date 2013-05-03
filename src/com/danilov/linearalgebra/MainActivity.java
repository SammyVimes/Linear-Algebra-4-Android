package com.danilov.linearalgebra;

import java.util.ArrayList;

import Jama.Matrix;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;

public class MainActivity extends SherlockFragmentActivity {
	
	public static String MATRIX = "MATRIX";
	public static String COLUMNS = "COLUMNS";
	public static String ROWS = "ROWS";
	public static String EIGEN_VALUES = "EIGEN_VALUES";
	public static String DIAGONAL_EIGENVALUE_MATRIX = "DIAGONAL_EIGENVALUE_MATRIX";
	public static String EIGEN_VECTOR_MATRIX = "EIGEN_VECTOR_MATRIX";
	public static int ACTION_DETERMINANT = 0;
	public static int ACTION_EIGEN = 1;
	public static int ACTION_GAUSS = 2;
	
	
	private Resources resources;
	private int matrixRows = 2;
	private int matrixCols = 2;
	private ArrayList<EditText> editableTextFields = new ArrayList<EditText>();
	private TableLayout table;
	private int curAction = 0;
	String[] numbers = {"2", "3", "4", "5", "6"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		resources = getResources();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, numbers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinnerRows = (Spinner) findViewById(R.id.spinnerRows);
        spinnerRows.setAdapter(adapter);
        spinnerRows.setPrompt("Rows"); 
        spinnerRows.setOnItemSelectedListener(new ItemSelectedListener());
        
        Spinner spinnerCols = (Spinner) findViewById(R.id.spinnerCols);
        spinnerCols.setAdapter(adapter);
        spinnerCols.setPrompt("Cols");
    	spinnerCols.setOnItemSelectedListener(new ItemSelectedListener());
    	
    	String actions[] = resources.getStringArray(R.array.array_spinner_actions);
    	ArrayAdapter<String> actionsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, actions);
        actionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinnerActions = (Spinner) findViewById(R.id.spinnerActions);
        spinnerActions.setAdapter(actionsAdapter);
        spinnerActions.setPrompt("Actions");
        spinnerActions.setSelection(0);
    	spinnerActions.setOnItemSelectedListener(new ItemSelectedListener());
        table = (TableLayout)findViewById(R.id.matrixTable);
        Button actionButton = (Button)findViewById(R.id.actionButton);
        actionButton.setOnClickListener(new MyClickListener());
        
        if(savedInstanceState != null){
        	matrixRows = savedInstanceState.getInt(ROWS);
        	matrixCols = savedInstanceState.getInt(COLUMNS);
            spinnerCols.setSelection(matrixCols - 2);
            spinnerRows.setSelection(matrixRows - 2);
        	Matrix matrix = (Matrix)savedInstanceState.getSerializable(MATRIX);
        	restoreMatrixState(matrix);
        }else{
        	updateMatrixTable();
        }
	}
	
	private void test(){
		String[] tmp = {"-3","-1","1"
					   ,"-8","4","1",
					   "-8","8","-3"};
		ArrayList<String> matrix = new ArrayList<String>();
		for(int i = 0; i < tmp.length; i++){
			matrix.add(tmp[i]);
		}
		CharacteristicPolynomial cp = new CharacteristicPolynomial(matrix, 3, 3);
		Fraction[] coeffs = cp.getPolynomial(3);
		cp.getEigenValues();
		int a = 0;
		a++;
	}
	
	private void processGauss(){
		ArrayList<String> matrix = getStringMatrixFromTable();
		if(matrix == null){
			easyDialog("Only integers are allowed for Gauss`s Method!");
			return;
		}
		test();
		Gauss gauss = new Gauss(matrix, matrixRows, matrixCols);
		gauss.solve();
		ArrayList<String> tmp = gauss.getMatrix();
		String[] solvedMatrix = new String[tmp.size()];
		for(int i = 0; i < tmp.size(); i++){
			solvedMatrix[i] = tmp.get(i);
		}
		Intent intent = new Intent(this, ResultActivity.class);
		intent.setAction(resources.getStringArray(R.array.array_action)[curAction]);
		intent.putExtra(ROWS, matrixRows);
		intent.putExtra(COLUMNS, matrixCols);
		intent.putExtra(MATRIX, solvedMatrix);
		startActivity(intent);
	}
	
	private void processEigen(){
		if(matrixCols != matrixRows){
			easyDialog("Matrix must be square");//TODO: USE RESOURCES, EVERYWHERE! RESOURCES!!!
			return;
		}
		Matrix matrix = getMatrixFromTable();
		if(matrix == null){
			return;
		}
		Eigen eigen = new Eigen(matrix);
		eigen.solve();
		Matrix eigenVectorMatrix = eigen.getEigenVectorMatrix();
		Matrix diagonalEigenvalueMatrix = eigen.getDiagonalEigenMatrix();
		double[] eigenValues = eigen.getEigenValues();
		Intent intent = new Intent(this, ResultActivity.class);
		intent.setAction(resources.getStringArray(R.array.array_action)[curAction]);
		intent.putExtra(EIGEN_VECTOR_MATRIX, eigenVectorMatrix);
		intent.putExtra(DIAGONAL_EIGENVALUE_MATRIX, diagonalEigenvalueMatrix);
		intent.putExtra(EIGEN_VALUES, eigenValues);
		startActivity(intent);
	}
	
	private void processDeterminantAndRank() {
		Matrix matrix = getMatrixFromTable();
		if(matrix == null){
			return;
		}
		int matrixRank = matrix.rank();
		int matrixDeterminant = 0;
		boolean isSquare = false;
		if(matrixCols == matrixRows){
			isSquare = true;
			matrixDeterminant = (int) matrix.det();
		}
		Resources res = this.getResources();
		String det = res.getString(R.string.determinant);
		String rank = res.getString(R.string.rank);
		String message = rank + " " + matrixRank + "\n";
		if(isSquare){
			message = message + det + " " + matrixDeterminant;
		}else{
			message = message + "Only square matrix has determinant!";
		}
		easyDialog(message);
	}
	
	private Matrix getMatrixFromTable(){
		boolean hasNonValidSymbols = false;
		Matrix matrix = null;
		double[][] values = new double[matrixRows][matrixCols];
		for(int i = 0; i < matrixRows; i++){
			for(int j = 0; j < matrixCols; j++){
				EditText et = editableTextFields.get(i*matrixCols + j);
				String tmp = et.getText().toString();
				if(hasInvalidSymbols(tmp)){
					hasNonValidSymbols = true;
					break;
				}else{
					tmp = handleSlashSymbol(tmp);
					if(!tmp.equals("")){
						values[i][j] = new Double(tmp);
					}
				}
			}
			if(hasNonValidSymbols){
				matrix = null;
				break;
			}
		}
		if(!hasNonValidSymbols){
			matrix = new Matrix(values);
		}
		return matrix;
	}
	
	private ArrayList<String> getStringMatrixFromTable(){
		ArrayList<String> matrix = new ArrayList<String>();
		boolean hasNonValidSymbols = false;
		for(int i = 0; i < editableTextFields.size(); i++){
			String tmp = editableTextFields.get(i).getText().toString();
			if(hasInvalidForGaussSymbols(tmp)){
				hasNonValidSymbols = true;
				break;
			}
			matrix.add(tmp);
		}
		if(hasNonValidSymbols){
			matrix = null;
		}
		return matrix;
		
	}
	
	private boolean hasInvalidSymbols(String string){
		boolean result = false;
		if(string.contains(":")){
			easyDialog("Matrix has non valid symbols");
			result = true;
		}
		return result;
	}
	
	private boolean hasInvalidForGaussSymbols(String string){
		boolean result = false;
		if(string.contains(":") || string.contains(".") || string.contains(",")){
			easyDialog("Matrix has non valid symbols");
			result = true;
		}
		return result;
	}
	
	private String handleSlashSymbol(String string){
		String result;
		if(string.contains("/")){
			int slashPos = string.indexOf("/");
			String firstPart = string.substring(0, slashPos);
			String secondPart = string.substring(slashPos + 1, string.length());
			Double tmp = new Double(new Double(firstPart)/new Double(secondPart));
			result = tmp.toString();
		}else{
			return string;
		}
		return result;
	}
	
	private void updateMatrixTable(){
		eraseTable();
		editableTextFields.clear();
		for(int i = 0; i < matrixRows*matrixCols; i++){
			EditText et = new EditText(this);
			et.setWidth(50);
			et.setHeight(50);
			et.setInputType(InputType.TYPE_CLASS_DATETIME);
			editableTextFields.add(et);
		}
		for(int i = 0; i < matrixRows; i++){
			TableRow rowTitle = new TableRow(this);
			for(int j = 0; j < matrixCols; j++){
				rowTitle.addView(editableTextFields.get(i*matrixCols + j));
			}
			table.addView(rowTitle);
		}
	}
	
	private void eraseTable(){
		table.removeAllViews();
	}
	
	private void onActionButtonPressed(int action){
		if(action == ACTION_EIGEN){
			processEigen();
		}else if(action == ACTION_DETERMINANT){
			processDeterminantAndRank();
		}else if(action == ACTION_GAUSS){
			processGauss();
		}
	}

	private class MyClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			onActionButtonPressed(curAction);
		}
		
	}
	
	private class ItemSelectedListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> parent, View arg1, int position,
				long arg3) {
			boolean needUpdate = false;
			switch(parent.getId()){
				case R.id.spinnerCols:
					Integer tmpCols = new Integer(numbers[position]);
					if(!(matrixCols == tmpCols)){
						needUpdate = true;
					}
					matrixCols = tmpCols;
					if(needUpdate){
						updateMatrixTable();
					}
					break;
				case R.id.spinnerRows:
					Integer tmpRows = new Integer(numbers[position]);
					if(!(matrixRows == tmpRows)){
						needUpdate = true;
					}
					matrixRows = tmpRows;
					if(needUpdate){
						updateMatrixTable();
					}
					break;
				case R.id.spinnerActions:
					if(position == ACTION_EIGEN){
						curAction = ACTION_EIGEN;
					}
					if(position == ACTION_DETERMINANT){
						curAction = ACTION_DETERMINANT;
					}
					if(position == ACTION_GAUSS){
						curAction = ACTION_GAUSS;
					}
					break;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
		
	}
	
	private void restoreMatrixState(Matrix matrix){
		updateMatrixTable();
		for(int i = 0; i < editableTextFields.size(); i++){
			double element = Double.valueOf(matrix.get(i/matrixCols, i%matrixCols));
			editableTextFields.get(i).setText(getProperDoubles(new String(element + "")));
		}
	}
	
	private String getProperDoubles(String str){
		String result = str;
		int dotPosition = str.indexOf(".");
		if(dotPosition != -1){
			if('0' == result.charAt(dotPosition + 1)){
				result = result.substring(0, dotPosition);
			}
		}
		return result;
	}
	
	@Override
	public void onSaveInstanceState(Bundle state){
		Matrix m = getMatrixFromTable();
		if(m != null){
			state.putSerializable(MATRIX, m);
			state.putInt(ROWS, m.getRowDimension());
			state.putInt(COLUMNS, m.getColumnDimension());
		}
		super.onSaveInstanceState(state);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	private void toaster(String message){
		Toast t = Toast.makeText(this, message, Toast.LENGTH_LONG);
		t.show();
	}
	
	private void easyDialog(String message){
		MyDialogFragment dialog = new MyDialogFragment();
		dialog.setMessage(message);
		dialog.show(getSupportFragmentManager(), "dlg");
	}

}
