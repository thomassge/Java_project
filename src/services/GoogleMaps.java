package services;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
//import com.teamdev.jxbrowser.chromium.Browser;
//import com.teamdev.jxbrowser.chromium.BrowserFactory;

public class GoogleMaps {
    public static void main(String[] args) {
        JFrame test = new JFrame("Google Maps");

        try {
            //String imageUrl = "https://api.tomtom.com/map/1/staticimage?key=wGzEG9NQKwuVBSDV8Wzy6BemGZp20KuR&zoom=15&center=8.678137129," +
                    //"50.107668121&format=jpg&layer=basic&style=main&width=512&height=512&view=Unified&language=de-DE";

            String imageUrl = "https://api.tomtom.com/map/1/staticimage?key=wGzEG9NQKwuVBSDV8Wzy6BemGZp20KuR&zoom=13&center=8.678137129,50.107668121&format=jpg&style=marker&layer=basic&main=bg&size=regular";
            String destinationFile = "image.jpg";
            String str = destinationFile;
            URL url = new URL(imageUrl);
            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream(destinationFile);

            byte[] b = new byte[2048];
            int length;

            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }

            is.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        test.add(new JLabel(new ImageIcon((new ImageIcon("image.jpg")).getImage().getScaledInstance(630, 600,
                java.awt.Image.SCALE_SMOOTH))));

        test.setVisible(true);
        test.pack();

    }
}