package oop.ex6.main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class that represent a variable in s-java.
 */
class Variable {

    // Useful value's.
    private final static String INT = "int", DOUBLE = "double", BOOLEAN = "boolean";
    private final static String CHAR = "char", STRING = "String";
    private final static String TRUE = "true", FALSE = "false";
    private final static char APOSTROPHE = '\'', CHAR_APOSTROPHES = '"';
    private final static String STRING_APOSTROPHES = "\"", DEFAULT_STRING = "DefaultString";
    private final static String DEFAULT_INT = "DefaultInt",DEFAULT_DOUBLE = "DefaultDouble";
    private final static String DEFAULT_BOOLEAN = "DefaultBoolean",DEFAULT_CHAR = "DefaultChar";

    // Errors string's.
    private final static String TYPE_ERROR_MESSAGE = "Illegal type of value";
    private final static String NAME_ERROR_MESSAGE = "Illegal name variable";
    private final static String VALUE_ERROR_MESSAGE = "Illegal value variable";
    private final static String FINAL_ASSIGNMENT = "Final variable can't change";
    private final static String MISSING_VARIABLE = "Try to copy a value from variable that doesn't exist.";
    private final static String UNEXCITED_VALUE = "Try to assignment to unexcited value";

    // Pattern's string's.
    private static final String TYPES_PATTERN = "int|double|String|boolean|char";
    private static final String NAME_PATTERN = "\\s*([a-zA-Z]|_\\w)+\\w*\\s*";

    // Pattern's
    private static Pattern typePattern = Pattern.compile(TYPES_PATTERN);
    private static Pattern namePattern = Pattern.compile(NAME_PATTERN);

    // Field's of Variable.
    private final String TYPE;
    private final String NAME;
    private boolean isFinal;
    private boolean hasValue = false;

    /**
     * Create variable without initialized the value of the variable.
     *
     * @param type The type of the variable.
     * @param name The name of the variable.
     */
    Variable(String type, String name, int originLine, boolean isFinal) throws IllegalException {
        if (!isLegalVariableType(type)) {
            throw new IllegalException(TYPE_ERROR_MESSAGE, originLine);
        }
        if (!isLegalVariableName(name)) {
            throw new IllegalException(NAME_ERROR_MESSAGE, originLine);
        }
        TYPE = type;
        NAME = name;
        this.isFinal = isFinal;
    }

    /**
     * Create a variable that represent a parameter.
     *
     * @param type The type of the parameter.
     * @param name The name of the parameter.
     * @return An Variable object which represent the parameter.
     */
    static Variable createParameter(String type, String name, int lineNumber, boolean isFinal)
            throws IllegalException {
        if (!isLegalVariableName(name)) {
            throw new IllegalException(NAME_ERROR_MESSAGE, lineNumber);
        }
        Variable variable = new Variable(type, name, lineNumber, isFinal);
        variable.hasValue = true;
        return variable;
    }

    /**
     * Verifying the legality of the variable name.
     *
     * @param name The name to check.
     */
    static boolean isLegalVariableName(String name) {
        Matcher matcher = namePattern.matcher(name);
        return matcher.matches() && !Reserved.isReserved(name);
    }

    /**
     * Check if the type of the variable is legal.
     *
     * @param type The type of the variable.
     */
    static boolean isLegalVariableType(String type) {
        Matcher matcher = typePattern.matcher(type);
        return matcher.matches();
    }

    /**
     * @return The type of the variable.
     */
    String getType() {
        return TYPE;
    }

    /**
     * @return Is the variable has value.
     */
    boolean hasValue() {
        return hasValue;
    }

    /**
     * @return The name of the variable.
     */
    String getName() {
        return NAME;
    }

    /**
     * Set a new value to the variable.
     *
     * @param value      The new value.
     * @param lineNumber The line number of change the variable value.
     * @throws IllegalException The new value is illegal.
     */
    void setValue(String value, int lineNumber) throws IllegalException {
        if (isFinal && hasValue) {
            throw new IllegalException(FINAL_ASSIGNMENT, lineNumber);
        }
        boolean isValue;
        isValue = !isLegalVariableName(value);
        if (isValue) {
            try {
                switch (TYPE) {
                    case INT:
                        if (!isInt(value)) {
                            throw new IllegalException(TYPE_ERROR_MESSAGE, lineNumber);
                        }
                        break;
                    case DOUBLE:
                        if (!isDouble(value)) {
                            throw new IllegalException(TYPE_ERROR_MESSAGE, lineNumber);
                        }
                        break;
                    case BOOLEAN:
                        if (!isBoolean(value)) {
                            throw new IllegalException(TYPE_ERROR_MESSAGE, lineNumber);
                        }
                        break;
                    case CHAR:
                        if (!isChar(value)) {
                            throw new IllegalException(TYPE_ERROR_MESSAGE, lineNumber);
                        }
                        break;
                    case STRING:
                        if (!isString(value)) {
                            throw new IllegalException(TYPE_ERROR_MESSAGE, lineNumber);
                        }
                        break;
                }
                hasValue = true;
            } catch (Exception e) {
                throw new IllegalException(TYPE_ERROR_MESSAGE, lineNumber);
            }
        } else {
            Variable variable = Parser.getVariable(value);
            if (variable == null) {
                throw new IllegalException(MISSING_VARIABLE,
                        lineNumber);
            } else if (!variable.hasValue) {
                throw new IllegalException(UNEXCITED_VALUE, lineNumber);
            } else {
                copyValue(variable.getType());
            }
        }
    }

