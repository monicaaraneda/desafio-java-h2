package ms.utils;

public class ValidationUtils {

    /**
     * Valida si el email proporcionado tiene un formato correcto.
     * 
     * @param email el email a validar
     * @return true si el email es válido, false de lo contrario
     */
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email != null && email.matches(emailRegex);
    }
}
