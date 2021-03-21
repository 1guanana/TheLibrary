/**
 * @author Ana Giljum
 * 
 * Date: Feb. 17, 2019
 */

package library.app;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;


public class UserInterface {
	
	//Creates an object of the database, and a table to display
	static Connector cur = new Connector();
	static JTable table = new JTable();
	static JFrame frame = new JFrame ("Library Home");
	static JPanel searchPanel = new JPanel();
	static JPanel delPanel = new JPanel();
	static JPanel addPanel = new JPanel();
	static JPanel checkPanel = new JPanel();
	static JPanel dbPanel = new JPanel();
	
	//Create a window frame
	public void createWindow() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(915,750);
		frame.setResizable(false);
		
		JButton clear = new JButton("Clear All");
		clear.setBounds(750,85,90,60);
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearAll();
			}
		});
		frame.add(clear);

		//Makes and adds the header
		JLabel header = new JLabel("the Library");
		header.setLabelFor(frame);
		header.setBounds(5, 0, 700, 55);
		header.setFont(new java.awt.Font("Garamond", Font.BOLD, 50));
		frame.add(header);
		
		//Creates and adds a 'help' feature
    	Icon img = new ImageIcon(this.getClass().getResource("/library/app/help.png"));
		JButton help = new JButton();
		help.setIcon(img);
		help.setBounds(855,5,50,50);
		help.setToolTipText("Help");
		help.setOpaque(true);
		frame.add(help);
		
		//Opens help screen when help button is pressed
		help.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,
						"HOW TO USE THE LIBRARY\n\n"
								+ "To initally show all entries in the database, click the 'Submit'\n"
								+ "button in the Search box with the text field empty.\n"
								+ "\n"
								+ "To search for a book, input an ISBN, a title, an author, or an item\n"
								+ "from the dropdown menu for genre and select the checkbox that corresponds\n"
								+ "to your search. To submit your search, click the 'Submit' button and the\n"
								+ "results will be shown below. The columns can be ordered alphabetically\n"
								+ "or numerically by clicking on the column header name (isbn,\n"
								+ "title, author, etc.).\n"
								+ "\n"
								+ "To delete a book, enter the ISBN or title of the book that you\n"
								+ "want removed from the database, and select the corresponding\n"
								+ "checkbox. When you press 'Delete', the book (if it exists in the\n"
								+ "database) will be deleted. The resulting database will be shown.\n"
								+ "below.\n"
								+ "WARNING: If two books share a title, and you are deleting based on\n"
								+ "title then both books will be deleted. Instead, if there are duplicate\n"
								+ "titles, delete books based on their unique ISBN.\n"
								+ "\n"
								+ "To add a book, input (at very least) the ISBN, title, and author of\n"
								+ "the new book and press the 'Add' button. If no year is entered, the\n"
								+ "default year is 0. Make sure that the ISBN is not already found in the\n"
								+ "database, and is a 13 digit number. The resulting database will be\n"
								+ "shown below.\n"
								+ "\n"
								+ "To check a book in or out, enter the ISBN of the desired book, and\n"
								+ "click the checkbox of either 'Check-In' or 'Check-Out' based on which\n"
								+ "you want to do. Be sure to check the availability of your desired book\n"
								+ "before trying to check it in or out.\n"
								+ "\n"
								+ "Happy reading!", "Help", JOptionPane.PLAIN_MESSAGE);
			}
		});
	}
	
	
	//Sets up panel that displays "search books"
	public void createSearch() {
		
		String[] genres = {"Comic", "Fantasy", "Fiction", "Horror", "Mystery", "Nonfiction", "SciFi"};
		JComboBox<String> genreList = new JComboBox<String>(genres);
		genreList.setSelectedIndex(-1);
		
		//Create check boxes to narrow search results
		JRadioButton isbnCheck = new JRadioButton("ISBN", false);
		JRadioButton titleCheck = new JRadioButton("Title", false);
		JRadioButton authorCheck = new JRadioButton("Author", false);
		JRadioButton genreCheck = new JRadioButton("Genre", false);
	
		//Add check boxes to a Button group, so only one can be selected at a time
		ButtonGroup searchGroup = new ButtonGroup();
		searchGroup.add(isbnCheck);
		searchGroup.add(titleCheck);
		searchGroup.add(authorCheck);
		searchGroup.add(genreCheck);
	
		//Creates the submit button and the search bar
		JButton submit = new JButton("Submit");
		JTextField searchBar = new JTextField(55);
	
		//Creates titled border
		TitledBorder searchBorder = new TitledBorder("Search");
		searchBorder.setTitleFont(searchBorder.getTitleFont().deriveFont(Font.BOLD));
		
		//Creates search panel
		searchPanel.setBounds(10, 60, 660, 115);
		searchPanel.setBorder(searchBorder);
		searchPanel.setBackground(new java.awt.Color(186,216,181));
		searchPanel.add(searchBar);
		searchPanel.add(Box.createRigidArea(new Dimension(0,40)));
		searchPanel.add(isbnCheck);
		searchPanel.add(Box.createRigidArea(new Dimension(20,0)));
		searchPanel.add(titleCheck);
		searchPanel.add(Box.createRigidArea(new Dimension(20,0)));
		searchPanel.add(authorCheck);
		searchPanel.add(Box.createRigidArea(new Dimension(20,0)));
		searchPanel.add(genreCheck);
		searchPanel.add(genreList);
		searchPanel.add(Box.createRigidArea(new Dimension(50,0)));
		searchPanel.add(submit);
		
		//Searches user input
		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Query database for items in text box based on check boxes
				String searchText = searchBar.getText().trim();
				//Makes sure entry is valid (alphanumeric, not empty)
				if ((!searchText.matches("[a-zA-Z0-9\\' ']+$")) && (!searchText.isEmpty())) {
					JOptionPane.showMessageDialog(null, "Please only enter letters and numbers.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				//Searches based on which check box is ticked
				try {
					if (isbnCheck.isSelected() && (!searchText.matches("[0-9]+$") || searchText.length() != 13)) {
					  JOptionPane.showMessageDialog(null, "Please enter a valid ISBN.", "Error", JOptionPane.ERROR_MESSAGE);
					  return;
					}
					else if (isbnCheck.isSelected()) {
						String isbn = searchText;
						ResultSet rs = cur.searchISBN(isbn);
						RStoTM(rs, table);
					}
					else if (titleCheck.isSelected()) {
						String title = "%" + searchText + "%";
						ResultSet rs = cur.searchTitle(title);
						RStoTM(rs, table);
					}
					else if (authorCheck.isSelected()) {
						String author = "%" + searchText + "%";
						ResultSet rs = cur.searchAuthor(author);
						RStoTM(rs, table);
					}
					else if(genreCheck.isSelected()) {
						String genre = (String) genreList.getSelectedItem();
						searchBar.setText(genre);
						ResultSet rs = cur.searchGenre(genre);
						RStoTM(rs, table);
					}
					else {
						ResultSet rs = cur.searchAll();
						RStoTM(rs, table);
					}
				}
				catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, "Failed to submit search.", "Failure", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
	
	
	//Sets up panel that displays "delete books"
	public void createDelete() {
		
		//Creates check boxes for the user to delete by ISBN or Title
		JRadioButton isbnDel = new JRadioButton("ISBN", false);
		JRadioButton titleDel = new JRadioButton("Title", false);
		
		//Adds check boxes to Button group, so only one can be selected at a time
		ButtonGroup delGroup = new ButtonGroup();
		delGroup.add(isbnDel);
		delGroup.add(titleDel);
		
		//Creates the delete button
		JButton delete = new JButton("Delete");
		JTextField delBar = new JTextField(20);
		
		//Creates titled border
		TitledBorder delBorder = new TitledBorder("Delete Books");
		delBorder.setTitleFont(delBorder.getTitleFont().deriveFont(Font.BOLD));
		
		//Creates the delete panel
		delPanel.setBounds(10, 180, 275, 115);
		delPanel.setBorder(delBorder);
		delPanel.setBackground(new java.awt.Color(181,204,216));
		delPanel.add(delBar);
		delPanel.add(Box.createRigidArea(new Dimension(0,40)));
		delPanel.add(isbnDel);
		delPanel.add(Box.createRigidArea(new Dimension(5,0)));
		delPanel.add(titleDel);
		delPanel.add(Box.createRigidArea(new Dimension(30,0)));
		delPanel.add(delete);
		
		//Deletes entry based on ISBN or title
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String delText = delBar.getText().trim();
				//Makes sure the entry is valid (not empty)
				if(delText.isEmpty()) {
				  JOptionPane.showMessageDialog(null, "Please enter an ISBN or title to be deleted.", "Failure", JOptionPane.ERROR_MESSAGE);
          return;
				}
				//Makes sure the entry is valid (ISBN is numeric)
				else if(isbnDel.isSelected() && (!delText.matches("[0-9]+$") || delText.length() != 13)) {
          JOptionPane.showMessageDialog(null, "Please enter a valid ISBN.", "Failure", JOptionPane.ERROR_MESSAGE);
          return;
        }
				//Makes sure the entry is valid (found in the database)
				try {
	        ResultSet res = cur.searchAll();
	        RStoTM(res, table);
	        if (!tableContains(table, delText)) {
	          JOptionPane.showMessageDialog(null, "No such book was found.", "Failure", JOptionPane.ERROR_MESSAGE);
	          return;
	        }
				} catch (SQLException ea) {
	          JOptionPane.showMessageDialog(null, "Failed to search.", "Failure", JOptionPane.ERROR_MESSAGE);
	      }	
				
				//Passed validation, deleting book based on which check box is ticked
				try {
					if (isbnDel.isSelected()) {
						ResultSet rs = cur.removeBooksISBN(delText);
						RStoTM(rs, table);
					}
					else if (titleDel.isSelected()) {
						ResultSet rs = cur.removeBooksTitle(delText);
						RStoTM(rs, table);
					}
					else {
						return;
					}
				} catch (SQLException e2) {
					JOptionPane.showMessageDialog(null, "Failed to delete book.", "Failure", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
	
	
	//Sets up the panel that displays "check-in/check-out"
		public void createCheck() {
			
			//Creates check boxes for the user to books in or out
			JRadioButton checkin = new JRadioButton("Check-In", false);
			JRadioButton checkout = new JRadioButton("Check-Out", false);
			
			//Adds check boxes to Button group so only one can be selected
			ButtonGroup checkGroup = new ButtonGroup();
			checkGroup.add(checkin);
			checkGroup.add(checkout);
			
			//Creates labeled text bar for input and submit button
			JButton submitInOut = new JButton("Submit");
			JTextField isbnInOut = new JTextField(15);
			JLabel checkLabel = new JLabel("ISBN: ");
			checkLabel.setLabelFor(isbnInOut);
			
			TitledBorder checkBorder = new TitledBorder("Check-In/Check-Out");
			checkBorder.setTitleFont(checkBorder.getTitleFont().deriveFont(Font.BOLD));
			
			//Creates the check-in/check-out panel
			checkPanel.setBounds(300, 180, 275, 115);
			checkPanel.setBorder(checkBorder);
			checkPanel.setBackground(new java.awt.Color(216,194,181));
			checkPanel.add(checkLabel);
			checkPanel.add(isbnInOut);
			checkPanel.add(Box.createRigidArea(new Dimension(0,40)));
			checkPanel.add(checkin);
			checkPanel.add(checkout);
			checkPanel.add(Box.createRigidArea(new Dimension(10,0)));
			checkPanel.add(submitInOut);
			
			//Checks books in or out depending on user input
			submitInOut.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String checkText = isbnInOut.getText().trim();
					//Makes sure the entry is valid (Numeric and 13 characters)
					if (!(checkText.matches("[0-9]+$") && checkText.length() == 13)) {
						JOptionPane.showMessageDialog(null, "Please enter a valid ISBN.", "Failure", JOptionPane.ERROR_MESSAGE);
						return;
					}
					//Makes sure the entry is valid (found in the database)
					try {
						ResultSet res = cur.searchAll();
						RStoTM(res, table);
					} catch (SQLException ea) {
						JOptionPane.showMessageDialog(null, "Failed to search.", "Failure", JOptionPane.ERROR_MESSAGE);
					}	
					if (!tableContains(table, checkText)) {
						JOptionPane.showMessageDialog(null, "No such book was found.", "Failure", JOptionPane.ERROR_MESSAGE);
						return;
					}
					//Makes sure the entry is valid (not empty)
					else if(checkText.isEmpty()) {
						JOptionPane.showMessageDialog(null, "Please enter an ISBN to check in or out", "Failure", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					//Passed validation, checking book in/out based on which check box is ticked
					try {
						if(checkin.isSelected()) {
							ResultSet rs = cur.checkStatus(checkText);
							RStoTM(rs, table);
							if(tableContains(table, "Y")) {
								JOptionPane.showMessageDialog(null, "That book is already checked in.", "Failure", JOptionPane.ERROR_MESSAGE);
							}
							else {
								rs = cur.checkIn(checkText);
								RStoTM(rs,table);
							}
						}
						else if(checkout.isSelected()) {
							ResultSet rs = cur.checkStatus(checkText);
							RStoTM(rs,table);
							if(tableContains(table, "N")) {
								JOptionPane.showMessageDialog(null, "That book is already checked out.", "Failure", JOptionPane.ERROR_MESSAGE);
							}
							else {
								rs = cur.checkOut(checkText);
								RStoTM(rs,table);
							}
						}
					}
					catch (SQLException e4) {
						JOptionPane.showMessageDialog(null, "Failed to check-in/check-out.", "Failure", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
		}

	
	//Sets up the panel that displays "add books"
	public void createAdd() {
		
		String[] genres = {"Comic", "Fantasy", "Horror", "Mystery", "Nonfiction", "SciFi", "Romance"};
		JComboBox<String> genreList = new JComboBox<String>(genres);
		genreList.setSelectedIndex(-1);

		//Creates text fields to add info on book to be added
		JButton add = new JButton("Add");
		JTextField isbnBar = new JTextField(9);
		JTextField titleBar = new JTextField(10);
		JTextField authorBar = new JTextField(10);
		JTextField yearBar = new JTextField(5);
		//JTextField genreBar = new JTextField(10);
		
		//Adds labels to the text fields
		JLabel isbnLabel = new JLabel("ISBN: ");
		JLabel titleLabel = new JLabel("Title: ");
		JLabel authorLabel = new JLabel("Author: ");
		JLabel yearLabel = new JLabel("Year: ");
		JLabel genreLabel = new JLabel("Genre: ");
		
		isbnLabel.setLabelFor(isbnBar);
		titleLabel.setLabelFor(titleBar);
		authorLabel.setLabelFor(authorBar);
		yearLabel.setLabelFor(yearBar);
		genreLabel.setLabelFor(genreList);
		
		//Creates a titled border
		TitledBorder addBorder = new TitledBorder("Add Books");
		addBorder.setTitleFont(addBorder.getTitleFont().deriveFont(Font.BOLD));
		
    			
		//Creates the add panel
		addPanel.setBounds(590, 180, 310, 115);
		addPanel.setBorder(addBorder);
		addPanel.setBackground(new java.awt.Color(211,181,216));
		addPanel.add(isbnLabel);
		addPanel.add(isbnBar);
		addPanel.add(titleLabel);
		addPanel.add(titleBar);
		addPanel.add(authorLabel);
		addPanel.add(authorBar);
		addPanel.add(Box.createRigidArea(new Dimension(26,0)));
		addPanel.add(yearLabel);
		addPanel.add(yearBar);
		addPanel.add(genreLabel);
		addPanel.add(genreList);
		addPanel.add(Box.createRigidArea(new Dimension(98,0)));
		addPanel.add(add);
		
		//Adds entry based on input entered by user
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String addISBN = isbnBar.getText().trim();
				String addTitle = titleBar.getText().trim();
				String addAuthor = authorBar.getText().trim();
				String addYear = yearBar.getText().trim();
				String addGenre = (String) genreList.getSelectedItem();
				//Makes sure the year, if entered, is valid (not empty)
				int addYearInt;
				try {
				  ResultSet res = cur.searchAll();
				  RStoTM(res, table);
				  if(addYear.isEmpty()) {
				    addYearInt = 0;
				  }
				  //Makes sure the year is valid (Numeric, greater than 0, less than 9999)
				  else if(addYear.matches("[0-9]+$") && (Integer.parseInt(addYear) > 0) && (Integer.parseInt(addYear) < 9999)) {
				    addYearInt = Integer.parseInt(addYear);
				  }
				  else {
				    JOptionPane.showMessageDialog(null, "Please enter a valid year.", "Failure", JOptionPane.ERROR_MESSAGE);
				    return;
				  }
				  //Makes sure the title and author are valid (alphanumeric, not empty)
				  if(!addTitle.matches("[a-zA-Z0-9\\' ']+$") || addTitle.isEmpty() 
				      || !addAuthor.matches("[a-zA-Z0-9\\' ']+$") || addAuthor.isEmpty()) {
				    JOptionPane.showMessageDialog(null, "Please provide an ISBN, title, "
				        + "and author using only letters and numbers.", "Failure", JOptionPane.ERROR_MESSAGE);
				    return;
				  }
				  //Makes sure the ISBN is valid 
				  else if(!addISBN.matches("[0-9]+$") || addISBN.length() != 13 || tableContains(table,addISBN)) {
				    JOptionPane.showMessageDialog(null, "Please enter a valid, unique ISBN.", "Failure", JOptionPane.ERROR_MESSAGE);
				    return;
				  }		
				  
				//Passed validation, adding book
					ResultSet rs = cur.addBooks(addISBN, addTitle, addAuthor, addYearInt, addGenre);
					RStoTM(rs, table);
				} catch (SQLException e3) {
					JOptionPane.showMessageDialog(null, "Failed to add book.", "Failure", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
	
	
	//Sets up the panel that displays the database
	public void createDBPanel() {
		
		dbPanel.setLayout(new BorderLayout());
		JScrollPane tableContainer = new JScrollPane(table);
		dbPanel.add(tableContainer, BorderLayout.SOUTH);
	}
	
	
	//Function to make the results of the database viewable
	public static void RStoTM(ResultSet rs, JTable table) throws SQLException {
		
		DefaultTableModel model = new DefaultTableModel();
			
		//Gets the metadata from the database results
		ResultSetMetaData metadata = rs.getMetaData();
			
		//Moves the column metadata into the model
		int col = metadata.getColumnCount();
		for(int indexCol = 1; indexCol <= col; indexCol++) {
			model.addColumn(metadata.getColumnLabel(indexCol));
		}
			
		Object[] row = new Object[col];
			
		//Fills in the rows based on the result set
		while(rs.next()) {
			for(int indexRow = 0; indexRow < col; indexRow++) {
				row[indexRow] = rs.getObject(indexRow + 1);
			}	
			model.addRow(row);
		}
		table.setAutoCreateRowSorter(true);
		table.setModel(model);
	}

	
	//Checks if a string is in a table
	public Boolean tableContains(JTable table, String value) {
		
		for (int i = 0; i < table.getRowCount(); i++) {
			for (int j = 0; j < table.getColumnCount(); j++) {
				//Checks the values at each cell in the table for a match
				if(table.getModel().getValueAt(i, j).equals(value)) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	//Clears all input fields
	public static void clearAll() {
		JRadioButton button = new JRadioButton();
		JComboBox<String> menu = new JComboBox<String>();
		
		//Clears text fields and check boxes in search panel
		ButtonGroup searchGroup = new ButtonGroup();
		Component [] cSearch = searchPanel.getComponents();
		for(int i = 0; i < cSearch.length; i++) {
			if(cSearch[i] instanceof JTextField) {
				JTextField text = (JTextField) cSearch[i];
				text.setText("");
			}
			else if(cSearch[i] instanceof JComboBox) {
				menu = (JComboBox<String>) cSearch[i];
				menu.setSelectedIndex(-1);
			}
			else if(cSearch[i] instanceof JRadioButton) {
				button = (JRadioButton) cSearch[i];
				searchGroup.add(button);
			}
			searchGroup.clearSelection();
		}
		
		//Clears text field and check boxes in delete panel
		ButtonGroup delGroup = new ButtonGroup();
		Component [] cDel = delPanel.getComponents();
		for(int i = 0; i < cDel.length; i++) {
			if(cDel[i] instanceof JTextField) {
				JTextField text = (JTextField) cDel[i];
				text.setText("");
			}
			else if(cDel[i] instanceof JRadioButton) {
				button = (JRadioButton) cDel[i];
				delGroup.add(button);
			}
			delGroup.clearSelection();
		}
		
		//Clears text fields in add panel
		Component [] cAdd = addPanel.getComponents();
		for(int i = 0; i < cAdd.length; i++) {
			if(cAdd[i] instanceof JTextField) {
				JTextField text = (JTextField) cAdd[i];
				text.setText("");
			}
			else if(cAdd[i] instanceof JComboBox) {
				menu = (JComboBox<String>) cAdd[i];
				menu.setSelectedIndex(-1);
			}
		}
		
		//Clears text field and check boxes in check panel
		ButtonGroup checkGroup = new ButtonGroup();
		Component [] cCheck = checkPanel.getComponents();
		for(int i = 0; i < cCheck.length; i++) {
			if(cCheck[i] instanceof JTextField) {
				JTextField text = (JTextField) cCheck[i];
				text.setText("");
			}
			else if(cCheck[i] instanceof JRadioButton) {
				button = (JRadioButton) cCheck[i];
				checkGroup.add(button);
			}
			checkGroup.clearSelection();
		}
		
		try {
		ResultSet res = cur.searchAll();
        RStoTM(res, table);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Failed to print database.", "Failure", JOptionPane.ERROR_MESSAGE);
		    return;
		}
	}

	
	public static void main(String[] args) {
		
		UserInterface UI = new UserInterface();
		
		//Creates instances for GUI
		UI.createWindow();
		UI.createSearch();
		UI.createDelete();
		UI.createAdd();
		UI.createCheck();
		UI.createDBPanel();
		
		//Sets up the frame window to display everything
		frame.add(searchPanel);
		frame.add(delPanel);
		frame.add(addPanel);
		frame.add(checkPanel);
		frame.add(dbPanel);
		
		frame.setVisible(true);
	}
	
}
