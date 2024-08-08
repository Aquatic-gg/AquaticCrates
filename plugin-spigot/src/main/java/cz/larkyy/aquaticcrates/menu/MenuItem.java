package cz.larkyy.aquaticcrates.menu;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class MenuItem {

    private final String id;
    private final ItemStack itemStack;
    private List<Consumer<InventoryClickEvent>> actions;
    private final List<Integer> slots;

    public MenuItem(String id, ItemStack is, List<Consumer<InventoryClickEvent>> actions, List<Integer> slots) {
        this.id = id;
        this.itemStack = is;
        this.actions = actions;
        this.slots = slots;
    }
    public MenuItem(String id, ItemStack is, Consumer<InventoryClickEvent> action, List<Integer> slots) {
        this.id = id;
        this.itemStack = is;
        this.actions = new ArrayList<>(Arrays.asList(action));
        this.slots = slots;
    }

    public String getId() {
        return id;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public List<Integer> getSlots() {
        return slots;
    }

    public void addAction(Consumer<InventoryClickEvent> action) {
        actions.add(action);
    }

    public void activate(InventoryClickEvent e) {
        for (Consumer<InventoryClickEvent> a : actions) {
            a.accept(e);
        }
    }

    public MenuItem clone() {
        return new MenuItem(id,itemStack.clone(),new ArrayList<>(actions),new ArrayList<>(slots));
    }

    public static Builder builder(String id, ItemStack is) {
        return new Builder(id,is);
    }

    public static class Builder {
        private final String id;
        private final ItemStack is;
        private List<Integer> slots;
        private Consumer<InventoryClickEvent> action;

        public Builder(String id, ItemStack is) {
            this.id = id;
            this.is = is;
            slots = new ArrayList<>();
            action = a -> {};
        }

        public Builder slots(List<Integer> slots) {
            this.slots = slots;
            return this;
        }

        public Builder action(Consumer<InventoryClickEvent> action) {
            this.action = action;
            return this;
        }

        public MenuItem build() {
            return new MenuItem(id,is,action,slots);
        }
    }
}
