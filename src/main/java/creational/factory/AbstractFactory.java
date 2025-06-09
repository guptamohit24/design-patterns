package creational.factory;

/**
 * Provide an interface for creating families of related products
 *
 * @see creational.factory.PizzaIngredientFactory
 */
public class AbstractFactory {
    public static void main(String[] args) {
        MumbaiPizzaStore pizzaStore = new MumbaiPizzaStore();
        pizzaStore.orderPizza("cheese");
    }
}

class DelhiPizzaStoreFactory extends BasePizzaStore {

    @Override
    public Pizza getPizza(String type) {
        return null; //pizza creation based on pizza offered by delhi pizza store
    }
}

class MumbaiPizzaStore extends BasePizzaStore {

    @Override
    public Pizza getPizza(String type) {
        PizzaIngredientFactory factory = new MumbaiPizzaIngredientFactory();
        if (type.equals("cheese")) {
            return new MumbaiCheesePizza(factory); //mumbai pizza store will get ingredient from mumbai ingredient factory
        }
        return null; //other pizza types based on mumbai pizza store
    }
}

class MumbaiCheesePizza extends Pizza {

    private final PizzaIngredientFactory factory;

    public MumbaiCheesePizza(PizzaIngredientFactory factory) {
        this.factory = factory;
    }

    @Override
    public void prepare() {
        System.out.println("Preparing " + getType());
        factory.createDough(); //use factory to create object based on ingredient factory actual type
        factory.createCheese();
        factory.createSauce();
    }

    @Override
    public String getType() {
        return "mumbai cheese pizza";
    }
}

//It defines family of products needed to create pizza
interface PizzaIngredientFactory {
    Dough createDough();

    Cheese createCheese();

    Sauce createSauce();
}

class DelhiPizzaIngredientFactory implements PizzaIngredientFactory {

    @Override
    public Dough createDough() {
        return new ThickCrustDough();
    }

    @Override
    public Cheese createCheese() {
        return new MozrellaCheese();
    }

    @Override
    public Sauce createSauce() {
        return new MarinaraSauce();
    }
}

//The job of concrete pizza ingredient factory to make pizza ingredients.
//Each factory knows how to create the right ingredient for their region
class MumbaiPizzaIngredientFactory implements PizzaIngredientFactory {

    @Override
    public Dough createDough() {
        return new ThickCrustDough();
    }

    @Override
    public Cheese createCheese() {
        return new ReggianoCheese();
    }

    @Override
    public Sauce createSauce() {
        return new PlumTomatoSauce();
    }
}

//A sample product type which will be created from factory
interface Dough {
}

//Sample concrete product from a specific ingredient factory based on region
class ThinCrustDough implements Dough {
}

class ThickCrustDough implements Dough {
}

interface Sauce {
}

class PlumTomatoSauce implements Sauce {
}

class MarinaraSauce implements Sauce {
}

class MozrellaCheese implements Cheese {
}

class ReggianoCheese implements Cheese {
}

interface Cheese {
}
