/**
 * The Expandable interface defines methods for checking, retrieving and saving data related to a specific entity.
 * It is designed to be implemented by classes that require dynamic data handling capabilities
 * such as checking for new data, counting local and server entries and saving data to files.
 */
package data;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface Expandable {
    //returns false in case the fileCount is equal to serverCount, and true otherwise

    /**
     * Checks if there is new data available that is not yet present locally.
     *
     * @return true if new data is available, false otherwise.
     * @throws FileNotFoundException if the local file to check the data is not found.
     */
    public boolean checkForNewData() throws FileNotFoundException;

    /**
     * Retrieves the count of local data entries.
     *
     * @return The count of local data entries.
     * @throws IOException if there is an error in accessing the local data.
     */
    public int getLocalCount() throws IOException;

    /**
     * Retrieves the count of data entries available on the server.
     *
     * @return The count of server data entries.
     */
    public int getServerCount();

    /**
     * Saves the current data to a file. This method is typically used to update the local data cache.
     *
     * @throws FileNotFoundException if the file to sava the data is not found or inaccessible.
     */
    public void saveAsFile() throws FileNotFoundException;
}
