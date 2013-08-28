package casmi.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;


public class ComboBox extends Component {

    private JComboBox<String> myInstance;

    private boolean isPerformable = true;

    private List<ActionListener> listeners = new ArrayList<ActionListener>();

    public ComboBox(String [] itemNames) {
        super();

        myInstance = new JComboBox<String>(itemNames);
        instance = myInstance;
    }

    public ComboBox(List<String> itemNames) {
        this(itemNames.toArray(new String[itemNames.size()]));
    }

    public void addActionListener(final ComboBoxActionListener listener) {
        ActionListener l = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                @SuppressWarnings("unchecked")
                JComboBox<String> cb = (JComboBox<String>)e.getSource();

                if (isPerformable) {
                    listener.performed(cb.getSelectedIndex());
                }
            }
        };

        myInstance.addActionListener(l);
    }

    public void clearActionListeners() {
        for (ActionListener l : listeners) {
            myInstance.removeActionListener(l);
        }
        listeners.clear();
    }

    public void setItems(String [] itemNames) {
        isPerformable = false;

        myInstance.removeAllItems();
        for(String i : itemNames) {
            myInstance.addItem(i);
        }

        isPerformable = true;
    }

    public void setItems(List<String> itemNames) {
        isPerformable = false;

        myInstance.removeAllItems();
        for(String i : itemNames) {
            myInstance.addItem(i);
        }

        isPerformable = true;
    }
}
