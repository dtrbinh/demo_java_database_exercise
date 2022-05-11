
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.print.attribute.standard.SheetCollate;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class SearchGUI extends JFrame implements ActionListener{
	private String urlDB ="jdbc:sqlserver://CATCH-ME:1433;";
	private Connection cnn;
	private Statement stmt;
	public void Connect() throws SQLException, ClassNotFoundException {
		System.out.println("Dang ket noi CSDL...");
		try{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String mainURL = urlDB
		            + "databaseName=javaDataDemo;"
		            + "integratedSecurity=true;"
		            + "encrypt = true;"
		            + "trustServerCertificate=true;";
			cnn = DriverManager.getConnection(mainURL);
			stmt = cnn.createStatement();
			System.out.println("Ket noi thanh cong.");
		} 
		catch(SQLException e) 
			{
			System.out.println("Ket noi CSDL that bai.");
			//System.out.println("Error: " + e.toString());
			}
	}
	public void Disconnect () throws SQLException {
		cnn.close();
		System.out.println("Da ngat ket noi CSDL.");
	}
	public void Update(String query) throws SQLException {
		if(cnn.isClosed()) {
			System.out.println("Ket noi da dong. Khong the thuc thi query.");
		} else {
			stmt.executeUpdate(query);
		}
	}
	public ResultSet Query(String query) throws SQLException {
		if(cnn.isClosed()) {
			System.out.println("Ket noi da dong. Khong the thuc thi query");
			return null;
		} else {
			return stmt.executeQuery(query);
		}
	}
	//Database
	DBHelper db;
	//--------
	JLabel lb_title,lb_info, lb_search;
	JTextField txt_info;
	JRadioButton radID, radName, radAddress, radTotal;
	ButtonGroup radGr;
	JButton btn_Search, btn_Reset, btn_Exit;
	JTable dataViewer;
	DefaultTableModel tableModel;
	
	String[] colNames = {"ID", "Name", "Address", "Total"};

	JScrollPane scrollPanel;
	JPanel mainPN, sub1, sub2, header;
	JPanel PN1, PN2, PN3;
	
	public void GUI() {
		lb_title = new JLabel("TIM KIEM THONG TIN");
		lb_info = new JLabel("Input Infomation");
		lb_search = new JLabel("Search as");
		
		txt_info = new JTextField(20);
		
		radID = new JRadioButton("ID");
		radName = new JRadioButton("Name");
		radAddress = new JRadioButton("Address");
		radTotal = new JRadioButton("Total");
		
		tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(colNames);
		dataViewer = new JTable(tableModel);
		dataViewer.setFillsViewportHeight(true);
		dataViewer.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		dataViewer.setMinimumSize(new Dimension(300, 200));
		
		btn_Search = new JButton("Search"); btn_Search.addActionListener(this);
		btn_Reset = new JButton("Reset"); btn_Reset.addActionListener(this);
		btn_Exit = new JButton("Exit"); btn_Exit.addActionListener(this);
		
		scrollPanel = new JScrollPane(dataViewer);
		scrollPanel.setPreferredSize( new Dimension(300, 200));
		
		mainPN = new JPanel(new GridLayout(2, 1));
		sub1 = new JPanel(new GridLayout(3, 1));
		sub2 = new JPanel(new GridLayout(1, 1));
		
		PN3 = new JPanel(new FlowLayout());
		PN3.add(btn_Search);
		PN3.add(btn_Reset);
		PN3.add(btn_Exit);
		
		PN1 = new JPanel(new FlowLayout());
		PN1.add(lb_info);
		PN1.add(txt_info);
		PN1.add(PN3);
		
		radGr = new ButtonGroup();
		radGr.add(radID);
		radGr.add(radName);
		radGr.add(radAddress);
		radGr.add(radTotal);
		
		PN2 = new JPanel(new FlowLayout());
		PN2.add(lb_search);
		
		PN2.add(radID);
		PN2.add(radName);
		PN2.add(radAddress);
		PN2.add(radTotal);
		
		header = new JPanel(new FlowLayout(FlowLayout.CENTER));
		header.add(lb_title);
		
		sub1.add(header);
		sub1.add(PN1);
		sub1.add(PN2);
		sub2.add(scrollPanel);
		mainPN.add(sub1);
		mainPN.add(sub2);
		add(mainPN);
		setSize(600,400);
		setVisible(true);
	}
	public String radSelection() {
		String key = "";
		if (radID.isSelected()) key = "ID";
		else if (radName.isSelected()) key = "Name";
		else if (radAddress.isSelected()) key = "Address";
		else if (radTotal.isSelected()) key = "Total";
		return key;
	}
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		// TODO Auto-generated method stub
		if(e.getSource()==btn_Search)
		{		
			if (txt_info.getText() != null || txt_info.getText() != "") {
				try {
					Connect();
					String query = "SELECT * FROM Table1 WHERE " + radSelection() + " = '" +  txt_info.getText() + "'";
					//System.out.println(query);
					ResultSet rs = null;
					rs = Query(query);
					tableModel.setRowCount(0);
					if (rs != null) {
						while (rs.next()) {
							String[] rowData = {rs.getString("ID"), rs.getString("Name"), rs.getString("Address"), rs.getString("Total")};
							tableModel.addRow(rowData);
						}
						System.out.println("Doc du lieu thanh cong.");
					} else {
						System.out.println("Doc du lieu that bai.");
					}
					Disconnect();
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		if(e.getSource()==btn_Reset)
		{
			txt_info.setText("");
		}
		if(e.getSource()==btn_Exit)
		{
			System.exit(0);
		}
	}
	public SearchGUI(String title)
	{
		super(title);
		GUI();
	}
	
	public static void main(String[] args) 
	{
		new SearchGUI("Tim kiem thong tin");
	}
	
}
