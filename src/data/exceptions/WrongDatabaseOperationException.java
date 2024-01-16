/**
 * This exception is thrown to indicate an incorrect operation attempted on the database.
 * It extends the standard Exception class to provide more specific error handling related to database operations.
 */
package data.exceptions;

/**
 * Constructs a WrongDatabaseOperationException with a default message.
 */
public class WrongDatabaseOperationException extends Exception{
    public WrongDatabaseOperationException() {
        super();
    }
}
