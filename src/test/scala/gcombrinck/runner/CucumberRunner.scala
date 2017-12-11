package gcombrinck.runner

import cucumber.api.CucumberOptions
import cucumber.api.testng.AbstractTestNGCucumberTests
import io.codearte.jfairy.Fairy
import io.codearte.jfairy.producer.person.{Person, PersonProperties}
//https://testingneeds.wordpress.com/2015/12/26/test-automation-with-cucumber-jvm/
@CucumberOptions(
  strict = true,
  features = Array("src/test/resources/features"),
  glue = Array("gcombrinck.stepdefs", "gcombrinck.stepdefs.helpers"),
  plugin = Array("pretty", "html:target/cucumber-report", "json:target/cucumber.json"),
  tags = Array("not @Wip"))
class CucumberRunner extends AbstractTestNGCucumberTests {
  println("*********************************RUNNING TESTS*********************************\n")

}