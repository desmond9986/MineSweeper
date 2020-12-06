package com.example.ass2_3011712;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class CustomBoardView extends View {
    private Cell[][] cells;
    private int coveredColor, markedColor, uncoveredColor, mineColor;
    private Paint paint;
    private Rect square;
    private float cellLength;

    public CustomBoardView(Context context) {
        super(context);
        init(null, 0);
    }

    public CustomBoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CustomBoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

   private void init(AttributeSet attrs, int defStyleAttr){
       // Load attributes specified in attrs_board_view.xml using the name we included
       final TypedArray a = getContext().obtainStyledAttributes(
               attrs, R.styleable.CustomBoardView, defStyleAttr, 0);

       try {
           // get the colors specified using the names in attrs_board_view.xml
           coveredColor = a.getColor(R.styleable.CustomBoardView_coveredColor, 0);
           uncoveredColor = a.getColor(R.styleable.CustomBoardView_uncoveredColor, 0);
           markedColor = a.getColor(R.styleable.CustomBoardView_markedColor, 0);
           mineColor = a.getColor(R.styleable.CustomBoardView_mineColor, 0);
       }finally {
           a.recycle();
       }
       startGame();
   }

   // call this method to start or restart the game
   public void startGame(){
        cells = new Cell[10][10];
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                cells[i][j] = new Cell(false);
            }
        }
   }

   public void onDraw(Canvas canvas){
        // get the view width and set content height same as width
        int contentWidth = getMeasuredWidth();
        int contentHeight = getMeasuredWidth();

        // initiate the paint
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        // set the length of cell
        cellLength = (float)contentWidth/10;

        // draw every cell on board
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                canvas.save();
                canvas.translate((i * cellLength), (j * cellLength));
                if(cells[i][j].getStatus() == Cell.Covered) {
                    paint.setColor(coveredColor);
                    canvas.drawRect(0,0, cellLength, cellLength, paint);
                }
                canvas.restore();
            }
        }

        // draw the vertical and horizontal lines
        paint.setColor(Color.WHITE);
        float xyPoint = cellLength;
        for(int i = 0; i < 11; i++){
            canvas.save();
            canvas.drawLine(xyPoint * i, 0, xyPoint * i, contentHeight, paint);
            canvas.restore();

            canvas.save();
            canvas.drawLine(0, xyPoint * i, contentWidth, xyPoint * i, paint);
            canvas.restore();
        }



   }

    // public method that needs to be overridden to handle the touches from a
    // user
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
    /**
     * Gets the covered color attribute value.
     *
     * @return The covered color attribute value.
     */
    public int getCoveredColor() {
        return coveredColor;
    }
    /**
     * Sets the view's covered color attribute value. In the view, this color
     * is used for covered cell
     *
     * @param coveredColor The covered color attribute value to use.
     */
    public void setCoveredColor(int coveredColor) {
        this.coveredColor = coveredColor;
    }
    /**
     * Gets the marked color attribute value.
     *
     * @return The marked color attribute value.
     */
    public int getMarkedColor() {
        return markedColor;
    }
    /**
     * Sets the view's marked color attribute value. In the view, this color
     * is used for marked cell
     *
     * @param markedColor The marked color attribute value to use.
     */
    public void setMarkedColor(int markedColor) {
        this.markedColor = markedColor;
    }
    /**
     * Gets the uncovered color attribute value.
     *
     * @return The uncovered color attribute value.
     */
    public int getUncoveredColor() {
        return uncoveredColor;
    }
    /**
     * Sets the view's uncovered color attribute value. In the view, this color
     * is used for uncovered cell
     *
     * @param uncoveredColor The uncovered color attribute value to use.
     */
    public void setUncoverColor(int uncoveredColor) {
        this.uncoveredColor = uncoveredColor;
    }
    /**
     * Gets the mine color attribute value.
     *
     * @return The mine color attribute value.
     */
    public int getMineColor() {
        return mineColor;
    }
    /**
     * Sets the view's mine color attribute value. In the view, this color
     * is used for mine cell
     *
     * @param mineColor The mine color attribute value to use.
     */
    public void setMineColor(int mineColor) {
        this.mineColor = mineColor;
    }
}
