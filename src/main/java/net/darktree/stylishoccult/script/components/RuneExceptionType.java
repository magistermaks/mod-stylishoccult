package net.darktree.stylishoccult.script.components;

public enum RuneExceptionType {
    INVALID_METADATA("invalid_metadata"),
    INVALID_STATE("invalid_state"),
    NUMBER_TOO_LONG("number_too_long"),
    INVALID_NUMBER("invalid_number"),
    INVALID_ARGUMENT("invalid_argument"),
    INVALID_ARGUMENT_COUNT("invalid_argument_count"),
    STACK_TOO_BIG("stack_too_big");

    private final String name;

    RuneExceptionType(String name ) {
        this.name = name;
    }

    public RuneException get() {
        return RuneException.of(this);
    }

    public String getName() {
        return name;
    }
}
