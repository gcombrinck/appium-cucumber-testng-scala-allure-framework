package gcombrinck.stepdefs.helpers

import java.io.File
import java.util.concurrent.TimeUnit

import cucumber.api.java.{After, Before}
import cucumber.api.scala.{EN, ScalaDsl}
import gcombrinck.lib.MailHelper
import gcombrinck.runner.CucumberRunner
import gcombrinck.stepdefs.screens._
import io.appium.java_client.AppiumDriver
import io.appium.java_client.android.{Activity, AndroidDriver, AndroidKeyCode}
import io.appium.java_client.ios.IOSDriver
import io.appium.java_client.pagefactory.AppiumFieldDecorator
import io.appium.java_client.remote.{AutomationName, MobileCapabilityType}
import io.appium.java_client.service.local.{AppiumDriverLocalService, AppiumServerHasNotBeenStartedLocallyException, AppiumServiceBuilder}
import io.codearte.jfairy.Fairy
import io.codearte.jfairy.producer.person.{Person, PersonProperties}
import org.openqa.selenium.{By, WebElement}
import org.openqa.selenium.remote.{CapabilityType, DesiredCapabilities}
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.ui.WebDriverWait


object BaseHelpers {
  protected var driver: AppiumDriver[WebElement] = _
}

trait BaseHelpers extends ScalaDsl with EN{
  var service: AppiumDriverLocalService = _
  var driver: AppiumDriver[WebElement] = _
  var homeScreen: HomeScreen = _
  var registrationScreen: RegistrationScreen = _
  var loginScreen: LoginScreen = _
  var reportsScreen: ReportsScreen = _
  var profileScreen: ProfileScreen = _
  var editProfileScreen: EditProfileScreen = _
  val os = ""
  var waitFor: WebDriverWait = _


  initialize()

  def initialize(): Unit = {
    if (BaseHelpers.driver == null) beforeScenario()
  }

  @Before
  def beforeScenario(): Unit = {
    Thread.sleep(3000)
    service = AppiumDriverLocalService.buildService(new AppiumServiceBuilder().withIPAddress("127.0.0.1").withLogFile(new File("appiumLog.log")))
    Thread.sleep(3000)
    service.start()
    Thread.sleep(3000)
    if (service == null || !service.isRunning) throw new AppiumServerHasNotBeenStartedLocallyException("An appium server node is not started!")
    if (os.equalsIgnoreCase("ios")) {
      iOSCaps()
    }
    else {
      androidCaps()
    }
    homeScreen = new HomeScreen
    registrationScreen = new RegistrationScreen
    loginScreen = new LoginScreen
    reportsScreen = new ReportsScreen
    profileScreen = new ProfileScreen
    editProfileScreen = new EditProfileScreen
    PageFactory.initElements(new AppiumFieldDecorator(driver, 10, TimeUnit.SECONDS), homeScreen)
    PageFactory.initElements(new AppiumFieldDecorator(driver, 10, TimeUnit.SECONDS), registrationScreen)
    PageFactory.initElements(new AppiumFieldDecorator(driver, 10, TimeUnit.SECONDS), loginScreen)
    PageFactory.initElements(new AppiumFieldDecorator(driver, 10, TimeUnit.SECONDS), reportsScreen)
    PageFactory.initElements(new AppiumFieldDecorator(driver, 10, TimeUnit.SECONDS), profileScreen)
    PageFactory.initElements(new AppiumFieldDecorator(driver, 10, TimeUnit.SECONDS), editProfileScreen)
  }

  @After def afterScenario(): Unit = {
    if (driver != null) {
      driver.quit()
    }

    if (service != null) {
      service.stop()
    }
  }

  def androidCaps(): Unit = {
    val path = System.getProperty("user.dir") + "/src/test/resources/"
    val appDir = new java.io.File(path)
    val app = new java.io.File(appDir, "****INSERT APK HERE****")

    val capabilities = DesiredCapabilities.android()
    capabilities.setCapability("deviceName", "Android")
    capabilities.setCapability("platformName", "Android")
    capabilities.setCapability("appWaitActivity", "****INSERT ACTIVITY HERE****")
    capabilities.setCapability("app", app.getAbsolutePath)
    capabilities.setCapability(CapabilityType.BROWSER_NAME, "")
    capabilities.setCapability("newCommandTimeout", 360)
    capabilities.setCapability("skipUnlock", true)
    capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "uiautomator2")
    driver = new AndroidDriver[WebElement](service.getUrl, capabilities)
    driver.manage.timeouts.implicitlyWait(30, TimeUnit.SECONDS)
    waitFor = new WebDriverWait(driver, 30)
  }

  def verify_email_android(): Unit = {
    val activity = new Activity("com.android.chrome", "com.google.android.apps.chrome.Main")
    activity.setAppWaitPackage("com.android.chrome")
    activity.setAppWaitActivity("org.chromium.chrome.browser.*")
    activity.setStopApp(true)
    Thread.sleep(2000)
    driver.asInstanceOf[AndroidDriver[WebElement]].startActivity(activity)

    Thread.sleep(2000)
    val urls = MailHelper.getRegistrationURLS
    for (i <- 0 until urls.size()) {
      Thread.sleep(2000)
      driver.findElement(By.id("com.android.chrome:id/tab_switcher_button")).click()
      driver.findElement(By.id("com.android.chrome:id/new_tab_button")).click()
      Thread.sleep(2000)
      driver.asInstanceOf[AndroidDriver[WebElement]].pressKeyCode(AndroidKeyCode.ENTER)
      val urlbar = driver.findElement(By.id("com.android.chrome:id/url_bar"))
      urlbar.click()
      println("#########Count: " + urls.size() + "#############")
      println(urls.get(i))
      println(driver.getContext)
      urlbar.sendKeys(urls.get(i).toString)
      driver.asInstanceOf[AndroidDriver[WebElement]].pressKeyCode(AndroidKeyCode.ENTER)
      Thread.sleep(10000)
    }
    MailHelper.mark_all_emails_as_read()
  }

  def iOSCaps(): Unit = {
    val path = System.getProperty("user.dir") + "/src/test/resources/"
    val appDir = new java.io.File(path)
    val app = new java.io.File(appDir, "HealthSnappd.app")

    val capabilities = DesiredCapabilities.iphone()
    capabilities.setCapability("platformVersion", "10.3")
    capabilities.setCapability("platformName", "iOS")
    capabilities.setCapability("deviceName", "iPhone 5s")
    capabilities.setCapability("app", app.getAbsolutePath)
    capabilities.setCapability("newCommandTimeout", 360)
    capabilities.setCapability("version", "1.0")
    capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.IOS_XCUI_TEST)
    driver = new IOSDriver[WebElement](service.getUrl, capabilities)
    driver.manage.timeouts.implicitlyWait(30, TimeUnit.SECONDS)
    driver.switchTo.alert.accept()
    waitFor = new WebDriverWait(driver, 30)
  }

}
