package listeners;

import models.Model;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * See klass tegeleb JComboBox-i valiku muutmisega
 * Kui valik muutub, siis uuendatakse mudelis valitud kategooriat
 */
public class ComboboxChange implements ItemListener {
    /**
     * Mudel, millele rakendatakse valitud kategooria muutused
     */
    private final Model model;

    /**
     * Konstruktor, mis võtab vastu mudeli ja seadistab selle klassisisesele muutujale
     * @param model Mudel, millele rakendatakse valitud kategooria muudatused
     */
    public ComboboxChange(Model model) {
        this.model = model;
    }

    /**
     * Meetod, mida kutsutakse, kui JComboBox-i valik muutub
     * Kui valik muutub, siis uuendatakse mudelis valitud kategooriat
     * @param e the event to be processed
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
        // https://stackoverflow.com/questions/330590/why-is-itemstatechanged-on-jcombobox-is-called-twice-when-changed
        if(e.getStateChange() == ItemEvent.SELECTED) { // Without this check, two choices will occur in a row
            model.setSelectedCategory(e.getItem().toString()); // Set selected category for next new game
            JOptionPane.showMessageDialog(null, e.getItem().toString()); // for testing
        }
    }
}
