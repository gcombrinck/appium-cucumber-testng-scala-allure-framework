Feature: Register a new User on the HealthSnapp App
         Registration of a new user should be quick and easy
  Scenario: Once the App has been installed on the users' phone, does it open when selecting the "HealthSnapp" icon
    Given I installed the app
    When I click on the HealthSnapp icon
    Then the app should be launched
