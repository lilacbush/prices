import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.Assert.*;

public class PricePeriodHelperTest {

    @Test
    public void newPricePeriodOverlapsOldShouldReturnTrueIfOverlaps() {
        Price price1 = new Price(1, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 3, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 10, 23, 59, 59), 12000);
        Price price2 = new Price(2, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 3, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 10, 23, 59, 59), 13000);

        PricePeriodHelper periodHelper = new PricePeriodHelper(price1, price2);
        assertTrue(periodHelper.newPricePeriodOverlapsOld());
    }

    @Test
    public void newPricePeriodOverlapsOldShouldReturnFalseIfNotOverlap() {
        Price price1 = new Price(1, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 3, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 10, 23, 59, 59), 12000);
        Price price2 = new Price(2, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 3, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 10, 23, 59, 58), 13000);

        PricePeriodHelper periodHelper = new PricePeriodHelper(price1, price2);
        assertFalse(periodHelper.newPricePeriodOverlapsOld());
    }

    @Test
    public void newPricePeriodIntersectsOldRightShouldReturnTrueIfIntersects() {
        Price price1 = new Price(1, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 3, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 10, 23, 59, 59), 12000);
        Price price2 = new Price(2, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 3, 0, 0, 1), LocalDateTime.of(2019, Month.MARCH, 10, 23, 59, 59), 13000);

        PricePeriodHelper periodHelper = new PricePeriodHelper(price1, price2);
        assertTrue(periodHelper.newPricePeriodIntersectsOldRight());
    }

    @Test
    public void newPricePeriodIntersectsOldRightShouldReturnFalseIfNotIntersect() {
        Price price1 = new Price(1, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 3, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 10, 23, 59, 59), 12000);
        Price price2 = new Price(2, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 3, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 10, 23, 59, 58), 13000);

        PricePeriodHelper periodHelper = new PricePeriodHelper(price1, price2);
        assertFalse(periodHelper.newPricePeriodIntersectsOldRight());
    }

    @Test
    public void newPricePeriodIntersectsOldLeftShouldReturnTrueIfIntersects() {
        Price price1 = new Price(1, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 3, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 10, 23, 59, 59), 12000);
        Price price2 = new Price(2, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 3, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 10, 23, 59, 58), 13000);

        PricePeriodHelper periodHelper = new PricePeriodHelper(price1, price2);
        assertTrue(periodHelper.newPricePeriodIntersectsOldLeft());
    }

    @Test
    public void newPricePeriodIntersectsOldLeftShouldReturnFalseIfNotIntersect() {
        Price price1 = new Price(1, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 3, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 10, 23, 59, 59), 12000);
        Price price2 = new Price(2, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 3, 0, 0, 1), LocalDateTime.of(2019, Month.MARCH, 10, 23, 59, 59), 13000);

        PricePeriodHelper periodHelper = new PricePeriodHelper(price1, price2);
        assertFalse(periodHelper.newPricePeriodIntersectsOldLeft());
    }

    @Test
    public void oldPricePeriodOverlapsNewShouldReturnTrueIfOverlaps() {
        Price price1 = new Price(1, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 3, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 10, 23, 59, 59), 12000);
        Price price2 = new Price(2, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 3, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 10, 23, 59, 59), 13000);

        PricePeriodHelper periodHelper = new PricePeriodHelper(price1, price2);
        assertTrue(periodHelper.oldPricePeriodOverlapsNew());
    }

    @Test
    public void oldPricePeriodOverlapsNewShouldReturnFalseIfNotOverlap() {
        Price price1 = new Price(1, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 3, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 10, 23, 59, 58), 12000);
        Price price2 = new Price(2, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 3, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 10, 23, 59, 59), 13000);

        PricePeriodHelper periodHelper = new PricePeriodHelper(price1, price2);
        assertFalse(periodHelper.oldPricePeriodOverlapsNew());
    }
}
