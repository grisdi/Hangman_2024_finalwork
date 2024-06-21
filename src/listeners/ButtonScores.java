package listeners;

import models.Database;
import models.Model;
import views.View;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonScores implements ActionListener {
    private final Model model;
    private final View view;
    private final String[] header = new String[] {"Kuupäev", "Nimi", "Sõna", "Tähed", "Mänguaeg"};
    private final DefaultTableModel dtm = new DefaultTableModel(header,0);
    private final JTable table = new JTable(dtm);
    private JDialog dialogScore;

    public ButtonScores(Model model, View view) {
        this.model = model;
        this.view = view;
        model.setDtm(dtm);
        createDialog();
    }

    private void createDialog() {
        dialogScore = new JDialog();
        dialogScore.setPreferredSize(new Dimension(530, 180));

        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.setDefaultEditor(Object.class, null);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(4).setCellRenderer(center);

        dialogScore.add(new JScrollPane(table));
        dialogScore.setTitle("Edetabel");
        dialogScore.pack();
        dialogScore.setLocationRelativeTo(null);
        dialogScore.setModal(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new Database(model).selectScores();
        if (!model.getDataScores().isEmpty()) {
            view.updateScoresTable();
            dialogScore.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(view, "Edetabel on veel tühi! Alusta mänguga!");
        }
    }
}
