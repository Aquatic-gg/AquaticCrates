package cz.larkyy.aquaticcrates.messages;

import gg.aquatic.aquaticseries.lib.StringExtKt;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class Message {
    private final List<String> values;

    public Message(Object value) {
        if (value instanceof List) {
            this.values = (List<String>) value;
        } else {
            this.values = new ArrayList<>();
        }
        if (value instanceof String) {
            this.values.add((String) value);
        }
    }

    public Message replace(String toReplace, String replacement) {
        this.values.replaceAll(x -> x.replace(toReplace,replacement));
        return this;
    }

    public void send(CommandSender sender) {
        if (values.size() == 1 && values.getFirst().isEmpty()) {
            return;
        }
        values.forEach(v -> {
            StringExtKt.toAquatic(v).send(sender);
        });
    }

    public void broadcast() {
        if (values.size() == 1 && values.getFirst().isEmpty()) {
            return;
        }
        values.forEach(v -> {
            StringExtKt.toAquatic(v).broadcast();
        });
    }
}
