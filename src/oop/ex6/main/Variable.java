package oop.ex6.main;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by David and Roi.
 */
class Variable {

    // Useful value's.
    private final static String INT = "int", DOUBLE = "double";
    private final static String CHAR = "char", STRING = "String", BOOLEAN = "boolean";
    private final static String TRUE = "true", FALSE = "false";

    // Errors string's.
    private final static String TYPE_ERROR_MESSAGE = "Illegal type of value";
    private final static String NAME_ERROR_MESSAGE = "Illegal name variable name";

    // Pattern's string's.
    private static final String TYPES_PATTERN = "int|double|String|boolean|char";
    private static final String NAME_PATTERN = "\\s*([a-zA-Z]|_\\w)+\\w*\\s*";

    // Pattern's
    private static Pattern typePattern = Pattern.compile(TYPES_PATTERN);
    private static Pattern namePattern = Pattern.compile(NAME_PATTERN);

    // Field's of Variable.
    private final String TYPE;
    private final String NAME;
    private final boolean FINAL;
    private boolean hasValue = false;

    /**
     * Create variable and initialized the value of the variable.
     *
     * @param type       The type of the variable.
     * @param name       The name of the variable.
     * @param value      The value of the variable.
     * @param originLine The line number of the creation of the variable.
     * @throws IllegalException The value that given was un valid.
     */
    Variable(String type, String name, String value, int originLine, boolean isFinal) throws
            IllegalException {
        if (!isLegalVariableType(type)) {
            throw new IllegalException(TYPE_ERROR_MESSAGE, originLine);
        }
        TYPE = type;
        NAME = name;
        FINAL = isFinal;
        setValue(value, originLine);
    }

    /**
     * Create variable without initialized the value of the variable.
     *
     * @param type The type of the variable.
     * @param name The name of the variable.
     */
    Variable(String type, String name, int originLine) throws IllegalException {
        if (!isLegalVariableType(type)) {
            throw new IllegalException(TYPE_ERROR_MESSAGE, originLine);
        }
        TYPE = type;
        NAME = name;
        FINAL = false;
        hasValue = false;
    }

    /**
     * Create a variable that represent a parameter.
     *
     * @param type The type of the parameter.
     * @param name The name of the parameter.
     * @return An Variable object which represent the parameter.
     */
    static Variable createParameter(String type, String name, int lineNumber) throws IllegalException {
        verifyLegalityVariableName(name, lineNumber, new HashMap<>());
        Variable variable = new Variable(type, name, lineNumber);
        variable.hasValue = true;
        return variable;
    }

    /**
     * Verifying the legality of the variable name.
     *
     * @param name       The name to check.
     * @param lineNumber The line of the creation of the variable.
     * @throws IllegalException The name is illegal.
     */
    static void verifyLegalityVariableName(String name, int lineNumber, HashMap<String, Variable> variables)
            throws IllegalException {
        Matcher matcher = namePattern.matcher(name);
        if (!matcher.matches() || Reserved.isReserved(name)) {
            throw new IllegalException(NAME_ERROR_MESSAGE, lineNumber);
        } else if (Reserved.isReserved(name)) {
            throw new IllegalException(NAME_ERROR_MESSAGE, lineNumber);
        } else if (variables.containsKey(name)) {
            throw new IllegalException(NAME_ERROR_MESSAGE, lineNumber);
        }
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
    String getTYPE() {
        return TYPE;
    }

    /**
     * @return Is the variable has value.
     */
    boolean hasValue() {
        return hasValue;
    }

//    /**
//     * Change the HasValue value.
//     *
//     * @param hasValue The new hasValue value.
//     */
//    void setHasValue(boolean hasValue) {
//        this.hasValue = hasValue;
//    }

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
        if (FINAL && hasValue) {
            throw new IllegalException("Final variable can't change", lineNumber);
        }
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
        } catch (Exception e) {
            throw new IllegalException(TYPE_ERROR_MESSAGE, lineNumber);
        }
    }

    /*
     * Check if the given string represent integer value.
     * @param value The value to check.
     * @return true if the value represent integer value, false otherwise.
     */
    private boolean isInt(String value) {
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
    private boolean isDouble(String value) {
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
    private boolean isBoolean(String value) {
        return value.equals(TRUE) || value.equals(FALSE) || isDouble(value);
    }

    /*
     * Check if the given string represent a char value.
     * @param value The value to check.
     * @return true if the value represent char value, false otherwise.
     */
    private boolean isChar(String value) {
        char[] helper = value.toCharArray();
        return helper.length == 1;
    }

    /*
     * Check if the given string represent a string value.
     * @param value The value to check.
     * @return true if the given string represent a string value, false otherwise.
     */
    private boolean isString(String value) {
        int firstIndex = value.indexOf('"'), lastIndex = value.lastIndexOf('"');
        if (firstIndex == -1 || firstIndex == lastIndex) {
            return false;
        } else {
            value = value.substring(firstIndex, lastIndex);
            return !value.contains("\"");
        }
    }

    /**
     * Copy the value of another variable to this variable.
     *
     * @param copyVariableType The type of the other variable.
     * @return true if the copy succeed, false otherwise.
     */
    boolean copyValue(String copyVariableType) {
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
}