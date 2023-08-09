public class HighestDifferentBit {
    public static void main(String[] args) {
        String str1 = "101010101";
        String str2 = "101111101";

        int position = findHighestDifferentBit(str1, str2);
        if (position != -1) {
            System.out.println("The highest different bit is at position: " + position);
        } else {
            System.out.println("No difference found.");
        }
    }

    public static int findHighestDifferentBit(String str1, String str2) {
        int length = Math.min(str1.length(), str2.length());

        for (int i = 0; i < length; i++) {
            if (str1.charAt(i) != str2.charAt(i)) {
                return length - i - 1;
            }
        }

        return -1; // No difference found
    }
}
