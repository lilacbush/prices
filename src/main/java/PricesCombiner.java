import java.util.*;
import java.util.stream.Collectors;

class PricesCombiner {
    private final Set<Price> oldPrices;
    private final Set<Price> newPrices;

    PricesCombiner(Set<Price> oldPrices, Set<Price> newPrices) {
        this.oldPrices = oldPrices;
        this.newPrices = newPrices;
    }

    Set<Price> combine() {
        newPrices.forEach(price -> {
            Set<Price> oldPricesForPrice = getSetOfOldPricesForSameProduct(price);
            if (oldPricesForPrice.isEmpty()) {
                oldPrices.add(price);
            } else {
                updatePricesForPrice(oldPricesForPrice, price);
            }
        });

        return new HashSet<>(oldPrices);
    }

    Set<Price> getSetOfOldPricesForSameProduct(Price price) {
        return oldPrices.stream().filter(p -> p.isForSameProductPrice(price)).collect(Collectors.toSet());
    }

    private void updatePricesForPrice(Set<Price> oldPricesForPrice, Price newPrice) {
        oldPricesForPrice.forEach(oldPrice -> {
            PricePeriodHelper periodHelper = new PricePeriodHelper(oldPrice, newPrice);

            boolean sameValue = oldPrice.getValue() == newPrice.getValue();

            if (periodHelper.newPricePeriodOverlapsOld()) {
                if (sameValue) {
                    periodHelper.extendOldToNew();
                } else {
                    addPrice(newPrice);
                    removePrice(oldPrice);
                }
            } else if (periodHelper.newPricePeriodIntersectsOldRight()) {
                if (sameValue) {
                    periodHelper.changeOldEndToNewEnd();
                } else {
                    addPrice(newPrice);
                    periodHelper.extendOldEndTillNewBegin();
                }
            } else if (periodHelper.newPricePeriodIntersectsOldLeft()) {
                if (sameValue) {
                    periodHelper.changeOldBeginToNewBegin();
                } else {
                    addPrice(newPrice);
                    periodHelper.startOldBeginAfterNewEnd();
                }
            } else if (periodHelper.oldPricePeriodOverlapsNew()) {
                if (!sameValue) {
                    addPrice(newPrice);
                    oldPrices.add(periodHelper.splitOld());
                }
            } else {
                addPrice(newPrice);
            }
        });
    }

    private void addPrice(Price priceToAdd) {
        oldPrices.add(priceToAdd);
    }

    private void removePrice(Price priceToRemove) {
        Iterator iterator = oldPrices.iterator();
        while (iterator.hasNext()) {
            Price p = (Price) iterator.next();
            if (p.equals(priceToRemove)) {
                iterator.remove();
            }
        }
    }
}
