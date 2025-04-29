package murray.csc325sprint1.Model;

import java.util.Objects;

public class User {

    private String fName,lName,email,secQuestion,secAnswer,password;

    public User(String fName, String lName, String email, String secQuestion, String secAnswer, String password) {
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.secQuestion = secQuestion;
        this.secAnswer = secAnswer;
        this.password = password;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSecQuestion() {
        return secQuestion;
    }

    public void setSecQuestion(String secQuestion) {
        this.secQuestion = secQuestion;
    }

    public String getSecAnswer() {
        return secAnswer;
    }

    public void setSecAnswer(String secAnswer) {
        this.secAnswer = secAnswer;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(fName, user.fName) && Objects.equals(lName, user.lName) && Objects.equals(email, user.email) && Objects.equals(secQuestion, user.secQuestion) && Objects.equals(secAnswer, user.secAnswer) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fName, lName, email, secQuestion, secAnswer, password);
    }

    @Override
    public String toString() {
        return "User{" +
                "fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                ", email='" + email + '\'' +
                ", secQuestion='" + secQuestion + '\'' +
                ", secAnswer='" + secAnswer + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
