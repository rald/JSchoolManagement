
/**
 * Write a description of class User here.
 *
 * @author Gerald Q. Cruz
 * @version Fri., Jun. 11, 2021 - 11:45 PM
 */

package edu.hogwarts.siesta;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class UserCreation extends JDialog {

    JLabel lblFirstName;
    JLabel lblLastName;
    JLabel lblPhone;
    JLabel lblAddress;
    JLabel lblUserName;
    JLabel lblPassWord;
    JLabel lblUserType;

    JTextField txtFirstName;
    JTextField txtLastName;
    JTextField txtPhone;
    JTextField txtAddress;
    JTextField txtUserName;
    JPasswordField txtPassWord;
    JComboBox cboUserType;

    JTable tblUser;
    JScrollPane spUser;

    JButton btnAdd;
    JButton btnUpdate;
    JButton btnRemove;
    JButton btnClear;
    JButton btnClose;

    String[] userTypes = {"Admin","Teacher"};

    String[] tableColumnNames={"ID","Firstname","Lastname","Phone","Address","Username","Usertype"};

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    DefaultTableModel d;

    int userId;
    String userFirstName;
    String userLastName;
    String userPhone;
    String userAddress;
    String userName;
    String userPass;
    String userType;

    /**
     * Constructor for objects of class User
     */
    UserCreation() {
        initGUI(); 
        connect();
        userLoad();
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

    public void userLoad() {
        int c;
        try {
            pst=con.prepareStatement("select * from user");

            rs=pst.executeQuery();

            ResultSetMetaData rsd=rs.getMetaData();
            c=rsd.getColumnCount();
            d=(DefaultTableModel)tblUser.getModel();
            d.setRowCount(0);

            while(rs.next()) {
                Vector v=new Vector();
                for(int i=0;i<c;i++) {
                    v.add(rs.getInt("user_id"));
                    v.add(rs.getString("user_firstname"));
                    v.add(rs.getString("user_lastname"));
                    v.add(rs.getString("user_phone"));
                    v.add(rs.getString("user_address"));
                    v.add(rs.getString("user_name"));
                    v.add(rs.getString("user_type"));
                }

                d.addRow(v);
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    } 

    public void clearUserForm() { 
        txtFirstName.setText("");
        txtLastName.setText("");
        txtPhone.setText("");
        txtAddress.setText("");
        txtUserName.setText("");
        txtPassWord.setText("");
        cboUserType.setSelectedIndex(-1);
        txtFirstName.requestFocus();
    }

    public boolean isValidUserForm() {
        userFirstName = txtFirstName.getText();
        userLastName = txtLastName.getText();
        userPhone = txtPhone.getText();
        userAddress = txtAddress.getText();
        userName = txtUserName.getText();
        userPass = txtPassWord.getText();
        int userTypeIndex = cboUserType.getSelectedIndex();

        if(userFirstName.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Firstname is empty");
            txtFirstName.requestFocus();
            return false;
        }

        if(userLastName.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Lastname is empty");
            txtLastName.requestFocus();
            return false;
        }

        if(userPhone.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Phone is empty");
            txtPhone.requestFocus();
            return false;
        }

        if(userAddress.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Address is empty");
            txtAddress.requestFocus();
            return false;
        }

        if(userName.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Username is empty");
            txtUserName.requestFocus();
            return false;
        }

        if(userPass.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Password is empty");
            txtPassWord.requestFocus();
            return false;
        }

        if(userTypeIndex==-1) {
            JOptionPane.showMessageDialog(this,"Please choose a usertype");
            cboUserType.requestFocus();
            return false;
        }

        userType = cboUserType.getSelectedItem().toString();

        return true;
    }

    public void btnAddActionPerformed(ActionEvent e) {

        if(!isValidUserForm()) return;

        try {
            pst = con.prepareStatement("insert into user(user_firstname,user_lastname,user_phone,user_address,user_name,user_pass,user_type) values(?,?,?,?,?,?,?)");

            pst.setString(1,userFirstName);
            pst.setString(2,userLastName);
            pst.setString(3,userPhone);
            pst.setString(4,userAddress);
            pst.setString(5,userName);
            pst.setString(6,userPass);
            pst.setString(7,userType);

            pst.executeUpdate();

            clearUserForm();

            userLoad();

            JOptionPane.showMessageDialog(this,"User added");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    } 

    public void btnUpdateActionPerformed(ActionEvent e) {

        if(tblUser.getRowCount()==0) {
            JOptionPane.showMessageDialog(this,"No user to select");    
            return;        
        }

        d=(DefaultTableModel)tblUser.getModel();
        int selectedRowIndex=tblUser.getSelectedRow();

        if(selectedRowIndex==-1) {
            JOptionPane.showMessageDialog(this,"Please select a user");    
            return;
        }  

        userId=(int)d.getValueAt(selectedRowIndex,0);

        if(!isValidUserForm()) return;

        try {
            pst = con.prepareStatement("update user set user_firstname=?,user_lastname=?,user_phone=?,user_address=?,user_name=?,user_pass=?,user_type=? where user_id=?");

            pst.setString(1,userFirstName);
            pst.setString(2,userLastName);
            pst.setString(3,userPhone);
            pst.setString(4,userAddress);
            pst.setString(5,userName);
            pst.setString(6,userPass);
            pst.setString(7,userType);
            pst.setInt(8,userId);

            pst.executeUpdate();

            clearUserForm();

            userLoad();

            btnAdd.setEnabled(true);

            JOptionPane.showMessageDialog(this,"User updated");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    } 

    public void btnRemoveActionPerformed(ActionEvent e) {

        if(tblUser.getRowCount()==0) {
            JOptionPane.showMessageDialog(this,"No user to select");    
            return;        
        }

        d=(DefaultTableModel)tblUser.getModel();
        int selectedRowIndex=tblUser.getSelectedRow();

        if(selectedRowIndex==-1) {
            JOptionPane.showMessageDialog(this,"Please select a user");    
            return;
        }

        userId=(int)d.getValueAt(selectedRowIndex,0);

        if (JOptionPane.showConfirmDialog(null, "Are you sure to remove the selected user?", "WARNING",
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            try {
                pst = con.prepareStatement("delete from user where user_id=?");

                pst.setInt(1,userId);

                pst.executeUpdate();

                clearUserForm();

                userLoad();

                btnAdd.setEnabled(true);

                JOptionPane.showMessageDialog(this,"User removed");
                
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }

        } else {
            JOptionPane.showMessageDialog(this,"Operation Canceled");
        }

    } 

    public void btnClearActionPerformed(ActionEvent e) { 
        clearUserForm();
        userLoad();
        btnAdd.setEnabled(true);        
    }    

    public void btnCloseActionPerformed(ActionEvent e) {        
        dispose();
    }

    public void tblUserMouseReleased(MouseEvent e) {
        
        d=(DefaultTableModel)tblUser.getModel();
        int selectedRowIndex=tblUser.getSelectedRow();
        userId=(int)d.getValueAt(selectedRowIndex,0);

        int c;
        try {
            pst=con.prepareStatement("select * from user where user_id=?");

            pst.setInt(1,userId);

            rs=pst.executeQuery();

            ResultSetMetaData rsd=rs.getMetaData();

            if(rs.next()) {
                txtFirstName.setText(rs.getString("user_firstname"));
                txtLastName.setText(rs.getString("user_lastname"));
                txtPhone.setText(rs.getString("user_phone"));
                txtAddress.setText(rs.getString("user_address"));
                txtUserName.setText(rs.getString("user_name"));
                txtPassWord.setText(rs.getString("user_pass"));
                cboUserType.setSelectedItem(rs.getString("user_type"));
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }       

        btnAdd.setEnabled(false);
    }

    public void initGUI() {

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        setTitle("User");

        setSize(640,640);

        setLayout(null);

        lblFirstName=new JLabel("Firstname");
        lblLastName=new JLabel("Lastname");
        lblPhone=new JLabel("Phone");
        lblAddress=new JLabel("Address");
        lblUserName=new JLabel("Username");
        lblPassWord=new JLabel("Password");
        lblUserType=new JLabel("Usertype");

        txtFirstName=new JTextField();
        txtLastName=new JTextField();
        txtPhone=new JTextField();
        txtAddress=new JTextField();
        txtUserName=new JTextField();
        txtPassWord=new JPasswordField();
        cboUserType=new JComboBox(userTypes);

        tblUser=new JTable(new DefaultTableModel(tableColumnNames,tableColumnNames.length){
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            }); 
              
        tblUser.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        spUser=new JScrollPane(tblUser);  

        btnAdd=new JButton("Add");
        btnUpdate=new JButton("Update");
        btnRemove=new JButton("Remove");
        btnClear=new JButton("Clear");
        btnClose=new JButton("Close");

        lblFirstName.setBounds(16,16,128,32);
        lblLastName.setBounds(16,64,128,32);
        lblPhone.setBounds(16,112,128,32);
        lblAddress.setBounds(16,160,128,32);
        lblUserName.setBounds(16,208,228,32);
        lblPassWord.setBounds(16,256,128,32);
        lblUserType.setBounds(16,304,128,32);

        txtFirstName.setBounds(146,16,256,32);
        txtLastName.setBounds(146,64,256,32);
        txtPhone.setBounds(146,112,256,32);
        txtAddress.setBounds(146,160,256,32);
        txtUserName.setBounds(146,208,256,32);
        txtPassWord.setBounds(146,256,256,32);
        cboUserType.setBounds(146,304,256,32);

        spUser.setBounds(16,400,600,200);        

        btnAdd.setBounds(16,352,96,32);
        btnUpdate.setBounds(128,352,96,32);
        btnRemove.setBounds(240,352,96,32);
        btnClear.setBounds(352,352,96,32);
        btnClose.setBounds(464,352,96,32);        

        add(lblFirstName);
        add(lblLastName);
        add(lblPhone);
        add(lblAddress);
        add(lblUserName);
        add(lblPassWord);
        add(lblUserType);

        add(txtFirstName);
        add(txtLastName);
        add(txtPhone);
        add(txtAddress);
        add(txtUserName);
        add(txtPassWord);

        add(cboUserType);
        add(spUser);

        add(btnAdd);
        add(btnUpdate);
        add(btnRemove);
        add(btnClear);
        add(btnClose);

        btnAdd.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {      
                    btnAddActionPerformed(e);
                }
            });

        btnUpdate.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {      
                    btnUpdateActionPerformed(e);
                }
            });

        btnRemove.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {      
                    btnRemoveActionPerformed(e);
                }
            });

        btnClear.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {      
                    btnClearActionPerformed(e);
                }
            });

        btnClose.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {      
                    btnCloseActionPerformed(e);
                }
            });

        tblUser.addMouseListener(new MouseAdapter() {
                public void mouseReleased(MouseEvent e) {
                    tblUserMouseReleased(e);                    
                } 
            });

        clearUserForm();

        setLocationRelativeTo(null);   

        setVisible(true);

        setModal(true);

    }
}
