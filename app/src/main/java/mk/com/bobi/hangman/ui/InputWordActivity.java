package mk.com.bobi.hangman.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import mk.com.bobi.hangman.R;

public class InputWordActivity extends Activity {

    @BindView(R.id.enteredWordEditText) EditText enteredWordEditText;
    @BindView(R.id.submitWordButton) Button submitWordButton;

    public static final String WORD_KEY = "WORD_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_word);
        ButterKnife.bind(this);

        submitWordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(enteredWordEditText.getText().toString().trim().equals("")){
                    Toast.makeText(InputWordActivity.this, "Please enter a word", Toast.LENGTH_SHORT).show();
                } else {
                    ActivityOptionsCompat options =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(InputWordActivity.this);
                    Intent intent = new Intent(InputWordActivity.this, HangmanActivity.class);
                    String trimmedWord = enteredWordEditText.getText().toString().toUpperCase().trim();
                    trimmedWord = trimmedWord.replaceAll("\\s+", " ");
                    intent.putExtra(WORD_KEY, trimmedWord);
                    startActivity(intent, options.toBundle());
                    finish();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        enteredWordEditText.setText("");
    }
}
