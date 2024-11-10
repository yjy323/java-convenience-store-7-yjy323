package store.view;

import java.text.NumberFormat;

public interface View {

    NumberFormat formatter = NumberFormat.getInstance();

    default String formatDecimal(int value) {
        return formatter.format(value);
    }
}
