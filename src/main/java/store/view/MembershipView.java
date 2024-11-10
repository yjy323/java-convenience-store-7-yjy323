package store.view;

import static store.ErrorMessages.PURCHASE_ETC;
import static store.Messages.CONFIRM_MEMBERSHIP;

import camp.nextstep.edu.missionutils.Console;

public class MembershipView {
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

    public boolean confirmMembership() {
        return confirm(String.format(CONFIRM_MEMBERSHIP.getMessage()));
    }
}
