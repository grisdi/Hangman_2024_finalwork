package controllers;

import listeners.*;
import models.Database;
import models.Model;
import views.View;

/**
 * See klass tegeleb sündmuste haldamisega ja ühendab mudeli, vaate ja kuulajate funktsionaalsuse
 */
public class Controller {
    /**
     * Konstruktor, mis loob andmebaasiühenduse, lisab erinevatele nuppudele ja sisestuskastile Listenerid
     * @param model mudel, mis sisaldab andmeid ja rakenduse loogikat
     * @param view vaade kuvab rakenduse kasutajaliidese
     */
    public Controller(Model model, View view) {
        new Database(model);

        // Comboboxi funtsionaalsus
        view.getSettings().getCmbCategory().addItemListener(new ComboboxChange(model));
        // Uus mäng funktsionaalsus
        view.getSettings().getBtnNewGame().addActionListener(new ButtonNew(model, view));
        // Katkesta nupu funktsionaalsus
        view.getGameBoard().getBtnCancel().addActionListener(new ButtonCancel(view));
        // Saada nupu funtsionaalsus
        view.getGameBoard().getBtnSend().addActionListener(new ButtonSend(model, view));
        // Edetabeli nupu funktsionaalsus
        view.getSettings().getBtnLeaderboard().addActionListener(new ButtonScores(model, view));
    }
}
