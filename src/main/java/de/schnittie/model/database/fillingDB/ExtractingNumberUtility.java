package de.schnittie.model.database.fillingDB;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractingNumberUtility {
    public static ArrayList<Integer> extractNumbers(String input) {
        ArrayList<Integer> numbers = new ArrayList<>();

        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String numberString = matcher.group();
            int number = Integer.parseInt(numberString);
            numbers.add(number);
        }
        
        return numbers;
    }
}
