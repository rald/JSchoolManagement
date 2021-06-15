/**
 * Write a description of class User here.
 *
 * @author Gerald Q. Cruz
 * @version Sun., Jun. 13, 2021 - 5:59 PM
 */

package edu.hogwarts.siesta;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import com.toedter.calendar.JDateChooser;

public class ExamCreation extends JDialog {

    JLabel lblExamName;
    JLabel lblExamTerm;
    JLabel lblExamDate;
    JLabel lblExamClass;
    JLabel lblExamSection;
    JLabel lblExamSubject;

    JTextField txtExamName;
    JComboBox cboExamTerm;
    JDateChooser dteExamDate;
    JComboBox cboExamClass;
    JComboBox cboExamSection;
    JComboBox cboExamSubject;

    JTable tblExam;
    JScrollPane spExam;

    JButton btnAdd;
    JButton btnUpdate;
    JButton btnRemove;
    JButton btnClear;
    JButton btnClose;

    String[] examTableColumnNames={"ID","Exam Name","Term","Date","Class","Section","Subject"};

    String[] examTerms={"1st","2nd","3rd","4th","5th"};

    String examName;
    String examTerm;
    java.util.Date examDate;
    String examClass;
    String examSection;
    String examSubject;
    int examId;

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    DefaultTableModel d;

