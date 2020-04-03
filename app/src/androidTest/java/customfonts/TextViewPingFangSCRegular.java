package customfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewPingFangSCRegular  extends TextView {

    public TextViewPingFangSCRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextViewPingFangSCRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewPingFangSCRegular(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/PingFang-SC-Regular.ttf");
            setTypeface(tf);
        }
    }
}
