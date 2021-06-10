
/**
 * Write a description of class Login here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import java.awt.*;
import javax.swing.*;


public class Login extends JFrame
{
    // instance variables - replace the example below with your own

    private JLabel lbluser;
    private JLabel lblpass;
    private JTextField txtuser;
    private JPasswordField txtpass;
    private JButton btnok;
    
    /**
     * Constructor for objects of class Login
     */
    public Login()
    {
        // initialise instance variables        
        initGUI();
    }

    void initGUI() {
        
        setTitle("JFrame");
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setSize(416,180);
        
        setResizable(false);
        
        setLocationRelativeTo(null);
        
        setLayout(null);
        
        lbluser=new JLabel("Username");
        lblpass=new JLabel("Password");
        txtuser=new JTextField();
        txtpass=new JPasswordField();
        btnok=new JButton("OK");
        
        lbluser.setHorizontalAlignment(SwingConstants.CENTER);
        lblpass.setHorizontalAlignment(SwingConstants.CENTER);
        
        lbluser.setBounds(16,16,128,32);
        lblpass.setBounds(16,64,128,32);
        txtuser.setBounds(144,16,256,32);
        txtpass.setBounds(144,64,256,32);
        btnok.setBounds(16,112,384,32);
        
        add(lbluser); 
        add(lblpass);
        add(txtuser); 
        add(txtpass);
        add(btnok);
        
        setVisible(true);

    }
    
}
