package com.danilov.linearalgebra;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class MyDialogFragment extends DialogFragment {
	
	private static String MESSAGE = "MESSAGE";
	private String message = "";
	
	public MyDialogFragment(){
		super();
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		if(savedInstanceState != null){
			restoreSavedInstanceState(savedInstanceState);
		}
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
	        .setNeutralButton(R.string.ok, new OnClickListener(){
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					dismiss();
				}
	        	
	        })
	        .setMessage(message);
	    return builder.create();
	 }
	
	private void restoreSavedInstanceState(Bundle savedInstanceState){
		message = savedInstanceState.getString(MESSAGE);
	}
	
	public void setMessage(String message){
		this.message = message;
	}
	
	
	@Override
	public void onSaveInstanceState(Bundle saved){
		saved.putString(MESSAGE, message);
		super.onSaveInstanceState(saved);
	}
}
