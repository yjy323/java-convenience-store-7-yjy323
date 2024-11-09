package store.service;

import java.util.ArrayList;
import java.util.List;
import store.model.Purchase;
import store.model.PurchaseProduct;
import store.service.parser.StringParser;

public class PurchaseService {

    public Purchase create(String purchaseLine) {
        List<String> purchaseData = List.of(purchaseLine.split(","));
        List<PurchaseProduct> products = new ArrayList<>();
        for (String data : purchaseData) {
            products.add(StringParser.parsePurchaseProduct(data));
        }
        return new Purchase(products);
    }
}
