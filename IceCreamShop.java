import java.util.ArrayList;
import java.util.List;

// Observer pattern
interface OrderObserver {
    void update(Order order);
}

// Concrete observer
class CustomerOrderObserver implements OrderObserver {
    private String customerName;

    public CustomerOrderObserver(String customerName) {
        this.customerName = customerName;
    }

    @Override
    public void update(Order order) {
        System.out.println("Dear " + customerName + ", your order status is now: " + order.getStatus());
    }
}

// Strategy pattern
interface PaymentStrategy {
    void pay(double amount);
}

// Concrete strategies
class CreditCardPaymentStrategy implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("Paid $" + amount + " using credit card.");
    }
}

class CashPaymentStrategy implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("Paid $" + amount + " in cash.");
    }
}

// Chain of Responsibility pattern
interface IceCreamHandler {
    void handleRequest(IceCream iceCream);
}

// Concrete handlers
class FlavorHandler implements IceCreamHandler {
    private IceCreamHandler next;

    public void setNext(IceCreamHandler next) {
        this.next = next;
    }

    @Override
    public void handleRequest(IceCream iceCream) {
        if (iceCream.getFlavor() == null) {
            System.out.println("Please select a flavor.");
        } else if (next != null) {
            next.handleRequest(iceCream);
        }
    }
}

class ToppingHandler implements IceCreamHandler {
    private IceCreamHandler next;

    public void setNext(IceCreamHandler next) {
        this.next = next;
    }

    @Override
    public void handleRequest(IceCream iceCream) {
        if (iceCream.getToppings().isEmpty()) {
            System.out.println("Please select at least one topping.");
        } else if (next != null) {
            next.handleRequest(iceCream);
        }
    }
}

// Command pattern
interface Command {
    void execute();
}

// Concrete command for placing an order
class PlaceOrderCommand implements Command {
    private Order order;

    public PlaceOrderCommand(Order order) {
        this.order = order;
    }

    @Override
    public void execute() {
        System.out.println("Placing order...");
        // Logic to place the order
        order.setStatus("Placed");
    }
}

// Concrete command for providing feedback
class ProvideFeedbackCommand implements Command {
    private String feedback;

    public ProvideFeedbackCommand(String feedback) {
        this.feedback = feedback;
    }

    @Override
    public void execute() {
        System.out.println("Providing feedback: " + feedback);
        // Logic to process the feedback
    }
}

// Decorator pattern
abstract class OrderDecorator extends Order {
    protected Order decoratedOrder;

    public OrderDecorator(Order decoratedOrder) {
        this.decoratedOrder = decoratedOrder;
    }

    public String getDescription() {
        return decoratedOrder.getDescription();
    }

    @Override
    public double calculateTotal() {
        return decoratedOrder.calculateTotal();
    }
}

// Concrete decorator for adding gift wrapping
class GiftWrappingDecorator extends OrderDecorator {
    public GiftWrappingDecorator(Order decoratedOrder) {
        super(decoratedOrder);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", Gift Wrapping";
    }

    @Override
    public double calculateTotal() {
        return super.calculateTotal() + 2.0; // Additional cost for gift wrapping
    }
}

// Concrete decorator for adding special packaging
class SpecialPackagingDecorator extends OrderDecorator {
    public SpecialPackagingDecorator(Order decoratedOrder) {
        super(decoratedOrder);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", Special Packaging";
    }

    @Override
    public double calculateTotal() {
        return super.calculateTotal() + 1.5; // Additional cost for special packaging
    }
}

// Builder pattern
class OrderBuilder {
    private Order order = new Order();

    public OrderBuilder addItem(IceCream item) {
        order.addItem(item);
        return this;
    }

    public OrderBuilder addObserver(OrderObserver observer) {
        order.addObserver(observer);
        return this;
    }

    public Order build() {
        return order;
    }
}

// State pattern
interface OrderState {
    void processOrder(Order order);
}

// Concrete states
class PlacedState implements OrderState {
    @Override
    public void processOrder(Order order) {
        System.out.println("Order placed.");
        order.setStatus("Placed");
    }
}

class PreparationState implements OrderState {
    @Override
    public void processOrder(Order order) {
        System.out.println("Order is being prepared.");
        order.setStatus("In Preparation");
    }
}

class DeliveryState implements OrderState {
    @Override
    public void processOrder(Order order) {
        System.out.println("Order is out for delivery.");
        order.setStatus("Out for Delivery");
    }
}

// Context class
class Order {
    private List<IceCream> items = new ArrayList<>();
    private String status;
    private List<OrderObserver> observers = new ArrayList<>();
    private String description;

    public void addItem(IceCream item) {
        items.add(item);
    }

    public void removeItem(IceCream item) {
        items.remove(item);
    }

    public double calculateTotal() {
        return items.stream().mapToDouble(IceCream::cost).sum();
    }

    public void addObserver(OrderObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(OrderObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (OrderObserver observer : observers) {
            observer.update(this);
        }
    }

    public void setStatus(String status) {
        this.status = status;
        notifyObservers();
    }

    public String getStatus() {
        return status;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
}

// Ice cream interface
interface IceCream {
    String getDescription();
    double cost();
    String getFlavor();
    List<String> getToppings();
    void setFlavor(String flavor);
    void addTopping(String topping);
}

// Concrete ice cream implementation
class BasicIceCream implements IceCream {
    private String flavor;
    private List<String> toppings = new ArrayList<>();

    @Override
    public String getDescription() {
        return "Basic Ice Cream";
    }

    @Override
    public double cost() {
        return 2.0;
    }

    @Override
    public String getFlavor() {
        return flavor;
    }

    @Override
    public List<String> getToppings() {
        return toppings;
    }

    @Override
    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }

    @Override
    public void addTopping(String topping) {
        toppings.add(topping);
    }
}

// Main class
public class IceCreamShop {
    public static void main(String[] args) {
        // Create an order using the Builder pattern
        OrderBuilder orderBuilder = new OrderBuilder();
        orderBuilder.addItem(new BasicIceCream())
                .addObserver(new CustomerOrderObserver("Alice"));

        Order order = orderBuilder.build();

        // Execute the PlaceOrderCommand
        Command placeOrderCommand = new PlaceOrderCommand(order);
        placeOrderCommand.execute();

        // Execute the ProvideFeedbackCommand
        Command provideFeedbackCommand = new ProvideFeedbackCommand("Great service!");
        provideFeedbackCommand.execute();

        // Decorate the order with gift wrapping
        order = new GiftWrappingDecorator(order);

        // Print the final order details
        System.out.println("Final order details:");
        System.out.println("Description: " + order.getDescription());
        System.out.println("Total Cost: $" + order.calculateTotal());
    }
}
