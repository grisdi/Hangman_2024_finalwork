package listeners;

import models.Database;
import models.Model;
import views.View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ButtonSend implements ActionListener {
    private final Model model;
    private final View view;
    private Database database;

    public ButtonSend(Model model, View view, Database database) {
        this.model = model;
        this.view = view;
        this.database = database;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String input = view.getGameBoard().getTxtChar().getText().trim().toUpperCase();
        if (input.length() != 1) {
            JOptionPane.showMessageDialog(view, "Sisesta üks täht!");
            return;
        }

        char guessedChar = input.charAt(0);
        System.out.println("Guessed characters: " + guessedChar);
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
                    database.saveScore(playerName, model.getCurrentWord(), model.getWrongGuesses().toString(), view.getGameTimer().getPlayedTimeInSeconds());
                }
            } else {
                JOptionPane.showMessageDialog(view, "Mäng läbi! Sõna oli: " + model.getCurrentWord());
            }

            int playAgain = JOptionPane.showConfirmDialog(view, "Kas soovite uuesti mängida?", "Mäng läbi", JOptionPane.YES_NO_OPTION);
            if (playAgain == JOptionPane.YES_OPTION) {
                view.getSettings().getBtnNewGame().doClick();
            } else {
                view.showButtons();
                view.getGameBoard().getTxtChar().setEnabled(false); // Disable text field after game ends
            }
        } else {
            view.getGameBoard().getTxtChar().setText("");
        }
    }


    private boolean isGameOver() {
        if (model.getGuessedChars() == null) {
            return false;
        }

        if (model.getWrongGuesses() == null) {
            return false;
        }

        return model.getWrongGuesses().size() >= model.getImageFiles().size() - 1 ||
                new String(model.getGuessedChars()).indexOf('_') == -1;
    }
}
