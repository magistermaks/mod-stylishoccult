package net.darktree.stylishoccult.script.component;

public enum RuneExceptionType {

	/**
	 * The number is incorrectly formatted
	 */
	INVALID_NUMBER("invalid_number"),

	/**
	 * The number is too long
	 */
	NUMBER_TOO_LONG("number_too_long"),

	/**
	 * Expected a different argument
	 */
	INVALID_ARGUMENT("invalid_argument"),

	/**
	 * Expected a different argument type
	 */
	INVALID_ARGUMENT_TYPE("invalid_argument_type"),

	/**
	 * Expected a different argument count
	 */
	INVALID_ARGUMENT_COUNT("invalid_argument_count"),

	/**
	 * Nothing was left to return (e.g. from stack)
	 */
	NOTHING_TO_RETURN("nothing_to_return"),

	/**
	 * Internal problem, invalid block state or NBT tag
	 */
	INVALID_STATE("invalid_state"),

	/**
	 * Maximum stack size exceeded
	 */
	STACK_TOO_LONG("stack_too_long"),

	/**
	 * Operation is in violation of the rule of Equivalent Exchange
	 */
	UNMET_EQUIVALENCY("unmet_equivalency"),

	/**
	 * A broken rune was activated
	 */
	BROKEN("broken");

	private final String name;

	RuneExceptionType( String name ) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
