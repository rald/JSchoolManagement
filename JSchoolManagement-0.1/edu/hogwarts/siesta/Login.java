
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

public class Login extends JFrame {

    private JLabel lblUserName;
    private JLabel lblPassWord;
    private JTextField txtUserName;
    private JPasswordField txtPassWord;
    private JButton btnOK;

    String userName;
    String userPassWord;

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    DefaultTableModel d;

    /**
     * Constructor for objects of class Login
     */
    public Login() {
        // initialise instance variables        
        initGUI();
        connect();
    }

    public void connect() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mariadb://localhost/JSchoolManagement","root","alohomora");
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public boolean isValidLoginForm() {

        userName=txtUserName.getText();
        userPassWord=txtPassWord.getText();

        if(userName.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Username is empty");
            txtUserName.requestFocus();
            return false;
        }

        if(userPassWord.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Password is empty");
            txtPassWord.requestFocus();
            return false;
        }

        return true;
    }

    public void btnOKActionPerformed(ActionEvent e) {

        if(!isValidLoginForm()) return;

        if(userName.equals("admin") && userPassWord.equals("admin")) {
            
            setVisible(false);

            JOptionPane.showMessageDialog(this,"Access granted");

            new AdminMain(0,"Admin","Admin");
            
        } else {

            try {
                pst = con.prepareStatement("select * from user where user_name=? and user_pass=?");

                pst.setString(1,userName);
                pst.setString(2,userPassWord);

                rs = pst.executeQuery();

                if(rs.next()) {
                    int userId = rs.getInt("user_id");
                    String userFirstName = rs.getString("user_firstname");
                    String userType = rs.getString("user_type");

                    setVisible(false);

                    JOptionPane.showMessageDialog(this,"Access granted");

                    if(userType.equals("Admin")) {
                        new AdminMain(userId,userFirstName,userType); 
                    } else if(userType.equals("Teacher")) {
                        new TeacherMain(userId,userFirstName,userType); 
                    } 

                } else {
                    JOptionPane.showMessageDialog(this,"Access denied");
                }

            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }

    }

    void initGUI() {

        setTitle("Login");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(424,184);

        setResizable(false);

        setLayout(null);

        lblUserName=new JLabel("Username");
        lblPassWord=new JLabel("Password");
        txtUserName=new JTextField();
        txtPassWord=new JPasswordField();
        btnOK=new JButton("OK");

        lblUserName.setHorizontalAlignment(SwingConstants.CENTER);
        lblPassWord.setHorizontalAlignment(SwingConstants.CENTER);

        lblUserName.setBounds(16,16,128,32);
        lblPassWord.setBounds(16,64,128,32);
        txtUserName.setBounds(144,16,256,32);
        txtPassWord.setBounds(144,64,256,32);
        btnOK.setBounds(16,112,384,32);

        add(lblUserName); 
        add(lblPassWord);
        add(txtUserName); 
        add(txtPassWord);
        add(btnOK);

        btnOK.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {      
                    btnOKActionPerformed(e);
                }
            });

        setLocationRelativeTo(null);

        setVisible(true);

    }

}
