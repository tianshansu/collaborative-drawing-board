/**
 * Name: Tianshan Su
 * Student ID: 875734
 */
package constants;

/**
 * Client Constants
 * This class defines constant messages used by the client user interface
 */
public class ClientConstants {
    /**
     * Message displayed when the manager declines the user's join request
     */
    public static final String JOIN_REQUEST_DECLINED = "The manager has declined you to join";

    /**
     * Message shown when the manager disconnects from the session
     */
    public static final String MANAGER_DISCONNECTED = "The manager has gone offline. This room is now closed.";

    /**
     * Message displayed when the user is kicked out by the manager
     */
    public static final String KICKED_BY_MANAGER = "You have been kicked by the manager";

    /**
     * Title of the dialog shown when the user is kicked
     */
    public static final String KICKED_TITLE = "Kicked";

    /**
     * Message displayed when the chosen username is already in use
     */
    public static final String NAME_TAKEN = "The username has been taken";

    /**
     * Message shown when no manager is available to accept new connections
     */
    public static final String NO_MANAGER_FOUND = "No manager is currently available. Please try again later.";

    /**
     * Message shown when the client fails to establish a connection with the server
     */
    public static final String UNABLE_TO_CONNECT = "Unable to connect to the server";

    /**
     * Title of the client-side user interface window
     */
    public static final String UI_TITLE = "Shared Whiteboard - Client";

}
