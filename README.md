Logging Erklärung
1. Es werden 2 IMPORTS benötigt:  import java.util.logging.Level;
                                  import java.util.logging.Logger;
2. Nun muss man den Logger einführen, um die Ausgaben zu ermöglichen.
   Dafür muss man in die 1.Zeile der neuen Klasse diesen Befehl eingeben:
       private static final Logger logger = Logger.getLogger(Drone.class.getName());
3. Jetzt gibt es verschiedene Möglichkeiten um die Ausgaben durchzuführen. Ich vermute ihr werdet nur normale Ausgaben machen ohne Errors etc.
   Deswegen benötigt ihr nur die Funktion INFO:
   logger.log(Level.INFO, "Das was ihr möchtet");
4. Ihr könnt natürlich auch Variablen dazu geben:
   logger.log(Level.INFO,"Drone id: " + this.id);
5. Wenn ihr Errors angeben möchtet dann funktioniert dies so:
    logger.log(Level.SEVERE, "Error retrieving the server count", e); //e ist die Exception e
