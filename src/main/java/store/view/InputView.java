package store.view;

import static store.ErrorMessages.PURCHASE_ETC;
import static store.Messages.CONFIRM_ADD_FREE;
import static store.Messages.CONFIRM_CONTINUE_PURCHASE;
import static store.Messages.CONFIRM_MEMBERSHIP;
import static store.Messages.CONFIRM_WITHOUT_PROMOTION;
import static store.Messages.PURCHASE_GUIDE;

import camp.nextstep.edu.missionutils.Console;

public class InputView implements View {

    public String readPurchase() {
        System.out.println(PURCHASE_GUIDE.getMessage());
        String purchase = Console.readLine();
        System.out.println();
        return purchase;
    }

    /*
     * Confirm View
     * */

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
        boolean sign = confirm(String.format(CONFIRM_ADD_FREE.getMessage(), productName));
        System.out.println();
        return sign;
    }

    public boolean confirmPurchaseWithoutPromotion(String productName, int lack) {
        boolean sign = confirm(String.format(CONFIRM_WITHOUT_PROMOTION.getMessage(), productName, formatDecimal(lack)));
        System.out.println();
        return sign;
    }

    public boolean confirmMembership() {
        boolean sign = confirm(String.format(CONFIRM_MEMBERSHIP.getMessage()));
        System.out.println();
        return sign;
    }

    public boolean confirmContinuePurchase() {
        boolean sign = confirm(String.format(CONFIRM_CONTINUE_PURCHASE.getMessage()));
        System.out.println();
        return sign;
    }
}
