
/**
 * Write a description of class User here.
 *
 * @author Gerald Q. Cruz
 * @version Wed., Jun. 16, 2021 - 1:58 PM
 */

package edu.hogwarts.siesta;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.text.*;

public class TeacherCreation extends JDialog {
    JLabel lblTeacherFirstName;
    JLabel lblTeacherLastName;
    JLabel lblTeacherQualification;
    JLabel lblTeacherSalary;
    JLabel lblTeacherPhone;
    JLabel lblTeacherAddress;

    JTextField txtTeacherFirstName;
    JTextField txtTeacherLastName;
    JTextField txtTeacherQualification;
    JTextField txtTeacherSalary;
    JTextField txtTeacherPhone;
    JTextField txtTeacherAddress;

    boolean cboStudentClassNameItemStateChangeActive=false;

    JTable tblTeacher;
    JScrollPane spTeacher;

    JButton btnAdd;
    JButton btnUpdate;
    JButton btnRemove;
    JButton btnClear;
    JButton btnClose;

    String[] tableColumnNames={"ID","Firstname","Lastname","Qualification","Salary","Phone","Address"};

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    DefaultTableModel d;

    int teacherId;
    String teacherFirstName;
    String teacherLastName;
    String teacherQualification;
    int teacherSalary;
    String teacherPhone;
    String teacherAddress;

