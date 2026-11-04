
import javax.swing.*;


public class Main {
    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                //display GUI
                new PostalCodeGUI().setVisible(true);
            //    System.out.println(PostalAPI.getSaleTaxData("70112"));
            }
        });
    }
}