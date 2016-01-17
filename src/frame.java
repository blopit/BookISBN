import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class frame extends JFrame {
	private TextField ISBNField;
	private String fileChosen;
	private HoughLineDetection ISBNDETECTOR;
	String path;
	BufferedImage img,img2;
	JLabel jl,jl2;
	public frame(){
		
		/*
		 * mItemOpen.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                fc.showOpenDialog(null);
                filePath = fc.getSelectedFile();
                path = filePath.getPath();
                txtPath.setText(path);
                try {
                    //view.removeAll();
                    myPicture = ImageIO.read(new File(path));
                    JLabel picLabel = new JLabel(new ImageIcon(myPicture));
                    view.add(picLabel);
                    revalidate();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });*/
		
		super("ISBN Detector");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(2000,1000);
		setLayout(new FlowLayout());
		ISBNField = new TextField(40);
		ISBNField.setEditable(false);
		ISBNDETECTOR = new HoughLineDetection();
		
	    JFileChooser fileChooser = new JFileChooser(".");
	    fileChooser.setControlButtonsAreShown(false);
	    add(fileChooser, BorderLayout.CENTER);
	    
	    ActionListener actionListener = new ActionListener() {
	      public void actionPerformed(ActionEvent actionEvent) {
	    	 if(jl != null){ 
	    	  remove(jl);
	    	  revalidate();
	    	  repaint();
	    	 }
	    	 if(jl2 != null){ 
		    	  remove(jl2);
		    	  revalidate();
		    	  repaint();
		    	 }
	    	  
	        JFileChooser theFileChooser = (JFileChooser) actionEvent
	            .getSource();
	        String command = actionEvent.getActionCommand();
	        if (command.equals(JFileChooser.APPROVE_SELECTION)) {
	          File selectedFile = theFileChooser.getSelectedFile();
	          fileChosen = selectedFile.getName();
	          try {
				img = ImageIO.read(new File(fileChosen));
				jl = new JLabel();
			    jl.setMaximumSize(new Dimension(300, 450));
				jl.setIcon(new ImageIcon(img));
			    add(jl);
			    
			    
			    
				revalidate();
				repaint();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	          ISBNField.setText(ISBNDETECTOR.getISBN(fileChosen));
	          
	          try {
				img2 = ImageIO.read(new File("gaussian45.jpg"));
				jl2 = new JLabel();
			    jl2.setMaximumSize(new Dimension(300, 450));
				jl2.setIcon(new ImageIcon(img2));
			    add(jl2);
			    revalidate();
				repaint();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
	          
	        } else if (command.equals(JFileChooser.CANCEL_SELECTION)) {
	          fileChosen = "";
	        }
	      }
	    };
	    fileChooser.addActionListener(actionListener);
	    
	    add(ISBNField);
		setVisible(true);

	}
	public static void main(String[] str){
		new frame();
	}
	
}
