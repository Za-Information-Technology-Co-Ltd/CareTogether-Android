package com.za.caretogether.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatRadioButton;

import com.za.caretogether.utils.ViewUtil;

/**
 * Created by arkar on 3/2/17.
 */

public class MMRadioButton extends AppCompatRadioButton {

    public MMRadioButton(Context context) {
        super(context);
//        setFontEmbed(context);
    }

    public MMRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
//        setFontEmbed(context);
    }

    public MMRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFontEmbed(context);
    }

    private void setFontEmbed(Context context) {
        setTypeface(Typeface.createFromAsset(context.getAssets(),
                "fonts/mm3.ttf"));
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (ViewUtil.isZawgyi(getContext())) {
            text = org.rabbitconverter.rabbit.Rabbit.uni2zg(text.toString());
        }
        super.setText(text, type);
    }
}
