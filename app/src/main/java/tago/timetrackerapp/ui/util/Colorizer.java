package tago.timetrackerapp.ui.util;

import android.graphics.Color;

import java.util.Random;

public class Colorizer {

    static final char [] hex = { '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    public static int getRandomColor() {
        char [] s = new char[7];
        Random r = new Random();
        int n = r.nextInt(0x1000000);
        s[0] = '#';
        for (int i=1;i<7;i++) {
            s[i] = hex[n & 0xf];
            n >>= 4;
        }
        return Color.parseColor(new String(s));
    }

}
