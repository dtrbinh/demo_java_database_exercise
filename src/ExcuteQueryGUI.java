
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.print.attribute.standard.SheetCollate;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class ExcuteQueryGUI extends JFrame implements ActionListener{
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
	JLabel lb_title,lb_urlDB, lb_query;
	JTextField txt_urlDB, txt_query;
	JButton btn_OK, btn_Reset, btn_Exit;
	JTable dataViewer;
	DefaultTableModel tableModel;
	
	String[] colNames = {"ID", "Name", "Address", "Total"};
	String[][] rowDatas = 
		{
//				{"","","",""},
//				{"","","",""},
//				{"","","",""},
//				{"","","",""},
//				{"","","",""},
//				{"","","",""},
	};

	JScrollPane scrollPanel;
	JPanel mainPN, sub1, sub2, header;
	JPanel PN1, PN2, PN3;
	
	public void GUI() {
		lb_title = new JLabel("TRUY XUAT CO SO DU LIEU");
		lb_urlDB = new JLabel("Input URL DB");
		lb_query = new JLabel("SQL Query");
		
		txt_urlDB = new JTextField(20);
		txt_query = new JTextField(20);
		
		tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(colNames);
		dataViewer = new JTable(tableModel);
		dataViewer.setFillsViewportHeight(true);
		dataViewer.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		dataViewer.setMinimumSize(new Dimension(300, 200));
		
		btn_OK = new JButton("OK"); btn_OK.addActionListener(this);
		btn_Reset = new JButton("Reset"); btn_Reset.addActionListener(this);
		btn_Exit = new JButton("Exit"); btn_Exit.addActionListener(this);
		
		scrollPanel = new JScrollPane(dataViewer);
		//scrollPanel.setPreferredSize( new Dimension(300, 200));
		
		mainPN = new JPanel(new GridLayout(2, 1));
		PN1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		PN1.add(lb_urlDB);
		PN1.add(txt_urlDB);
		
		PN2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		PN2.add(lb_query);
		PN2.add(txt_query);
		
		PN3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		PN3.add(btn_OK);
		PN3.add(btn_Reset);
		PN3.add(btn_Exit);
		header = new JPanel(new FlowLayout(FlowLayout.CENTER));
		header.add(lb_title);
		sub1 = new JPanel(new GridLayout(3, 1));
		sub1.add(header);
		sub1.add(PN1);
		sub1.add(PN2);
		
		
		sub2 = new JPanel(new GridLayout(2, 1));
		sub2.add(scrollPanel);
		sub2.add(PN3);
		
		mainPN.add(sub1);
		mainPN.add(sub2);
		
		add(mainPN);
		setSize(600,600);
		setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		// TODO Auto-generated method stub
		if(e.getSource()==btn_OK)
		{		
			if (txt_query.getText() != "" && txt_urlDB.getText() != "") {
				try {
					urlDB = txt_urlDB.getText();
					String sql = txt_query.getText();
					Connect();
					ResultSet rs = null;
					if (txt_query.getText().toLowerCase().contains("select"))
					{
						
						rs = Query(sql);
					}else if (txt_query.getText().toLowerCase().contains("insert")) 
					{	//INSERT INTO Table1 (ID, Name, Address, Total) VALUES ( 5, 'Nguyen D', 'Ha Giang', 12.34)
						
						System.out.println("INSERTING...");
						Update(sql);
						System.out.println("INSERTED!");
						rs = Query("SELECT * FROM TABLE1");
					}else if (txt_query.getText().toLowerCase().contains("update")) 
					{	
						//UPDATE Table1 SET ID = 2, Name = 'Nguyen H', Address = 'Binh Dinh', Total = 24.11 WHERE ID = 2	
						int i = sql.indexOf("WHERE");
						String cond = sql.substring(i+5);
						//System.out.println("SELECT * FROM Table1 WHERE " + cond);
						String checker = "SELECT * FROM Table1 WHERE" + cond;
						rs = Query(checker);
						if(rs.next() == true) {
							System.out.println("UPDATING...");
							Update(sql);
							System.out.println("UPDATED!");
							
							rs = Query("SELECT * FROM TABLE1");
							
						}else {
							System.out.println("Khong ton tai record co: " + cond +".");
						}
						
					}else if (txt_query.getText().toLowerCase().contains("delete")) 
					{
						//DELETE FROM Table1 WHERE ID = 5
						System.out.println("DELETING...");
						Update(sql);
						System.out.println("DELETED!");
						
						rs = Query("SELECT * FROM TABLE1");
					}
					else {}
					if (rs != null) {
						tableModel.setRowCount(0);
						while (rs.next()) {
							String[] rowData = {rs.getString("ID"), rs.getString("Name"), rs.getString("Address"), rs.getString("Total")};
							tableModel.addRow(rowData);
						}
						System.out.println("Doc du lieu thanh cong.");
					} else {
						System.out.println("Khong co du lieu tra ve.");
					}
					Disconnect();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		if(e.getSource()==btn_Reset)
		{
			txt_urlDB.setText("");
			txt_query.setText("");
		}
		if(e.getSource()==btn_Exit)
		{
			System.exit(0);
		}
	}
	public ExcuteQueryGUI(String title)
	{
		super(title);
		GUI();
	}
	
	public static void main(String[] args) 
	{
		new ExcuteQueryGUI("Truy Xuat Co So Du Lieu");
	}
	
}
