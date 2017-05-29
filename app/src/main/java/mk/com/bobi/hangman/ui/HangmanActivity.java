package mk.com.bobi.hangman.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import mk.com.bobi.hangman.R;
import mk.com.bobi.hangman.logic.Game;

public class HangmanActivity extends Activity implements View.OnClickListener{

    @BindView(R.id.firstKeyboardRowLinearLayout) LinearLayout firstKeyboardRow;
    @BindView(R.id.secondKeyboardRowLinearLayout) LinearLayout secondKeyboardRow;
    @BindView(R.id.thirdKeyboardRowLinearLayout) LinearLayout thirdKeyboardRow;
    @BindView(R.id.progressTextView) TextView progressTextView;
    @BindView(R.id.statusTextView) TextView statusTextView;
    @BindView(R.id.hangmanImageView) ImageView hangmanImageView;

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hangman);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        game = new Game(this);
        if (bundle == null) {
            game.setAnswer();
        } else {
            String answer = bundle.getString(InputWordActivity.WORD_KEY, "THERE IS A BUG IN THIS GAME");
            game.setAnswer(answer);
        }

        loadKeyboard();

        String remainingTries = String.format(getString(R.string.tries_remaining), game.getRemainingTries());
        statusTextView.setText(remainingTries);

        progressTextView.setText(game.getCurrentProgress());

    }

    private void loadKeyboard() {
        String[] firstRowChars = getResources().getStringArray(R.array.first_keyboard_row);
        String[] secondRowChars = getResources().getStringArray(R.array.second_keyboard_row);
        String[] thirdRowChars = getResources().getStringArray(R.array.third_keyboard_row);
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.weight = 1;
        for (String s : firstRowChars) {
            Button button = new Button(this);
            button.setText(s);
            button.setOnClickListener(this);
            firstKeyboardRow.addView(button, lp);
        }
        for (String s : secondRowChars) {
            Button button = new Button(this);
            button.setText(s);
            button.setOnClickListener(this);
            secondKeyboardRow.addView(button, lp);
        }
        for (String s : thirdRowChars) {
            Button button = new Button(this);
            button.setText(s);
            button.setOnClickListener(this);
            thirdKeyboardRow.addView(button, lp);
        }
    }

    @Override
    public void onClick(final View view) {
        view.animate().rotation(360).withEndAction(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.INVISIBLE);
            }
        });
        char guessedLetter = ((Button)view).getText().charAt(0);
        if (game.applyGuess(guessedLetter)){
            // guessed letter was correct
            progressTextView.setText(game.getCurrentProgress());
            if (game.isWon()){
                // TODO: game won - basic dialog added. Figure dialog button(s) and navigation afterwards
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(HangmanActivity.this);
                alertDialog.setTitle("Congratulations!");
                alertDialog.setMessage("You won! The word was " + game.getAnswer());

                alertDialog.setPositiveButton("Back to main menu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
                alertDialog.setCancelable(false);
                alertDialog.show();
            }
        } else {
            // guessed letter was wrong
            updateImage();
            if (game.isLost()) {
                // TODO: game lost - basic dialog added. Figure dialog button(s) and navigation afterwards
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(HangmanActivity.this);
                alertDialog.setTitle("Too bad!");
                alertDialog.setMessage("You lost! The word was " + game.getAnswer());

                alertDialog.setPositiveButton("Back to main menu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
                alertDialog.setCancelable(false);
                alertDialog.show();
            }
            String remainingTries = String.format(getString(R.string.tries_remaining), game.getRemainingTries());
            statusTextView.setText(remainingTries);
        }
    }

    private void updateImage() {
        int remainingTries = game.getRemainingTries();
        switch (remainingTries) {
            case 6:
                hangmanImageView.setImageResource(R.mipmap.hangman_0);
                break;
            case 5:
                hangmanImageView.setImageResource(R.mipmap.hangman_1);
                break;
            case 4:
                hangmanImageView.setImageResource(R.mipmap.hangman_2);
                break;
            case 3:
                hangmanImageView.setImageResource(R.mipmap.hangman_3);
                break;
            case 2:
                hangmanImageView.setImageResource(R.mipmap.hangman_4);
                break;
            case 1:
                hangmanImageView.setImageResource(R.mipmap.hangman_5);
                break;
            case 0:
                hangmanImageView.setImageResource(R.mipmap.hangman_6);
                break;

        }

    }

}
