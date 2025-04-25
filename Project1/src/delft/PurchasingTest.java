package delft;

import org.junit.jupiter.api.*;
import net.jqwik.api.*;
import static org.junit.jupiter.api.Assertions.*;


class PurchasingTest {

    private final Purchasing purchasing = new Purchasing();

    @Provide
    Arbitrary<Double> randomPurchases() {
        return Arbitraries.create(() -> purchasing.purchaseBook());
    }

    // Test the price is always from 10 to 100
    @Property
    void purchaseBookAlwaysReturnsValidPrice(@ForAll("randomPurchases") double price) {
        assertTrue(price>=10&&price<=100);
    }

    // Test we get different values on each call
    @Test
    void multipleCallsProduceDifferentValues() {
        double first = purchasing.purchaseBook();
        double second = purchasing.purchaseBook();
        double third = purchasing.purchaseBook();

        assertTrue(first!=second&&second!=third);
    }
}
