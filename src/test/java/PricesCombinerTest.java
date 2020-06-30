import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PricesCombinerTest {

    /*
    Пересекающиеся даты, тот же самый номер и отдел, но другой продукт - должны просто добавиться
     */
    @Test
    public void combineShouldAddPriceForProductWithoutPrice() {
        Price price1 = new Price(1, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 1, 0, 0, 0), LocalDateTime.of(2019, Month.APRIL, 1, 23, 59, 59), 12000);
        Price price2 = new Price(2, "002", 1, 1, LocalDateTime.of(2019, Month.MARCH, 1, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 3, 23, 59, 59), 12000);

        Set<Price> oldPrices = new HashSet<>();
        oldPrices.add(price1);

        Set<Price> newPrices = new HashSet<>();
        newPrices.add(price2);

        PricesCombiner combiner = new PricesCombiner(oldPrices, newPrices);

        Set<Price> expected = new HashSet<>();
        expected.add(price1);
        expected.add(price2);

        Set<Price> actual = combiner.combine();

        assertThat(actual, is(expected));
    }

    /*
    Пересекающиеся даты, тот же самый номер и продукт, но другой отдел - должны просто добавиться
     */
    @Test
    public void combineShouldAddPriceIfPeriodsOverlapButNewPriceIsForAnotherDepart() {
        Price price1 = new Price(1, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 1, 0, 0, 0), LocalDateTime.of(2019, Month.APRIL, 1, 23, 59, 59), 12000);
        Price price2 = new Price(2, "001", 1, 2, LocalDateTime.of(2019, Month.MARCH, 1, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 3, 23, 59, 59), 10000);

        Set<Price> oldPrices = new HashSet<>();
        oldPrices.add(price1);

        Set<Price> newPrices = new HashSet<>();
        newPrices.add(price2);

        PricesCombiner combiner = new PricesCombiner(oldPrices, newPrices);

        Set<Price> expected = new HashSet<>();
        expected.add(price1);
        expected.add(price2);

        Set<Price> actual = combiner.combine();

        assertThat(actual, is(expected));
    }

    /*
    Пересекающиеся даты, тот же самый продукт и отдел, но другой номер - должны просто добавиться
     */
    @Test
    public void combineShouldAddPriceIfPeriodsOverlapButNewPriceHasAnotherNumber() {
        Price price1 = new Price(1, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 1, 0, 0, 0), LocalDateTime.of(2019, Month.APRIL, 1, 23, 59, 59), 12000);
        Price price2 = new Price(2, "001", 2, 1, LocalDateTime.of(2019, Month.MARCH, 1, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 3, 23, 59, 59), 10000);

        Set<Price> oldPrices = new HashSet<>();
        oldPrices.add(price1);

        Set<Price> newPrices = new HashSet<>();
        newPrices.add(price2);

        PricesCombiner combiner = new PricesCombiner(oldPrices, newPrices);

        Set<Price> expected = new HashSet<>();
        expected.add(price1);
        expected.add(price2);

        Set<Price> actual = combiner.combine();

        assertThat(actual, is(expected));
    }

    /*
    Тот же самый продукт, номер и отдел, но даты не пересекаются - должны просто добавиться
     */
    @Test
    public void combineShouldAddPriceIfPeriodsNotIntersect() {
        Price price1 = new Price(1, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 1, 0, 0, 0), LocalDateTime.of(2019, Month.APRIL, 1, 23, 59, 59), 12000);
        Price price2 = new Price(2, "001", 1, 1, LocalDateTime.of(2019, Month.APRIL, 2, 0, 0, 0), LocalDateTime.of(2019, Month.APRIL, 3, 23, 59, 59), 10000);

        Set<Price> oldPrices = new HashSet<>();
        oldPrices.add(price1);

        Set<Price> newPrices = new HashSet<>();
        newPrices.add(price2);

        PricesCombiner combiner = new PricesCombiner(oldPrices, newPrices);

        Set<Price> expected = new HashSet<>();
        expected.add(price1);
        expected.add(price2);

        Set<Price> actual = combiner.combine();

        assertThat(actual, is(expected));
    }

    /*
    Тот же самый продукт, номер и отдел, даты пересекаются, значения цен одинаковые:
    период действия имеющейся цены должен увеличиться согласно периоду новой цены
     */
    @Test
    public void combineShouldExtendPeriodIfNewPriceIsSame() {
        Price price1 = new Price(1, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 1, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 15, 23, 59, 59), 12000);
        Price price2 = new Price(2, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 4, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 20, 23, 59, 59), 12000);
        Price resultPrice = new Price(1, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 1, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 20, 23, 59, 59), 12000);

        Set<Price> oldPrices = new HashSet<>();
        oldPrices.add(price1);

        Set<Price> newPrices = new HashSet<>();
        newPrices.add(price2);

        PricesCombiner combiner = new PricesCombiner(oldPrices, newPrices);

        Set<Price> expected = new HashSet<>();
        expected.add(resultPrice);

        Set<Price> actual = combiner.combine();

        assertThat(actual, is(expected));
    }

    /*
    Тот же самый продукт, номер и отдел, даты пересекаются (старый диапазон включает в себя новый диапазон), значения цен одинаковые:
    период действия имеющейся цены должен остаться таким же
     */
    @Test
    public void combineShouldSavePeriodIfNewPeriodIsInOldPeriod() {
        Price price1 = new Price(1, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 1, 0, 0, 0), LocalDateTime.of(2019, Month.APRIL, 1, 23, 59, 59), 12000);
        Price price2 = new Price(2, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 4, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 6, 23, 59, 59), 12000);
        Price resultPrice = new Price(1, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 1, 0, 0, 0), LocalDateTime.of(2019, Month.APRIL, 1, 23, 59, 59), 12000);

        Set<Price> oldPrices = new HashSet<>();
        oldPrices.add(price1);

        Set<Price> newPrices = new HashSet<>();
        newPrices.add(price2);

        PricesCombiner combiner = new PricesCombiner(oldPrices, newPrices);

        Set<Price> expected = new HashSet<>();
        expected.add(resultPrice);

        Set<Price> actual = combiner.combine();

        assertThat(actual, is(expected));
    }

    /*
    Тот же самый продукт, номер и отдел, даты пересекаются (новый диапазон включает в себя старый диапазон), значения цен одинаковые:
    период действия имеющейся цены должен увеличиться согласно периоду новой цены
     */
    @Test
    public void combineShouldExtendPeriodIfOldPeriodIsInNewPeriod() {
        Price price1 = new Price(1, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 3, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 5, 23, 59, 59), 12000);
        Price price2 = new Price(2, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 1, 0, 0, 0), LocalDateTime.of(2019, Month.APRIL, 13, 23, 59, 59), 12000);
        Price resultPrice = new Price(1, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 1, 0, 0, 0), LocalDateTime.of(2019, Month.APRIL, 13, 23, 59, 59), 12000);

        Set<Price> oldPrices = new HashSet<>();
        oldPrices.add(price1);

        Set<Price> newPrices = new HashSet<>();
        newPrices.add(price2);

        PricesCombiner combiner = new PricesCombiner(oldPrices, newPrices);

        Set<Price> expected = new HashSet<>();
        expected.add(resultPrice);

        Set<Price> actual = combiner.combine();

        assertThat(actual, is(expected));
    }

    /*
    Тот же самый продукт, номер и отдел, даты пересекаются (новая заканчивается позже), значения цен разные:
    должна добавиться новая цена, а период действия старой цены должен уменьшиться согласно периоду новой цены
     */
    @Test
    public void combineShouldAddNewAndReduceOldIfIntersectRight() {
        Price price1 = new Price(1, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 3, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 10, 23, 59, 59), 12000);
        Price price2 = new Price(2, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 5, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 13, 23, 59, 59), 13000);
        Price resultPrice1 = new Price(1, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 3, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 4, 23, 59, 59), 12000);

        Set<Price> oldPrices = new HashSet<>();
        oldPrices.add(price1);

        Set<Price> newPrices = new HashSet<>();
        newPrices.add(price2);

        PricesCombiner combiner = new PricesCombiner(oldPrices, newPrices);

        Set<Price> expected = new HashSet<>();
        expected.add(resultPrice1);
        expected.add(price2);

        Set<Price> actual = combiner.combine();

        assertThat(actual, is(expected));
    }

    /*
    Тот же самый продукт, номер и отдел, даты пересекаются (новая начинается раньше), значения цен разные:
    должна добавиться новая цена, а период действия старой цены должен уменьшиться согласно периоду новой цены
     */
    @Test
    public void combineShouldAddNewAndReduceOldIfIntersectLeft() {
        Price price1 = new Price(1, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 10, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 30, 23, 59, 59), 12000);
        Price price2 = new Price(2, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 5, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 13, 23, 59, 59), 13000);
        Price resultPrice1 = new Price(1, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 14, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 30, 23, 59, 59), 12000);

        Set<Price> oldPrices = new HashSet<>();
        oldPrices.add(price1);

        Set<Price> newPrices = new HashSet<>();
        newPrices.add(price2);

        PricesCombiner combiner = new PricesCombiner(oldPrices, newPrices);

        Set<Price> expected = new HashSet<>();
        expected.add(resultPrice1);
        expected.add(price2);

        Set<Price> actual = combiner.combine();

        assertThat(actual, is(expected));
    }

    /*
    Тот же самый продукт, номер и отдел, даты пересекаются (старый диапазон включает в себя новый диапазон), значения цен разные:
    период действия старой цены должен уменьшиться до начала нового диапазона,
    должна добавиться новая цена,
    должна добавиться ещё одна цена с диапазоном от конца новой до конца старой
     */
    @Test
    public void combineShouldAddNewAndSplitOldIfNewPeriodIsInOldPeriod() {
        Price price1 = new Price(1, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 1, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 20, 23, 59, 59), 12000);
        Price price2 = new Price(2, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 5, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 13, 23, 59, 59), 13000);
        Price resultPrice1 = new Price(1, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 1, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 4, 23, 59, 59), 12000);
        Price resultPrice2 = new Price("001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 14, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 20, 23, 59, 59), 12000);

        Set<Price> oldPrices = new HashSet<>();
        oldPrices.add(price1);

        Set<Price> newPrices = new HashSet<>();
        newPrices.add(price2);

        PricesCombiner combiner = new PricesCombiner(oldPrices, newPrices);

        Set<Price> expected = new HashSet<>();
        expected.add(resultPrice1);
        expected.add(price2);
        expected.add(resultPrice2);

        Set<Price> actual = combiner.combine();

        assertThat(actual, is(expected));
    }

    /*
    Тот же самый продукт, номер и отдел, даты пересекаются (новый диапазон включает в себя старый диапазон), значения цен разные:
    новая цена должна быть добавлена, старая не должна попасть в результат
     */
    @Test
    public void combineShouldAddNewAndExcludeOldIfOldPeriodIsInNewPeriod() {
        Price price1 = new Price(1, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 15, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 20, 23, 59, 59), 12000);
        Price price2 = new Price(2, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 5, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 30, 23, 59, 59), 13000);

        Set<Price> oldPrices = new HashSet<>();
        oldPrices.add(price1);

        Set<Price> newPrices = new HashSet<>();
        newPrices.add(price2);

        PricesCombiner combiner = new PricesCombiner(oldPrices, newPrices);

        Set<Price> expected = new HashSet<>();
        expected.add(price2);

        Set<Price> actual = combiner.combine();

        assertThat(actual, is(expected));
    }

    /*
    Тот же самый продукт, номер и отдел, новая пересекается с несколькими старыми, значения цен разные:
    новая должна добавиться, периоды старых уменьшиться
     */
    @Test
    public void combineShouldAddNewAndReduceOldIfIntersectFew() {
        Price price1 = new Price(1, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 3, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 10, 23, 59, 59), 10000);
        Price price2 = new Price(2, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 11, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 15, 23, 59, 59), 12000);
        Price price3 = new Price(3, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 7, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 13, 23, 59, 59), 13000);
        Price resultPrice1 = new Price(1, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 3, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 6, 23, 59, 59), 10000);
        Price resultPrice2 = new Price(2, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 14, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 15, 23, 59, 59), 12000);

        Set<Price> oldPrices = new HashSet<>();
        oldPrices.add(price1);
        oldPrices.add(price2);

        Set<Price> newPrices = new HashSet<>();
        newPrices.add(price3);

        PricesCombiner combiner = new PricesCombiner(oldPrices, newPrices);

        Set<Price> expected = new HashSet<>();
        expected.add(resultPrice1);
        expected.add(price3);
        expected.add(resultPrice2);

        Set<Price> actual = combiner.combine();

        assertThat(actual, is(expected));
    }

    /*
    Тот же самый продукт, номер и отдел, несколько новых пересекаются с несколькими старыми, значения цен разные:
    новые должны добавиться, периоды старых уменьшиться, полностью покрывемая старая не должна попасть в результат
     */
    @Test
    public void combineShouldAddNewAndReduceOldIfFewIntersectFew() {
        Price price1 = new Price(1, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 3, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 6, 23, 59, 59), 10000);
        Price price2 = new Price(2, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 7, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 20, 23, 59, 59), 12000);
        Price price3 = new Price(3, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 21, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 30, 23, 59, 59), 13000);
        Price price4 = new Price(4, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 5, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 10, 23, 59, 59), 9000);
        Price price5 = new Price(5, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 11, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 25, 23, 59, 59), 14000);

        Price resultPrice1 = new Price(1, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 3, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 4, 23, 59, 59), 10000);
        Price resultPrice2 = new Price(2, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 26, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 30, 23, 59, 59), 13000);

        Set<Price> oldPrices = new HashSet<>();
        oldPrices.add(price1);
        oldPrices.add(price2);
        oldPrices.add(price3);

        Set<Price> newPrices = new HashSet<>();
        newPrices.add(price4);
        newPrices.add(price5);

        PricesCombiner combiner = new PricesCombiner(oldPrices, newPrices);

        Set<Price> expected = new HashSet<>();
        expected.add(resultPrice1);
        expected.add(price4);
        expected.add(price5);
        expected.add(resultPrice2);

        Set<Price> actual = combiner.combine();

        assertThat(actual, is(expected));
    }

    /*
    Тот же самый продукт, номер и отдел, диапазон новой перекрывает несколько старых, значения цен разные:
    новая должна добавиться, все старые не должны попасть в результат
     */
    @Test
    public void combineShouldAddNewAndExcludeOldIfNewCoverFewOld() {
        Price price1 = new Price(1, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 3, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 6, 23, 59, 59), 10000);
        Price price2 = new Price(2, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 7, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 20, 23, 59, 59), 12000);
        Price price3 = new Price(3, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 3, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 30, 23, 59, 59), 13000);

        Set<Price> oldPrices = new HashSet<>();
        oldPrices.add(price1);
        oldPrices.add(price2);

        Set<Price> newPrices = new HashSet<>();
        newPrices.add(price3);

        PricesCombiner combiner = new PricesCombiner(oldPrices, newPrices);

        Set<Price> expected = new HashSet<>();
        expected.add(price3);

        Set<Price> actual = combiner.combine();

        assertThat(actual, is(expected));
    }

    @Test
    public void getSetOfOldPricesForSameProductShouldReturnOnlySamePrices() {
        Price price1 = new Price(1, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 3, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 6, 23, 59, 59), 10000);
        Price price2 = new Price(2, "001", 1, 1, LocalDateTime.of(2019, Month.MARCH, 7, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 20, 23, 59, 59), 12000);
        Price price3 = new Price(3, "001", 2, 1, LocalDateTime.of(2019, Month.FEBRUARY, 3, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 30, 23, 59, 59), 13000);
        Price price4 = new Price(4, "001", 1, 2, LocalDateTime.of(2019, Month.JANUARY, 3, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 30, 23, 59, 59), 13000);
        Price price5 = new Price(5, "002", 1, 1, LocalDateTime.of(2019, Month.JANUARY, 1, 0, 0, 0), LocalDateTime.of(2019, Month.MARCH, 30, 23, 59, 59), 13000);

        Set<Price> oldPrices = new HashSet<>();
        oldPrices.add(price1);
        oldPrices.add(price2);
        oldPrices.add(price3);
        oldPrices.add(price4);
        oldPrices.add(price5);

        Set<Price> newPrices = new HashSet<>();

        PricesCombiner combiner = new PricesCombiner(oldPrices, newPrices);

        Set<Price> expected = new HashSet<>();
        expected.add(price1);
        expected.add(price2);

        Set<Price> actual = combiner.getSetOfOldPricesForSameProduct(price1);

        assertThat(actual, is(expected));
    }
}
