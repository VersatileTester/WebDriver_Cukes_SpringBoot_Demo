@All @GoogleFeature
Feature: Google Search
  As a Developer
  I want to ...
  So that ...

  @Google1
  Scenario Outline: Searching for multiple terms
    Given I navigate to the Google home page
    When I google search the term <term>
    Then I should see at least one result containing the term <term>

    Examples:
    |term         |
    |Bruce Wayne  |
    |Clark Kent   |
    |Peter Parker |
    |Barry Allen  |
