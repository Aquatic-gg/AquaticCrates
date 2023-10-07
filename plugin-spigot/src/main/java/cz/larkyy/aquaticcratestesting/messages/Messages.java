package cz.larkyy.aquaticcratestesting.messages;

import cz.larkyy.aquaticcratestesting.AquaticCratesTesting;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;

public enum Messages {

    HELP("help", Arrays.asList(
            "&f ",
            " &3&lAquaticCrates",
            "&f ",
            "&3Crate Commands",
            " &3| &7/acrates crate give <crate>",
            "&3Key Commands",
            " &3| &7/acrates key give <crate> [amount] [player] [virtual]",
            " &3| &7/acrates key take <crate> <amount> <player>",
            " &3| &7/acrates key bank",
            "&3Item Commands",
            " &3| &7/acrates item save <identifier>",
            " &3| &7/acrates item give <identifier> [amount] [player]",
            "&3Util Commands",
            " &3| &7/acrates reload",
            "&f "
    )),
    NO_REWARD_AVAILABLE("no-reward-available","&cThere is no reward available in this crate!"),
    KEY_BANK_TITLE("key-bank-title","&3Your Virtual Keys:"),
    KEY_BANK_FORMAT("key-bank-format","&7- &f%crate%&7: &b%amount%"),
    ONLY_FOR_PLAYERS("only-for-players","&cThis command is only for players!"),
    PLUGIN_RELOADED("plugin-reloaded","&3[AquaticCrates] &fPlugin has been &breloaded&f!"),
    NO_PERMISSION("no-permission","&cYou don't have permissions to do that!"),
    INVALID_CRATE("invalid-crate","&cThis crate identifier is invalid!"),
    INVALID_PLAYER("invalid-player","&cThis player is invalid!"),
    INVALID_NUMBER("invalid-number","&cThis number is invalid!"),
    CRATE_NO_PERMISSION("crate-no-permission","&cYou do not have permissions to open this crate!"),
    OPEN_ALREADY_OPENING("already-being-opened","&cSomeone is already opening the crate!"),
    OPEN_LIMIT_REACHED("opening-limit","&cMany people is opening the crate at the moment!"),

    // Item Command messages
    MUST_HAVE_ITEM_IN_HAND("no-item-in-hand","&cYou must have an item in your hand!"),
    ITEM_UNKNOWN_IDENTIFIER("unknown-item-identifier","&cThere is no item with this identifier!"),
    ITEM_SAVED("item-saved","&fItem has been saved!"),

    // Key Command messages
    KEY_UNKNOWN_IDENTIFIER("unknown-key-identifier","&cThere is no key with this identifier!"),
    KEY_GIVE_SENDER("key-given-sender","&f%amount%x %crate% Key has been given to %player%!"),
    KEY_GIVE_SENDER_VIRTUAL("key-given-sender-virtual","&f%amount%x Virtual %crate% Key has been given to %player%!"),
    KEY_GIVE_RECEIVER("key-given-receiver","&fYou have been given &f%amount%x %crate% Key!"),
    KEY_GIVE_RECEIVER_VIRTUAL("key-given-receiver-virtual","&fYou have been given &f%amount%x&f Virtual %crate% Key!"),
    KEY_TAKE_SENDER("key-taken-sender","&fYou have taken %amount%x Virtual %crate% Key from %player%!"),
    KEY_TAKE_RECEIVER("key-taken-receiver","&fYou have been taken %amount%x Virtual %crate% Key!"),
    DO_NOT_HAVE_KEY("dont-have-key","&cYou do not have the key to open the crate!");
    private final String path;
    private final Object defVal;

    Messages(String path, Object defVal) {
        this.path = path;
        this.defVal = defVal;
    }

    public Message replace(String s1, String s2) {
        return get().replace(s1,s2);
    }

    public Message get() {
        if (getMsgFile().contains(path)) {
            return new Message(getMsgFile().get(path));
        } else {
            getMsgFile().set(path,defVal);
            AquaticCratesTesting.getMessageHandler().save();
            return new Message(defVal);
        }
    }

    private FileConfiguration getMsgFile() {
        return AquaticCratesTesting.getMessageHandler().getCfg();
    }

    public void send(CommandSender sender) {
        if (getMsgFile().contains(path)) {
            new Message(getMsgFile().get(path)).send(sender);
        } else {
            new Message(defVal).send(sender);
        }
    }
}
