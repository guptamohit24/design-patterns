package creational.factory;

/**
 * Let subclass decide object instantiation
 */
public class FactoryMethod {

    public static void main(String[] args) {
        PunePizzaStore punePizzaStore = new PunePizzaStore();
        Pizza pizza = punePizzaStore.orderPizza("tandoori");
        System.out.println(pizza.getType() +" pizza for you");

        BengaluruPizzaStore bengaluruPizzaStore = new BengaluruPizzaStore();
        pizza = bengaluruPizzaStore.orderPizza("cheese");
        System.out.println(pizza.getType() +" pizza for you");
    }
}

abstract class BasePizzaStore {

    public Pizza orderPizza(String type) {
        Pizza pizza = getPizza(type); //subclass will take care of instantiation
        pizza.prepare();
        pizza.bake();
        pizza.cut();
        pizza.box();
        return pizza;
    }

    public abstract Pizza getPizza(String type); //factory method
}

class PunePizzaStore extends BasePizzaStore {

    @Override
    public Pizza getPizza(String type) {
        Pizza pizza = null;
        if (type.equals("cheese")) {
            pizza = new PuneCheesePizza();
        } else if (type.equals("extravaganza")) {
            pizza = new PuneExtravaganzaPizza();
        } else if (type.equals("tandoori")) {
            pizza = new PuneTandooriPizza();
        }
        return pizza;
    }
}

class BengaluruPizzaStore extends BasePizzaStore {

    @Override
    public Pizza getPizza(String type) {
        Pizza pizza = null;
        if (type.equals("cheese")) {
            pizza = new BengaluruCheesePizza();
        } else if (type.equals("extravaganza")) {
            pizza = new BengaluruExtravaganzaPizza();
        } else if (type.equals("tandoori")) {
            pizza = new BengaluruTandooriPizza();
        }
        return pizza;
    }
}

class PuneCheesePizza extends Pizza {

    @Override
    public void prepare() {
        System.out.println("Preparing Pune Cheese");
    }

    @Override
    public String getType() {
        return "pune cheese";
    }
}

class BengaluruCheesePizza extends Pizza {

    @Override
    public void prepare() {
        System.out.println("Preparing Bengaluru Cheese");
    }

    @Override
    public String getType() {
        return "bengaluru cheese";
    }
}

class PuneTandooriPizza extends Pizza {

    @Override
    public void prepare() {
        System.out.println("Preparing Pune Tandoori");
    }

    @Override
    public String getType() {
        return "pune tandoori";
    }
}

class BengaluruExtravaganzaPizza extends Pizza {

    @Override
    public void prepare() {
        System.out.println("Preparing Bengaluru Veg Extravaganza");
    }

    @Override
    public String getType() {
        return "bengaluru extravaganza";
    }
}

class PuneExtravaganzaPizza extends Pizza {

    @Override
    public void prepare() {
        System.out.println("Preparing Pune Veg Extravaganza");
    }

    @Override
    public String getType() {
        return "pune extravaganza";
    }
}

class BengaluruTandooriPizza extends Pizza {

    @Override
    public void prepare() {
        System.out.println("Preparing Bengaluru Tandoori");
    }

    @Override
    public String getType() {
        return "bengaluru tandoori";
    }
}


