package processing;

/**
 * This interface is to be implemented by any class that holds information
 * which needs to be displayed and therefore initialized.
 * @param <T> Takes in the specific datatype of the object that needs to be created
 * and therefore returned later.
 */
public interface Initializable<T>  {
    /**
     * This method is to be overwritten by the specific class that wants to initialize data.
     * @return The specific datatype that was created.
     */
    T initialize();
}
