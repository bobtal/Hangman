package mk.com.bobi.hangman.logic;

import android.content.Context;
import android.content.ContextWrapper;

import java.util.Random;

import mk.com.bobi.hangman.R;

/**
 * Created by Boban Talevski on 22.04.2017.
 */

public class Game extends ContextWrapper {
    public static final int MAX_MISSES = 7;
    private String answer;
    private String hits;
    private int misses;

    public Game(Context base) {
        super(base);
        hits = "";
        misses = 0;
    }

    public void setAnswer(){
        String[] words = getResources().getStringArray(R.array.words);
        Random random = new Random();
        answer = (words[random.nextInt(words.length)]).toUpperCase();
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getRemainingTries() {
        return MAX_MISSES - misses;
    }

    // Updates the progress String with underscores for each unknown letter
    // adding non breaking spaces between each letter (guessed or unguessed) to
    // simulate the letter spacing missing in versions prior to 21
    public String getCurrentProgress() {
        String progress = "";
        for (char c : answer.toCharArray()) {
            String display;
            if (c == ' ') {
                display = "\u00A0 \u00A0";
                progress += display;
                continue;
            }
            // it's a guessed letter
            if (hits.indexOf(c) != -1) {
                display = "\u00A0" + c + "\u00A0";
            } else {
                display = "\u00A0" + "_" + "\u00A0";
            }
            progress += display;
        }
        return progress;
    }

    public boolean applyGuess(char letter) {
        boolean isHit = answer.indexOf(letter) != -1;
        if (isHit) {
            hits += letter;
        } else {
            misses++;
        }
        getCurrentProgress();
        return isHit;
    }

    public boolean isLost() {
        return getRemainingTries() == 0;
    }

    public boolean isWon() {
        return getCurrentProgress().indexOf('_') == -1;
    }

    public String getAnswer() {
        return answer.toUpperCase();
    }

}
