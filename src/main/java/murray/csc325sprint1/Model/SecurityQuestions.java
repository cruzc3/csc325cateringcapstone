package murray.csc325sprint1.Model;

public enum SecurityQuestions {

    MOTHER_MAIDEN_NAME("What is your mothers maiden name?"),
    FIRST_PET_NAME("What is your first pets name?"),
    CITY_OF_BIRTH("What city were you born in?"),
    FAVORITE_BOOK("What is your favorite book?");

    private final String question;

    SecurityQuestion(String question){
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public static String[] getAllQuestions() {
        SecurityQuestion[] values = SecurityQuestion.values();
        String[] questions = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            questions[i] = values[i].getQuestion();
        }
        return questions;
    }

}
