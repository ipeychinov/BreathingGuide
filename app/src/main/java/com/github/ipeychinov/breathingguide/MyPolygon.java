package com.github.ipeychinov.breathingguide;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MyPolygon {

    //array of X coordinates of polygon elements
    public int[] xpoints;

    //array of Y coordinates of polygon elements
    public int[] ypoints;

    //number of points in the polygon
    public int npoints;

    //list of Y coordinates of the breathing IN graph
    private List<Integer> breathInGraph;

    //list of Y coordinates of the breathing OUT graph
    private List<Integer> breathOutGraph;

    //Index of next element of IN graph to be added to polygon
    private int breathInIndex;

    //Index of next element of OUT graph to be added to polygon
    private int breathOutIndex;

    //Flag indicating IN/OUT graph at the end of the polygon
    //positive -> breathIn
    //negative -> breathOut
    private int directionFlag;

    //Instantiates a new Polygon with a default size of 400 points
    public MyPolygon() {
        this(400);
    }

    //Instantiates a new Polygon with the width of the screen
    //@param screenWidth - the width of the screen o.O
    public MyPolygon(int screenWidth) {
        npoints = screenWidth;

        xpoints = new int[npoints];
        ypoints = new int[npoints];

        breathInGraph = new ArrayList<Integer>();
        breathOutGraph = new ArrayList<Integer>();

        breathInIndex = 0;
        breathOutIndex = 0;

        directionFlag = 0;

        initGraph(2.5, 2.5);
        initPolygon();
    }

    //Resets the current polygon to an empty one
    public void reset() {
        xpoints = new int[npoints];
        ypoints = new int[npoints];

        breathInGraph.clear();
        breathOutGraph.clear();

        breathInIndex = 0;
        breathOutIndex = 0;
    }

    //Initializes the Breathing Graphs
    //@param piModIn - Math.PI modifier for the breathingInGraph
    //@param piModOut - Math.PI modifier for the breathingOutGraph
    private void initGraph(double piModIn, double piModOut) {
        //First derivative of the function defining the Y coordinates
        int derivative = 0;

        boolean breathInFull = false;
        boolean breathInFound = false;
        boolean breathOutFull = false;
        boolean breathOutFound = false;

        //Loop for filling the graphs with points
        for (int x = 0, y = 0; ; x++) {
            //Break out of endless loop when graphs are full
            if (breathInFull && breathOutFull) break;

            // BreathInGraph
            derivative = roundDerivative((50 * Math.PI * piModIn * Math.cos((Math.PI * piModIn * x) / 100.0) / (-100.0)));
            y = 100 - (int) (50 * Math.sin((x / 100.0) * piModIn * Math.PI));

            //When derivative == 0 and y > 100 we have a Minimum
            //indicating a starting point for the breathInGraph
            if (!breathInFull && derivative == 0 && y > 100) {
                breathInFound = true;
            }
            if (breathInFound) {

                //When derivative == 0 and y < 100 we have a Maximum
                //indicating an ending point for the breathInGraph
                if (derivative == 0 && y < 100) {
                    breathInFull = true;
                    breathInFound = false;
                } else {
                    breathInGraph.add(y*2);
                }
            }

            // BreathOutGraph
            derivative = roundDerivative((50 * Math.PI * piModOut * Math.cos((Math.PI * piModOut * x) / 100.0) / (-100.0)));
            y = 100 - (int) (50 * Math.sin((x / 100.0) * piModOut * Math.PI));

            //When derivative == 0 and y < 100 we have a Maximum
            //indicating a starting point for the breathOutGraph
            if (!breathOutFull && derivative == 0 && y < 100) {
                breathOutFound = true;
            }
            if (breathOutFound) {

                //When derivative == 0 and y > 100 we have a Minimum
                //indicating an ending point for the breathOutGraph
                if (derivative == 0 && y > 100) {
                    breathOutFull = true;
                    breathOutFound = false;
                } else {
                    breathOutGraph.add(y*2);
                }
            }
        }
    }

    //Initializes the Polygon
    private void initPolygon() {
        directionFlag = -1;
        for (int x = 0; x < npoints; x++) {
            xpoints[x] = x;
            if (directionFlag < 0) {
                if (breathOutIndex < breathOutGraph.size()) {
                    ypoints[x] = breathOutGraph.get(breathOutIndex++);
                } else {
                    directionFlag = 1;
                    breathOutIndex = 0;
                    ypoints[x] = breathInGraph.get(breathInIndex++);
                }
            } else {
                if (breathInIndex < breathInGraph.size()) {
                    ypoints[x] = breathInGraph.get(breathInIndex++);
                } else {
                    directionFlag = -1;
                    breathInIndex = 0;
                    ypoints[x] = breathOutGraph.get(breathOutIndex++);
                }
            }
        }
    }

    //Rounds derivative if it is close enough to 0(zero)
    private int roundDerivative(double x) {
        if (x > -0.1 && x < 0.1) {
            return 0;
        } else if (x < -0.1) {
            return -1;
        } else {
            return 1;
        }
    }

    //Shifts the Polygon one point to the left
    public void translate() {
        for (int i = 0; i < npoints - 1; i++) {
            ypoints[i] = ypoints[i + 1];
        }

        //shifts the last point from the appropriate graph
        if (directionFlag < 0) {
            if (breathOutIndex < breathOutGraph.size()) {
                ypoints[npoints - 1] = breathOutGraph.get(breathOutIndex++);
            } else {
                directionFlag = 1;
                breathOutIndex = 0;
            }
        } else if (directionFlag > 0) {
            if (breathInIndex < breathInGraph.size()) {
                ypoints[npoints - 1] = breathInGraph.get(breathInIndex++);
            } else {
                directionFlag = -1;
                breathInIndex = 0;
            }
        }
    }

    //Shifts the Polygon deltaX points to the left
    public void translate(int deltaX){
        for(int i = 0; i < npoints - deltaX; i++){
            ypoints[i] = ypoints[i + deltaX];
        }

        for(int i = npoints - deltaX; i < npoints; i++){
            if (directionFlag < 0) {
                if (breathOutIndex < breathOutGraph.size()) {
                    ypoints[i] = breathOutGraph.get(breathOutIndex++);
                } else {
                    directionFlag = 1;
                    breathOutIndex = 0;
                    ypoints[i] = breathInGraph.get(breathInIndex++);
                }
            } else if (directionFlag > 0) {
                if (breathInIndex < breathInGraph.size()) {
                    ypoints[i] = breathInGraph.get(breathInIndex++);
                } else {
                    directionFlag = -1;
                    breathInIndex = 0;
                    ypoints[i] = breathOutGraph.get(breathOutIndex++);
                }
            }
        }
    }

    //Adjusts the Polygon middle
    //@param midPointY - Y coordinate of the previous middle point
    public void adjust(int midPointY, int midDirectionFlag) {

        //initiates it with new values
        initPolygon();

        //finds a similar middle point to the right of the center
        for(int i = npoints-1; i > npoints/2; i--){
            if(ypoints[i] + 1 > midPointY && ypoints[i] - 1 < midPointY){
                if(findPointDirection(i) == midDirectionFlag) {
                    translate(i - npoints/2);
                    break;
                }
            }
        }
    }

    //Adjusts the Polygon according to new Math.PI modifier
    //@param piModIn - Math.PI modifier
    public void adjustInGraph(double piModIn){
        //saves the Y coordinate of the previous middle point
        int midPointY = ypoints[npoints/2];
        int midDirectionFlag = findPointDirection(npoints / 2);

        //reset the breathInGraph and the last element index
        breathInGraph.clear();
        breathInIndex = 0;
        breathOutIndex = 0;

        //First derivative of the function defining the Y coordinates
        int derivative = 0;

        boolean breathInFull = false;
        boolean breathInFound = false;

        //Loop for filling the graph with points
        for (int x = 0, y = 0; ; x++) {
            //Break out of endless loop when graph is full
            if (breathInFull) break;

            derivative = roundDerivative((50.0 * Math.PI * piModIn * Math.cos((Math.PI * piModIn * x) / 100.0) / (-100.0)));
            y = 100 - (int) (50 * Math.sin((x / 100.0) * piModIn * Math.PI));

            //When derivative == 0 and y > 100 we have a Minimum
            //indicating a starting point for the breathInGraph
            if (!breathInFull && derivative == 0 && y > 100) {
                breathInFound = true;
            }
            if (breathInFound) {

                //When derivative == 0 and y < 100 we have a Maximum
                //indicating an ending point for the breathInGraph
                if (derivative == 0 && y < 100) {
                    breathInFull = true;
                    breathInFound = false;
                } else {
                    breathInGraph.add(y*2);
                }
            }
        }

        adjust(midPointY, midDirectionFlag);
    }

    //Adjusts the Polygon according to new Math.PI modifier
    //@param piModOut - Math.PI modifier
    public void adjustOutGraph(double piModOut){
        int midPointY = ypoints[npoints/2];
        int midDirectionFlag = findPointDirection(npoints / 2);

        //reset the breathOutGraph and the last element index
        breathOutGraph.clear();
        breathInIndex = 0;
        breathOutIndex = 0;

        //First derivative of the function defining the Y coordinates
        int derivative = 0;

        boolean breathOutFull = false;
        boolean breathOutFound = false;

        //Loop for filling the graphs with points
        for (int x = 0, y = 0; ; x++) {
            //Break out of endless loop when graph is full
            if (breathOutFull) break;

            derivative = roundDerivative((50.0 * Math.PI * piModOut * Math.cos((Math.PI * piModOut * x) / 100.0) / (-100.0)));
            y = 100 - (int) (50 * Math.sin((x / 100.0) * piModOut * Math.PI));

            //When derivative == 0 and y < 100 we have a Maximum
            //indicating a starting point for the breathOutGraph
            if (!breathOutFull && derivative == 0 && y < 100) {
                breathOutFound = true;
            }
            if (breathOutFound) {

                //When derivative == 0 and y > 100 we have a Minimum
                //indicating an ending point for the breathOutGraph
                if (derivative == 0 && y > 100) {
                    breathOutFull = true;
                    breathOutFound = false;
                } else {
                    breathOutGraph.add(y*2);
                }
            }
        }

        adjust(midPointY, midDirectionFlag);
    }

    //Determines the graph to which the point belongs
    //@param point - target point
    private int findPointDirection(int point) {
        int countBack = npoints;
        int tempDirectionFlag = directionFlag;

        if (tempDirectionFlag < 0) {
            countBack -= breathOutIndex;
            tempDirectionFlag = 1;
        } else {
            countBack -= breathInIndex;
            tempDirectionFlag = -1;
        }

        if(point > countBack) return tempDirectionFlag;

        while (true) {
            if (tempDirectionFlag < 0) {
                countBack -= breathOutGraph.size();
                if (point > countBack) {
                    break;
                } else {
                    tempDirectionFlag = 1;
                }
            } else {
                countBack -= breathInGraph.size();
                if (point > countBack) {
                    break;
                } else {
                    tempDirectionFlag = -1;
                }
            }
        }
        return tempDirectionFlag;
    }

}
