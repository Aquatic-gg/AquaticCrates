package cz.larkyy.aquaticcratestesting.utils.colors;

import org.bukkit.ChatColor;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GradientPattern implements IPattern {
    Pattern pattern = Pattern.compile("(\\{|<|\\[)#([0-9A-Fa-f]{6})>(}|>|\\})(.*?)(\\{|<|\\[)#([0-9A-Fa-f]{6})<(}|>|\\})");

    public String process(String string) {
        Matcher matcher = this.pattern.matcher(string);
        while (matcher.find()) {
            String start = matcher.group(2);
            String end   = matcher.group(6);

            String content = matcher.group(4);
            String lastColor = ChatColor.getLastColors(content);
            string = string.replace(matcher.group(),
                    Colors.color(ChatColor.stripColor(content), new Color(Integer.parseInt(start, 16)), new Color(Integer.parseInt(end, 16)), lastColor));
        }
        return string;
    }
}
