package nodomain.com.i_news.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mukhamed.issa on 5/28/16.
 */

public class DateParser {

    public static Date parse(String input) {
        try {
            SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssz" );

            if (input.endsWith( "Z" )) {
                input = input.substring(0, input.length() - 1) + "GMT-00:00";
            } else {
                int inset = 6;

                String s0 = input.substring(0, input.length() - inset);
                String s1 = input.substring(input.length() - inset, input.length());

                input = s0 + "GMT" + s1;
            }

            return df.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String toString(Date input) {
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.US);
        return dateFormat.format(input);
    }
}
