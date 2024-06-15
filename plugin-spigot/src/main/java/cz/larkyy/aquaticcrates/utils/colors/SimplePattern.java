package cz.larkyy.aquaticcrates.utils.colors;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimplePattern implements IPattern {
    Pattern pattern = Pattern.compile("&#([0-9A-Fa-f]{6})");

    public String process(String string) {
        Matcher matcher = this.pattern.matcher(string);
        while (matcher.find()) {
            String color = matcher.group(1);
            string = string.replace(matcher.group(), Colors.getColor(color) + "");
        }
        return string;
    }
}
