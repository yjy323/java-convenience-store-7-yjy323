package store.view;

import static store.ErrorMessages.PURCHASE_ETC;
import static store.Messages.CONFIRM_ADD_FREE;
import static store.Messages.CONFIRM_WITHOUT_PROMOTION;

import camp.nextstep.edu.missionutils.Console;
import java.text.NumberFormat;

public class PromotionInputView {

    private String formatDecimal(int value) {
        NumberFormat formatter = NumberFormat.getInstance();
        return formatter.format(value);
    }

    private boolean confirm(String msg) {
        while (true) {
            System.out.println(msg);
            String input = Console.readLine();
            if (input.equals("Y")) {
                return true;
            }
            if (input.equals("N")) {
                return false;
            }
            System.out.println(PURCHASE_ETC.getMessage());
        }
    }

    public boolean confirmAdditionalFree(String productName) {
        return confirm(String.format(CONFIRM_ADD_FREE.getMessage(), productName));
    }

    public boolean confirmPurchaseWithoutPromotion(String productName, int lack) {
        return confirm(String.format(CONFIRM_WITHOUT_PROMOTION.getMessage(), productName, formatDecimal(lack)));
    }
}
