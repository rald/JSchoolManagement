

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

public class AdminMain extends JFrame {

    int userId;
    String userName;
    String userType;  

    JLabel lblUserName;
    JLabel lblUserType; 

    JButton btnUserCreation;
    JButton btnClassCreation;
    JButton btnSubjectCreation;

    AdminMain(int userId,String userName,String userType) {
        initGUI(userId,userName,userType);
    }

    public void btnUserCreationActionPerformed(ActionEvent e) {
        new UserCreation();
    }

    public void btnClassCreationActionPerformed(ActionEvent e) {
        new ClassCreation();
    }

    public void btnSubjectCreationActionPerformed(ActionEvent e) {
        new SubjectCreation();
    }

    public void initGUI(int userId,String userName,String userType) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Admin");
        setSize(640,480);
        setResizable(false);
        setLayout(null);

        this.userId=userId;
        this.userName=userName;
        this.userType=userType;

        lblUserName=new JLabel(userName);        
        lblUserType=new JLabel(userType);

        btnUserCreation=new JButton("User");
        btnClassCreation=new JButton("Class");
        btnSubjectCreation=new JButton("Subject");

        lblUserName.setBounds(16,16,128,32);
        lblUserType.setBounds(16,48,128,32);

        btnUserCreation.setBounds(16,112,128,32);
        btnClassCreation.setBounds(16,160,128,32);
        btnSubjectCreation.setBounds(16,208,128,32);

        lblUserName.setSize(lblUserName.getPreferredSize());

        add(lblUserName);
        add(lblUserType);

        add(btnUserCreation);
        add(btnClassCreation);
        add(btnSubjectCreation);

        btnUserCreation.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    btnUserCreationActionPerformed(e);
                }    
            });

        btnClassCreation.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    btnClassCreationActionPerformed(e);
                }    
            });

        btnSubjectCreation.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    btnSubjectCreationActionPerformed(e);
                }    
            });

        setLocationRelativeTo(null);

        setVisible(true);
    }
}
