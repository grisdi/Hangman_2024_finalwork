package listeners;

import models.Model;
import views.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonNew implements ActionListener {
    private Model model;
    private View view;
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
    }
}
