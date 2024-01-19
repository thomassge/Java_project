package processing;

import java.io.*;

public interface Streamable {
    public static String reader(String filename) {
        StringBuilder responseContent;
        try {
            String data;
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
            responseContent = new StringBuilder();
            while ((data = reader.readLine()) != null) {
                responseContent.append(data);
            }   // Erschafft den String
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("In reader: " + e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println("In reader: " + e);
            throw new RuntimeException(e);
        }
        return responseContent.toString();
    }

    public static void writer(String data, String filename) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            System.out.println("In writer: " + e);
            throw new RuntimeException(e);
        }
    }
}