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

public class ClassCreation extends JDialog {

    JLabel lblClassName;
    JLabel lblClassSection;

    JComboBox cboClassName;
    JComboBox cboClassSection;

    JTable tblClass;
    JScrollPane spClass;

    JButton btnAdd;
    JButton btnUpdate;
    JButton btnRemove;
    JButton btnClear;
    JButton btnClose;

    String[] classNames={"1","2","3","4","5","6","7","8","9","10","11","12","13"};

    String[] sectionNames={"A","B","C","D","E"};

    String[] classTableColumnNames={"ID","Class Name","Section"};

    String className;
    String classSection;
    int classId;

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    DefaultTableModel d;

    public ClassCreation() {
        initGUI();
        connect();
        classLoad();
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

    public void classLoad() {
        try {
            pst=con.prepareStatement("select * from class");

            rs=pst.executeQuery();

            d=(DefaultTableModel)tblClass.getModel();
            d.setRowCount(0);

            while(rs.next()) {
                Vector v = new Vector();

                v.add(rs.getInt("class_id"));
                v.add(rs.getString("class_name"));
                v.add(rs.getString("class_section"));

                d.addRow(v);
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    } 

    public void clearClassForm() { 
        cboClassName.setSelectedIndex(-1);
        cboClassSection.setSelectedIndex(-1);
        cboClassName.requestFocus();
    }

    public boolean isValidClassForm() {
        int classNameIndex = cboClassName.getSelectedIndex();
        int classSectionIndex = cboClassSection.getSelectedIndex();

        if(classNameIndex==-1) {
            JOptionPane.showMessageDialog(this,"Please choose a class");
            cboClassName.requestFocus();
            return false;
        }

        if(classSectionIndex==-1) {
            JOptionPane.showMessageDialog(this,"Please choose a section");
            cboClassSection.requestFocus();
            return false;
        }

        className = cboClassName.getSelectedItem().toString();
        classSection = cboClassSection.getSelectedItem().toString();

        return true;
    }

    public boolean isValidClass() {
        try {
            pst=con.prepareStatement("select * from class where class_name=? and class_section=?");
            pst.setString(1,className);
            pst.setString(2,classSection);
            rs=pst.executeQuery();

            if(rs.next()) {
                JOptionPane.showMessageDialog(this,"Class already exists");
                return false;
            }  

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return true;
    }

    public void btnAddActionPerformed(ActionEvent e) {

        if(!isValidClassForm()) return;

        if(!isValidClass()) return;

        try {
            pst = con.prepareStatement("insert into class(class_name,class_section) values(?,?)");

            pst.setString(1,className);
            pst.setString(2,classSection);

            pst.executeUpdate();

            clearClassForm();

            classLoad();

            JOptionPane.showMessageDialog(this,"Class added");

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    } 

    public void btnUpdateActionPerformed(ActionEvent e) {

        if(tblClass.getRowCount()==0) {
            JOptionPane.showMessageDialog(this,"No class to select");    
            return;        
        }

        d=(DefaultTableModel)tblClass.getModel();
        int selectedRowIndex=tblClass.getSelectedRow();

        if(selectedRowIndex==-1) {
            JOptionPane.showMessageDialog(this,"Please select a class");    
            return;
        }  

        classId=(int)d.getValueAt(selectedRowIndex,0);

        if(!isValidClassForm()) return;

        if(!isValidClass()) return;

        try {
            pst = con.prepareStatement("update class set class_name=?,class_section=? where class_id=?");

            pst.setString(1,className);
            pst.setString(2,classSection);
            pst.setInt(3,classId);

            pst.executeUpdate();

            clearClassForm();

            classLoad();

            btnAdd.setEnabled(true);

            JOptionPane.showMessageDialog(this,"Class updated");

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    } 

    public void btnRemoveActionPerformed(ActionEvent e) {

        if(tblClass.getRowCount()==0) {
            JOptionPane.showMessageDialog(this,"No class to select");    
            return;        
        }

        d=(DefaultTableModel)tblClass.getModel();
        int selectedRowIndex=tblClass.getSelectedRow();

        if(selectedRowIndex==-1) {
            JOptionPane.showMessageDialog(this,"Please select a class");    
            return;
        }

        classId=(int)d.getValueAt(selectedRowIndex,0);

        if (JOptionPane.showConfirmDialog(null, "Are you sure to remove the selected class?", "WARNING",
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            try {
                pst = con.prepareStatement("delete from class where class_id=?");

                pst.setInt(1,classId);

                pst.executeUpdate();

                clearClassForm();

                classLoad();

                btnAdd.setEnabled(true);

                JOptionPane.showMessageDialog(this,"Class removed");
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }

        } else {
            JOptionPane.showMessageDialog(this,"Operation Canceled");
        }

    } 

    public void btnClearActionPerformed(ActionEvent e) { 
        clearClassForm();
        classLoad();
        btnAdd.setEnabled(true);        
    }    

    public void btnCloseActionPerformed(ActionEvent e) {        
        dispose();
    }

    public void tblClassMouseReleased(MouseEvent e) {
        d=(DefaultTableModel)tblClass.getModel();
        int selectedRowIndex=tblClass.getSelectedRow();
        classId=(int)d.getValueAt(selectedRowIndex,0);

        int c;
        try {
            pst=con.prepareStatement("select * from class where class_id=?");

            pst.setInt(1,classId);

            rs=pst.executeQuery();

            ResultSetMetaData rsd=rs.getMetaData();

            if(rs.next()) {
                cboClassName.setSelectedItem(rs.getString("class_name"));                
                cboClassSection.setSelectedItem(rs.getString("class_section"));
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }       

        btnAdd.setEnabled(false);
    }

    public void initGUI() {

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        setTitle("Class");

        setSize(640,640);

        setLayout(null);

        lblClassName=new JLabel("Class");
        lblClassSection=new JLabel("Section");

        cboClassName=new JComboBox(classNames);
        cboClassSection=new JComboBox(sectionNames);

        tblClass=new JTable(new DefaultTableModel(classTableColumnNames,classTableColumnNames.length){
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            }); 

        tblClass.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        spClass=new JScrollPane(tblClass);  

        btnAdd=new JButton("Add");
        btnUpdate=new JButton("Update");
        btnRemove=new JButton("Remove");
        btnClear=new JButton("Clear");
        btnClose=new JButton("Close");

        lblClassName.setBounds(16,16,128,32);
        lblClassSection.setBounds(16,64,128,32);

        cboClassName.setBounds(160,16,256,32);
        cboClassSection.setBounds(160,64,256,32);

        spClass.setBounds(16,400,600,200);        

        btnAdd.setBounds(16,352,96,32);
        btnUpdate.setBounds(128,352,96,32);
        btnRemove.setBounds(240,352,96,32);
        btnClear.setBounds(352,352,96,32);
        btnClose.setBounds(464,352,96,32);        

        add(lblClassName);
        add(lblClassSection);

        add(cboClassName);
        add(cboClassSection);

        add(spClass);

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

        tblClass.addMouseListener(new MouseAdapter() {
                public void mouseReleased(MouseEvent e) {
                    tblClassMouseReleased(e);                    
                } 
            });

        clearClassForm();

        setLocationRelativeTo(null);

        setVisible(true);

        setModal(true);
    }
}
