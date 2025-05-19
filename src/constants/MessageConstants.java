/**
 * Name: Tianshan Su
 * Student ID: 875734
 */
package constants;

/**
 * Message Constants - This class holds all static error message strings used throughout the program
 */
public class MessageConstants {
    /**
     * Error message shown when the number of command-line arguments is not exactly 3
     */
    public static final String ARGUMENT_LENGTH_INCORRECT = "The length of arguments must be 3";

    /**
     * Error message shown when the provided port number is not a valid integer
     */
    public static final String PORT_NUMBER_INCORRECT_FORMAT = "The port number is in incorrect format";

    /**
     * Error message shown when the provided port number is outside the valid range (1024–65535)
     */
    public static final String PORT_OUT_OF_RANGE = "The port number is out of range";

    /**
     * Title text used for general error dialogs
     */
    public static final String DIALOG_TEXT_ERROR = "Error";

    /**
     * Title text used for dialogs when the manager has gone offline
     */
    public static final String DIALOG_TEXT_MANAGER_OFFLINE = "Manager offline";

    /**
     * Unknown host
     */
    public static final String UNKNOWN_HOST = "Unknown Host";

    /**
     * Port already in use
     */
    public static final String PORT_ALREADY_IN_USE ="Port already in use: ";

}
