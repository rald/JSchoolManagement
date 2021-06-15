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

public class SubjectCreation extends JDialog {

    JLabel lblSubjectName;

    JTextField txtSubjectName;

    JTable tblSubject;
    JScrollPane spSubject;

    JButton btnAdd;
    JButton btnUpdate;
    JButton btnRemove;
    JButton btnClear;
    JButton btnClose;

    String[] subjectTableColumnNames={"ID","Subject Name"};

    String subjectName;
    int subjectId;

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    DefaultTableModel d;

    public SubjectCreation() {
        initGUI();
        connect();
        subjectLoad();
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

    public void subjectLoad() {
        int c;
        try {
            pst=con.prepareStatement("select * from subject");

            rs=pst.executeQuery();

            ResultSetMetaData rsd=rs.getMetaData();
            c=rsd.getColumnCount();
            d=(DefaultTableModel)tblSubject.getModel();
            d.setRowCount(0);

            while(rs.next()) {
                Vector v=new Vector();
                for(int i=0;i<c;i++) {
                    v.add(rs.getInt("subject_id"));
                    v.add(rs.getString("subject_name"));
                }

                d.addRow(v);
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    } 

    public void clearSubjectForm() { 
        txtSubjectName.setText("");
        txtSubjectName.requestFocus();
    }

    public boolean isValidSubjectForm() {
        subjectName=txtSubjectName.getText();

        if(subjectName.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Please enter a subject");
            txtSubjectName.requestFocus();
            return false;
        }

        return true;
    }

    public void btnAddActionPerformed(ActionEvent e) {

        if(!isValidSubjectForm()) return;

        try {
            pst = con.prepareStatement("insert into subject(subject_name) values(?)");

            pst.setString(1,subjectName);

            pst.executeUpdate();

            clearSubjectForm();

            subjectLoad();

            JOptionPane.showMessageDialog(this,"Subject added");

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    } 

    public void btnUpdateActionPerformed(ActionEvent e) {

        if(tblSubject.getRowCount()==0) {
            JOptionPane.showMessageDialog(this,"No subject to select");    
            return;        
        }

        d=(DefaultTableModel)tblSubject.getModel();
        int selectedRowIndex=tblSubject.getSelectedRow();

        if(selectedRowIndex==-1) {
            JOptionPane.showMessageDialog(this,"Please select a subject");    
            return;
        }  

        subjectId=(int)d.getValueAt(selectedRowIndex,0);

        if(!isValidSubjectForm()) return;

        try {
            pst = con.prepareStatement("update subject set subject_name=? where subject_id=?");

            pst.setString(1,subjectName);
            pst.setInt(2,subjectId);

            pst.executeUpdate();

            clearSubjectForm();

            subjectLoad();

            btnAdd.setEnabled(true);

            JOptionPane.showMessageDialog(this,"Subject updated");

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    } 

    public void btnRemoveActionPerformed(ActionEvent e) {

        if(tblSubject.getRowCount()==0) {
            JOptionPane.showMessageDialog(this,"No subject to select");    
            return;        
        }

        d=(DefaultTableModel)tblSubject.getModel();
        int selectedRowIndex=tblSubject.getSelectedRow();

        if(selectedRowIndex==-1) {
            JOptionPane.showMessageDialog(this,"Please select a subject");    
            return;
        }

        subjectId=(int)d.getValueAt(selectedRowIndex,0);

        if (JOptionPane.showConfirmDialog(null, "Are you sure to remove the subject?", "WARNING",
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            try {
                pst = con.prepareStatement("delete from subject where subject_id=?");

                pst.setInt(1,subjectId);

                pst.executeUpdate();

                clearSubjectForm();

                subjectLoad();

                btnAdd.setEnabled(true);

                JOptionPane.showMessageDialog(this,"Subject removed");

            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }

        } else {
            JOptionPane.showMessageDialog(this,"Operation Canceled");
        }

    } 

    public void btnClearActionPerformed(ActionEvent e) { 
        clearSubjectForm();
        subjectLoad();
        btnAdd.setEnabled(true);        
    }    

    public void btnCloseActionPerformed(ActionEvent e) {        
        dispose();
    }

    public void tblSubjectMouseReleased(MouseEvent e) {
        
        d=(DefaultTableModel)tblSubject.getModel();
        int selectedIndex=tblSubject.getSelectedRow();
        subjectId=(int)d.getValueAt(selectedIndex,0);

        int c;
        try {
            pst=con.prepareStatement("select * from subject where subject_id=?");

            pst.setInt(1,subjectId);

            rs=pst.executeQuery();

            ResultSetMetaData rsd=rs.getMetaData();

            if(rs.next()) {
                txtSubjectName.setText(rs.getString("subject_name"));  
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }       

        btnAdd.setEnabled(false);
    }

    public void initGUI() {

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        setTitle("Subject");

        setSize(640,640);

        setLayout(null);

        lblSubjectName=new JLabel("Subject");

        txtSubjectName=new JTextField();

        tblSubject=new JTable(new DefaultTableModel(subjectTableColumnNames,subjectTableColumnNames.length) {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });

        tblSubject.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        spSubject=new JScrollPane(tblSubject);  

        btnAdd=new JButton("Add");
        btnUpdate=new JButton("Update");
        btnRemove=new JButton("Remove");
        btnClear=new JButton("Clear");
        btnClose=new JButton("Close");

        lblSubjectName.setBounds(16,16,128,32);

        txtSubjectName.setBounds(160,16,256,32);

        spSubject.setBounds(16,400,600,200);        

        btnAdd.setBounds(16,352,96,32);
        btnUpdate.setBounds(128,352,96,32);
        btnRemove.setBounds(240,352,96,32);
        btnClear.setBounds(352,352,96,32);
        btnClose.setBounds(464,352,96,32);        

        add(lblSubjectName);

        add(txtSubjectName);

        add(spSubject);

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

        tblSubject.addMouseListener(new MouseAdapter() {
                public void mouseReleased(MouseEvent e) {
                    tblSubjectMouseReleased(e);                    
                } 
            });

        clearSubjectForm();

        setLocationRelativeTo(null);

        setVisible(true);

        setModal(true);
    }
}
