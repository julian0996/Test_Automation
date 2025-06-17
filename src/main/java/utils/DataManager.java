package utils;

import com.github.javafaker.Faker;

import java.util.Locale;
import java.util.Random;

public class DataManager {

    private static Faker faker = new Faker(new Locale("es-MX"));

    /*
     *
     */
    public static <E extends Enum<E>> E convertEnum(Class<E> enumClass, String value) {
        if(value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("value cannon be null or empty");
        }
        try {
            String normalizedValue = value.trim()
                    .replaceAll("([a-z])([A-Z])", "$1_$2")
                    .toUpperCase();
            return Enum.valueOf(enumClass, normalizedValue);
        } catch(IllegalArgumentException ex){
            throw new RuntimeException("Invalid enum value for " + enumClass.getSimpleName() + ": " +"value" , ex);
        }
    }

    /*
     *
     */
    public static String getRandomNameCustomer() {
        return faker.name().firstName().trim();
    }

    public static String getGenderRandom() {
        String[] generos = {"Male", "Female"};
        Random random = new Random();
        return generos[random.nextInt(generos.length)];
    }

}
