
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

public class TeacherMain extends JFrame {
    
    JLabel lblUserName;
    JLabel lblUserType; 
    
    JButton btnTeacher;
    JButton btnStudent;
    JButton btnMarks;
    JButton btnLogout;
    
    TeacherMain(int id,String user_name,String user_type) {
        initGUI(id,user_name,user_type);
    }
    
    public void initGUI(int userId,String userName,String userType) {
        
        setTitle("Teacher Main");
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setSize(640,480);

        setResizable(false);
        
        setLayout(null);
        
        lblUserName=new JLabel(userName);
        lblUserType=new JLabel(userType);
        
        btnTeacher=new JButton("Teacher");
        btnStudent=new JButton("Student");
        btnMarks=new JButton("Marks");
        btnLogout=new JButton("Logout");
        
        lblUserName.setBounds(16,16,128,32);
        lblUserType.setBounds(16,48,128,32);
        
        btnTeacher.setBounds(16,112,128,32);
        btnStudent.setBounds(16,160,128,32);
        btnMarks.setBounds(16,208,128,32);
        btnLogout.setBounds(16,256,128,32);
        
        
        add(lblUserName);
        add(lblUserType);
        
        setLocationRelativeTo(null);
        
        setVisible(true);
    }
}
