# For Tests 1 - 2
Feature: Login

  To be able to access website
  As a registered user
  I want to able to login

  Scenario: Valid login
    Given I am a user of marketalertum
    When I login using valid credentials
    Then I should see my alerts

  Scenario: Invalid login
    Given I am a user of marketalertum
    When I login using invalid credentials
    Then I should see the login screen again