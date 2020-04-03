package customfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewHelveticaNeueBold extends TextView {

    public TextViewHelveticaNeueBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextViewHelveticaNeueBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewHelveticaNeueBold(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/HelveticaNeueBd.ttf");
            setTypeface(tf);
        }
    }
}

