package listeners;

import views.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * See klass tegeleb katkestamise nupu fuktsionaalsusega
 * Kui nuppu klikatakse, katkestatakse mäng ja taaskäivitatakse algseaded
 */
public class ButtonCancel implements ActionListener {
    /**
     * Vaade, mis kuvab rakenduse kasutajaliidese
     */
    private final View view;

    /**
     * Konstruktor, mis võtab vastu vaate ja seadistab selle klassisisesele muutujale
     * @param view vaade, mis kuvab rakenduse kasutajaliidese
     */
    public ButtonCancel(View view) {
        this.view = view;
    }

    /**
     * Meetod, mida kutsutakse, kui katkestamise nuppu klikatakse
     * Tagastab vaate algseaded ja peatab mänguaja
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // System.out.println("Klikk");  // Kontroll, uue mängule klikates toob teate konsooli "klikk"
        view.showButtons();
        view.getGameTimer().stopTime();
        view.getGameTimer().setRunning(false);
    }
}
