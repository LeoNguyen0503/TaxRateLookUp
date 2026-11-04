

import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// import java.awt.event.FocusEvent;
// import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;

public class PostalCodeGUI extends JFrame {
    public PostalCodeGUI(){
       //set up GUI and add title
       this.setTitle("Postal Code Tax Rate Lookup");

       //configure gui to end the program when exit
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

       //set size
       setSize(450,650);

       URL iconURL = getClass().getResource("/assets/IMG_4926.png");

        if (iconURL != null) {
            ImageIcon icon = new ImageIcon(iconURL);

            // JFrame window icon (macOS and Windows)
            setIconImage(icon.getImage());

            // Windows taskbar icon only
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                if (Taskbar.isTaskbarSupported()) {
                    Taskbar taskbar = Taskbar.getTaskbar();

                    if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
                        taskbar.setIconImage(icon.getImage());
                    }
                }
            }
        }

       //make the GUI to appear at the center of the screen
       setLocationRelativeTo(null);

       //make layout manager to null to manually position our components within the gui
       setLayout(null);

       setResizable(true);

       addGuiComponents();

   }

    private void addGuiComponents(){
       //search field
       JTextField searchTextField = new JTextField();


       //set the location and size of our component
       searchTextField.setBounds(15,15,360,45);

       //change the font style and size
       searchTextField.setFont(new Font("Dialog", Font.PLAIN, 24));

       add(searchTextField);

       JPanel taxPanel = new JPanel(new GridLayout(5, 2, 12, 10));
        taxPanel.setBorder(BorderFactory.createTitledBorder("Sales tax rates"));
        taxPanel.setBounds(30, 100, 390, 220);

        JLabel totalRate = new JLabel("--");
        JLabel cityRate = new JLabel("--");
        JLabel localRate = new JLabel("--");
        JLabel countyRate = new JLabel("--");
        JLabel stateRate = new JLabel("--");

        taxPanel.add(new JLabel("Total"));
        taxPanel.add(totalRate);
        taxPanel.add(new JLabel("City"));
        taxPanel.add(cityRate);
        taxPanel.add(new JLabel("Local"));
        taxPanel.add(localRate);
        taxPanel.add(new JLabel("County"));
        taxPanel.add(countyRate);
        taxPanel.add(new JLabel("State"));
        taxPanel.add(stateRate);

        Font taxFont = new Font("Dialog", Font.PLAIN, 18);
        for (Component component : taxPanel.getComponents()) {
            component.setFont(taxFont);
        }

        Font rateFont = new Font("Dialog", Font.BOLD, 20);
        totalRate.setFont(rateFont);
        cityRate.setFont(rateFont);
        localRate.setFont(rateFont);
        countyRate.setFont(rateFont);
        stateRate.setFont(rateFont);

        add(taxPanel);

       //search button
       JButton searchButton = new JButton(loadImage("/assets/search.png"));
       //change the cursor to a hand cursor when hovering over this button
       searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
       searchButton.setBounds(380, 15, 50,44);
       searchButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               // get location from user
               String userInput = searchTextField.getText();

               // validate input - remove whitespcae to ensure non-empty text
               if (userInput.replaceAll("\\s", "").length() <=0){
                    JOptionPane.showMessageDialog(PostalCodeGUI.this, "Please enter a postal code.");
                   return;
               };

               // retrieve weather data
               JSONObject saleTaxData = PostalAPI.getSaleTaxData(userInput);

               // update GUI

                if (saleTaxData == null) {
                    JOptionPane.showMessageDialog(PostalCodeGUI.this, "Could not retrieve tax rates. Please enter a valid postal code.");
                    return;
                }

                totalRate.setText(formatRate(saleTaxData, "combine"));
                cityRate.setText(formatRate(saleTaxData, "city"));
                localRate.setText(formatRate(saleTaxData, "local"));
                countyRate.setText(formatRate(saleTaxData, "county"));
                stateRate.setText(formatRate(saleTaxData, "state"));



           }
       });
       add(searchButton);

         searchTextField.addActionListener(new ActionListener() {
         @Override
            public void actionPerformed(ActionEvent e) {
             searchButton.doClick();
            }
        });

   }

    private ImageIcon loadImage(String path){
       URL imageURL = getClass().getResource(path);

        if (imageURL == null) {
            System.err.println("Could not find image: " + path);
            return null;
        }

        return new ImageIcon(imageURL);
   }

   private String formatRate(JSONObject data, String key) {
        Object value = data.get(key);
        if (!(value instanceof Number)) {
            return "N/A";
        }

        NumberFormat formatter = NumberFormat.getPercentInstance(Locale.US);
        formatter.setMinimumFractionDigits(3);
        formatter.setMaximumFractionDigits(3);
        return formatter.format(((Number) value).doubleValue());
    }
}
