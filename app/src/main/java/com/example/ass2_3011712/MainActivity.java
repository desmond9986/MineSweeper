package com.example.ass2_3011712;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button btn_reset, btn_mode;
    private CustomBoardView game_board;
    private TextView tv_mine, tv_marked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_reset = findViewById(R.id.btn_reset);
        btn_mode = findViewById(R.id.btn_mode);
        game_board = findViewById(R.id.game_board);
        tv_mine = findViewById(R.id.tv_mine);
        tv_marked = findViewById(R.id.tv_marked);
        tv_mine.setText("Mines \n" + game_board.getMineCount());
        tv_marked.setText("Mines marked \n" + game_board.getMarkedCount());
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game_board.startGame();
                btn_mode.setText("Marking Mode");
                tv_mine.setText("Mines \n" + game_board.getMineCount());
                tv_marked.setText("Mines marked \n" + game_board.getMarkedCount());
                game_board.isUncoverMode(true);
                game_board.invalidate();

            }
        });

        btn_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn_mode.getText().toString().equals("Uncover Mode")){
                    btn_mode.setText("Marking Mode");
                    game_board.isUncoverMode(true);
                }
                else{
                    btn_mode.setText("Uncover Mode");
                    game_board.isUncoverMode(false);
                }
            }
        });
        game_board.setLoseGameListener(new CustomBoardView.LoseGameListener() {
            @Override
            public void onEvent() {
                lostgameDialog();
            }
        });
        game_board.setMarkedCountListener(new CustomBoardView.MarkedCountListener() {
            @Override
            public void onMarkedChanged() {
                tv_marked.setText("Mines marked \n" + game_board.getMarkedCount());
            }
        });
        game_board.setWinGameListener(new CustomBoardView.WinGameListener() {
            @Override
            public void onEvent() {
                winGameDialog();
            }
        });
    }

    // a method that showing a dialog to tell user that they have lost the game
    private void lostgameDialog(){
        // we need a builder to create the dialog for us
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // set the title and the message to be displayed on the dialog
        builder.setMessage("You have uncovered a mine, press reset to play again!");

        // add in a positive button here
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        // create the dialog and display it
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // a method that showing a dialog to tell user that they have won the game
    private void winGameDialog(){
        // we need a builder to create the dialog for us
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // set the title and the message to be displayed on the dialog
        builder.setMessage("You have won the game, press reset to play again!");

        // add in a positive button here
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        // create the dialog and display it
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}