    /*
     * Check if the given string represent integer value.
     * @param value The value to check.
     * @return true if the value represent integer value, false otherwise.
     */
    private static boolean isInt(String value) {
        try {
            int helper = Integer.parseInt(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /*
     * Check if the given string represent a double value.
     * @param value The value to check.
     * @return true if the value represent double value, false otherwise.
     */
    private static boolean isDouble(String value) {
        try {
            double helper = Double.parseDouble(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /*
     * Check if the given string represent a boolean value.
     * @param value The value to check.
     * @return true if the value represent boolean value, false otherwise.
     */
    private static boolean isBoolean(String value) {
        return value.equals(TRUE) || value.equals(FALSE) || isDouble(value);
    }

    /*
     * Check if the given string represent a char value.
     * @param value The value to check.
     * @return true if the value represent char value, false otherwise.
     */
    private static boolean isChar(String value) {
        return value.indexOf(APOSTROPHE) == 0 && value.lastIndexOf(APOSTROPHE) == 2 && value.length() == 3;
    }

    /*
     * Check if the given string represent a string value.
     * @param value The value to check.
     * @return true if the given string represent a string value, false otherwise.
     */
    private static boolean isString(String value) {
        int firstIndex = value.indexOf(CHAR_APOSTROPHES), lastIndex = value.lastIndexOf(CHAR_APOSTROPHES);
        if (firstIndex == -1 || firstIndex == lastIndex) {
            return false;
        } else {
            value = value.substring(firstIndex + 1, lastIndex);
            return !value.contains(STRING_APOSTROPHES);
        }
    }

    /**
     * Copy the value of another variable to this variable.
     *
     * @param copyVariableType The type of the other variable.
     * @return true if the copy succeed, false otherwise.
     */
    private boolean copyValue(String copyVariableType) {
        boolean isLegal = false;
        switch (TYPE) {
            case INT:
                if (copyVariableType.equals(INT)) {
                    isLegal = true;
                }
                break;
            case DOUBLE:
                if (copyVariableType.equals(DOUBLE) || (copyVariableType.equals(INT))) {
                    isLegal = true;
                }
                break;
            case BOOLEAN:
                switch (copyVariableType) {
                    case DOUBLE:
                    case INT:
                    case BOOLEAN:
                        isLegal = true;
                        break;
                }
                break;
            case CHAR:
                if (copyVariableType.equals(CHAR)) {
                    isLegal = true;
                }
                break;
            case STRING:
                if (copyVariableType.equals(STRING)) {
                    isLegal = true;
                }
                break;
        }
        if (isLegal) {
            hasValue = true;
        }
        return isLegal;
    }

    /**
     * check if a variable is a boolean expression (boolean, int or double)
     * @return True if it is boolean expression, false otherwise.
     */
    boolean isBooleanExpression() {
        return hasValue && (TYPE.equals(BOOLEAN) || TYPE.equals(INT) || TYPE.equals(DOUBLE));
    }

    /**
     * Create default variable.
     *
     * @param value      The value that the variable has.
     * @param numberLine THe line to call this function.
     * @return A variable match to the parameters.
     * @throws IllegalException The value wasn't legal.
     */
    static Variable createDefaultVariable(String value, int numberLine) throws IllegalException {
        Variable variable;
        if (isInt(value)) {
            variable = createParameter(INT, DEFAULT_INT, numberLine, true);
        } else if (isDouble(value)) {
            variable = createParameter(DOUBLE, DEFAULT_DOUBLE, numberLine, true);
        } else if (isChar(value)) {
            variable = createParameter(CHAR, DEFAULT_CHAR, numberLine, true);
        } else if (isBoolean(value)) {
            variable = createParameter(BOOLEAN, DEFAULT_BOOLEAN, numberLine, true);
        } else if (isString(value)) {
            variable = createParameter(STRING, DEFAULT_STRING, numberLine, true);
        } else {
            throw new IllegalException(VALUE_ERROR_MESSAGE, numberLine);
        }
        return variable;
    }

    /**
     * represent a print version of a variable.
     * @return The String representation of the value.
     */
    public String toString() {
        return (TYPE + ", " + NAME + ", has Value: " + hasValue + ", is final: " + isFinal);
    }
}