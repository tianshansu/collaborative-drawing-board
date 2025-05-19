/**
 * Name: Tianshan Su
 * Student ID: 875734
 */
package enums;

/**
 * The enum class - Join result
 */
public enum JoinResult {
    /**
     * success if the server accept the join request
     */
    SUCCESS,

    /**
     * name_taken if the user's username is already taken by someone else
     */
    NAME_TAKEN,

    /**
     * rejected if the server reject the join request
     */
    REJECTED
}
