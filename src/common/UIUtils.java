/**
 * Name: Tianshan Su
 * Student ID: 875734
 */
package common;

import constants.MessageConstants;

import javax.swing.*;

/**
 * Utils class
 */
public class UIUtils {

    /**
     * Exit the program with an error message
     * @param jFrame the jFrame to show the message above
     * @param msg the error message
     */
    public static void exitWithMsg(JFrame jFrame,String msg) {
        JOptionPane.showMessageDialog(
                jFrame,
                msg,
                MessageConstants.DIALOG_TEXT_ERROR,
                JOptionPane.ERROR_MESSAGE
        );
        System.exit(0);
    }
}
