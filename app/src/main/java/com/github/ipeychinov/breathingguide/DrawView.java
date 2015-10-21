package com.github.ipeychinov.breathingguide;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * TODO: document your custom view class.
 */
public class DrawView extends View {

    private static MyPolygon polygon;
    private Path path;
    private Paint paint;

    public DrawView(Context context) {
        super(context);
        init(context);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context);
        init(context);
    }

    public DrawView(Context context, AttributeSet attrs, int ds) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        polygon = new MyPolygon(size.x);

        updatePath();

        paint = new Paint(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
    }

    private void updatePath() {
        path = new Path();
        path.moveTo(polygon.xpoints[0], polygon.ypoints[0]);
        for (int i = 0; i < polygon.npoints; i++) {
            path.lineTo(polygon.xpoints[i], polygon.ypoints[i]);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPath(path, paint);
        canvas.drawCircle(polygon.xpoints[polygon.npoints/2], polygon.ypoints[polygon.npoints/2], 10, paint);

        polygon.translate();
        updatePath();

        this.postInvalidateDelayed(75);
    }

    public static void adjustInGraph(double piModIn) {
        polygon.adjustInGraph(piModIn);
    }

    public static void adjustOutGraph(double piModOut) {
        polygon.adjustOutGraph(piModOut);
    }
}
