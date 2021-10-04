package paket.projekat;

import java.util.Calendar;

public class DataValidation {
    private static int currentYear = Calendar.getInstance().get(Calendar.YEAR);

    public static String validationUserData(String firstName, String secondName, String userName, String password, String day, String month, String year, String numbOfTelephone, String gender, String email) {
        String result = "";

        if(!checkFirstName(firstName)) {
            result += "Ime mora da sadrži samo slova.\n";
            return result;
        }
        if(!checkSecondName(secondName)) {
            result += "Prezime mora da sadrži samo slova.\n";
            return result;
        }
        if(!checkUserName(userName)) {
            result += "Korisničko ime mora da bude minimalne dužine 5 i maksimalne dužine 15 karaktera.\n";
            result += "Korisničko ime sme da sadrži samo donju crtu(_) od specijalnih karaktera.\n";
            result += "Korisničko ime sme samo da sadrži kao prvi karakter slovo [a-z] ili [A-Z].\n";
            return result;
        }
        if(!checkPassword(password)) {
            result += "Lozinka mora da bude minimalne dužine 6 i maksimalne dužine 20 karaktera.\n";
            return result;
        }
        if(!checkDay(day)) {
            result += "Dan mora biti u opsegu od 1 do 31.\n";
            return result;
        }
        if(!checkMonth(month)) {
            result += "Mesec mora biti u opsegu od 1 do 12.\n";
            return result;
        }
        if(!checkYear(year)) {
            result += "Godina mora biti u opsegu od 1900 do "+ currentYear +".\n";
            return result;
        }
        if(!checkNumberOfTelephone(numbOfTelephone)) {
            result += "Telefon mora da sadrži samo cifre i da ima minimalnu dužinu od 6 cifara.\n";
            return result;
        }
        if(!checkGender(gender)) {
            result += "Pol mora da sadrži samo m ili z/ž.\n";
            return result;
        }
        if(!checkEmail(email)) {
            result += "Email nije validan.\n";
            return result;
        }

        return result;
    }

    public static Boolean checkFirstName(String firstName) {
        for(int i = 0; i < firstName.length(); i++) {
            if(!Character.isLetter(firstName.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static Boolean checkSecondName(String secondName) {
        for(int i = 0; i < secondName.length(); i++) {
            if(!Character.isLetter(secondName.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static Boolean checkUserName(String userName) {
        // Username treba da bude minimalne duzine 5, a maksimalne duzine 15 karaktera.
        // Username sme da sadrzi samo donju crtu(_) od specijalnih karaktera.
        // Username sme samo da sadrzi kao prvi karakter slovo [a-z] ili [A-Z].
        final String regularExpression = "^[a-zA-Z][a-zA-Z0-9_]{4,14}$";
        if(userName.matches(regularExpression)) {
            return true;
        }
        else {
            return false;
        }
    }

    public static Boolean checkPassword(String password) {
        if(password.length() >= 6 && password.length() <= 20) {
            return true;
        }
        else {
            return false;
        }
    }

    public static Boolean checkDay(String day) {
        try {
            Integer d = Integer.parseInt(day);
            if(d >= 1 && d <= 31) {
                return true;
            }
            else {
                return false;
            }
        }
        catch (Exception e) {
            return false;
        }
    }

    public static Boolean checkMonth(String month) {
        try {
            Integer m = Integer.parseInt(month);
            if(m >= 1 && m <= 12) {
                return true;
            }
            else {
                return false;
            }
        }
        catch (Exception e) {
            return false;
        }
    }

    public static Boolean checkYear(String year) {
        try {
            Integer y = Integer.parseInt(year);
            if(y >= 1900 && y <= currentYear) {
                return true;
            }
            else {
                return false;
            }
        }
        catch (Exception e) {
            return false;
        }
    }

    public static Boolean checkNumberOfTelephone(String numbOfTelephone) {
        try {
            Integer nt = Integer.parseInt(numbOfTelephone);
            if(numbOfTelephone.length() >= 6) {
                return true;
            }
            else {
                return false;
            }
        }
        catch (Exception e) {
            return false;
        }
    }

    public static Boolean checkGender(String gender) {
        if(gender.equals("m") || gender.equals("z") || gender.equals("ž")) {
            return true;
        }
        else {
            return false;
        }
    }

    public static Boolean checkEmail(String email) {
        final String regularExpression =
                "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        if(email.matches(regularExpression)) {
            return true;
        }
        else {
            return false;
        }
    }
}
