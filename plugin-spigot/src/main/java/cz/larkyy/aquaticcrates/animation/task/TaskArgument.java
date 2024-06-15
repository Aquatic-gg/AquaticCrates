package cz.larkyy.aquaticcrates.animation.task;

public class TaskArgument {

    private final boolean required;
    private final String id;

    private final Object defaultValue;

    public TaskArgument(String id, Object defaultValue, boolean required) {
        this.required = required;
        this.defaultValue = defaultValue;
        this.id = id;
    }

    public boolean isRequired() {
        return required;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public String getId() {
        return id;
    }
}
