package store.service;

import java.util.ArrayList;
import java.util.List;
import store.domain.Promotion;

public class PromotionLoadService {

    private final List<Promotion> promotions = new ArrayList<>();

    public void load(List<String> rawData) {
        for (String input : rawData) {
            Promotion promotion = Promotion.createPromotion(input);
            if (promotions.contains(promotion)) {
                throw new IllegalArgumentException();
            }
            promotions.add(promotion);
        }
    }
}
