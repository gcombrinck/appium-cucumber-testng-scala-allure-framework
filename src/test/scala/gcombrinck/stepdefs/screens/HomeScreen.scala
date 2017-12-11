package gcombrinck.stepdefs.screens

import java.util.concurrent.TimeUnit

import io.appium.java_client.AppiumDriver
import io.appium.java_client.pagefactory.{AndroidFindBy, WithTimeout, iOSFindBy}
import org.openqa.selenium.{NoSuchElementException, WebElement}
import org.testng.Assert

@WithTimeout(time = 10, unit = TimeUnit.SECONDS)
class HomeScreen() {

  @AndroidFindBy(id = "com.gcombrinck:id/img_branded_logo")
  @iOSFindBy(xpath = "/AppiumAUT/XCUIElementTypeApplication/XCUIElementTypeWindow[2]/XCUIElementTypeStatusBar/XCUIElementTypeOther[2]/XCUIElementTypeOther[4]")
  var img_branded_logo: WebElement = _
}


