package com.example.ass2_3011712;

// this class represents a cell in minesweeper
public class Cell {
    private String status;
    private final String Covered = "covered";
    private final String Uncovered ="uncovered";
    private final String Marked = "marked";
    private boolean isMine;

    public Cell(boolean isMine){
        status = Covered;
        this.isMine = isMine;
    }
    // call this method to check the
    public String getStatus() {
        return status;
    }
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
