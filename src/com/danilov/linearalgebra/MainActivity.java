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

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;

public class MainActivity extends SherlockFragmentActivity {
	
	public static String ACTION_EIGEN = "EIGEN";
	public static String MATRIX = "MATRIX";
	public static String COLUMNS = "COLUMNS";
	public static String ROWS = "ROWS";
	public static String EIGEN_VALUES = "EIGEN_VALUES";
	public static String DIAGONAL_EIGENVALUE_MATRIX = "DIAGONAL_EIGENVALUE_MATRIX";
	public static String EIGEN_VECTOR_MATRIX = "EIGEN_VECTOR_MATRIX";
	public static String ACTION_DETERMINANT = "ACTION_DETERMINANT";
	
	
	private int matrixRows = 2;
	private int matrixCols = 2;
	private ArrayList<EditText> editableTextFields = new ArrayList<EditText>();
	private TableLayout table;
	private String curAction = "";
	String[] numbers = {"2", "3", "4", "5", "6"};
	String[] actions = {"Eigen", "Determinant and rank"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, numbers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> actionsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, actions);
        actionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinnerRows = (Spinner) findViewById(R.id.spinnerRows);
        spinnerRows.setAdapter(adapter);
        spinnerRows.setPrompt("Rows"); 
        spinnerRows.setOnItemSelectedListener(new ItemSelectedListener());
        
        Spinner spinnerCols = (Spinner) findViewById(R.id.spinnerCols);
        spinnerCols.setAdapter(adapter);
        spinnerCols.setPrompt("Cols");
    	spinnerCols.setOnItemSelectedListener(new ItemSelectedListener());
    	
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
	
	private void processEigen(){
		Eigen eigen = new Eigen(getMatrixFromTable());
		eigen.solve();
		Matrix eigenVectorMatrix = eigen.getEigenVectorMatrix();
		Matrix diagonalEigenvalueMatrix = eigen.getDiagonalEigenMatrix();
		double[] eigenValues = eigen.getEigenValues();
		Intent intent = new Intent(this, ResultActivity.class);
		intent.setAction(ACTION_EIGEN);
		intent.putExtra(EIGEN_VECTOR_MATRIX, eigenVectorMatrix);
		intent.putExtra(DIAGONAL_EIGENVALUE_MATRIX, diagonalEigenvalueMatrix);
		intent.putExtra(EIGEN_VALUES, eigenValues);
		startActivity(intent);
	}
	
	private void processDeterminantAndRank() {
		Matrix matrix = getMatrixFromTable();
		int matrixRank = matrix.rank();
		int matrixDeterminant = (int) matrix.det();
		Resources res = this.getResources();
		String det = res.getString(R.string.determinant);
		String rank = res.getString(R.string.rank);
		String message = det + " " + matrixDeterminant + "\n"+ 
		rank + " " + matrixRank;
		MyDialogFragment dialog = new MyDialogFragment();
		dialog.setMessage(message);
		dialog.show(getSupportFragmentManager(), "dlg");
	}
	
	private Matrix getMatrixFromTable(){
		double[][] values = new double[matrixRows][matrixCols];
		for(int i = 0; i < matrixRows; i++){
			for(int j = 0; j < matrixCols; j++){
				EditText et = editableTextFields.get(i*matrixCols + j);
				String tmp = et.getText().toString();
				if(!tmp.equals("")){
					values[i][j] = new Double(tmp);
				}
			}
		}
		Matrix matrix = new Matrix(values);
		return matrix;
	}
	
	private void updateMatrixTable(){
		eraseTable();
		editableTextFields.clear();
		for(int i = 0; i < matrixRows*matrixCols; i++){
			EditText et = new EditText(this);
			et.setWidth(50);
			et.setHeight(50);
			et.setInputType(InputType.TYPE_CLASS_NUMBER);
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
	
	private void onActionButtonPressed(String action){
		if(action.equals(ACTION_EIGEN)){
			processEigen();
		}else if(action.equals(ACTION_DETERMINANT)){
			processDeterminantAndRank();
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
					if(actions[position].equals("Eigen")){
						curAction = ACTION_EIGEN;
					}
					if(actions[position].equals("Determinant and rank")){
						curAction = ACTION_DETERMINANT;
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
			Double element = new Double(matrix.get(i/matrixCols, i%matrixCols));
			editableTextFields.get(i).setText(element.toString());
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle state){
		Matrix m = getMatrixFromTable();
		state.putSerializable(MATRIX, getMatrixFromTable());
		state.putInt(ROWS, m.getRowDimension());
		state.putInt(COLUMNS, m.getColumnDimension());
		super.onSaveInstanceState(state);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
