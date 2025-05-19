/**
 * Name: Tianshan Su
 * Student ID: 875734
 */
package constants;

/**
 * Server Constants - This class defines constant values used by the server components of the shared whiteboard system
 */
public class ServerConstants {
    /**
     * The RMI service name used for binding the whiteboard server
     */
    public static final String SERVICE_NAME = "WhiteBoardService";

    /**
     * Directory path for saving whiteboard canvas files (in serialized form)
     */
    public static final String CANVAS_SAVE_PATH = "saved_canvas/";

    /**
     * Directory path for saving whiteboard images (exported as images)
     */
    public static final String CANVAS_SAVE_AS_PATH = "saved_imgs/";

    /**
     * Title of the manager's UI window
     */
    public static final String WINDOW_TITLE = "Shared Whiteboard - Manager";

    /**
     * Label for the "File" menu in the manager UI
     */
    public static final String MENU_FILE = "File";

    /**
     * Label for creating a new whiteboard in the menu
     */
    public static final String MENU_NEW = "New";

    /**
     * Label for opening an existing whiteboard in the menu
     */
    public static final String MENU_OPEN = "Open";

    /**
     * Label for saving the current whiteboard in the menu
     */
    public static final String MENU_SAVE = "Save";

    /**
     * Label for saving the current whiteboard as an image in the menu
     */
    public static final String MENU_SAVE_AS = "SaveAs";

    /**
     * Label for closing the current whiteboard in the menu
     */
    public static final String MENU_CLOSE = "Close";

    /**
     * Message shown in the input dialog when prompting for a filename to save the canvas
     */
    public static final String INPUT_DIALOG_SAVE_MSG = "Enter filename to save:";

    /**
     * Title of the save canvas input dialog
     */
    public static final String INPUT_DIALOG_SAVE_TITLE = "Save Canvas";

    /**
     * Message shown in the input dialog when prompting for a filename to open a saved canvas
     */
    public static final String INPUT_DIALOG_OPEN_MSG = "Enter filename to open:";

    /**
     * Title of the open canvas input dialog
     */
    public static final String INPUT_DIALOG_OPEN_TITLE = "Open Canvas";

    /**
     * Message displayed when the specified file to open does not exist
     */
    public static final String FILE_NOT_EXIST_MSG = "This file does not exist!";

    /**
     * Title used in general error dialogs
     */
    public static final String ERROR = "Error";

    /**
     * Message shown in the input dialog when prompting for a filename to save the canvas as an image
     */
    public static final String INPUT_DIALOG_SAVE_AS_MSG = "Enter filename to save the image:";

    /**
     * Title of the input dialog for saving the canvas as an image
     */
    public static final String INPUT_DIALOG_SAVE_AS_TITLE = "Save Canvas As Image";

    /**
     * Title of the dialog shown when trying to overwrite an existing file
     */
    public static final String FILE_ALREADY_EXISTS_TITLE = "File Already Exists";

    /**
     * Message asking the user if they want to overwrite an existing file
     */
    public static final String FILE_ALREADY_EXISTS_MSG = "File already exists. Do you want to overwrite it?";

    /**
     * Message shown when a new user requests to join the whiteboard
     * The actual username is prepended dynamically
     */
    public static final String JOIN_REQUEST_MSG = " wants to join the whiteboard. Allow?";

    /**
     * Title of the dialog shown when a join request is received
     */
    public static final String JOIN_REQUEST_TITLE = "A New User Wants to Join";

    /**
     * Message shown to clients when there is no active whiteboard available
     */
    public static final String NO_ACTIVE_BOARD_MSG = "No active whiteboard. Please wait for manager to create one";





}
