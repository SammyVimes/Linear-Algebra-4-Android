package com.danilov.linearalgebra;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class MyKeyboard implements OnKeyListener, OnKeyboardActionListener{
	
	public final static int CodeDelete   = -5; // Keyboard.KEYCODE_DELETE
	public final static int CodeCancel   = -3; // Keyboard.KEYCODE_CANCEL
	public final static int CodePrev     = 55000;
	public final static int CodeAllLeft  = 55001;
	public final static int CodeLeft     = 55002;
	public final static int CodeRight    = 55003;
	public final static int CodeAllRight = 55004;
	public final static int CodeNext     = 55005;
	public final static int CodeClear    = 55006;
	
	private KeyboardView mKeyboardView;
	private Activity mHostActivity;
	private Keyboard mKeyboard;

	public MyKeyboard(Activity host, int viewid, int layoutid) {
		this.mHostActivity = host;
		mKeyboardView= (KeyboardView)mHostActivity.findViewById(viewid);
        mKeyboardView.setKeyboard(new Keyboard(mHostActivity, layoutid));
        mKeyboard = mKeyboardView.getKeyboard();
        mKeyboardView.setPreviewEnabled(false); // NOTE Do not show the preview balloons
        mKeyboardView.setOnKeyboardActionListener(this);
        // Hide the standard keyboard initially
        mHostActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
	


	@Override
	public void onKey(int primaryCode, int[] arg1) {
		View focusCurrent = mHostActivity.getWindow().getCurrentFocus();
	    if( focusCurrent==null || focusCurrent.getClass()!=MyEditText.class ) return;
	    EditText edittext = (EditText) focusCurrent;
	    Editable editable = edittext.getText();
	    int start = edittext.getText().length();
	    // Handle key
	    if( primaryCode==CodeCancel ) {
	        hideCustomKeyboard();
	    } else if( primaryCode==CodeDelete ) {
	        if( editable!=null && start>0 ) editable.delete(start - 1, start);
	    } else if( primaryCode==CodeClear ) {
	        if( editable!=null ) editable.clear();
	    } else if( primaryCode==CodeLeft ) {
	        if( start>0 ) edittext.setSelection(start - 1);
	    } else if( primaryCode==CodeRight ) {
	        if (start < edittext.length()) edittext.setSelection(start + 1);
	    } else if( primaryCode==CodeAllLeft ) {
	        edittext.setSelection(0);
	    } else if( primaryCode==CodeAllRight ) {
	        edittext.setSelection(edittext.length());
	    } else if( primaryCode==CodePrev ) {
	        View focusNew= edittext.focusSearch(View.FOCUS_BACKWARD);
	        if( focusNew!=null ) focusNew.requestFocus();
	    } else if( primaryCode==CodeNext ) {
	        View focusNew= edittext.focusSearch(View.FOCUS_FORWARD);
	        if( focusNew!=null ) focusNew.requestFocus();
	    } else {// Insert character
	        editable.insert(start, Character.toString((char) primaryCode));
	    }
	}
	
	public void hideCustomKeyboard() {
		mKeyboardView.setVisibility(View.GONE);
		mKeyboardView.setEnabled(false);
	}

	public void showCustomKeyboard( View v ) {
		mKeyboardView.setVisibility(View.VISIBLE);
		mKeyboardView.setEnabled(true);
	    if( v!=null ){
	    	((InputMethodManager)mHostActivity.getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
	    }
	}

	public boolean isCustomKeyboardVisible() {
	    return mKeyboardView.getVisibility() == View.VISIBLE;
	}


	@Override
	public void onPress(int arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onRelease(int arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onText(CharSequence arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void swipeUp() {
	}
	
	@Override
	public void swipeRight() {
	}

	@Override
	public void swipeLeft() {
	}

	@Override
	public void swipeDown() {
	}

}
