package cz.larkyy.aquaticcratestesting.hologram;

import java.util.ArrayList;
import java.util.List;

public class Placeholder {

    private final String placeholder;
    private final String value;

    public Placeholder(String placeholder, String value) {
        this.placeholder = placeholder;
        this.value = value;
    }

    public String replace(String input) {
        return input.replace(placeholder,value);
    }

    public List<String> replace(List<String> input) {
         List<String> output = new ArrayList<>(input);
         output.replaceAll(this::replace);
         return output;
    }
}
