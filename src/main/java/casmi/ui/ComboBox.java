package casmi.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JComboBox;


public class ComboBox extends Component {

    private JComboBox<String> myInsance;

    public ComboBox(String [] itemNames) {
        super();

        myInsance = new JComboBox<String>(itemNames);
        instance = myInsance;
    }

    public ComboBox(List<String> itemNames) {
        this(itemNames.toArray(new String[itemNames.size()]));
    }

    public void addActionListener(final ComboBoxActionListener listener) {
        myInsance.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                @SuppressWarnings("unchecked")
                JComboBox<String> cb = (JComboBox<String>)e.getSource();

                listener.performed(cb.getSelectedIndex());
            }
        });
    }
}
