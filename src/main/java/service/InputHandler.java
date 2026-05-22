package service;

import lombok.extern.log4j.Log4j2;

import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
public class InputHandler {
    public static boolean isChoiceValid(String input) {
        int choice;
        try {
            choice = Integer.parseInt(input);
        } catch (Exception e) {
            return false;
        }
        return choice >= 1 && choice <= 6;
    }

    public static boolean isNameValid(String providedName) {
        String regex = "[^a-zA-ZÀ-ÿ\\s\\-]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(providedName.trim());
        return !matcher.find();
    }

    public static boolean isTypeValid(String providedType) {
        return providedType.equalsIgnoreCase("cachorro")
                || providedType.equalsIgnoreCase("gato");
    }

    public static boolean isSexValid(String providedSex) {
        String sex = normalizeText(providedSex);
        return sex.trim().equalsIgnoreCase("macho")
                || sex.trim().equalsIgnoreCase("femea");
    }

    public static boolean isInteger(String providedAge) {
        try {
            Integer.parseInt(providedAge);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isAgeValid(String providedAge) {
        if (!isInteger(providedAge)) return false;
        int age = Integer.parseInt(providedAge);
        return age > 0 && age <= 20;
    }

    public static boolean isWeightValid(String providedWeight) {
        double weight;
        try {
            weight = Double.parseDouble(providedWeight);
        } catch (Exception e) {
            return false;
        }
        return weight > 0 && weight <= 20;
    }

    public static boolean isAnswerValid(String providedAnswer) {
        String answer = normalizeText(providedAnswer);
        return answer.trim().equalsIgnoreCase("sim")
                || answer.trim().equalsIgnoreCase("nao");
    }

    public static String normalizeText(String input) {
        String normalizedInput = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalizedInput).replaceAll("");
    }
}
