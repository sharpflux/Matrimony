package customfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewSFProTextHeavy extends TextView {

    public TextViewSFProTextHeavy(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextViewSFProTextHeavy(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewSFProTextHeavy(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/SanFranciscoText-Heavy.otf");
            setTypeface(tf);
        }
    }
}

