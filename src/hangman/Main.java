package hangman;

import org.json.JSONException;

import java.io.IOException;

public  class Main {

    public static void main(String[] args) throws IOException, JSONException, InvalidCountException, InvalidRangeException, UnbalancedException, UndersizeException {

        Game.Start();

    }
}