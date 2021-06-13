/**
 * Write a description of class User here.
 *
 * @author Gerald Q. Cruz
 * @version Sun., Jun. 13, 2021 - 5:59 PM
 */

package edu.hogwarts.siesta;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class Classes extends JDialog {

    /**
     * Constructor for objects of class Classes
     */
    public Classes() {
        initGUI();
    }

    public void initGUI() {
        
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        setTitle("Class");
        
        setSize(640,640);
        
        setVisible(true);
        
        setModal(true);
    }
}
