package com.example.ass2_3011712;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btn_reset;
    CustomBoardView game_board;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_reset = findViewById(R.id.btn_reset);
        game_board = findViewById(R.id.game_board);
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game_board.startGame();
                game_board.invalidate();
            }
        });
        game_board.setLoseGameListener(new CustomBoardView.LoseGameListener() {
            @Override
            public void onEvent() {
                Toast.makeText(getApplicationContext(), "You have uncovered a mine, press reset to play again", Toast.LENGTH_SHORT).show();
            }
        });
    }
}