package com.example.ass2_3011712;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomBoardView extends View {
    private Cell[][] cells;

    private int coveredColor, markedColor, uncoveredColor, mineColor; // color for different state of cell
    private Paint paint; // paint tool
    private TextPaint textPaint;    // text paint tool

    private float cellLength;   // length of cell
    private int contentWidth, contentHeight; // width and height for view
    private RectF textBounds;
    private boolean mineFound;  // store boolean to tract whether u user have clicked a mine
    private LoseGameListener loseGameListener;  // listener for lose game
    private MarkedCountListener markedCountListener;    // listener for mark cell
    private boolean isUncoverMode;  // store boolean to track the mode of current board
    private int mineCount, markedCount; // store integer to track the number of mines and mines marked

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
       // initiate the paint
       paint = new Paint(Paint.ANTI_ALIAS_FLAG);
       textPaint = new TextPaint();
       textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
       // start game
       startGame();
   }

   // colculate the mine around each cell
   private ArrayList<Integer> mineAroundCount(ArrayList<Integer> mines){
        ArrayList<Integer> mineAround = new ArrayList<Integer>();
        int count;
        for(int i = 1; i <= 100; i++){
            count = 0;
            // check if the cell is above the last row
            if(i < 91){
                if(mines.contains(i+10))
                    count++;
                // check if the cell is not the first column
                if(i % 10 != 1){
                    if(mines.contains(i+10-1))
                        count++;
                }
                // check if the cell is not the last column
                if(i % 10 != 0){
                    if(mines.contains(i+10+1))
                        count++;
                }
            }
            // check if the cell is below first row
            if(i > 10){
                if(mines.contains(i-10))
                    count++;
                // check if the cell is not the first column
                if(i % 10 != 1){
                    if(mines.contains(i-10-1))
                        count++;
                }
                // check if the cell is not the last column
                if(i % 10 != 0){
                    if(mines.contains(i-10+1))
                        count++;
                }
            }
            // check if the cell is not the first column
            if(i % 10 != 1){
                if(mines.contains(i-1))
                    count++;
            }
            // check if the cell is not the last column
            if(i % 10 != 0){
                if(mines.contains(i+1))
                    count++;
            }
            mineAround.add(count);
        }
        return mineAround;
   }

   // call this method to start or restart the game
   public void startGame() {
       ArrayList<Integer> mines = setMines();
       ArrayList<Integer> mineAround = mineAroundCount(mines);
       cells = new Cell[10][10];
       for (int i = 0; i < 10; i++) {
           for (int j = 0; j < 10; j++) {
               // check if this iteration is a mine
               if (mines.contains(i * 10 + j + 1))
                   cells[i][j] = new Cell(true, mineAround.get(i * 10 + j));
               else
                   cells[i][j] = new Cell(false, mineAround.get(i * 10 + j));
           }
       }
       // set mine found to false
       mineFound = false;
       isUncoverMode = true;
       // set the initial state of board
       markedCount = 0;
       mineCount = mines.size();
   }
   // call this method and return the list that randomly place 20 mines in cells
   private ArrayList<Integer> setMines(){
        ArrayList<Integer> mines = new ArrayList<Integer>();
        int check;
        for(int i = 0; i < 20; i++){
            do {
                check = (int) (Math.random() * 100) + 1;
                // check if this number is unique
                if (!mines.contains(check)) {
                    mines.add(check);
                    break;
                }
            }while(true);
        }
        return mines;
   }

   public void onDraw(Canvas canvas){
        // set the length of cell
        cellLength = (float)contentWidth/10;
        // set text size
        textPaint.setTextSize(cellLength/1.5f);
        textPaint.setTextAlign(Paint.Align.CENTER);
        // draw every cell on board
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                canvas.save();
                canvas.translate((j * cellLength), (i * cellLength));
                if(cells[i][j].getStatus().equals(Cell.Covered))
                    paint.setColor(coveredColor);
                else if(cells[i][j].getStatus().equals(Cell.Uncovered)){
                    if(cells[i][j].isMine())
                        paint.setColor(mineColor);
                    else
                        paint.setColor(uncoveredColor);
                }
                else if(cells[i][j].getStatus().equals(Cell.Marked))
                    paint.setColor(markedColor);
                canvas.drawRect(0,0, cellLength, cellLength, paint);
                canvas.restore();
                // check if this is an uncovered cell
                if(cells[i][j].getStatus().equals(Cell.Uncovered)){
                    // set vertical center for the text
                    float textHeight = textPaint.descent() - textPaint.ascent();
                    float textOffset = (textHeight / 2) - textPaint.descent();
                    textBounds = new RectF(0, 0, cellLength, cellLength);

                    canvas.save();
                    canvas.translate((j * cellLength), (i * cellLength));
                    // check if this is a mine
                    if(cells[i][j].isMine()) {
                        textPaint.setColor(Color.BLACK);
                        canvas.drawText("M", textBounds.centerX(), textBounds.centerY() + textOffset, textPaint);
                        // set mine found to true
                        mineFound = true;
                        loseGameListener.onEvent();
                    }
                    else{
                        // check if the mines around the cell is not 0
                        if(cells[i][j].getMineAround() != 0) {
                            textPaint.setColor(Color.WHITE);
                            canvas.drawText("" + cells[i][j].getMineAround(), textBounds.centerX(), textBounds.centerY() + textOffset, textPaint);
                        }
                        }
                    canvas.restore();

                }

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

   // call this method to check if the mines around the cell is zero then uncover all the cells around it
   private void checkMineAroundAndUncover(int row, int col){
        // check if the cell is not mine
        if(!cells[row][col].isMine()){
            // check if the mines around the cell is zero
            if(cells[row][col].getMineAround() == 0){
                // check if the cell is above the last row
                if(row < 9){
                    // check if the cell below is covered then uncover it
                    if(cells[row + 1][col].getStatus().equals(Cell.Covered)) {
                        cells[row + 1][col].uncover();
                        checkMineAroundAndUncover(row + 1, col);
                    }
                    // check if the cell is not the first column
                    if(col > 0){
                        if(cells[row + 1][col - 1].getStatus().equals(Cell.Covered)) {
                            cells[row + 1][col - 1].uncover();
                            checkMineAroundAndUncover(row + 1, col - 1);
                        }
                    }
                    // check if the cell is not the last column
                    if(col < 9){
                        if(cells[row + 1][col + 1].getStatus().equals(Cell.Covered)) {
                            cells[row + 1][col + 1].uncover();
                            checkMineAroundAndUncover(row + 1, col + 1);
                        }
                    }
                }
                // check if the cell is below first row
                if(row > 0){
                    if(cells[row - 1][col].getStatus().equals(Cell.Covered)) {
                        cells[row - 1][col].uncover();
                        checkMineAroundAndUncover(row - 1, col);
                    }
                    // check if the cell is not the first column
                    if(col > 0){
                        if(cells[row - 1][col - 1].getStatus().equals(Cell.Covered)) {
                            cells[row - 1][col - 1].uncover();
                            checkMineAroundAndUncover(row - 1, col - 1);
                        }
                    }
                    // check if the cell is not the last column
                    if(col < 9){
                        if(cells[row - 1][col + 1].getStatus().equals(Cell.Covered)) {
                            cells[row - 1][col + 1].uncover();
                            checkMineAroundAndUncover(row - 1, col + 1);
                        }
                    }
                }
                // check if the cell is not the first column
                if(col > 0){
                    if(cells[row][col - 1].getStatus().equals(Cell.Covered)) {
                        cells[row][col - 1].uncover();
                        checkMineAroundAndUncover(row, col - 1);
                    }
                }
                // check if the cell is not the last column
                if(col < 9){
                    if(cells[row][col + 1].getStatus().equals(Cell.Covered)) {
                        cells[row][col + 1].uncover();
                        checkMineAroundAndUncover(row, col + 1);
                    }
                }
            }
        }
   }

    // public method that needs to be overridden to handle the touches from a
    // user
    public boolean onTouchEvent(MotionEvent event) {
        // check if a mine is uncovered
        if(!mineFound) {
            // determine what kind of touch event we have
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                //Get where the event occurred.
                float x = event.getX();
                float y = event.getY();

                int row = 0;
                int col = 0;

                // check which column is touched
                for (int i = 1; i <= 10; i++) {
                    if (x < (i * cellLength)) {
                        col = i;
                        break;
                    }
                }
                // check which row is touched
                for (int i = 1; i <= 10; i++) {
                    if (y < (i * cellLength)) {
                        row = i;
                        break;
                    }
                }
                if(isUncoverMode) {
                    // only to uncover the covered cell which is not marked
                    if(cells[row - 1][col - 1].getStatus().equals(Cell.Covered)) {
                        // uncover the cell that touched by user
                        cells[row - 1][col - 1].uncover();
                        if(cells[row - 1][col - 1].getStatus().equals(Cell.Uncovered))
                            checkMineAroundAndUncover(row - 1, col - 1);
                    }
                }
                else{
                    // unmark a uncovered cell
                    if(cells[row - 1][col - 1].getStatus().equals(Cell.Marked)){
                        // unmark the cell that touched by user
                        cells[row - 1][col - 1].unmark();
                        // decrement of the marked count and notify listener
                        markedCount--;
                        markedCountListener.onMarkedChanged();
                    }
                    // mark a covered cell
                    else if(cells[row - 1][col - 1].getStatus().equals(Cell.Covered)){
                        // mark the cell that touched by user
                        cells[row - 1][col - 1].mark();
                        // increment of the marked count and notify listener
                        markedCount++;
                        markedCountListener.onMarkedChanged();
                    }
                }
                return true;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                // tell android the state of this view has changed
                invalidate();
                return true;
            }
        }
        else if (mineFound && event.getAction() == MotionEvent.ACTION_DOWN) {
            // tell user that they have lost the game
            loseGameListener.onEvent();
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // get the view width and set content height same as width
        contentWidth = getMeasuredWidth();
        contentHeight = getMeasuredWidth();
        this.setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
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
    /**
     * Gets the mine count value.
     *
     * @return The number of mines.
     */
    public int getMineCount(){
        return mineCount;
    }
    /**
     * Gets the mines marked count value.
     *
     * @return The number of mines marked.
     */
    public int getMarkedCount(){
        return markedCount;
    }
    /**
     * Sets the mode of the game
     *
     * @param isUncoverMode If this is uncover mode then set true.
     */
    public void isUncoverMode(boolean isUncoverMode) {
        this.isUncoverMode = isUncoverMode;
    }
    // interface of lose game listener
    public interface LoseGameListener{
        void onEvent();
    }
    public void setLoseGameListener(LoseGameListener eventListener) {
        loseGameListener = eventListener;
    }
    // interface of mines marked count listener
    public interface MarkedCountListener{
        void onMarkedChanged();
    }
    public void setMarkedCountListener(MarkedCountListener eventListener){
        markedCountListener = eventListener;
    }
}
