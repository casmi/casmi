package casmi.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;


public class TextField extends Component {

    private JTextField myInstance;

    public TextField() {
        myInstance = new JTextField();
        instance = myInstance;
    }

    public void addActionListener(final TextFieldActionListener listener) {
        myInstance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                @SuppressWarnings("unchecked")
                JTextField t = (JTextField)e.getSource();

                listener.performed(t.getText());
            }
        });

    }
}
