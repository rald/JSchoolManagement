/**
 *
 * @author Gerald Q. Cruz
 * @version Thu., Jun. 17, 2021 - 3:57 AM
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

public class MarkCreation extends JDialog {
    JLabel lblMarkStudentID;
    JLabel lblMarkStudentFirstName;
    JLabel lblMarkStudentLastName;
    JLabel lblMarkClassName;
    JLabel lblMarkClassSection;
    JLabel lblMarkSubject;
    JLabel lblMarkGrade;

    JTextField txtMarkStudentID;
    JTextField txtMarkStudentFirstName;
    JTextField txtMarkStudentLastName;
    JComboBox cboMarkClassName;
    JComboBox cboMarkClassSection;
    JComboBox cboMarkSubject;
    JTextField txtMarkGrade;
    JButton btnSearch;

    boolean cboMarkClassNameItemStateChangeActive=false;

    JTable tblMark;
    JScrollPane spMark;

    JButton btnAdd;
    JButton btnUpdate;
    JButton btnRemove;
    JButton btnClear;
    JButton btnClose;

    String[] tableColumnNames={"ID","Student ID","Frstname","Lastname","Class","Section","Subject","Grade"};

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    DefaultTableModel d;

    int markID;
    String markStudentID;
    String markStudentFirstName;
    String markStudentLastName;
    String markClassName;
    String markClassSection;
    String markSubject;
    String markGrade;
    int markClassNameIndex;
    int markClassSectionIndex;
    int markSubjectIndex;

    MarkCreation() {
        initGUI();
        connect();
        clearMarkForm();
        cboMarkClassNameItemStateChangeActive=true;
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

    public void markLoad() {
        try {
            pst=con.prepareStatement("select * from mark");

            rs=pst.executeQuery();

            d=(DefaultTableModel)tblMark.getModel();
            d.setRowCount(0);

            while(rs.next()) {
                Vector v=new Vector();

                v.add(rs.getInt("mark_id"));
                v.add(rs.getInt("mark_student_id"));
                v.add(rs.getString("mark_student_firstname"));
                v.add(rs.getString("mark_student_lastname"));
                v.add(rs.getString("mark_class_name"));
                v.add(rs.getString("mark_class_section"));
                v.add(rs.getString("mark_subject"));
                v.add(rs.getString("mark_grade"));

                d.addRow(v);
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    } 

    public void classLoad() {
        try {
            cboMarkClassName.removeAllItems();
            pst = con.prepareStatement("select distinct class_name from class");
            rs = pst.executeQuery();
            while(rs.next()) {
                cboMarkClassName.addItem(rs.getString("class_name"));
            }
            cboMarkClassName.setSelectedIndex(-1);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }               
    }

    public void sectionLoad() {
        try {
            cboMarkClassSection.removeAllItems();
            if(cboMarkClassName.getSelectedIndex()==-1) {
                return;
            }
            markClassName=cboMarkClassName.getSelectedItem().toString();
            if(markClassName.isEmpty()) {
                return;
            }
            pst = con.prepareStatement("select distinct class_section from class where class_name=?");
            pst.setString(1,markClassName);
            rs = pst.executeQuery();      
            while(rs.next()) {
                cboMarkClassSection.addItem(rs.getString("class_section"));
            }
            cboMarkClassSection.setSelectedIndex(-1);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }                   
    }

    public void subjectLoad() {
        try {
            cboMarkSubject.removeAllItems();
            pst = con.prepareStatement("select distinct subject_name from subject");
            rs = pst.executeQuery();
            while(rs.next()) {
                cboMarkSubject.addItem(rs.getString("subject_name"));
            }
            cboMarkSubject.setSelectedIndex(-1);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }          
    }

    public void clearMarkForm() { 
        txtMarkStudentID.setText("");
        txtMarkStudentFirstName.setText("");
        txtMarkStudentLastName.setText("");
        txtMarkGrade.setText("");

        txtMarkStudentID.requestFocus();

        cboMarkClassNameItemStateChangeActive=false;

        classLoad();
        sectionLoad();
        subjectLoad();

        cboMarkClassNameItemStateChangeActive=true;

        markLoad();
    }

    public boolean isValidMarkForm() {
        markStudentID = txtMarkStudentID.getText();
        markStudentFirstName = txtMarkStudentFirstName.getText();
        markStudentLastName = txtMarkStudentLastName.getText();
        markClassNameIndex = cboMarkClassName.getSelectedIndex();
        markClassSectionIndex = cboMarkClassSection.getSelectedIndex();
        markSubjectIndex = cboMarkSubject.getSelectedIndex();
        markGrade = txtMarkGrade.getText();

        if(markStudentID.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Student ID is empty");
            txtMarkStudentID.requestFocus();
            return false;
        }

        if(markStudentFirstName.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Student firstname is empty");
            txtMarkStudentFirstName.requestFocus();
            return false;
        }

        if(markStudentLastName.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Student lastname is empty");
            txtMarkStudentLastName.requestFocus();
            return false;
        }

        if(markClassNameIndex==-1) {
            JOptionPane.showMessageDialog(this,"Please select a class");
            txtMarkStudentID.requestFocus();
            return false;
        }

        if(markClassSectionIndex==-1) {
            JOptionPane.showMessageDialog(this,"Please select a section");
            txtMarkStudentID.requestFocus();
            return false;
        }

        if(markSubjectIndex==-1) {
            JOptionPane.showMessageDialog(this,"Please select a subject");
            txtMarkStudentID.requestFocus();
            return false;
        }

        if(markGrade.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Grade is empty");
            txtMarkGrade.requestFocus();
            return false;
        }

        return true;
    }

    public void btnAddActionPerformed(ActionEvent e) {

        if(!isValidMarkForm()) return;

        try {
            pst = con.prepareStatement("insert into mark(mark_student_id,mark_student_firstname,mark_student_lastname,mark_class_name,mark_class_section,mark_subject,mark_grade) values(?,?,?,?,?,?,?)");

            pst.setString(1,markStudentID);
            pst.setString(2,markStudentFirstName);
            pst.setString(3,markStudentLastName);
            pst.setString(4,markClassName);
            pst.setString(5,markClassSection);
            pst.setString(6,markSubject);
            pst.setString(7,markGrade);

            pst.executeUpdate();

            clearMarkForm();

            JOptionPane.showMessageDialog(this,"Mark added");

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    } 

    public void btnUpdateActionPerformed(ActionEvent e) {

        if(tblMark.getRowCount()==0) {
            JOptionPane.showMessageDialog(this,"No mark to select");    
            return;        
        }

        d=(DefaultTableModel)tblMark.getModel();
        int selectedRowIndex=tblMark.getSelectedRow();

        if(selectedRowIndex==-1) {
            JOptionPane.showMessageDialog(this,"Please select a mark");    
            return;
        }  

        markID=(int)d.getValueAt(selectedRowIndex,0);

        if(!isValidMarkForm()) return;

        try {
            pst = con.prepareStatement("update mark set mark_student_id=?,mark_student_firstname=?,mark_student_lastname=?,mark_class_name=?,mark_class_section=?,mark_subject=?,mark_grade=? where mark_id=?");

            pst.setString(1,markStudentID);
            pst.setString(2,markStudentFirstName);
            pst.setString(3,markStudentLastName);
            pst.setString(4,markClassName);
            pst.setString(5,markClassSection);
            pst.setString(6,markSubject);
            pst.setString(7,markGrade);
            pst.setInt(8,markID);

            pst.executeUpdate();

            clearMarkForm();

            btnAdd.setEnabled(true);

            JOptionPane.showMessageDialog(this,"Mark updated");

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    } 

    public void btnRemoveActionPerformed(ActionEvent e) {

        if(tblMark.getRowCount()==0) {
            JOptionPane.showMessageDialog(this,"No mark to select");    
            return;        
        }

        d=(DefaultTableModel)tblMark.getModel();
        int selectedRowIndex=tblMark.getSelectedRow();

        if(selectedRowIndex==-1) {
            JOptionPane.showMessageDialog(this,"Please select a mark");    
            return;
        }

        markID=(int)d.getValueAt(selectedRowIndex,0);

        if (JOptionPane.showConfirmDialog(null, "Are you sure to remove the selected mark?", "WARNING",
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            try {
                pst = con.prepareStatement("delete from mark where mark_id=?");

                pst.setInt(1,markID);

                pst.executeUpdate();

                clearMarkForm();

                btnAdd.setEnabled(true);

                JOptionPane.showMessageDialog(this,"Mark removed");

            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }

        } else {
            JOptionPane.showMessageDialog(this,"Operation Canceled");
        }

    } 

    public void btnClearActionPerformed(ActionEvent e) { 
        clearMarkForm();
        btnAdd.setEnabled(true);        
    }    

    public void btnCloseActionPerformed(ActionEvent e) {        
        dispose();
    }

    public void tblMarkMouseReleased(MouseEvent e) {

        d=(DefaultTableModel)tblMark.getModel();
        int selectedRowIndex=tblMark.getSelectedRow();
        markID=(int)d.getValueAt(selectedRowIndex,0);

        try {
            pst=con.prepareStatement("select * from mark where mark_id=?");

            pst.setInt(1,markID);

            rs=pst.executeQuery();

            if(rs.next()) {
                markStudentID=rs.getString("mark_student_id");
                markStudentFirstName=rs.getString("mark_student_firstname");
                markStudentLastName=rs.getString("mark_student_lastname");
                markClassName=rs.getString("mark_class_name");
                markClassSection=rs.getString("mark_class_section");
                markSubject=rs.getString("mark_subject");
                markGrade=rs.getString("mark_grade");

                txtMarkStudentID.setText(markStudentID);
                txtMarkStudentFirstName.setText(markStudentFirstName);
                txtMarkStudentLastName.setText(markStudentLastName);
                
                cboMarkClassNameItemStateChangeActive=false;

                classLoad();
                cboMarkClassName.setSelectedItem(markClassName);
                sectionLoad();
                cboMarkClassSection.setSelectedItem(markClassSection);
                subjectLoad();
                cboMarkSubject.setSelectedItem(markSubject);

                cboMarkClassNameItemStateChangeActive=true;
                
                txtMarkGrade.setText(markGrade);
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }       

        btnAdd.setEnabled(false);
    }

    public void initGUI() {

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        setTitle("Mark");

        setSize(640,640);

        setLayout(null);

        lblMarkStudentID=new JLabel("Student ID");
        lblMarkStudentFirstName=new JLabel("Firstname");
        lblMarkStudentLastName=new JLabel("Lastname");
        lblMarkClassName=new JLabel("Class");
        lblMarkClassSection=new JLabel("Section");
        lblMarkSubject=new JLabel("Subject");
        lblMarkGrade=new JLabel("Grade");

        txtMarkStudentID=new JTextField();
        txtMarkStudentFirstName=new JTextField();
        txtMarkStudentLastName=new JTextField();
        cboMarkClassName=new JComboBox();
        cboMarkClassSection=new JComboBox();       
        cboMarkSubject=new JComboBox();  
        txtMarkGrade=new JTextField();

        btnSearch=new JButton("Search");

        tblMark=new JTable(new DefaultTableModel(tableColumnNames,tableColumnNames.length){
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            }); 

        tblMark.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        spMark=new JScrollPane(tblMark);  

        btnAdd=new JButton("Add");
        btnUpdate=new JButton("Update");
        btnRemove=new JButton("Remove");
        btnClear=new JButton("Clear");
        btnClose=new JButton("Close");

        lblMarkStudentID.setBounds(16,16,128,32);
        lblMarkStudentFirstName.setBounds(16,64,128,32);
        lblMarkStudentLastName.setBounds(16,112,128,32);
        lblMarkClassName.setBounds(16,160,128,32);
        lblMarkClassSection.setBounds(16,208,128,32);
        lblMarkSubject.setBounds(16,256,128,32);
        lblMarkGrade.setBounds(16,304,128,32);

        txtMarkStudentID.setBounds(146,16,256,32);
        txtMarkStudentFirstName.setBounds(146,64,256,32);
        txtMarkStudentLastName.setBounds(146,112,256,32);
        cboMarkClassName.setBounds(146,160,256,32);
        cboMarkClassSection.setBounds(146,208,256,32);
        cboMarkSubject.setBounds(146,256,256,32);
        txtMarkGrade.setBounds(146,304,256,32);

        btnSearch.setBounds(418,16,96,32);

        spMark.setBounds(16,400,600,200);        

        btnAdd.setBounds(16,352,96,32);
        btnUpdate.setBounds(128,352,96,32);
        btnRemove.setBounds(240,352,96,32);
        btnClear.setBounds(352,352,96,32);
        btnClose.setBounds(464,352,96,32);        

        add(lblMarkStudentID);
        add(lblMarkStudentFirstName);
        add(lblMarkStudentLastName);
        add(lblMarkClassName);
        add(lblMarkClassSection);
        add(lblMarkSubject);
        add(lblMarkGrade);

        add(txtMarkStudentID);
        add(txtMarkStudentFirstName);
        add(txtMarkStudentLastName);
        add(cboMarkClassName);
        add(cboMarkClassSection);
        add(cboMarkSubject);
        add(txtMarkGrade);
        add(btnSearch);

        add(spMark);

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

        tblMark.addMouseListener(new MouseAdapter() {
                public void mouseReleased(MouseEvent e) {
                    tblMarkMouseReleased(e);                    
                } 
            });

        setLocationRelativeTo(null);   
        setVisible(true);
        setModal(true);

    }
}
