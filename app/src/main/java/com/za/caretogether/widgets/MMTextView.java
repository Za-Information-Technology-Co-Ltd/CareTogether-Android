package com.za.caretogether.widgets;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.za.caretogether.utils.ViewUtil;


/**
 * Created by arkar on 3/2/17.
 */

public class MMTextView extends AppCompatTextView {

    public MMTextView(Context context) {
        super(context);
    }

    public MMTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MMTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (ViewUtil.isZawgyi(getContext())) {
            text = org.rabbitconverter.rabbit.Rabbit.uni2zg(text.toString());
        }
        super.setText(text, type);
    }
}
