public class Anagramm {
    public static boolean anagrammCheck(String arg0, String arg1) {
        boolean anagramm = false;
        if (arg0.length() != arg1.length()) {
            return false;
        }

        final int[] arr = new int[arg0.length()];

        for (int i = 0; i < arg0.length(); i++) {
            int a = arg0.charAt(i);
            int b = arg1.charAt(i);

            int c = a - b;

            arr[i] = c;
        }

        if (checkSum(arr) == 0) {
            return true;
        }
        else 
            return anagramm;
    }

    public static int checkSum(int array[]) {
        int result = 0;
        for(int zahl : array) {
            result = zahl + result;
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(anagrammCheck(args[0], args[1]));
    }
}