package casmi;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuBar;


public class MenuBar {

    private final JMenuBar jMenuBar;
    private List<Menu> menuList = new ArrayList<Menu>();
    
    MenuBar () {
        jMenuBar = new JMenuBar();
    }
    
    public void addMenu(Menu menu) {
        menuList.add(menu);
        jMenuBar.add(menu.getJMenu());
    }
    
    public Menu getMenu(int index) {
        return menuList.get(index);
    }
    
    public int getMenuCount() {
        return jMenuBar.getMenuCount();
    }
    
    public boolean isSelected() {
        return jMenuBar.isSelected();
    }
    
    final JMenuBar getJMenuBar() {
        return jMenuBar;
    }
}
