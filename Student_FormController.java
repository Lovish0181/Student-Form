package Students;
import javafx.scene.control.DatePicker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class Student_FormController {
	Connection con;
	PreparedStatement pst;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnsearch;

    @FXML
    private ComboBox<String> rollCombo;

    @FXML
    private TextField txtper;

    @FXML
    private TextField txtname;

    @FXML
    private ImageView profileImage;

    @FXML
    private Button btnsave;

    @FXML
    private Button btndelete;

    @FXML
    private Button btnupdate;

    @FXML
    private TextField txtpath;

    @FXML
    private Button btnbrowse;
    
    @FXML
    private DatePicker DOB;
String filepath;
    @FXML
    void dobrowse(ActionEvent event) {
    	FileChooser chooser=new FileChooser();
    	chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("*.*", "*.*")
                );
    	File file=chooser.showOpenDialog(null);
    	filepath=file.getAbsolutePath();
    	txtpath.setText(filepath);
    	
    	try {
    		profileImage.setImage(new Image(new FileInputStream(file)));
		} 
    	catch (FileNotFoundException e) {	e.printStackTrace();}

    }

    @FXML
    void dodelete(ActionEvent event) {
    	try {
			pst=con.prepareStatement("delete from students where Rollno=?");
            pst.setInt(1,Integer.parseInt(rollCombo.getEditor().getText()));			
			int count=pst.executeUpdate();
			if(count==0)
				btndelete.setText("Invalid Roll No.");
			else
				btndelete.setText("Deleted");
			 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @FXML
    void dorollCombo(ActionEvent event) {
    	rollCombo.getEditor().getText();
    }

    @FXML
    void dosave(ActionEvent event) {
    	LocalDate dat=DOB.getValue();
    	try {
    		
			pst=con.prepareStatement("insert into students values(?,?,?,?,?,current_date())");
			java.sql.Date date=java.sql.Date.valueOf(dat);
			pst.setString(1,txtname.getText());
			pst.setInt(2,Integer.parseInt(rollCombo.getEditor().getText()));
			pst.setFloat(3,Float.parseFloat(txtper.getText()));
			pst.setString(4,txtpath.getText());
		    pst.setDate(5,date);
			pst.executeUpdate();
			btnsave.setText("Saved");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @FXML
    void dosearch(ActionEvent event) {
    	try {
			pst=con.prepareStatement("select * from students where Rollno=?");
			pst.setInt(1,Integer.parseInt(rollCombo.getEditor().getText()));
			ResultSet records=pst.executeQuery();
			if(records.next())
			{
				String sname=records.getString("Name");
				int rollno=records.getInt("Rollno");
				float percentage=records.getFloat("Per");
				String path=records.getString("Picpath");
				@SuppressWarnings("unused")
				String date=records.getString("DOB");
				//date---------------------
				rollCombo.getEditor().setText(String.valueOf(rollno));
				txtname.setText(sname);
				txtper.setText(String.valueOf(percentage));
				txtpath.setText(path);
				//LocalDate dat=LocalDate.parse(date);
				//DOB.setValue(dat);				
			}
			else
			{
				btnsearch.setText("Invalid Roll");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	try {
			profileImage.setImage(new Image(new FileInputStream(txtpath.getText())));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


    }

    @FXML
    void doupdate(ActionEvent event) {
    	try {
    	pst=con.prepareStatement("UPDATE students SET Name=?,Per=?,Picpath=? where Rollno=?");
    	
    	
    	pst.setString(1,txtname.getText());
    	pst.setFloat(2,Float.parseFloat(txtper.getText()));
		pst.setString(3,txtpath.getText());
		pst.setInt(4,Integer.parseInt(rollCombo.getEditor().getText()));
		//pst.setDate(5,DOB);
		//pst.setDate(6,, null);
		pst.executeUpdate();
		btnupdate.setText("Updated");
		/*if(count==0)
		{
			 btnupdate.setText("Invalid roll");
		}
		else
		    	
			
		*/ 
	} 
		catch (SQLException e) {
		e.printStackTrace();
    }
    }
    void fillRolls()
    {
	   ArrayList<String> rolls=new ArrayList<String>();
    	try {
			pst=con.prepareStatement("select distinct Rollno from students" );
			ResultSet records=	pst.executeQuery();
			
			while(records.next())//checks more records
			{
				int roll=records.getInt("Rollno");
				rolls.add(String.valueOf(roll));
				System.out.println(roll);
			}
			rollCombo.getItems().addAll(rolls);
			
		} 
    	catch (SQLException e) {
			e.printStackTrace();
		}
		

    }
    
    @FXML
    void doshowall(ActionEvent event) 
    {
    		try {
				pst=con.prepareStatement("select * from students" );
				ResultSet records=	pst.executeQuery();
				
				while(records.next())//checks more records
				{
					int roll=records.getInt("Rollno");
					String sname=records.getString("Name");
					float per=records.getFloat("Per");
					String path=records.getString("Picpath");
					java.sql.Date dt= records.getDate("DOB");
					DOB.setValue(dt.toLocalDate());
					System.out.println(roll+"  "+sname+"  "+per+" "+path);
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    }

    @FXML
    void initialize() {
    	con=Connect.getConnection();
    	fillRolls();
    }
}
