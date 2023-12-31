package data;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface Expandable {
    //returns false in case the fileCount is equal to serverCount, and true otherwise
    public boolean checkForNewData(int count) throws FileNotFoundException;
    public int getCountOffLocalJson() throws IOException;
    public void saveAsFile();
}
