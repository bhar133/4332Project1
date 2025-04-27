package delft;

import java.util.Random;

public class Purchasing {
    public double purchaseBook() {
        Random rand = new Random();
        return 10 + rand.nextInt(91); // $10 to $100
    }
}
