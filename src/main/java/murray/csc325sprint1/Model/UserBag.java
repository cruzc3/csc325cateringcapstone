package murray.csc325sprint1.Model;

import java.util.HashMap;

public class UserBag {

    private HashMap<String,User> userBag = new HashMap<>();

    public void insertUser(User u){
        if(!userBag.containsKey(u.getEmail())){
            userBag.put(u.getEmail(),u);
            System.out.println("User inserted");
        } else {
            System.out.println("User already exists");
        }
    }

    public void deleteUser(User u){
        if (userBag.remove(u.getEmail()) != null) {
            System.out.println("User deleted.");
        } else {
            System.out.println("User not found.");
        }
    }

    public void updateUser(User u){
        if(userBag.containsKey(u.getEmail())){
            userBag.put(u.getEmail(), u);
            System.out.println("User updated");
        } else {
            System.out.println("User not found!");
        }
    }

    public User findUser(User u){
        return userBag.get(u.getEmail());
    }

}
