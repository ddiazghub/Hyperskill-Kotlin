package machine

import kotlin.system.exitProcess

class CoffeeResources(
    var water: Int = 0,
    var milk: Int = 0,
    var coffeeBeans: Int = 0,
    var cups: Int = 1,
    var money: Int = 0
) {
    var name: String = ""

    constructor(
        name: String,
        water: Int = 0,
        milk: Int = 0,
        coffeeBeans: Int = 0,
        cups: Int = 1,
        money: Int = 0
    ): this(water, milk, coffeeBeans, cups, money) {
        this.name = name
    }

    fun take(): Int {
        val take = money
        money = 0
        return take
    }

    fun enoughFor(other: CoffeeResources): Boolean {
        val missing = when {
            this.water < other.water -> "water"
            this.milk < other.milk -> "milk"
            this.coffeeBeans < other.coffeeBeans -> "coffee beans"
            this.cups < other.cups -> "disposable cups"
            this.money < other.money -> "money"
            else -> return true
        }

        println("Sorry, not enough $missing!")
        return false
    }

    fun makeCoffee(recipe: CoffeeResources) {
        this.water -= recipe.water
        this.milk -= recipe.milk
        this.coffeeBeans -= recipe.coffeeBeans
        this.cups -= recipe.cups
        this.money += recipe.money
    }

    operator fun plus(other: CoffeeResources) {
        this.water += other.water
        this.milk += other.milk
        this.coffeeBeans += other.coffeeBeans
        this.cups += other.cups
        this.money += other.money
    }

    override fun toString(): String {
        return """
            $water ml of water
            $milk ml of milk
            $coffeeBeans g of coffee beans
            $cups disposable cups
            $$money of money
        """.trimIndent()
    }
}

enum class MachineState {
    MENU,
    BUY,
    FILL_WATER,
    FILL_MILK,
    FILL_COFFEE,
    FILL_CUPS
}

class CoffeeMachine(val recipes: Array<CoffeeResources>, val resources: CoffeeResources) {
    var state: MachineState = MachineState.MENU
    val toAdd: CoffeeResources = CoffeeResources()

    fun process(action: String) {
        when (state) {
            MachineState.MENU -> this.menu(action)
            MachineState.BUY -> {
                this.buy(action)
                this.state = MachineState.MENU
            }
            MachineState.FILL_WATER -> {
                toAdd.water = action.toInt()
                this.state = MachineState.FILL_MILK
            }
            MachineState.FILL_MILK -> {
                toAdd.milk = action.toInt()
                this.state = MachineState.FILL_COFFEE
            }
            MachineState.FILL_COFFEE -> {
                toAdd.coffeeBeans = action.toInt()
                this.state = MachineState.FILL_CUPS
            }
            MachineState.FILL_CUPS -> {
                toAdd.cups = action.toInt()
                this.resources + this.toAdd
                this.state = MachineState.MENU
            }
        }
    }

    fun nextPrompt(): String {
        return when (state) {
            MachineState.MENU -> "Write action (buy, fill, take, remaining, exit):"
            MachineState.BUY -> "What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:"
            MachineState.FILL_WATER -> "Write how many ml of water you want to add:"
            MachineState.FILL_MILK -> "Write how many ml of milk you want to add:"
            MachineState.FILL_COFFEE -> "Write how many grams of coffee beans you want to add:"
            MachineState.FILL_CUPS -> "Write how many disposable cups you want to add:"
        }
    }

    fun menu(action: String) {
        when (action) {
            "buy" -> this.state = MachineState.BUY
            "fill" -> this.state = MachineState.FILL_WATER
            "take" -> this.take()
            "remaining" -> {
                println("The coffee machine has:")
                println(this.resources)
            }
            "exit" -> exitProcess(0)
        }
    }

    fun buy(action: String) {
        val coffeeId = when (action) {
            "back" -> return
            else -> action.toInt()
        }

        val recipe = recipes[coffeeId - 1]

        if (resources.enoughFor(recipe)) {
            resources.makeCoffee(recipe)
            println("I have enough resources, making you a coffee!")
        }
    }

    fun take() = println("I gave you $${resources.take()}")
}

fun main() {
    val resources = CoffeeResources(
        water = 400,
        milk = 540,
        coffeeBeans = 120,
        cups = 9,
        money = 550
    )

    val recipes = arrayOf(
        CoffeeResources(
            name = "espresso",
            water = 250,
            coffeeBeans = 16,
            money = 4
        ),
        CoffeeResources(
            name = "latte",
            water = 350,
            milk = 75,
            coffeeBeans = 20,
            money = 7
        ),
        CoffeeResources(
            name = "cappuccino",
            water = 200,
            milk = 100,
            coffeeBeans = 12,
            money = 6
        )
    )

    val machine = CoffeeMachine(recipes, resources)

    while (true) {
        println(machine.nextPrompt())
        val action = readln()
        machine.process(action)
    }
}