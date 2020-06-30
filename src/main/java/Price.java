import java.time.LocalDateTime;
import java.util.Objects;

class Price {
    private long id;
    private String productCode;
    private int number;
    private int depart;
    private LocalDateTime begin;
    private LocalDateTime end;
    private long value;

    LocalDateTime getBegin() {
        return begin;
    }

    LocalDateTime getEnd() {
        return end;
    }

    long getValue() {
        return value;
    }

    void setBegin(LocalDateTime begin) {
        this.begin = begin;
    }

    void setEnd(LocalDateTime end) {
        this.end = end;
    }

    Price(long id, String productCode, int number, int depart, LocalDateTime begin, LocalDateTime end, long value) {
        this.id = id;
        this.productCode = productCode;
        this.number = number;
        this.depart = depart;
        this.begin = begin;
        this.end = end;
        this.value = value;
    }

    Price(String productCode, int number, int depart, LocalDateTime begin, LocalDateTime end, long value) {
        this.productCode = productCode;
        this.number = number;
        this.depart = depart;
        this.begin = begin;
        this.end = end;
        this.value = value;
    }

    Price(Price price) {
        this.productCode = price.productCode;
        this.number = price.number;
        this.depart = price.depart;
        this.begin = price.begin;
        this.end = price.end;
        this.value = price.value;
    }

    /**
     * Цена считается той же, если это цена для того же продукта в пределах того же отдела и с таким же номером
     * @param o - цена, с которой сравнивается текущая цена
     * @return true - та же сама цена
     *         false - другая цена
     */
    boolean isForSameProductPrice(Price o) {
        return productCode.equals(o.productCode) &&
                number == o.number &&
                depart == o.depart;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return number == price.number &&
                depart == price.depart &&
                value == price.value &&
                Objects.equals(productCode, price.productCode) &&
                Objects.equals(begin, price.begin) &&
                Objects.equals(end, price.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productCode, number, depart, begin, end, value);
    }

    @Override
    public String toString() {
        return "{id = " + id +
                ", productCode = '" + productCode + '\'' +
                ", number = " + number +
                ", depart = " + depart +
                ", begin = " + begin +
                ", end = " + end +
                ", value = " + value +
                '}';
    }
}