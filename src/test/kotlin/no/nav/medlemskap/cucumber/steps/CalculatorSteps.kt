package no.nav.medlemskap.cucumber.steps
import io.cucumber.java.en.Given
import io.cucumber.java.en.When
import io.cucumber.java.en.Then
import org.junit.jupiter.api.Assertions.assertEquals

class CalculatorSteps {
    private var result = 0

    @Given("I have entered {int} into the calculator")
    fun iHaveEnteredNumberIntoTheCalculator(number: Int) {
        result += number
    }

    @When("I press add")
    fun iPressAdd() {
        // Assuming addition operation
        // You can modify this as per your application logic
    }

    @Then("the result should be {int} on the screenuld be told \"Nope\"gr")
    fun theResultShouldBeOnTheScreen(expectedResult: Int) {
        assertEquals(expectedResult, result)
    }
}