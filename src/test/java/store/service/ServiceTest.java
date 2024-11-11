package store.service;

import camp.nextstep.edu.missionutils.test.NsTest;
import store.Application;
import store.model.Inventory;

public abstract class ServiceTest extends NsTest {
    protected Inventory productInventory;
    protected Inventory promotionProductInventory;

    public ServiceTest() {
        productInventory = initProducts();
        promotionProductInventory = initPromotionProducts();
    }

    protected abstract Inventory initProducts();

    protected abstract Inventory initPromotionProducts();


    @Override
    protected void runMain() {
        Application.main(new String[]{});
    }
}
