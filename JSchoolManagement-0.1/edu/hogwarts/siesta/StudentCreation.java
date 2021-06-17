/**
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

import com.toedter.calendar.JDateChooser;

public class StudentCreation extends JDialog {
    JLabel lblStudentFirstName;
    JLabel lblStudentLastName;
    JLabel lblStudentDateOfBirth;
    JLabel lblStudentPhone;
    JLabel lblStudentAddress;
    JLabel lblStudentClassName;
    JLabel lblStudentClassSection;

    JTextField txtStudentFirstName;
    JTextField txtStudentLastName;
    JDateChooser dteStudentDateOfBirth;
    JTextField txtStudentPhone;
    JTextField txtStudentAddress;
    JComboBox cboStudentClassName;
    JComboBox cboStudentClassSection;

    boolean cboStudentClassNameItemStateChangeActive=false;

    JTable tblStudent;
    JScrollPane spStudent;

    JButton btnAdd;
    JButton btnUpdate;
    JButton btnRemove;
    JButton btnClear;
    JButton btnClose;

    String[] tableColumnNames={"ID","Firstname","Lastname","Birth","Phone","Address","Class","Section"};

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    DefaultTableModel d;

    int studentId;
    String studentFirstName;
    String studentLastName;
    java.util.Date studentDateOfBirth;
    String studentPhone;
    String studentAddress;
    String studentClassName;
    String studentClassSection;

    StudentCreation() {
        initGUI(); 
        connect();
        clearStudentForm();
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

    public void studentLoad() {
        try {
            pst=con.prepareStatement("select * from student");

            rs=pst.executeQuery();

            d=(DefaultTableModel)tblStudent.getModel();
            d.setRowCount(0);

            while(rs.next()) {
                Vector v=new Vector();

                v.add(rs.getInt("student_id"));
                v.add(rs.getString("student_firstname"));
                v.add(rs.getString("student_lastname"));
                v.add(rs.getString("student_date_of_birth"));                
                v.add(rs.getString("student_phone"));
                v.add(rs.getString("student_address"));
                v.add(rs.getString("student_class_name"));
                v.add(rs.getString("student_class_section"));

                d.addRow(v);
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    } 

    public void classLoad() {
        try {
            cboStudentClassName.removeAllItems();
            pst = con.prepareStatement("select distinct class_name from class");
            rs = pst.executeQuery();
            while(rs.next()) {
                cboStudentClassName.addItem(rs.getString("class_name"));
            }
            cboStudentClassName.setSelectedIndex(-1);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }               
    }

    public void sectionLoad() {
        try {
            cboStudentClassSection.removeAllItems();
            if(cboStudentClassName.getSelectedIndex()==-1) {
                return;
            }
            studentClassName=cboStudentClassName.getSelectedItem().toString();
            if(studentClassName.isEmpty()) {
                return;
            }
            pst = con.prepareStatement("select distinct class_section from class where class_name=?");
            pst.setString(1,studentClassName);
            rs = pst.executeQuery();      
            while(rs.next()) {
                cboStudentClassSection.addItem(rs.getString("class_section"));
            }
            cboStudentClassSection.setSelectedIndex(-1);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }                   
    }

    public void clearStudentForm() { 
        txtStudentFirstName.setText("");
        txtStudentLastName.setText("");
        dteStudentDateOfBirth.setCalendar(null);
        txtStudentPhone.setText("");
        txtStudentAddress.setText("");
        txtStudentFirstName.requestFocus();

        cboStudentClassNameItemStateChangeActive=false;

        classLoad();
        sectionLoad();

        cboStudentClassNameItemStateChangeActive=true;

        studentLoad();
    }

    public boolean isValidStudentForm() {
        studentFirstName = txtStudentFirstName.getText();
        studentLastName = txtStudentLastName.getText();
        studentPhone = txtStudentPhone.getText();
        studentAddress = txtStudentAddress.getText();

        int studentClassNameIndex = cboStudentClassName.getSelectedIndex();
        int studentClassSectionIndex = cboStudentClassSection.getSelectedIndex();

        if(studentFirstName.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Firstname is empty");
            txtStudentFirstName.requestFocus();
            return false;
        }

        if(studentLastName.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Lastname is empty");
            txtStudentLastName.requestFocus();
            return false;
        }
        
        if(dteStudentDateOfBirth.getCalendar()==null) {
            JOptionPane.showMessageDialog(this,"Please select a date of birth");
            dteStudentDateOfBirth.requestFocus();
            return false;
        }
        
        if(studentPhone.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Phone is empty");
            txtStudentPhone.requestFocus();
            return false;
        }

        if(studentAddress.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Address is empty");
            txtStudentAddress.requestFocus();
            return false;
        }

        if(studentClassNameIndex==-1) {
            JOptionPane.showMessageDialog(this,"Please choose a class");
            cboStudentClassName.requestFocus();
            return false;
        }

        if(studentClassSectionIndex==-1) {
            JOptionPane.showMessageDialog(this,"Please choose a section");
            cboStudentClassSection.requestFocus();
            return false;
        }

        studentDateOfBirth=dteStudentDateOfBirth.getDate();
        
        studentClassName=cboStudentClassName.getSelectedItem().toString(); 
        studentClassSection=cboStudentClassSection.getSelectedItem().toString(); 
        
        return true;
    }

    public void btnAddActionPerformed(ActionEvent e) {

        if(!isValidStudentForm()) return;

        try {
            pst = con.prepareStatement("insert into student(student_firstname,student_lastname,student_date_of_birth,student_phone,student_address,student_class_name,student_class_section) values(?,?,?,?,?,?,?)");

            pst.setString(1,studentFirstName);
            pst.setString(2,studentLastName);

            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            String date = df.format(studentDateOfBirth);
            pst.setString(3,date);

            pst.setString(4,studentPhone);
            pst.setString(5,studentAddress);
            pst.setString(6,studentClassName);
            pst.setString(7,studentClassSection);

            pst.executeUpdate();

            clearStudentForm();

            JOptionPane.showMessageDialog(this,"Student added");
            
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    } 

    public void btnUpdateActionPerformed(ActionEvent e) {

        if(tblStudent.getRowCount()==0) {
            JOptionPane.showMessageDialog(this,"No student to select");    
            return;        
        }

        d=(DefaultTableModel)tblStudent.getModel();
        int selectedRowIndex=tblStudent.getSelectedRow();

        if(selectedRowIndex==-1) {
            JOptionPane.showMessageDialog(this,"Please select a student");    
            return;
        }  

        studentId=(int)d.getValueAt(selectedRowIndex,0);

        if(!isValidStudentForm()) return;

        try {
            pst = con.prepareStatement("update student set student_firstname=?,student_lastname=?,student_date_of_birth=?,student_phone=?,student_address=?,student_class_name=?,student_class_section=? where student_id=?");

            pst.setString(1,studentFirstName);
            pst.setString(2,studentLastName);

            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            String date = df.format(studentDateOfBirth);
            pst.setString(3,date);
            
            pst.setString(4,studentPhone);
            pst.setString(5,studentAddress);
            pst.setString(6,studentClassName);
            pst.setString(7,studentClassSection);

            pst.setInt(8,studentId);

            pst.executeUpdate();

            clearStudentForm();

            btnAdd.setEnabled(true);

            JOptionPane.showMessageDialog(this,"Student updated");

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    } 

    public void btnRemoveActionPerformed(ActionEvent e) {

        if(tblStudent.getRowCount()==0) {
            JOptionPane.showMessageDialog(this,"No student to select");    
            return;        
        }

        d=(DefaultTableModel)tblStudent.getModel();
        int selectedRowIndex=tblStudent.getSelectedRow();

        if(selectedRowIndex==-1) {
            JOptionPane.showMessageDialog(this,"Please select a student");    
            return;
        }

        studentId=(int)d.getValueAt(selectedRowIndex,0);

        if (JOptionPane.showConfirmDialog(null, "Are you sure to remove the selected student?", "WARNING",
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            try {
                pst = con.prepareStatement("delete from student where student_id=?");

                pst.setInt(1,studentId);

                pst.executeUpdate();

                clearStudentForm();

                btnAdd.setEnabled(true);

                JOptionPane.showMessageDialog(this,"Student removed");

            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }

        } else {
            JOptionPane.showMessageDialog(this,"Operation Canceled");
        }

    } 

    public void btnClearActionPerformed(ActionEvent e) { 
        clearStudentForm();
        btnAdd.setEnabled(true);        
    }    

    public void btnCloseActionPerformed(ActionEvent e) {        
        dispose();
    }

    public void cboStudentClassNameItemStateChanged(ItemEvent e) {
        if(e.getStateChange() == ItemEvent.SELECTED) {
            studentClassName=e.getItem().toString();
            sectionLoad();
        }
    }

    public void tblStudentMouseReleased(MouseEvent e) {

        d=(DefaultTableModel)tblStudent.getModel();
        int selectedRowIndex=tblStudent.getSelectedRow();
        studentId=(int)d.getValueAt(selectedRowIndex,0);

        try {
            pst=con.prepareStatement("select * from student where student_id=?");

            pst.setInt(1,studentId);

            rs=pst.executeQuery();

            if(rs.next()) {
                
                studentFirstName=rs.getString("student_firstname");
                studentLastName=rs.getString("student_lastname");
                
                try {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                    studentDateOfBirth=df.parse(rs.getString("student_date_of_birth"));
                    dteStudentDateOfBirth.setDate(studentDateOfBirth);
                } catch(ParseException pe) {
                    pe.printStackTrace();
                }
                
                studentPhone=rs.getString("student_phone");
                studentAddress=rs.getString("student_address");
                
                studentClassName=rs.getString("student_class_name");
                studentClassSection=rs.getString("student_class_section");
                
                txtStudentFirstName.setText(studentFirstName);
                txtStudentLastName.setText(studentLastName);
                txtStudentPhone.setText(studentPhone);
                txtStudentAddress.setText(studentAddress);
                
                cboStudentClassNameItemStateChangeActive=false;

                classLoad();
                cboStudentClassName.setSelectedItem(studentClassName);
                sectionLoad();
                cboStudentClassSection.setSelectedItem(studentClassSection);
                
                cboStudentClassNameItemStateChangeActive=true;
                
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }       

        btnAdd.setEnabled(false);
    }

    public void initGUI() {

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        setTitle("Student");

        setSize(640,640);

        setLayout(null);

        lblStudentFirstName=new JLabel("Firstname");
        lblStudentLastName=new JLabel("Lastname");
        lblStudentDateOfBirth=new JLabel("Birthday");
        lblStudentPhone=new JLabel("Phone");
        lblStudentAddress=new JLabel("Address");
        lblStudentClassName=new JLabel("Class");
        lblStudentClassSection=new JLabel("Section");

        txtStudentFirstName=new JTextField();
        txtStudentLastName=new JTextField();
        dteStudentDateOfBirth=new JDateChooser("yyyy/MM/dd","####/##/##",'_');
        txtStudentPhone=new JTextField();
        txtStudentAddress=new JTextField();
        cboStudentClassName=new JComboBox();
        cboStudentClassSection=new JComboBox();

        tblStudent=new JTable(new DefaultTableModel(tableColumnNames,tableColumnNames.length){
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            }); 

        tblStudent.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        spStudent=new JScrollPane(tblStudent);  

        btnAdd=new JButton("Add");
        btnUpdate=new JButton("Update");
        btnRemove=new JButton("Remove");
        btnClear=new JButton("Clear");
        btnClose=new JButton("Close");

        lblStudentFirstName.setBounds(16,16,128,32);
        lblStudentLastName.setBounds(16,64,128,32);
        lblStudentDateOfBirth.setBounds(16,112,128,32);
        lblStudentPhone.setBounds(16,160,128,32);
        lblStudentAddress.setBounds(16,208,128,32);
        lblStudentClassName.setBounds(16,256,228,32);
        lblStudentClassSection.setBounds(16,304,128,32);

        txtStudentFirstName.setBounds(146,16,256,32);
        txtStudentLastName.setBounds(146,64,256,32);
        dteStudentDateOfBirth.setBounds(146,112,256,32);
        txtStudentPhone.setBounds(146,160,256,32);
        txtStudentAddress.setBounds(146,208,256,32);
        cboStudentClassName.setBounds(146,256,256,32);
        cboStudentClassSection.setBounds(146,304,256,32);

        spStudent.setBounds(16,400,600,200);        

        btnAdd.setBounds(16,352,96,32);
        btnUpdate.setBounds(128,352,96,32);
        btnRemove.setBounds(240,352,96,32);
        btnClear.setBounds(352,352,96,32);
        btnClose.setBounds(464,352,96,32);        

        add(lblStudentFirstName);
        add(lblStudentLastName);
        add(lblStudentDateOfBirth);
        add(lblStudentPhone);
        add(lblStudentAddress);
        add(lblStudentClassName);
        add(lblStudentClassSection);

        add(txtStudentFirstName);
        add(txtStudentLastName);
        add(dteStudentDateOfBirth);
        add(txtStudentPhone);
        add(txtStudentAddress);
        add(cboStudentClassName);
        add(cboStudentClassSection);

        add(spStudent);

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

        cboStudentClassName.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if(cboStudentClassNameItemStateChangeActive) {
                        cboStudentClassNameItemStateChanged(e);
                    }
                }
            });

        tblStudent.addMouseListener(new MouseAdapter() {
                public void mouseReleased(MouseEvent e) {
                    tblStudentMouseReleased(e);                    
                } 
            });

        setLocationRelativeTo(null);   
        setVisible(true);
        setModal(true);

    }
}
