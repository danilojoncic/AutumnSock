package comedic.relief.framework.eye_candy;

import com.github.lalyos.jfiglet.FigletFont;

import java.io.IOException;
import static comedic.relief.framework.eye_candy.ColorPicker.*;


public class BannerLoader {
    public static void printBanner() {
        String bannerText = "AutumnSock";
        try {
            String banner = FigletFont.convertOneLine(bannerText);
            System.out.println(ANSI_GREEN + banner + ANSI_RESET);
            System.out.println("It's not" + ANSI_GREEN + " SPRING" + ANSI_RESET + " and we are not wearing "+ColorPicker.ANSI_GREEN + "BOOT"+ ANSI_RESET + "s...");
        } catch (IOException e) {
            System.out.println("Error generating banner");
        }
    }
}
