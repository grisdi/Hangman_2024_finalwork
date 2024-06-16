package controllers;

import listeners.*;
import models.Database;
import models.Model;
import views.View;

public class Controller {
    public Controller(Model model, View view) {
        Database database = new Database(model);

        // Comboboxi funtsionaalsus
        view.getSettings().getCmbCategory().addItemListener(new ComboboxChange(model));
        // Uus mäng funktsionaalsus
        view.getSettings().getBtnNewGame().addActionListener(new ButtonNew(model, view));
        // Katkesta nupu funktsionaalsus
        view.getGameBoard().getBtnCancel().addActionListener(new ButtonCancel(model, view));
        // Saada nupu funtsionaalsus
        view.getGameBoard().getBtnSend().addActionListener(new ButtonSend(model, view, database));
        // Edetabeli nupu funktsionaalsus
        view.getSettings().getBtnLeaderboard().addActionListener(new ButtonScores(model, view));
    }
}
