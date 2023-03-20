package machine;

import java.util.Arrays;
import java.util.Scanner;

public class CoffeeMachine {
    static Scanner scanner = new Scanner(System.in);
    int water;
    int milk;
    int coffee;
    int cups;
    int money;

    static int[] espresso = {250, 0, 16, 4}; // water, milk, coffee, price
    static int[] latte = {350, 75, 20, 7};
    static int[] cappuccino = {200, 100, 12, 6};
    boolean run = true;

    enum State {ACTION, BUY, FILL_WATER, FILL_MILK, FILL_COFFEE, FILL_CUPS};
    private State state;

    public boolean isRunning() {
        return run;
    }

    public static void main(String[] args) {
        CoffeeMachine cm = new CoffeeMachine();
        while(cm.isRunning()) cm.handle(scanner.nextLine());
    }

    public CoffeeMachine(int water, int milk, int coffee, int cups, int money) {
        this.water = water;
        this.milk = milk;
        this.coffee = coffee;
        this.cups = cups;
        this.money = money;
        this.state = State.ACTION;
    }

    public CoffeeMachine() {
        this(400, 540, 120, 9, 550);
    }

    public void handle(String input) {
        switch(state) {
            case ACTION -> action(input);
            case BUY -> buy(input);
        }
    }

    static int readInt(String message) {
        System.out.printf("%s%n", message);
        return scanner.nextInt();
    }

    static String readString(String message) {
        System.out.printf("%s%n", message);
        return scanner.next();
    }

    public void action(String action) {
//        String action = readString("Write action (buy, fill, take, remaining, exit):");
        switch(action) {
            case "buy":
                state = State.BUY;
                System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:");
                break;
            case "fill":
                fill();
                break;
            case "take":
                take();
                break;
            case "remaining":
                status();
                break;
            case "exit":
                this.run = false;
                break;
            default:
                break;
        }
    }

    private void buy(String input) {
//        String input = readString("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - back to menu:");
        if ("back".equals(input)) return;
        if(!input.matches("\\d+")) return;
        int selection = Integer.parseInt(input) - 1;
        int[][] coffeeTypes = {espresso,latte,cappuccino};
        if(checkInventory(coffeeTypes[selection])) {
            System.out.println("I have enough resources, making you a coffee!");
            subtractInventory(coffeeTypes[selection]);
        } else {
            printMissing(coffeeTypes[selection]);
        }
        state = State.ACTION;
    }

    private boolean checkInventory(int[] coffeeType) {
        return (water >= coffeeType[0] && milk >= coffeeType[1] && coffee >= coffeeType[2] && cups >= 1);
    }

    private void printMissing(int[] coffeeType){
        System.out.print("Sorry, not enough");
        if (water < coffeeType[0]) System.out.print(" water");
        if (water < coffeeType[0] && milk < coffeeType[1]) System.out.print(",");
        if (milk < coffeeType[1]) System.out.print(" milk");
        if (milk < coffeeType[1] && coffee < coffeeType[3]) System.out.print(",");
        if (coffee < coffeeType[3]) System.out.println(" coffee");
        if (coffee < coffeeType[3] && cups < 1) System.out.print(",");
        if (cups < 1) System.out.print(" cups");
        System.out.printf("!%n%n");
    }

    private void subtractInventory(int[] coffeeType) {
        water -= coffeeType[0];
        milk -= coffeeType[1];
        coffee -= coffeeType[2];
        money += coffeeType[3];
        cups--;
    }

    private void fill() {
        water += readInt("Write how many ml of water you want to add:");
        milk += readInt("Write how many ml of milk you want to add:");
        coffee += readInt("Write how many grams of coffee beans you want to add:");
        cups += readInt("Write how many disposable cups you want to add:");
    }

    private void take() {
        System.out.printf("I gave you $%d%n", money);
        money = 0;
    }

    private void status() {
        System.out.printf("%nThe coffee machine has:%n" +
                "%d ml of water%n" +
                "%d ml of milk%n" +
                "%d g of coffee beans%n" +
                "%d disposable cups%n" +
                "$%d of money%n%n",
                water, milk, coffee, cups, money);
    }

    static void calc(int water, int milk, int coffee, int cups) {
        int cupsWater = water/200;
        int cupsMilk = milk/50;
        int cupsCoffee = coffee/15;
        int[] maxes = {cupsWater, cupsMilk, cupsCoffee};
        Arrays.sort(maxes);
        int maxCups = maxes[0];
        maxes = null;
        if (maxCups == cups)
            System.out.printf("Yes, I can make that amount of coffee%n");
        else if (maxCups > cups)
            System.out.printf("Yes, I can make that amount of coffee (and even %d more than that)%n",
                    maxCups - cups);
        else
            System.out.printf("No, I can make only %d cup(s) of coffee", maxCups);
    }
}
