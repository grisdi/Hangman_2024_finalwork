package listeners;

import views.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonCancel implements ActionListener {
    private final View view;
    public ButtonCancel(View view) {
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // System.out.println("Klikk");  // Kontroll, uue mängule klikates toob teate konsooli "klikk"
        view.showButtons();
        view.getGameTimer().stopTime();
        view.getGameTimer().setRunning(false);
    }
}
