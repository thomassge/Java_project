public class Four {

    public static boolean anagrammTester(String[] args) {
        boolean anagramm = false;
        int arg1 = args[0].length();
        int arg2 = args[1].length();

        if(arg1 != arg2) {
            return false;
        }
        
        for (int i = 0; i < args[0].length(); i++) {
            
            for (int j = 0; j < args[1].length(); j++) {
                
                if(args[0].charAt(i) == args[1].charAt(j)) {
                    args[0] = args[0].replace(args[0].charAt(i), '\0');
                    args[1] = args[1].replace(args[1].charAt(j), '\0');
                    if((args[1].isBlank() == true) && (args[0].isBlank() == true)) {
                        return true;
                    }
                    j = 500;
                }

                else if(args[0].charAt(i) != args[1].charAt(j)) {
                    if(j >= args[1].length()) {
                        return false;
                    }
                }
            }

        };

        return anagramm;
    };
    public static void main(String[] args) {

        

        System.out.println(anagrammTester(args));

        /*String hallo = "hallo";

        hallo = hallo.replace(hallo.charAt(1), '\0');
        System.out.println(hallo);*/
        


        /*String hallo = "Hallo";
        for (int i = 0; i < hallo.length(); i++) {
            hallo = args[0].substring(0, i);
            System.out.println(hallo);
        } */
    }
}