package casmi;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;


public class MenuItem {

    private final MenuItem instance;
    
    private final JMenuItem jMenuItem;
    
    private MenuItemSelectCallback callback;
    
    public MenuItem() {
        instance = this;
        jMenuItem = new JMenuItem();
    }
    
    public MenuItem(String title) {
        instance = this;
        jMenuItem = new JMenuItem(title);
        
        jMenuItem.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent ev) {
                if (callback != null) {
                    callback.run(instance);
                }
            }
        });
    }
    
    public String getTitle() {
        return jMenuItem.getText();
    }
    
    public void addSelectCallback(MenuItemSelectCallback callback) {
        this.callback = callback;        
    }
    
    final JMenuItem getJMenuItem() {
        return jMenuItem;
    }
}
