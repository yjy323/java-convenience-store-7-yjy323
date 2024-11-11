package store.service;

import static store.ErrorMessages.PURCHASE_NON_EXIST;
import static store.ErrorMessages.PURCHASE_NOT_ENOUGH_QUANTITY;

import camp.nextstep.edu.missionutils.DateTimes;
import java.util.ArrayList;
import java.util.List;
import store.dto.PurchaseDto;
import store.model.Inventory;
import store.model.Payment;
import store.model.Product;
import store.model.Promotion;
import store.model.Purchase;
import store.service.parser.StringParser;
import store.view.InputView;

public class PurchaseService {

    private final InputView inputView;
    private final Inventory productInventory;
    private final Inventory promotionProductInventory;

    public PurchaseService(InputView inputView, Inventory productInventory, Inventory promotionProductInventory) {
        this.inputView = inputView;
        this.productInventory = productInventory;
        this.promotionProductInventory = promotionProductInventory;
    }

    private boolean isPromotionActive(PurchaseDto dto) {
        Product product;
        if (!promotionProductInventory.hasProduct(dto.getName())) {
            return false;
        }
        product = promotionProductInventory.search(dto.getName());
        if (product.getPromotion().isEmpty()) {
            return false;
        }
        return product.getPromotion().get().isPromotionPeriod(DateTimes.now().toLocalDate());
    }

    private Purchase createPurchase(Inventory inventory, String productName, int orderQuantity) {
        Product product = inventory.search(productName);
        product.validateUpdateQuantity(orderQuantity);
        return new Purchase(product, orderQuantity);
    }

    private int calcLackStock(Product product, Promotion promotion, PurchaseDto dto) {
        int buy = promotion.getBuy();
        int free = promotion.getFree();
        return dto.getQuantity() - product.getQuantity() + product.getQuantity() % (buy + free);
    }

    private void purchaseRegularInsteadPromotionStock(List<Purchase> purchases, Product product, PurchaseDto dto) {

        int regularQuantity = dto.getQuantity() - product.getQuantity();
        purchases.add(createPurchase(productInventory, product.getName(), regularQuantity));
        dto.setQuantity(product.getQuantity());
    }

    private void validatePromotionQuantity(PurchaseDto dto) {
        int regularQuantity = productInventory.search(dto.getName()).getQuantity();
        int promotionQuantity = promotionProductInventory.search(dto.getName()).getQuantity();
        if (dto.getQuantity() > regularQuantity + promotionQuantity) {
            throw new IllegalArgumentException(PURCHASE_NOT_ENOUGH_QUANTITY.getMessage());
        }
    }

    private void promotionStockManagement(List<Purchase> purchases, Product product, Promotion promotion,
                                          PurchaseDto dto) {
        validatePromotionQuantity(dto);
        if (product.getQuantity() < dto.getQuantity()) {
            int lackStock = calcLackStock(product, promotion, dto);
            boolean isAgreedWithoutPromotion = inputView.confirmPurchaseWithoutPromotion(dto.getName(), lackStock);
            if (isAgreedWithoutPromotion) {
                purchaseRegularInsteadPromotionStock(purchases, product, dto);
                return;
            }
            dto.setQuantity(dto.getQuantity() - lackStock);
        }
    }

    private void getAdditionalFree(Promotion promotion, PurchaseDto dto) {
        int orderQuantity = dto.getQuantity();
        if (promotion.canGetFree(orderQuantity)) {
            if (inputView.confirmAdditionalFree(dto.getName())) {
                dto.setQuantity(orderQuantity + 1);
            }
        }
    }

    private void savePurchasePromotionData(List<Purchase> purchases, PurchaseDto dto) {
        Product product = promotionProductInventory.search(dto.getName());
        Promotion promotion = product.getPromotion().get();
        getAdditionalFree(promotion, dto);
        promotionStockManagement(purchases, product, promotion, dto);
        purchases.add(createPurchase(promotionProductInventory, dto.getName(), dto.getQuantity()));
    }

    private void savePurchaseData(List<Purchase> purchases, PurchaseDto dto) {
        if (!productInventory.hasProduct(dto.getName())) {
            throw new IllegalArgumentException(PURCHASE_NON_EXIST.getMessage());
        }
        purchases.add(createPurchase(productInventory, dto.getName(), dto.getQuantity()));
    }

    public Payment purchaseProcess() {
        List<PurchaseDto> purchaseDtoList = StringParser.parsePurchaseDtoList(inputView.readPurchase());

        List<Purchase> purchases = new ArrayList<>();
        for (PurchaseDto dto : purchaseDtoList) {
            if (isPromotionActive(dto)) {
                savePurchasePromotionData(purchases, dto);
                continue;
            }
            savePurchaseData(purchases, dto);
        }
        return new Payment(purchases);
    }
}