package listeners;

import models.Database;
import models.Model;
import models.datastructures.DataWords;
import views.View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

/**
 * Nupu Uus tegevus, mis alustab mängu
 */
public class ButtonNew implements ActionListener {
    static {
        Logger.getLogger((ButtonNew.class.getName()));
    }

    private final Model model;
    private final View view;

    /**
     * Nupu Uus konstruktor
     * @param model Rakenduse mudel
     * @param view Rakenduse vaade
     */
    public ButtonNew(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // System.out.println("Klikk");  // Kontroll, uue mängule klikates toob teate konsooli "klikk"
        view.hideButtons();
        if(!view.getGameTimer().isRunning()) {  // ! tähendab eitust. Mängu aeg ei jookse
            view.getGameTimer().setSeconds(0);  // Aeg nullida
            view.getGameTimer().setMinutes(0);  // Minutid nullida
            view.getGameTimer().setRunning(true);  // Aeg jooksma
            view.getGameTimer().startTime();  // Käivita aeg
        } else {
            view.getGameTimer().stopTime();
            view.getGameTimer().setRunning(false);
        }

        // TODO siit jätkub kodutöö  (umbes kaks rida koodi, üks meetod, juhusliksõna, mis andmebaasist võetakse)
        DataWords word = Database.getInstance(model).selectRandomWord(model.getSelectedCategory());
        if (word != null) {
            // Initialize the game state with the new word
            model.startNewGame(word.word());
            view.setFirstPicture();
            view.getGameBoard().displayWord(word.word());
        } else {
            // Handle the case where no word was found
            JOptionPane.showMessageDialog(view, "Valitud kategooriast sõna, mida kuvada, ei ole.");
        }
    }
}
