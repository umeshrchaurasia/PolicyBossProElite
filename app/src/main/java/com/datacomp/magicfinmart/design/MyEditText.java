package com.datacomp.magicfinmart.design;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class MyEditText extends android.support.v7.widget.AppCompatEditText {

    //The image we are going to use for the Clear button
    //private Drawable imgCloseButton = getResources().getDrawable(R.drawable.clear_button_image);

    public MyEditText(Context context) {
        super(context);
        init();
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        setTypeface();
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    void init() {

        // Set bounds of the Clear button so it will look ok
        //imgCloseButton.setBounds(0, 0, imgCloseButton.getIntrinsicWidth(), imgCloseButton.getIntrinsicHeight());

        // There may be initial text in the field, so we may need to display the  button
        //handleClearButton();

        //if the Close image is displayed and the user remove his finger from the button, clear it. Otherwise do nothing
       /* this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
 
                MyEditText et = MyEditText.this;
 
                if (et.getCompoundDrawables()[2] == null)
                    return false;
                 
                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;
                 
                if (event.getX() > et.getWidth() - et.getPaddingRight() - imgCloseButton.getIntrinsicWidth()) {
                    et.setText("");
                    MyEditText.this.handleClearButton();
                }
                return false;
            }
        });*/
 
        /*//if text changes, take care of the button
        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
 
                MyEditText.this.handleClearButton();
            }
 
            @Override
            public void afterTextChanged(Editable arg0) {
            }
 
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });*/
    }

    //intercept Typeface change and set it with our custom font
    public void setTypeface() {
        super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto_MediumItalic.ttf"));
    }

    public void setTypeface(Typeface tf, int style) {
        if (style == Typeface.BOLD) {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto_MediumItalic.ttf"));
        } else {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto_MediumItalic.ttf"));
        }
    }
     
  /*  void handleClearButton() {
        if (this.getText().toString().equals(""))
        {
            // add the clear button
            this.setCompoundDrawables(this.getCompoundDrawables()[0], this.getCompoundDrawables()[1], null, this.getCompoundDrawables()[3]);
        }
        else
        {
            //remove clear button
            this.setCompoundDrawables(this.getCompoundDrawables()[0], this.getCompoundDrawables()[1], imgCloseButton, this.getCompoundDrawables()[3]);
        }
    }*/
}