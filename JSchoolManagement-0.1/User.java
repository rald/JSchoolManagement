
/**
 * Write a description of class User here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class User extends JFrame
{
    // instance variables - replace the example below with your own
 
    JLabel lblfirstname;
    JLabel lbllastname;
    JLabel lblphone;
    JLabel lbladdress;
    JLabel lbluser;
    JLabel lblpass;
    JLabel lblusertype;
    
    JTextField txtfirstname;
    JTextField txtlastname;
    JTextField txtphone;
    JTextField txtaddress;
    JTextField txtuser;
    JPasswordField txtpass;
    JComboBox cbousertype;
    
    JButton btnadd;
    JButton btnupdate;
    JButton btnremove;
    JButton btnclear;
    JButton btnclose;

    Object[][] userData={
            {"0","Harry","Potter","09198688380","Hogwarts","hedwig","student"},
            {"1","Ron","Weasley","09198688381","Hogwarts","scabbers","student"},
            {"2","Hermione","Granger","09198688382","Hogwarts","crookshanks","teacher"}
    };
    
    Object[] columnNames={"ID","Firstname","Lastname","Phone","Address","Username","Usertype"};
    
    JTable tbluser;
    JScrollPane scpuser;
    
    /**
     * Constructor for objects of class User
     */
    public User()
    {
        initGUI(); 
    }

    public void initGUI()
    {
        setTitle("User");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setSize(640,640);
        
        setLayout(null);
        
        lblfirstname=new JLabel("Firstname");
        lbllastname=new JLabel("Lastname");
        lblphone=new JLabel("Phone");
        lbladdress=new JLabel("Address");
        lbluser=new JLabel("Username");
        lblpass=new JLabel("Password");
        lblusertype=new JLabel("Usertype");
       
        txtfirstname=new JTextField();
        txtlastname=new JTextField();
        txtphone=new JTextField();
        txtaddress=new JTextField();
        txtuser=new JTextField();
        txtpass=new JPasswordField();
        cbousertype=new JComboBox();
        
        btnadd=new JButton("Add");
        btnupdate=new JButton("Update");
        btnremove=new JButton("Remove");
        btnclear=new JButton("Clear");
        btnclose=new JButton("Close");
        
        lblfirstname.setBounds(16,16,128,32);
        lbllastname.setBounds(16,64,128,32);
        lblphone.setBounds(16,112,128,32);
        lbladdress.setBounds(16,160,128,32);
        lbluser.setBounds(16,208,228,32);
        lblpass.setBounds(16,256,128,32);
        lblusertype.setBounds(16,304,128,32);

        txtfirstname.setBounds(146,16,256,32);
        txtlastname.setBounds(146,64,256,32);
        txtphone.setBounds(146,112,256,32);
        txtaddress.setBounds(146,160,256,32);
        txtuser.setBounds(146,208,256,32);
        txtpass.setBounds(146,256,256,32);
        cbousertype.setBounds(146,304,256,32);
        
        btnadd.setBounds(16,352,96,32);
        btnupdate.setBounds(128,352,96,32);
        btnremove.setBounds(240,352,96,32);
        btnclear.setBounds(352,352,96,32);
        btnclose.setBounds(464,352,96,32);        
          
        add(lblfirstname);
        add(lbllastname);
        add(lblphone);
        add(lbladdress);
        add(lbluser);
        add(lblpass);
        add(lblusertype);
        
        add(txtfirstname);
        add(txtlastname);
        add(txtphone);
        add(txtaddress);
        add(txtuser);
        add(txtpass);
        add(cbousertype);
       
        add(btnadd);
        add(btnupdate);
        add(btnremove);
        add(btnclear);
        add(btnclose);
       
        tbluser=new JTable(userData,columnNames);
        scpuser=new JScrollPane(tbluser);  
        scpuser.setBounds(16,400,600,200);
        add(scpuser);
        
        setLocationRelativeTo(null);
        
        setVisible(true);
    }
}