    TeacherCreation() {
        initGUI(); 
        connect();
        clearTeacherForm();
        cboStudentClassNameItemStateChangeActive=true;
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

    public void teacherLoad() {
        try {
            pst=con.prepareStatement("select * from teacher");

            rs=pst.executeQuery();

            d=(DefaultTableModel)tblTeacher.getModel();
            d.setRowCount(0);

            while(rs.next()) {
                Vector v=new Vector();

                v.add(rs.getInt("teacher_id"));
                v.add(rs.getString("teacher_firstname"));
                v.add(rs.getString("teacher_lastname"));
                v.add(rs.getString("teacher_qualification"));                
                v.add(rs.getString("teacher_salary"));
                v.add(rs.getString("teacher_phone"));
                v.add(rs.getString("teacher_address"));

                d.addRow(v);
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    } 

    public void clearTeacherForm() { 
        txtTeacherFirstName.setText("");
        txtTeacherLastName.setText("");
        txtTeacherQualification.setText("");
        txtTeacherSalary.setText("");
        txtTeacherPhone.setText("");
        txtTeacherAddress.setText("");

        txtTeacherFirstName.requestFocus();

        teacherLoad();
    }

    public boolean isValidTeacherForm() {
        teacherFirstName = txtTeacherFirstName.getText();
        teacherLastName = txtTeacherLastName.getText();
        teacherQualification = txtTeacherQualification.getText();
        teacherSalary = Integer.parseInt(txtTeacherSalary.getText());
        teacherPhone = txtTeacherPhone.getText();
        teacherAddress = txtTeacherAddress.getText();

        if(teacherFirstName.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Firstname is empty");
            txtTeacherFirstName.requestFocus();
            return false;
        }

        if(teacherLastName.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Lastname is empty");
            txtTeacherLastName.requestFocus();
            return false;
        }

        if(teacherQualification.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Phone is empty");
            txtTeacherQualification.requestFocus();
            return false;
        }

        if(teacherSalary==0) {
            JOptionPane.showMessageDialog(this,"Phone is empty");
            txtTeacherSalary.requestFocus();
            return false;
        }

        if(teacherPhone.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Phone is empty");
            txtTeacherPhone.requestFocus();
            return false;
        }

        if(teacherAddress.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Address is empty");
            txtTeacherAddress.requestFocus();
            return false;
        }

        return true;
    }

    public void btnAddActionPerformed(ActionEvent e) {

        if(!isValidTeacherForm()) return;

        try {
            pst = con.prepareStatement("insert into teacher(teacher_firstname,teacher_lastname,teacher_qualification,teacher_salary,teacher_phone,teacher_address) values(?,?,?,?,?,?)");

            pst.setString(1,teacherFirstName);
            pst.setString(2,teacherLastName);
            pst.setString(3,teacherQualification);
            pst.setInt(4,teacherSalary);
            pst.setString(5,teacherPhone);
            pst.setString(6,teacherAddress);

            pst.executeUpdate();

            clearTeacherForm();

            JOptionPane.showMessageDialog(this,"Teacher added");

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    } 

    public void btnUpdateActionPerformed(ActionEvent e) {

        if(tblTeacher.getRowCount()==0) {
            JOptionPane.showMessageDialog(this,"No teacher to select");    
            return;        
        }

        d=(DefaultTableModel)tblTeacher.getModel();
        int selectedRowIndex=tblTeacher.getSelectedRow();

        if(selectedRowIndex==-1) {
            JOptionPane.showMessageDialog(this,"Please select a teacher");    
            return;
        }  

        teacherId=(int)d.getValueAt(selectedRowIndex,0);

        if(!isValidTeacherForm()) return;

        try {
            pst = con.prepareStatement("update teacher set teacher_firstname=?,teacher_lastname=?,teacher_qualification=?,teacher_salary=?,teacher_phone=?,teacher_address=? where teacher_id=?");

            pst.setString(1,teacherFirstName);
            pst.setString(2,teacherLastName);
            pst.setString(3,teacherQualification);
            pst.setInt(4,teacherSalary);
            pst.setString(5,teacherPhone);
            pst.setString(6,teacherAddress);
            pst.setInt(7,teacherId);

            pst.executeUpdate();

            clearTeacherForm();

            btnAdd.setEnabled(true);

            JOptionPane.showMessageDialog(this,"Teacher updated");

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    } 

    public void btnRemoveActionPerformed(ActionEvent e) {

        if(tblTeacher.getRowCount()==0) {
            JOptionPane.showMessageDialog(this,"No teacher to select");    
            return;        
        }

        d=(DefaultTableModel)tblTeacher.getModel();
        int selectedRowIndex=tblTeacher.getSelectedRow();

        if(selectedRowIndex==-1) {
            JOptionPane.showMessageDialog(this,"Please select a teacher");    
            return;
        }

        teacherId=(int)d.getValueAt(selectedRowIndex,0);

        if (JOptionPane.showConfirmDialog(null, "Are you sure to remove the selected teacher?", "WARNING",
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            try {
                pst = con.prepareStatement("delete from teacher where teacher_id=?");

                pst.setInt(1,teacherId);

                pst.executeUpdate();

                clearTeacherForm();

                btnAdd.setEnabled(true);

                JOptionPane.showMessageDialog(this,"Teacher removed");

            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }

        } else {
            JOptionPane.showMessageDialog(this,"Operation Canceled");
        }

    } 

    public void btnClearActionPerformed(ActionEvent e) { 
        clearTeacherForm();
        btnAdd.setEnabled(true);        
    }    

    public void btnCloseActionPerformed(ActionEvent e) {        
        dispose();
    }

    public void tblTeacherMouseReleased(MouseEvent e) {

        d=(DefaultTableModel)tblTeacher.getModel();
        int selectedRowIndex=tblTeacher.getSelectedRow();
        teacherId=(int)d.getValueAt(selectedRowIndex,0);

        try {
            pst=con.prepareStatement("select * from teacher where teacher_id=?");

            pst.setInt(1,teacherId);

            rs=pst.executeQuery();

            if(rs.next()) {
                teacherFirstName=rs.getString("teacher_firstname");
                teacherLastName=rs.getString("teacher_lastname");
                teacherQualification=rs.getString("teacher_qualification");
                teacherSalary=rs.getInt("teacher_salary");
                teacherPhone=rs.getString("teacher_phone");
                teacherAddress=rs.getString("teacher_address");

                txtTeacherFirstName.setText(teacherFirstName);
                txtTeacherLastName.setText(teacherLastName);
                txtTeacherQualification.setText(teacherQualification);
                txtTeacherSalary.setText(Integer.toString(teacherSalary));
                txtTeacherPhone.setText(teacherPhone);
                txtTeacherAddress.setText(teacherAddress);
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }       

        btnAdd.setEnabled(false);
    }

    public void initGUI() {

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        setTitle("Teacher");

        setSize(640,640);

        setLayout(null);

        lblTeacherFirstName=new JLabel("Firstname");
        lblTeacherLastName=new JLabel("Lastname");
        lblTeacherQualification=new JLabel("Qualification");
        lblTeacherSalary=new JLabel("Salary");
        lblTeacherPhone=new JLabel("Phone");
        lblTeacherAddress=new JLabel("Address");

        txtTeacherFirstName=new JTextField();
        txtTeacherLastName=new JTextField();
        txtTeacherQualification=new JTextField();
        txtTeacherSalary=new JTextField();
        txtTeacherPhone=new JTextField();
        txtTeacherAddress=new JTextField();

        tblTeacher=new JTable(new DefaultTableModel(tableColumnNames,tableColumnNames.length){
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            }); 

        tblTeacher.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        spTeacher=new JScrollPane(tblTeacher);  

        btnAdd=new JButton("Add");
        btnUpdate=new JButton("Update");
        btnRemove=new JButton("Remove");
        btnClear=new JButton("Clear");
        btnClose=new JButton("Close");

        lblTeacherFirstName.setBounds(16,16,128,32);
        lblTeacherLastName.setBounds(16,64,128,32);
        lblTeacherQualification.setBounds(16,112,128,32);
        lblTeacherSalary.setBounds(16,160,128,32);
        lblTeacherPhone.setBounds(16,208,128,32);
        lblTeacherAddress.setBounds(16,256,128,32);

        txtTeacherFirstName.setBounds(146,16,256,32);
        txtTeacherLastName.setBounds(146,64,256,32);
        txtTeacherQualification.setBounds(146,112,256,32);
        txtTeacherSalary.setBounds(146,160,256,32);
        txtTeacherPhone.setBounds(146,208,256,32);
        txtTeacherAddress.setBounds(146,256,256,32);

        spTeacher.setBounds(16,400,600,200);        

        btnAdd.setBounds(16,352,96,32);
        btnUpdate.setBounds(128,352,96,32);
        btnRemove.setBounds(240,352,96,32);
        btnClear.setBounds(352,352,96,32);
        btnClose.setBounds(464,352,96,32);        

        add(lblTeacherFirstName);
        add(lblTeacherLastName);
        add(lblTeacherQualification);
        add(lblTeacherSalary);
        add(lblTeacherPhone);
        add(lblTeacherAddress);

        add(txtTeacherFirstName);
        add(txtTeacherLastName);
        add(txtTeacherQualification);
        add(txtTeacherSalary);
        add(txtTeacherPhone);
        add(txtTeacherAddress);

        add(spTeacher);

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

        tblTeacher.addMouseListener(new MouseAdapter() {
                public void mouseReleased(MouseEvent e) {
                    tblTeacherMouseReleased(e);                    
                } 
            });

        setLocationRelativeTo(null);   
        setVisible(true);
        setModal(true);

    }
}
