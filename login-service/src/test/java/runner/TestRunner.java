package runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features", // Το path προς τα feature αρχεία σου
        glue = "steps"
)
public class TestRunner {
    // This class will be used to run your Cucumber tests
}
