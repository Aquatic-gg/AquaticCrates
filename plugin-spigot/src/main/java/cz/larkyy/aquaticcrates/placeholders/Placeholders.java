package cz.larkyy.aquaticcrates.placeholders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Placeholders {

    private final List<Placeholder> placeholders;

    public Placeholders() {
        placeholders = new ArrayList<>();
    }
    public Placeholders(List<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public Placeholders(Placeholder... placeholders) {
        this.placeholders = new ArrayList<>(Arrays.stream(placeholders).toList());
    }

    public void addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
    }

    public void addPlaceholder(String placeholder, String value) {
        this.placeholders.add(new Placeholder(placeholder,value));
    }

    public String replace(String input) {
        for (var placeholder : placeholders) {
            input = placeholder.replace(input);
        }
        return input;
    }

    public List<String> replace(List<String> input) {
        for (var placeholder : placeholders) {
            placeholder.replace(input);
        }
        return input;
    }


}
