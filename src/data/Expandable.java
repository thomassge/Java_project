package data;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface Expandable {
    //returns false in case the fileCount is equal to serverCount, and true otherwise
    public boolean checkForNewData() throws FileNotFoundException;
    public int getLocalCount() throws IOException;
    public int getServerCount();
    public void saveAsFile() throws FileNotFoundException;
}
