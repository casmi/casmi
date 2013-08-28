package casmi.ui;

import javax.swing.JLabel;


public class Label extends Component {

    private JLabel myInstance;

    public Label(String t) {
        super();

        myInstance = new JLabel(t, JLabel.CENTER);

        instance = myInstance;
    }

    public void setText(String t) {
        myInstance.setText(t);
    }
}
