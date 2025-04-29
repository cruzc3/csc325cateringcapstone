package murray.csc325sprint1.Model;

import murray.csc325sprint1.MenuItem;

import java.util.HashMap;
import java.util.Map;

public class MenuBag {

    private static Map<String, MenuItem> menuMap = new HashMap<>();

    private static MenuBag instance = null;

    private MenuBag() {
        // private constructor to prevent external instantiation
    }

    public static MenuBag getInstance() {
        if (instance == null) {
            instance = new MenuBag();
        }
        return instance;
    }

    public void insertMenuItem(MenuItem m){
        menuMap.put(m.getName(), m);
    }

    public void deleteMenuItem(String name){
        menuMap.remove(name);
    }

    public void updateMenuItem(MenuItem m){
        menuMap.put(m.getName(), m);
    }

    public MenuItem findItem(String name){
        return menuMap.get(name);
    }

}
