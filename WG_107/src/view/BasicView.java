package view;

public class BasicView {
    public static String centeredText(String text, int width) {
        int textLength = text.length();
        return width > textLength
                ? /*true*/ (" ".repeat((width - textLength) / 2) + text)
                : /*false*/ text;
    }

    public static void headerMenu(String menuName, int width) {
        System.out.println("\n" + "=".repeat(width));
        System.out.println(centeredText(menuName, width));
        System.out.println("=".repeat(width) + "\n");
    }

    public static void footerMenu(String inputCommand, int width) {
        System.out.println("\n" + "=".repeat(width));
        System.out.print(inputCommand);
    }
}
