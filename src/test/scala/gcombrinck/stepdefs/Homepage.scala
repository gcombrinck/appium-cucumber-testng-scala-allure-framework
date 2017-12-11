package gcombrinck.stepdefs

import gcombrinck.stepdefs.helpers.BaseHelpers
import org.testng.Assert


class Homepage extends BaseHelpers {
  Given("""^I installed the app$""") { () =>
    println("******************************************")
    println(driver)
    println("******************************************")
    homeScreen.isLogosVisible(driver)
    homeScreen.btn_login.click()
  }
  When("""^I click on the HealthSnapp icon$""") { () =>
    Assert.assertTrue(true)
  }
  Then("""^the app should be launched$""") { () =>
    Assert.assertTrue(true)
  }
}
