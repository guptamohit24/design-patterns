package creational.factory;

/**
 * The simple factory isn't actually a Design pattern, it is more of a programming idiom
 * It simply used to decouple clients from concrete classes
 */
public class SimpleFactory {

    public static void main(String[] args) {
        PizzaStoreFactory storeFactory = new PizzaStoreFactory();
        PizzaStore pizzaStore = new PizzaStore(storeFactory);

        Pizza pizza = pizzaStore.orderPizza("cheese");
        System.out.println(pizza.getType() +" pizza for you");
    }
}

class PizzaStore {
    private final PizzaStoreFactory factory;

    public PizzaStore(PizzaStoreFactory factory) {
        this.factory = factory;
    }

    public Pizza orderPizza(String type) {
        Pizza pizza = factory.getPizza(type); //Encapsulating object creation

        pizza.prepare();
        pizza.bake();
        pizza.cut();
        pizza.box();
        return pizza;
    }
}

abstract class Pizza {

    public abstract void prepare();

    public abstract String getType();

    public void bake() {
        System.out.println("Bake for 25 minutes at 350");
    }

    public void cut() {
        System.out.println("Cutting the pizza into diagonal slices");
    }

    public void box() {
        System.out.println("place Pizza in official PizzaStore box");
    }
}

class CheezePizza extends Pizza {

    @Override
    public void prepare() {
        System.out.println("Preparing Cheese Pizza");
    }

    @Override
    public String getType() {
        return "cheese";
    }
}

class IndianTandoori extends Pizza {

    @Override
    public void prepare() {
        System.out.println("Preparing Indian Tandoori");
    }

    @Override
    public String getType() {
        return "tandoori";
    }
}

class VegExtravaganza extends Pizza {

    @Override
    public void prepare() {
        System.out.println("Preparing Veg Extravaganza");
    }

    @Override
    public String getType() {
        return "extravaganza";
    }
}

class PizzaStoreFactory {

    /**
     * Define a factory method used by client to instantiate a pizza
     *
     * @param type of pizza
     * @return Pizza actual instance based on type
     */
    public Pizza getPizza(String type) { //this can be a static method
        Pizza pizza = null;
        if (type.equals("cheese")) {
            pizza = new CheezePizza();
        } else if (type.equals("extravaganza")) {
            pizza = new VegExtravaganza();
        } else if (type.equals("tandoori")) {
            pizza = new IndianTandoori();
        }
        return pizza;
    }
}