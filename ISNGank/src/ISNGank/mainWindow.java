package ISNGank;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.json.JSONArray;
import org.json.JSONObject;

import ISNGank.mainWindow;

public class mainWindow {
	
	private JFrame frame;
	static String dir = Paths.get(".").toAbsolutePath().normalize().toString();
	static String url = null;
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mainWindow window = new mainWindow();
					//window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public mainWindow() {
		init();
	}
	public void init()
	{
	    JFrame frame;
    
	    JPanel textPanel, buttonPane, fieldsPanel;
	    JLabel text, username, password;
	    JTextField isnField, usernameField, passwordField;
	    JButton ok, cancel;

        frame = new JFrame("GankISN");

        textPanel = new JPanel();
        buttonPane = new JPanel();
        fieldsPanel = new JPanel();
        
        text = new JLabel(
        	"<html><p>Please enter your ISN, plus your<br> ISN Username and Password</p></html>"
        );
        text.setHorizontalAlignment(JLabel.CENTER);
        text.setVerticalAlignment(JLabel.CENTER);
        username = new JLabel("Username");
        password = new JLabel("Password");
        isnField = new JTextField("");
        usernameField = new JTextField("");
        passwordField = new JTextField("");
        ok = new JButton("OK");
        cancel = new JButton("Cancel");

        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.PAGE_AXIS));
        textPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.PAGE_AXIS));
        
        TitledBorder fieldsBorder = BorderFactory.createTitledBorder("ISN");
        fieldsBorder = BorderFactory.createTitledBorder(
        		BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "ISN");
        fieldsBorder.setTitleJustification(TitledBorder.LEFT);
        
        fieldsPanel.setBorder(fieldsBorder);
        buttonPane.setLayout(new FlowLayout());
        
        textPanel.add(text);
        fieldsPanel.add(isnField);
        fieldsPanel.add(username);
        fieldsPanel.add(usernameField);
        fieldsPanel.add(password);
        fieldsPanel.add(passwordField);
        buttonPane.add(ok);
        buttonPane.add(cancel);
        
        frame.add(textPanel, BorderLayout.PAGE_START);
        frame.add(fieldsPanel, BorderLayout.CENTER);
        frame.add(buttonPane, BorderLayout.PAGE_END);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        
    	// LISTENERS

		ok.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				if
					(
						isnField.getText() != null &
						usernameField.getText() != null &
						passwordField.getText() != null
					)
				{
					
					JFileChooser chooser = new JFileChooser();
					//set it to be a save dialog
					 chooser.setDialogType(JFileChooser.SAVE_DIALOG);
					//set a default filename (this is where you default extension first comes in)
					 chooser.setSelectedFile(new File("ISNData.json"));
					//Set an extension filter, so the user sees other XML files
				
					if(chooser.showSaveDialog(chooser) == JFileChooser.APPROVE_OPTION)
					{
					   String filename = chooser.getSelectedFile().toString();
					   if (!filename .endsWith(".json"))
					        filename += ".json";

					   url = "https://goisn.net/"+isnField.getText().toLowerCase().replaceAll("[\\\\/\\ ]", "")+"/rest/";
						System.out.println(url);
		
						JSONService ISN = new JSONService(url, usernameField.getText(), passwordField.getText());
						JSONObject serverData = new JSONObject();
						
						serverData.append("Users", ISN.getUsers());
						
						serverData.append("Clients", ISN.getClients());
						
						serverData.append("Agencies", ISN.getAgencies());
						JSONObject agentsJson = new JSONObject(ISN.getAgents());
						/*
						for(int i = 0; agentsJson.length() > i; i++)
						{
							String agentID = new JSONObject(new JSONArray(agentsJson).getString(i)).get("id").toString();
							JSONObject agentInfo = new JSONObject(ISN.getDataFromServer("agent/"+ agentID)).getJSONObject("agent");
							System.out.println(agentInfo.getString("first")+" "+agentInfo.getString("last")+"\n"+agentInfo.getString("img"));
						}
						*/
						serverData.append("Agents", agentsJson);
						
						serverData.append("EscrowOfficers", ISN.getEscrowOfficers());
						
						serverData.append("Contacts", ISN.getContacts());
						
						System.out.println(serverData.toString());
						
					    try {
						    BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
							writer.write(serverData.toString());
						    writer.close();

						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					     
					
						
					 }
					
				}
			}
		});	
	}
}















