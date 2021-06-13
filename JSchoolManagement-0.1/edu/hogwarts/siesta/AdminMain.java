
/**
 * Write a description of class User here.
 *
 * @author Gerald Q. Cruz
 * @version Fri., Jun. 11, 2021 - 11:45 PM
 */

package edu.hogwarts.siesta;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.sql.*;

public class AdminMain extends JFrame
{

    int userId;
    String userName;
    String userType;  

    JLabel lblUserName;
    JLabel lblUserType; 

    JButton btnUserCreation;
    JButton btnClasses;

    AdminMain(int userId,String userName,String userType) {
        initGUI(userId,userName,userType);
    }

    public void btnUserCreationActionPerformed(ActionEvent e) {
        new UserCreation();
    }
    
    public void btnClassesActionPerformed(ActionEvent e) {
        new Classes();
    }

    public void initGUI(int userId,String userName,String userType) {
        setTitle("Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(640,480);
        setResizable(false);
        setLayout(null);

        this.userId=userId;
        this.userName=userName;
        this.userType=userType;

        lblUserName=new JLabel(userName);
        lblUserType=new JLabel(userType);

        btnUserCreation=new JButton("User Creation");
        btnClasses=new JButton("Class");

        lblUserName.setBounds(16,16,128,32);
        lblUserType.setBounds(16,48,128,32);

        btnUserCreation.setBounds(16,112,174,32);
        btnClasses.setBounds(16,160,174,32);

        add(lblUserName);
        add(lblUserType);

        add(btnUserCreation);
        add(btnClasses);

        btnUserCreation.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    btnUserCreationActionPerformed(e);
                }    
            });

        btnClasses.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    btnClassesActionPerformed(e);
                }    
            });

        setLocationRelativeTo(null);

        setVisible(true);
    }
}
