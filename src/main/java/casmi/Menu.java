package casmi;

import javax.swing.JMenu;


public class Menu {

    private final JMenu jMenu;
    
    public Menu() {
        jMenu = new JMenu();
    }
    
    public Menu(String title) {
        jMenu = new JMenu(title);
    }
    
    public MenuItem addMenuItem(MenuItem item) {
        jMenu.add(item.getJMenuItem());
        return item;
    }
    
    public MenuItem addMenuItem(String title) {
        MenuItem item = new MenuItem(title);
        return addMenuItem(item);
    }
    
    public void addSeparator() {
        jMenu.addSeparator();
    }
    
    public void inserSeparator(int index) {
        jMenu.insertSeparator(index);
    }
    
    final JMenu getJMenu() {
        return jMenu;
    }
}
