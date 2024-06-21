package com.example.tic_tac_toe;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    TextView scoreX, scoreO, turn, restart;
    ImageButton[] buttons = new ImageButton[9];
    ImageButton resetScoreButton;
    boolean turnVal = true;
    int[][] board = new int[3][3];
    int scoreXCount = 0;
    int scoreOCount = 0;
    int moveCount = 0;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        scoreX = findViewById(R.id.scoreX);
        scoreO = findViewById(R.id.scoreO);
        turn = findViewById(R.id.turn);
        restart = findViewById(R.id.restart);
        resetScoreButton = findViewById(R.id.reset_score); // Initialize the reset score button

        buttons[0] = findViewById(R.id.button1);
        buttons[1] = findViewById(R.id.button2);
        buttons[2] = findViewById(R.id.button3);
        buttons[3] = findViewById(R.id.button4);
        buttons[4] = findViewById(R.id.button5);
        buttons[5] = findViewById(R.id.button6);
        buttons[6] = findViewById(R.id.button7);
        buttons[7] = findViewById(R.id.button8);
        buttons[8] = findViewById(R.id.button9);

        sharedPreferences = getSharedPreferences("TicTacToeScores", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        loadScores();

        for (int i = 0; i < buttons.length; i++) {
            setButtonClickListener(buttons[i], i);
        }

        restart.setOnClickListener(v -> resetGame());
        resetScoreButton.setOnClickListener(v -> resetScores()); // Set click listener for reset score button
    }

    private void setButtonClickListener(ImageButton button, int buttonIndex) {
        button.setOnClickListener(view -> {
            int row = buttonIndex / 3;
            int col = buttonIndex % 3;
            if (board[row][col] == 0) {
                board[row][col] = turnVal ? 1 : 2;
                button.setImageResource(turnVal ? R.drawable.cross : R.drawable.circle);
                button.setEnabled(false);
                moveCount++;
                if (checkWin()) {
                    if (turnVal) {
                        scoreXCount++;
                        scoreX.setText("X: " + scoreXCount);
                        saveScores();
                        Toast.makeText(this, "X wins!", Toast.LENGTH_SHORT).show();
                    } else {
                        scoreOCount++;
                        scoreO.setText("O: " + scoreOCount);
                        saveScores();
                        Toast.makeText(this, "O wins!", Toast.LENGTH_SHORT).show();
                    }
                    disableButtons();
                } else if (moveCount == 9) {
                    Toast.makeText(this, "It's a draw!", Toast.LENGTH_SHORT).show();
                } else {
                    turnVal = !turnVal;
                    turn.setText(turnVal ? "X's Turn" : "O's Turn");
                }
            }
        });
    }

    private boolean checkWin() {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != 0 && board[i][0] == board[i][1] && board[i][1] == board[i][2])
                return true;
            if (board[0][i] != 0 && board[0][i] == board[1][i] && board[1][i] == board[2][i])
                return true;
        }
        if (board[0][0] != 0 && board[0][0] == board[1][1] && board[1][1] == board[2][2])
            return true;
        return board[0][2] != 0 && board[0][2] == board[1][1] && board[1][1] == board[2][0];
    }

    private void disableButtons() {
        for (ImageButton button : buttons) {
            button.setEnabled(false);
        }
    }

    private void resetGame() {
        board = new int[3][3];
        turnVal = true;
        moveCount = 0;
        turn.setText("X's Turn");
        for (ImageButton button : buttons) {
            button.setImageResource(android.R.color.transparent);
            button.setEnabled(true);
        }
    }

    private void resetScores() {
        scoreXCount = 0;
        scoreOCount = 0;
        scoreX.setText("X: " + scoreXCount);
        scoreO.setText("O: " + scoreOCount);
        saveScores();
        Toast.makeText(this, "Scores have been reset!", Toast.LENGTH_SHORT).show();
    }

    private void saveScores() {
        editor.putInt("scoreX", scoreXCount);
        editor.putInt("scoreO", scoreOCount);
        editor.apply();
    }

    private void loadScores() {
        scoreXCount = sharedPreferences.getInt("scoreX", 0);
        scoreOCount = sharedPreferences.getInt("scoreO", 0);
        scoreX.setText("X: " + scoreXCount);
        scoreO.setText("O: " + scoreOCount);
    }
}
