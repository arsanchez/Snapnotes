package clases;

import java.util.Random;

/**
 * Created by Argenis on 6/16/15.
 */
public class Utils {

    public static int randomInt(int min, int max){

        Random rand = new Random();
        int randomNum = rand.nextInt(((max-min)+1)+min);

        return randomNum;
    }
}
