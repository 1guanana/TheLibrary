/**
 * @author Ana Giljum
 * 
 * Date: Feb. 17, 2019
 */

package library.app;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;


public class Connector {

	private static Connection conn;
	private static boolean exists = false;
	
	//Opens the connection to the database
	private void getConnection() {
		
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:database.db");

			initialize();
		}
		catch (ClassNotFoundException | SQLException | IOException e) {
			JOptionPane.showMessageDialog(null, "Failed to connect to database.", "Failure", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	//Initializes the database
	private void initialize() throws SQLException, IOException {
		
		//Checks if the database already has data
		if(!exists) {
			exists = true;
			
			//Checks to see if table already exists
			Statement state = conn.createStatement();
			ResultSet res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='Books'");
			//If not, make the table
			if(!res.next()) {

				
				//Create the table and attributes
				Statement state2 = conn.createStatement();
				state2.execute("CREATE TABLE Books(rowid INTEGER PRIMARY KEY, isbn VARCHAR(13) NOT NULL,"
						+ "title VARCHAR(50) NOT NULL, author VARCHAR(50) NOT NULL,"
						+ "year INTEGER, genre VARCHAR(25), available CHAR(1) NOT NULL);");
				
						
				//Populate fields
				PreparedStatement prep = conn.prepareStatement("INSERT INTO Books VALUES(null,?,?,?,?,?,?);");
				prep.setString(1, "9781501175466");
				prep.setString(2, "It");
				prep.setString(3, "Stephen King");
				prep.setInt(4, 1986);
				prep.setString(5, "Horror");
				prep.setString(6, "Y");
				prep.execute();
				
				PreparedStatement prep1 = conn.prepareStatement("INSERT INTO Books VALUES(null,?,?,?,?,?,?);");
				prep1.setString(1, "9780064404990");
				prep1.setString(2, "The Lion, the Witch, and the Wardrobe");
				prep1.setString(3, "CS Lewis");
				prep1.setInt(4, 1950);
				prep1.setString(5, "Fantasy");
				prep1.setString(6, "Y");
				prep1.execute();
				
				PreparedStatement prep2 = conn.prepareStatement("INSERT INTO Books VALUES(null,?,?,?,?,?,?);");
				prep2.setString(1, "9780307474278");
				prep2.setString(2, "The Da Vinci Code");
				prep2.setString(3, "Dan Brown");
				prep2.setInt(4, 2003);
				prep2.setString(5, "Mystery");
				prep2.setString(6, "Y");
				prep2.execute();
				
				PreparedStatement prep3 = conn.prepareStatement("INSERT INTO Books VALUES(null,?,?,?,?,?,?);");
				prep3.setString(1, "9780060850524");
				prep3.setString(2, "Brave New World");
				prep3.setString(3, "Aldous Huxley");
				prep3.setInt(4,1932);
				prep3.setString(5, "SciFi");
				prep3.setString(6, "Y");
				prep3.execute();
				
				PreparedStatement prep4 = conn.prepareStatement("INSERT INTO Books VALUES(null,?,?,?,?,?,?);");
				prep4.setString(1, "9780553380163");
				prep4.setString(2, "A Brief History of Time");
				prep4.setString(3, "Stephen Hawking");
				prep4.setInt(4, 1988);
				prep4.setString(5, "Nonfiction");
				prep4.setString(6, "Y");
				prep4.execute();
				
				PreparedStatement prep5 = conn.prepareStatement("INSERT INTO Books VALUES(null,?,?,?,?,?,?);");
				prep4.setString(1, "9781401245252");
				prep4.setString(2, "Watchmen");
				prep4.setString(3, "Alan Moore");
				prep4.setInt(4, 1986);
				prep4.setString(5, "Comic");
				prep4.setString(6, "Y");
				prep4.execute();
				
				PreparedStatement prep6 = conn.prepareStatement("INSERT INTO Books VALUES(null,?,?,?,?,?,?);");
				prep4.setString(1, "9780553380163");
				prep4.setString(2, "Life of Pi");
				prep4.setString(3, "Yann Martel");
				prep4.setInt(4, 2003);
				prep4.setString(5, "Fiction");
				prep4.setString(6, "Y");
				prep4.execute();
				
				PreparedStatement prep7 = conn.prepareStatement("INSERT INTO Books VALUES(null,?,?,?,?,?,?);");
				prep4.setString(1, "9781117970486");
				prep4.setString(2, "American Gods");
				prep4.setString(3, "Neil Gaiman");
				prep4.setInt(4, 2001);
				prep4.setString(5, "Fantasy");
				prep4.setString(6, "Y");
				prep4.execute();
				
				PreparedStatement prep8 = conn.prepareStatement("INSERT INTO Books VALUES(null,?,?,?,?,?,?);");
				prep4.setString(1, "9780062073488");
				prep4.setString(2, "And Then There Were None");
				prep4.setString(3, "Agatha Christie");
				prep4.setInt(4, 1939);
				prep4.setString(5, "Mystery");
				prep4.setString(6, "Y");
				prep4.execute();
				
				PreparedStatement prep9 = conn.prepareStatement("INSERT INTO Books VALUES(null,?,?,?,?,?,?);");
				prep4.setString(1, "9780812988529");
				prep4.setString(2, "Slaughterhouse Five");
				prep4.setString(3, "Kurt Vonnegut");
				prep4.setInt(4, 1969);
				prep4.setString(5, "Fiction");
				prep4.setString(6, "Y");
				prep4.execute();
			}	
		}
	}
	
	
	//Adds books based on input
	public ResultSet addBooks(String isbn, String title, String author, int addYear, String genre) throws SQLException {
		
		if (conn == null) {
			getConnection();
		}
		
		PreparedStatement prep = conn.prepareStatement("INSERT INTO Books values(null,?,?,?,?,?,?);");
		prep.setString(1, isbn);
		prep.setString(2, title);
		prep.setString(3, author);
		prep.setInt(4, addYear);
		prep.setString(5, genre);
		prep.setString(6, "Y");
		prep.execute();
		
		//Allows results to be displayed
		PreparedStatement prep1 = conn.prepareStatement("SELECT isbn,title,author,year,genre,available FROM Books ORDER BY title ASC;");
		ResultSet res = prep1.executeQuery();
		
		return res;
	}
	
	
	//Removes books based on ISBN
	public ResultSet removeBooksISBN(String isbn) throws SQLException {
		
		if (conn == null) {
			getConnection();
		}
		
		PreparedStatement prep = conn.prepareStatement("DELETE FROM Books WHERE isbn = ?;");
		prep.setString(1, isbn);
		prep.executeUpdate();
		
		//Allows results to be displayed
		PreparedStatement prep1 = conn.prepareStatement("SELECT isbn,title,author,year,genre,available FROM Books ORDER BY title ASC;");
		ResultSet res = prep1.executeQuery();
		
		return res;
	}
	
	
	//Removes books based on title
	public ResultSet removeBooksTitle(String title) throws SQLException {
		
	  if (conn == null) {
			getConnection();
		}
		
		PreparedStatement prep = conn.prepareStatement("DELETE FROM Books WHERE title = ?;");
		prep.setString(1, title);
		prep.executeUpdate();
		
		//Allows results to be displayed
		PreparedStatement prep1 = conn.prepareStatement("SELECT isbn,title,author,year,genre,available FROM Books ORDER BY title ASC;");
		ResultSet res = prep1.executeQuery();
		
		return res;
	}
	
	
	//Search books based on ISBN
	public ResultSet searchISBN(String isbn) throws SQLException {
		
		if(conn == null) {
			getConnection();
		}
		
		String sql = "SELECT isbn,title,author,year,genre,available FROM Books WHERE isbn = ?;";
		
		
		PreparedStatement prep = conn.prepareStatement(sql);
		prep.setString(1, isbn);
		ResultSet res = prep.executeQuery();
		
		return res;
	}
	
	
	//Search books based on title
	public ResultSet searchTitle(String title) throws SQLException {
		
		if(conn == null) {
			getConnection();
		}
		
		String sql = "SELECT isbn,title,author,year,genre,available FROM Books WHERE title LIKE ? ORDER BY title ASC;";
		
		PreparedStatement prep = conn.prepareStatement(sql);
		prep.setString(1, title);
		ResultSet res = prep.executeQuery();
		
		return res;
	}
	
	
	//Search books based on author
	public ResultSet searchAuthor(String author) throws SQLException {
		
		if(conn == null) {
			getConnection();
		}
		
		String sql = "SELECT isbn,title,author,year,genre,available FROM Books WHERE author LIKE ? ORDER BY title ASC;";
		
		PreparedStatement prep = conn.prepareStatement(sql);
		prep.setString(1, author);
		ResultSet res = prep.executeQuery();
		
		return res;
	}
	
	
	//Search based on genre
	public ResultSet searchGenre(String genre) throws SQLException {
		
		if(conn == null) {
			getConnection();
		}
		
		String sql = "SELECT isbn,title,author,year,genre,available FROM Books WHERE genre = ? ORDER BY title ASC;";
		
		PreparedStatement prep = conn.prepareStatement(sql);
		prep.setString(1, genre);
		ResultSet res = prep.executeQuery();
		
		return res;
	}
	
	
	//Search all books
	public ResultSet searchAll() throws SQLException {
		
		if(conn == null) {
			getConnection();
		}
		
		String sql = "SELECT isbn,title,author,year,genre,available FROM Books ORDER BY title ASC;";
		
		PreparedStatement prep = conn.prepareStatement(sql);
		ResultSet res = prep.executeQuery();
		
		return res;
	}
	
	
	//Check if a book has already been check in/out
	public ResultSet checkStatus(String isbn) throws SQLException {
		
		if(conn == null) {
			getConnection();
		}
		
		PreparedStatement prep = conn.prepareStatement("SELECT available FROM Books WHERE isbn = ?;");
		prep.setString(1, isbn);
		ResultSet res = prep.executeQuery();
		
		return res;
	}
	
	
	//Check a book in
	public ResultSet checkIn(String isbn) throws SQLException {
		
		if(conn == null) {
			getConnection();
		}
		
		String sql = "UPDATE Books SET available = 'Y' WHERE isbn = ?;";
		
		PreparedStatement prep = conn.prepareStatement(sql);
		prep.setString(1, isbn);
		prep.executeUpdate();
		
		//Allows results to be displayed
		PreparedStatement prep1 = conn.prepareStatement("SELECT isbn,title,author,year,genre,available FROM Books WHERE isbn = ?;");
		prep1.setString(1, isbn);
		ResultSet res = prep1.executeQuery();
		
		return res;
	}
	
	
	//Check a book out
	public ResultSet checkOut(String isbn) throws SQLException {
		
		if(conn == null) {
			getConnection();
		}
		
		String sql = "UPDATE Books SET available = 'N' WHERE isbn = ?;";
		
		PreparedStatement prep = conn.prepareStatement(sql);
		prep.setString(1, isbn);
		prep.executeUpdate();
		
		//Allows results to be displayed
		PreparedStatement prep1 = conn.prepareStatement("SELECT isbn,title,author,year,genre,available FROM Books WHERE isbn = ?;");
		prep1.setString(1, isbn);
		ResultSet res = prep1.executeQuery();
		
		return res;
	}

}