    public ExamCreation() {
        initGUI();
        connect();
        examLoad();
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

    public void examLoad() {
        int c;
        try {
            pst=con.prepareStatement("select * from exam");

            rs=pst.executeQuery();

            ResultSetMetaData rsd=rs.getMetaData();
            c=rsd.getColumnCount();
            d=(DefaultTableModel)tblExam.getModel();
            d.setRowCount(0);

            while(rs.next()) {
                Vector v=new Vector();
                for(int i=0;i<c;i++) {
                    v.add(rs.getInt("exam_id"));
                    v.add(rs.getString("exam_name"));
                }

                d.addRow(v);
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    } 

    public void classLoad() {
        try {
            pst = con.prepareStatement("select distinct class_name from class");
            rs = pst.executeQuery();
            cboExamClass.removeAllItems();
            while(rs.next()) {
                cboExamClass.addItem(rs.getString("class_name"));
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }               
    }

    public void sectionLoad() {
        try {
            pst = con.prepareStatement("select distinct class_section from class");
            rs = pst.executeQuery();
            cboExamSection.removeAllItems();
            while(rs.next()) {
                cboExamSection.addItem(rs.getString("class_section"));
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }                   
    }

    public void subjectLoad() {

    }

    public void clearExamForm() { 
        txtExamName.setText("");
        cboExamTerm.setSelectedIndex(-1);
        dteExamDate.setCalendar(null);
        cboExamClass.setSelectedIndex(-1);
        cboExamSection.setSelectedIndex(-1);
        cboExamSubject.setSelectedIndex(-1);

        txtExamName.requestFocus();
    }

    public boolean isValidExamForm() {
        examName=txtExamName.getText();

        if(examName.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Please enter an exam");
            txtExamName.requestFocus();
            return false;
        }

        return true;
    }

    public void btnAddActionPerformed(ActionEvent e) {

        if(!isValidExamForm()) return;

        try {
            pst = con.prepareStatement("insert into exam(exam_name) values(?)");

            pst.setString(1,examName);

            pst.executeUpdate();

            clearExamForm();

            examLoad();

            JOptionPane.showMessageDialog(this,"Exam added");

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    } 

    public void btnUpdateActionPerformed(ActionEvent e) {

        if(tblExam.getRowCount()==0) {
            JOptionPane.showMessageDialog(this,"No exam to select");    
            return;        
        }

        d=(DefaultTableModel)tblExam.getModel();
        int selectedRowIndex=tblExam.getSelectedRow();

        if(selectedRowIndex==-1) {
            JOptionPane.showMessageDialog(this,"Please select an exam");    
            return;
        }  

        examId=(int)d.getValueAt(selectedRowIndex,0);

        if(!isValidExamForm()) return;

        try {
            pst = con.prepareStatement("update exam set exam_name=? where exam_id=?");

            pst.setString(1,examName);
            pst.setInt(2,examId);

            pst.executeUpdate();

            clearExamForm();

            examLoad();

            btnAdd.setEnabled(true);

            JOptionPane.showMessageDialog(this,"Exam updated");

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    } 

    public void btnRemoveActionPerformed(ActionEvent e) {

        if(tblExam.getRowCount()==0) {
            JOptionPane.showMessageDialog(this,"No exam to select");    
            return;        
        }

        d=(DefaultTableModel)tblExam.getModel();
        int selectedRowIndex=tblExam.getSelectedRow();

        if(selectedRowIndex==-1) {
            JOptionPane.showMessageDialog(this,"Please select an exam");    
            return;
        }

        examId=(int)d.getValueAt(selectedRowIndex,0);

        if (JOptionPane.showConfirmDialog(null, "Are you sure to remove the selected exam?", "WARNING",
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            try {
                pst = con.prepareStatement("delete from exam where exam_id=?");

                pst.setInt(1,examId);

                pst.executeUpdate();

                clearExamForm();

                examLoad();

                btnAdd.setEnabled(true);

                JOptionPane.showMessageDialog(this,"Exam removed");

            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }

        } else {
            JOptionPane.showMessageDialog(this,"Operation Canceled");
        }

    } 

    public void btnClearActionPerformed(ActionEvent e) { 
        clearExamForm();
        examLoad();
        btnAdd.setEnabled(true);        
    }    

    public void btnCloseActionPerformed(ActionEvent e) {        
        dispose();
    }

    public void cboExamClassItemStateChanged(ItemEvent e) {
        if(e.getStateChange() == ItemEvent.SELECTED) {
            Object item = e.getItem();
            examClass=((JComboBox)item).getSelectedItem().toString();
            sectionLoad();
        }
    }

    public void tblExamMouseReleased(MouseEvent e) {

        d=(DefaultTableModel)tblExam.getModel();
        int selectedIndex=tblExam.getSelectedRow();
        examId=(int)d.getValueAt(selectedIndex,0);

        int c;
        try {
            pst=con.prepareStatement("select * from exam where exam_id=?");

            pst.setInt(1,examId);

            rs=pst.executeQuery();

            ResultSetMetaData rsd=rs.getMetaData();

            if(rs.next()) {
                txtExamName.setText(rs.getString("exam_name"));  
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }       

        btnAdd.setEnabled(false);
    }

    public void initGUI() {

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        setTitle("Exam");

        setSize(640,640);

        setLayout(null);

        lblExamName=new JLabel("Exam Name");
        lblExamTerm=new JLabel("Term");
        lblExamDate=new JLabel("Date");
        lblExamClass=new JLabel("Class");
        lblExamSection=new JLabel("Section");
        lblExamSubject=new JLabel("Subject");

        txtExamName=new JTextField();
        cboExamTerm=new JComboBox(examTerms);
        dteExamDate=new JDateChooser("yyyy/MM/dd","####/##/##",'_');

        cboExamClass=new JComboBox();
        cboExamSection=new JComboBox();
        cboExamSubject=new JComboBox();

        tblExam=new JTable(new DefaultTableModel(examTableColumnNames,examTableColumnNames.length) {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });

        tblExam.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        spExam=new JScrollPane(tblExam);  

        btnAdd=new JButton("Add");
        btnUpdate=new JButton("Update");
        btnRemove=new JButton("Remove");
        btnClear=new JButton("Clear");
        btnClose=new JButton("Close");

        lblExamName.setBounds(16,16,128,32);
        lblExamTerm.setBounds(16,64,128,32);
        lblExamDate.setBounds(16,112,128,32);
        lblExamClass.setBounds(16,160,128,32);
        lblExamSection.setBounds(16,208,128,32);
        lblExamSubject.setBounds(16,256,128,32);

        txtExamName.setBounds(160,16,256,32);
        cboExamTerm.setBounds(160,64,256,32);
        dteExamDate.setBounds(160,112,256,32);
        cboExamClass.setBounds(160,160,256,32);
        cboExamSection.setBounds(160,208,256,32);
        cboExamSubject.setBounds(160,256,256,32);

        spExam.setBounds(16,400,600,200);        

        btnAdd.setBounds(16,352,96,32);
        btnUpdate.setBounds(128,352,96,32);
        btnRemove.setBounds(240,352,96,32);
        btnClear.setBounds(352,352,96,32);
        btnClose.setBounds(464,352,96,32);        

        add(lblExamName);
        add(lblExamTerm);
        add(lblExamDate);
        add(lblExamClass);
        add(lblExamSection);
        add(lblExamSubject);

        add(txtExamName);
        add(cboExamTerm);
        add(dteExamDate);
        add(cboExamClass);
        add(cboExamSection);
        add(cboExamSubject);

        add(spExam);

        add(btnAdd);
        add(btnUpdate);
        add(btnRemove);
        add(btnClear);
        add(btnClose);

        classLoad();

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

        cboExamClass.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    cboExamClassItemStateChanged(e);                    
                } 
            });

        tblExam.addMouseListener(new MouseAdapter() {
                public void mouseReleased(MouseEvent e) {
                    tblExamMouseReleased(e);                    
                } 
            });

        clearExamForm();

        setLocationRelativeTo(null);

        setVisible(true);

        setModal(true);
    }
}
