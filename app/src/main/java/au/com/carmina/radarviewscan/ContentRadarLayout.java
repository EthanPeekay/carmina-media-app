package au.com.carmina.radarviewscan;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by Admin on 02-02-2017.
 */

public class ContentRadarLayout extends RelativeLayout {

    private Handler handler;

    public ContentRadarLayout(Context context) {
        super(context);
        init();
    }

    @TargetApi(21)
    public ContentRadarLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public ContentRadarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ContentRadarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init()
    {
        this.handler = new Handler();
    }
}
