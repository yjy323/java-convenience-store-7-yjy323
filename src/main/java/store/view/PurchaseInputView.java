package store.view;

import static store.Messages.PURCHASE_GUIDE;

import camp.nextstep.edu.missionutils.Console;


public class PurchaseInputView {

    public String read() {
        System.out.println(PURCHASE_GUIDE.getMessage());
        return Console.readLine();
    }
}
