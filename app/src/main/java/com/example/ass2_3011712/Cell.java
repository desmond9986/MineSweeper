package com.example.ass2_3011712;

// this class represents a cell in minesweeper
public class Cell {
    private String status;
    public final static String Covered = "covered";
    public final static String Uncovered ="uncovered";
    public final static String Marked = "marked";
    private boolean isMine;
    private int mineAround;

    public Cell(boolean isMine, int mineAround){
        status = Covered;
        this.isMine = isMine;
        this.mineAround = mineAround;
    }
    // call this method to check the status
    public String getStatus() {
        return status;
    }
    // call this method to check the number of mines around this cell
    public int getMineAround() { return mineAround; }
    // call this method to check whether this is a mine
    public boolean isMine() {
        return isMine;
    }
    // call this method to uncover the cell
    public void uncover(){
        status = Uncovered;
    }
    // call this method to mark the cell as mine
    public void mark(){
        status = Marked;
    }
    // call this method to unmark the cell
    public void unmark(){
        status = Covered;
    }
}
