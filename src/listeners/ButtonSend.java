package listeners;

import models.Database;
import models.Model;
import views.View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Nupu Saada funktsionaalsus. Võtab mängija sisestatud tähe ja kontrollib mängu olekut
 */
public class ButtonSend implements ActionListener {
    private final Model model;
    private final View view;

    /**
     * Saada nupu konstruktor
     * @param model Rakenduse model
     * @param view Rakenduse vaade
     */
    public ButtonSend(Model model, View view) {
        this.model = model;
        this.view = view;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String input = view.getGameBoard().getTxtChar().getText().trim().toUpperCase();
        if (input.length() != 1 || !input.matches("[A-ZÄÖÕÜ]")) {
            JOptionPane.showMessageDialog(view, "Sisesta üks täht!");
            return;
        }

        char guessedChar = input.charAt(0);
        boolean isCorrect = model.getCurrentWord().indexOf(guessedChar) >= 0;

        if (isCorrect) {
            // Uuendab äraarvatud tähe
            for (int i = 0; i < model.getCurrentWord().length(); i++) {
                if (model.getCurrentWord().charAt(i) == guessedChar) {
                    model.getGuessedChars()[i] = guessedChar;
                }
            }
            view.getGameBoard().updateGameBoard();
        } else {
            model.getWrongGuesses().add(guessedChar);
            view.getGameBoard().updateGameBoard();
        }

        if (isGameOver()) {
            view.getGameTimer().stopTime();
            view.getGameTimer().setRunning(false);

            if (new String(model.getGuessedChars()).indexOf('_') == -1) {
                String playerName = JOptionPane.showInputDialog(view, "Palju õnne arvasid sõna ära! Sisesta oma nimi: ");
                if (playerName != null && !playerName.trim().isEmpty()) {
                    Database.getInstance(model).saveScore(playerName, model.getCurrentWord().toLowerCase(), model.getWrongGuesses().toString(), view.getGameTimer().getPlayedTimeInSeconds());
                }
            } else {
                JOptionPane.showMessageDialog(view, "Mäng läbi! Sõna oli: " + model.getCurrentWord());
            }

            view.showButtons();
            view.getGameTimer().stopTime();
            view.getGameTimer().setRunning(false);

            model.getWrongGuesses().clear();
        }
    }

    /**
     * Kontrollib, kas mäng on läbi
     * @return true, kui mäng läbi, false kui mitte
     */
    private boolean isGameOver() {
        if (model.getGuessedChars() == null) {
            return false;
        }

        if (model.getWrongGuesses() == null) {
            return false;
        }

        boolean gameOver = model.getWrongGuesses().size() >= model.getImageFiles().size() - 1 ||
                new String(model.getGuessedChars()).indexOf('_') == -1;

        System.out.println("Is game over: " + gameOver);
        return gameOver;
    }
}
