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
import java.text.*;

import com.toedter.calendar.JDateChooser;

public class ExamCreation extends JDialog {

    JLabel lblExamName;
    JLabel lblExamTerm;
    JLabel lblExamDate;
    JLabel lblExamClassName;
    JLabel lblExamClassSection;
    JLabel lblExamSubject;

    JTextField txtExamName;
    JComboBox cboExamTerm;
    JDateChooser dteExamDate;
    JComboBox cboExamClassName;
    JComboBox cboExamClassSection;
    JComboBox cboExamSubject;

    boolean cboExamClassNameItemStateChangeActive=false;

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
    String examClassName;
    String examClassSection;
    String examSubject;
    int examId;

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    DefaultTableModel d;

    public ExamCreation() {
        initGUI();
        connect(); 
        clearExamForm();
        cboExamClassNameItemStateChangeActive=true;
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

        try {
            pst=con.prepareStatement("select * from exam");

            rs=pst.executeQuery();

            d=(DefaultTableModel)tblExam.getModel();
            d.setRowCount(0);

            while(rs.next()) {
                Vector v=new Vector();
                v.add(rs.getInt("exam_id"));
                v.add(rs.getString("exam_name"));
                v.add(rs.getString("exam_term"));
                v.add(rs.getString("exam_date"));
                v.add(rs.getString("exam_class_name"));
                v.add(rs.getString("exam_class_section"));
                v.add(rs.getString("exam_subject"));
                d.addRow(v);
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    } 

    public void classLoad() {
        try {
            cboExamClassName.removeAllItems();
            pst = con.prepareStatement("select distinct class_name from class");
            rs = pst.executeQuery();
            while(rs.next()) {
                cboExamClassName.addItem(rs.getString("class_name"));
            }
            cboExamClassName.setSelectedIndex(-1);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }               
    }

    public void sectionLoad() {
        try {
            cboExamClassSection.removeAllItems();
            if(cboExamClassName.getSelectedIndex()==-1) {
                return;
            }
            examClassName=cboExamClassName.getSelectedItem().toString();
            if(examClassName.isEmpty()) {
                return;
            }
            pst = con.prepareStatement("select distinct class_section from class where class_name=?");
            pst.setString(1,examClassName);
            rs = pst.executeQuery();      
            while(rs.next()) {
                cboExamClassSection.addItem(rs.getString("class_section"));
            }
            cboExamClassSection.setSelectedIndex(-1);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }                   
    }

    public void subjectLoad() {
        try {
            cboExamSubject.removeAllItems();
            pst = con.prepareStatement("select distinct subject_name from subject");
            rs = pst.executeQuery();
            while(rs.next()) {
                cboExamSubject.addItem(rs.getString("subject_name"));
            }
            cboExamSubject.setSelectedIndex(-1);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }          
    }

    public void clearExamForm() { 
        txtExamName.setText("");
        cboExamTerm.setSelectedIndex(-1);
        dteExamDate.setCalendar(null);
        txtExamName.requestFocus();

        cboExamClassNameItemStateChangeActive=false;
        
        classLoad();
        sectionLoad();
        subjectLoad();
        
        cboExamClassNameItemStateChangeActive=true;
        
        examLoad();
    }

    public boolean isValidExamForm() {
        examName=txtExamName.getText();

        int examTermIndex=cboExamTerm.getSelectedIndex();
        int examClassNameIndex=cboExamClassName.getSelectedIndex();
        int examClassSectionIndex=cboExamClassSection.getSelectedIndex();
        int examSubjectIndex=cboExamSubject.getSelectedIndex();      

        if(examName.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Please enter an exam name");
            txtExamName.requestFocus();
            return false;
        }

        if(examTermIndex==-1) {
            JOptionPane.showMessageDialog(this,"Please select a term");
            cboExamTerm.requestFocus();
            return false;
        }

        if(dteExamDate.getCalendar()==null) {
            JOptionPane.showMessageDialog(this,"Please select a date");
            dteExamDate.requestFocus();
            return false;
        }

        if(examClassNameIndex==-1) {
            JOptionPane.showMessageDialog(this,"Please select a class");
            cboExamClassName.requestFocus();
            return false;
        }

        if(examClassSectionIndex==-1) {
            JOptionPane.showMessageDialog(this,"Please select a section");
            cboExamClassSection.requestFocus();
            return false;
        }

        if(examSubjectIndex==-1) {
            JOptionPane.showMessageDialog(this,"Please select a subject");
            cboExamSubject.requestFocus();
            return false;
        }

        examDate=dteExamDate.getDate();
        examTerm=cboExamTerm.getSelectedItem().toString(); 
        examClassName=cboExamClassName.getSelectedItem().toString(); 
        examClassSection=cboExamClassSection.getSelectedItem().toString(); 
        examSubject=cboExamSubject.getSelectedItem().toString(); 

        return true;
    }

    public void btnAddActionPerformed(ActionEvent e) {
        if(!isValidExamForm()) return;
        try {
            pst = con.prepareStatement("insert into exam(exam_name,exam_term,exam_date,exam_class_name,exam_class_section,exam_subject) values(?,?,?,?,?,?)");

            pst.setString(1,examName);
            pst.setString(2,examTerm);

            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            String date = df.format(examDate);

            pst.setString(3,date);

            pst.setString(4,examClassName);
            pst.setString(5,examClassSection);
            pst.setString(6,examSubject);

            pst.executeUpdate();

            clearExamForm();

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
            pst = con.prepareStatement("update exam set exam_name=?,exam_term=?,exam_date=?,exam_class_name=?,exam_class_section=?,exam_subject=? where exam_id=?");

            pst.setString(1,examName);
            pst.setString(2,examTerm);

            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            String date = df.format(examDate);            
            pst.setString(3,date);

            pst.setString(4,examClassName);
            pst.setString(5,examClassSection);
            pst.setString(6,examSubject);
            pst.setInt(7,examId);

            pst.executeUpdate();

            clearExamForm();

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
        btnAdd.setEnabled(true);        
    }    

    public void btnCloseActionPerformed(ActionEvent e) {        
        dispose();
    }

    public void cboExamClassNameItemStateChanged(ItemEvent e) {
        if(e.getStateChange() == ItemEvent.SELECTED) {
            examClassName=e.getItem().toString();
            sectionLoad();
        }
    }

    public void tblExamMouseReleased(MouseEvent e) {

        d=(DefaultTableModel)tblExam.getModel();
        int selectedIndex=tblExam.getSelectedRow();
        examId=(int)d.getValueAt(selectedIndex,0);

        try {
            pst=con.prepareStatement("select * from exam where exam_id=?");

            pst.setInt(1,examId);

            rs=pst.executeQuery();

            if(rs.next()) {
                examName=rs.getString("exam_name");  
                examTerm=rs.getString("exam_term"); 

                try {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                    examDate=df.parse(rs.getString("exam_date"));
                    dteExamDate.setDate(examDate);
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }

                examClassName=rs.getString("exam_class_name");
                examClassSection=rs.getString("exam_class_section");  
                examSubject=rs.getString("exam_subject"); 

                txtExamName.setText(examName);
                cboExamTerm.setSelectedItem(examTerm);
                

                cboExamClassNameItemStateChangeActive=false;

                classLoad();
                cboExamClassName.setSelectedItem(examClassName);
                sectionLoad();
                cboExamClassSection.setSelectedItem(examClassSection);
                subjectLoad();
                cboExamSubject.setSelectedItem(examSubject);

                cboExamClassNameItemStateChangeActive=true;
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
        lblExamClassName=new JLabel("Class");
        lblExamClassSection=new JLabel("Section");
        lblExamSubject=new JLabel("Subject");

        txtExamName=new JTextField();
        cboExamTerm=new JComboBox(examTerms);
        dteExamDate=new JDateChooser("yyyy/MM/dd","####/##/##",'_');
        cboExamClassName=new JComboBox();
        cboExamClassSection=new JComboBox();
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
        lblExamClassName.setBounds(16,160,128,32);
        lblExamClassSection.setBounds(16,208,128,32);
        lblExamSubject.setBounds(16,256,128,32);

        txtExamName.setBounds(160,16,256,32);
        cboExamTerm.setBounds(160,64,256,32);
        dteExamDate.setBounds(160,112,256,32);
        cboExamClassName.setBounds(160,160,256,32);
        cboExamClassSection.setBounds(160,208,256,32);
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
        add(lblExamClassName);
        add(lblExamClassSection);
        add(lblExamSubject);

        add(txtExamName);
        add(cboExamTerm);
        add(dteExamDate);
        add(cboExamClassName);
        add(cboExamClassSection);
        add(cboExamSubject);

        add(spExam);

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

        cboExamClassName.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if(cboExamClassNameItemStateChangeActive) {
                        cboExamClassNameItemStateChanged(e);
                    }
                }
            });

        tblExam.addMouseListener(new MouseAdapter() {
                public void mouseReleased(MouseEvent e) {
                    tblExamMouseReleased(e);                    
                } 
            });

        setLocationRelativeTo(null);
        setVisible(true);
        setModal(true);
    }
}
