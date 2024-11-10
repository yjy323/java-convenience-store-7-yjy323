package store;

import store.controller.StoreController;
import store.view.FileLoader;
import store.view.InputView;
import store.view.OutputView;

public class Application {

    public static void main(String[] args) {
        StoreController storeController = new StoreController(new FileLoader(), new InputView(), new OutputView());

        storeController.init();
        storeController.run();
    }
}
