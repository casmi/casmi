package casmi.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;


public class TextField extends Component {

    private JTextField myInstance;

    private boolean isPerformable = true;

    private List<ActionListener> listeners = new ArrayList<ActionListener>();

    public TextField() {
        myInstance = new JTextField();
        instance = myInstance;
    }

    public void addActionListener(final TextFieldActionListener listener) {
        ActionListener l = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField t = (JTextField)e.getSource();

                if (isPerformable) {
                    listener.performed(t.getText());
                }
            }
        };

        myInstance.addActionListener(l);
        listeners.add(l);
    }

    public void clearActionListeners() {
        for (ActionListener l : listeners) {
            myInstance.removeActionListener(l);
        }
        listeners.clear();
    }

    public void setText(String t) {
        isPerformable = false;

        myInstance.setText(t);

        isPerformable = true;
    }
}
