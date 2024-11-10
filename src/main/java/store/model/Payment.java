package store.model;

import java.util.List;

public class Payment {
    private final List<Purchase> purchases;
    private boolean membershipStatus = false;

    public Payment(List<Purchase> purchases) {
        this.purchases = purchases;
    }


    public boolean isMembershipStatus() {
        return membershipStatus;
    }

    public void applyMembership() {
        membershipStatus = true;
    }
}
