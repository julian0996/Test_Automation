package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ErrorController {
    private static final Logger LOGGER = LogManager.getLogger(ErrorController.class);

    /**
     * Valida que una cadena no sea null ni vacía.
     *
     * @param value Valor a validar.
     * @param fieldName Nombre del campo (para el log).
     * @return true si es válido, false si hay error.
     */
    public static boolean validateRequiredField(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            LOGGER.error("El campo es obligatorio {}", fieldName);
            return false;
        }
        return true;
    }

    /**
     * Valida que un objeto no sea null.
     *
     * @param object Objeto a validar.
     * @param fieldName Nombre del campo (para el log).
     * @return true si es válido, false si hay error.
     */
    public static boolean validateNotNull(Object object, String fieldName) {
        if (object == null) {
            LOGGER.error("El campo no puede ser null {}", fieldName);
            return false;
        }
        return true;
    }
}
