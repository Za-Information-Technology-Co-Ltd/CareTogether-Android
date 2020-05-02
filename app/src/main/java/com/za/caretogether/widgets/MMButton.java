package com.za.caretogether.widgets;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

import com.za.caretogether.utils.ViewUtil;

import org.rabbitconverter.rabbit.Rabbit;


/**
 * Created by arkar on 3/2/17.
 */

public class MMButton extends AppCompatButton {

    public MMButton(Context context) {
        super(context);
    }

    public MMButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MMButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (ViewUtil.isZawgyi(getContext())) {
            text = Rabbit.uni2zg(text.toString());
        }
        super.setText(text, type);
    }
}
