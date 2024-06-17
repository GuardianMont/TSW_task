package ec;

/**
 * Classe per la validazione dei dati.
 */
public class Validation {
    public static boolean checkEmail(String email){
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    }

    public static boolean checkPhoneNumber(String phoneNumber){
        return phoneNumber.matches("^\\d{10}$");
    }

    public static boolean checkPassword(String password){
        return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$");
    }
    public static boolean checkPasswordMatching(String password, String confirmPassword){
        return password.equals(confirmPassword);
    }

    public static boolean checkNameSurname(String name){
        return name.matches("^[a-zA-Z ]+$");
    }
}
