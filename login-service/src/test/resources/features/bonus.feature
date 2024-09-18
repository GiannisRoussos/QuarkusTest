Feature: Bonus update event

Feature: Bonus update event

  Scenario: Successfully update player's bonus after login event
    Given a player with userId "123e4567-e89b-12d3-a456-426614174000" exists in the system with a bonus of 10.00
    When a "bonus_update_event" is sent with userId "123e4567-e89b-12d3-a456-426614174000"
    Then the player's bonus should be incremented by 1.00

