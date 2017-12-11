package gcombrinck.stepdefs.screens

import java.util
import java.util.concurrent.TimeUnit

import io.appium.java_client.AppiumDriver
import io.appium.java_client.pagefactory.WithTimeout
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.{By, WebElement}
import org.testng.Assert

@WithTimeout(time = 5, unit = TimeUnit.SECONDS)
class LoginScreen {

  @FindBy(id = "com.gcombrinck:id/btn_login")
  var btn_login: WebElement = _

  @FindBy(id = "com.gcombrinck:id/edt_email")
  var edt_email: WebElement = _

  @FindBy(id = "com.gcombrinck:id/edt_password")
  var edt_password: WebElement = _

  @FindBy(id = "com.gcombrinck.actived:id/btn_forgot_password")
  var btn_forgot_password: WebElement = _

  @FindBy(id = "com.gcombrinck:id/txt_dialog_message")
  var dialog_msg: WebElement = _

  @FindBy(id = "com.gcombrinck:id/txt_dialog_title")
  var dialog_title: WebElement = _

  @FindBy(id = "com.gcombrinck:id/ic_dialog_icon")
  var dialog_icon: WebElement = _

  @FindBy(id = "android:id/button1")
  var dialog_btn: WebElement = _


  @WithTimeout(time = 5, unit = TimeUnit.SECONDS)
  def login(userName: String, password: String) {
    edt_email.sendKeys(userName)
    edt_password.sendKeys(password)
    btn_login.click()
  }

  @WithTimeout(time = 5, unit = TimeUnit.SECONDS)
  def login_empty_username_password(): Unit = {
    edt_email.sendKeys("")
    edt_password.sendKeys("")
    btn_login.click()
  }

  def getErrorMessage(driver:AppiumDriver[WebElement], index:Int, text:String){
    val ERROR_MSG: util.List[WebElement] = driver.findElements(By.id("com.gcombrinck:id/textinput_error"))
    Assert.assertEquals(ERROR_MSG.get(index).getText, text)
  }

  def isInvalidCredentialsNotificationDisplayed(): Unit ={
    Assert.assertTrue(dialog_icon.isDisplayed)
    Assert.assertEquals(dialog_title.getText, "Oops!")
    Assert.assertEquals(dialog_msg.getText, "Invalid email and password combination.")
    dialog_btn.click()
  }

  def clear(): Unit ={
    edt_email.clear()
    edt_password.clear()
  }
}
