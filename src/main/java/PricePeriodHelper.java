import java.time.LocalDateTime;

class PricePeriodHelper {
    private Price oldPrice;
    private LocalDateTime oldBegin;
    private LocalDateTime oldEnd;
    private LocalDateTime newBegin;
    private LocalDateTime newEnd;

    PricePeriodHelper(Price oldPrice, Price newPrice) {
        this.oldPrice = oldPrice;
        oldBegin = oldPrice.getBegin();
        oldEnd = oldPrice.getEnd();
        newBegin = newPrice.getBegin();
        newEnd = newPrice.getEnd();
    }

    boolean newPricePeriodOverlapsOld() {
        return !newBegin.isAfter(oldBegin) && !newEnd.isBefore(oldEnd);
    }

    boolean newPricePeriodIntersectsOldRight() {
        return !newBegin.isBefore(oldBegin) && !newEnd.isBefore(oldEnd) && !newBegin.isAfter(oldEnd);
    }

    boolean newPricePeriodIntersectsOldLeft() {
        return !newBegin.isAfter(oldBegin) && !newEnd.isAfter(oldEnd) && !newEnd.isBefore(oldBegin);
    }

    boolean oldPricePeriodOverlapsNew() {
        return !newBegin.isBefore(oldBegin) && !newEnd.isAfter(oldEnd);
    }

    void extendOldToNew() {
        oldPrice.setBegin(newBegin);
        oldPrice.setEnd(newEnd);
    }

    void changeOldEndToNewEnd() {
        oldPrice.setEnd(newEnd);
    }

    void changeOldBeginToNewBegin() {
        oldPrice.setBegin(newBegin);
    }

    void extendOldEndTillNewBegin() {
        oldPrice.setEnd(newBegin.minusSeconds(1));
    }

    void startOldBeginAfterNewEnd() {
        oldPrice.setBegin(newEnd.plusSeconds(1));
    }

    Price splitOld() {
        Price oldPriceCopy = new Price(oldPrice);
        oldPrice.setEnd(newBegin.minusSeconds(1));
        oldPriceCopy.setBegin(newEnd.plusSeconds(1));
        return oldPriceCopy;
    }
}
