

/**
 * Write a description of class User here.
 *
 * @author Gerald Q. Cruz
 * @version Sun., Jun. 13, 2021 - 8:42 AM
 */

package edu.hogwarts.siesta;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.sql.*;

public class StudentMain extends JFrame {
    
    JLabel lblusername;
    JLabel lblusertype; 
    
    StudentMain(int id,String user_name,String user_type) {
        initGUI(id,user_name,user_type);
    }
    
    public void initGUI(int id,String user_name,String user_type) {
        
        setTitle("Student");
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setSize(640,480);

        setResizable(false);
        
        setLayout(null);
        
        lblusername=new JLabel(user_name);
        lblusertype=new JLabel(user_type);
        
        lblusername.setBounds(16,16,128,32);
        lblusertype.setBounds(16,48,128,32);
            
        add(lblusername);
        add(lblusertype);
        
        setLocationRelativeTo(null);
        
        setVisible(true);
    }
}
