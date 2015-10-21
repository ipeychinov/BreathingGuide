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

public class DrawView extends View {

    //Polygon holding the graph points
    private static MyPolygon polygon;

    //Path used to draw the polyline connecting the points of the polygon
    private Path path;

    //Paint for coloring the path and circle
    private Paint paint;

    //Instantiates the View
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

    //Initializes the class members
    private void init(Context context) {
        //get the width of the screen
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        //create new polygon with the width of the screen
        polygon = new MyPolygon(size.x);

        //initialize the path
        updatePath();

        //set paint properties
        paint = new Paint(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
    }

    //Updates the path with the current polygon points
    private void updatePath() {
        path = new Path();
        //set starting point
        path.moveTo(polygon.xpoints[0], polygon.ypoints[0]);

        //draw lines from point to point
        for (int i = 0; i < polygon.npoints; i++) {
            path.lineTo(polygon.xpoints[i], polygon.ypoints[i]);
        }
    }

    //Draws the graph
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //draw path
        canvas.drawPath(path, paint);

        //draw circle around the middle point
        canvas.drawCircle(polygon.xpoints[polygon.npoints/2], polygon.ypoints[polygon.npoints/2], 10, paint);

        //shift polygon to the left
        polygon.translate();
        //update path with new polygon values
        updatePath();

        //schedule invalidation on the
        this.postInvalidateDelayed(75);
    }

    //Adjusts the In graph
    //@param piModIn - Math.PI modifier
    public static void adjustInGraph(double piModIn) {
        polygon.adjustInGraph(piModIn);
    }

    //Adjusts the Out graph
    //@param piModIn - Math.PI modifier
    public static void adjustOutGraph(double piModOut) {
        polygon.adjustOutGraph(piModOut);
    }
}
