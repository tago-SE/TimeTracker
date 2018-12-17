package tago.timetrackerapp.ui.util;

import android.graphics.Color;

import java.util.Random;

public class Colorizer {

    static final char [] hex = { '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    public static String getRandomColorStr() {
        char [] s = new char[7];
        Random r = new Random();
        int n = r.nextInt(0x1000000);
        s[0] = '#';
        for (int i=1;i<7;i++) {
            s[i] = hex[n & 0xf];
            n >>= 4;
        }
        return new String(s);
    }

    public static int getRandomColor() {
        return Color.parseColor(new String(getRandomColorStr()));
    }

        public static int getRandomColorExcludeBlack() {
            String colorStr = getRandomColorStr();
            int zeroCount = 0;
            for (int i = 0; i < colorStr.length(); i++) {
                if (colorStr.charAt(i) == '0') {
                    zeroCount++;
                }
            }
            if (zeroCount >= 6)
                return getRandomColorExcludeBlack();
            return Color.parseColor(colorStr);
    }

}
