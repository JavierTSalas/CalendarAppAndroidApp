package com.salas.javiert.magicmirror.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import com.salas.javiert.magicmirror.R;

/**
 * Created by javi6 on 5/28/2017.
 */

public class ClassView extends View {

    boolean mExpanded;
    boolean mHasImage;
    boolean mShowText;

    public ClassView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ClassView,
                0, 0);

        try {
            mExpanded = a.getBoolean(R.styleable.ClassView_expanded, false);
            mHasImage = a.getBoolean(R.styleable.ClassView_hasImage, false);
            mShowText = a.getBoolean(R.styleable.ClassView_showText, false);
        } finally {
            a.recycle();
        }
    }

    public boolean isExpanded() {
        return mExpanded;
    }

    public boolean hasImage() {
        return mHasImage;
    }

    public boolean showText() {
        return mShowText;
    }
}



