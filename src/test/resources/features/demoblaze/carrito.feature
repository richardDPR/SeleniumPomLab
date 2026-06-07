@demoblaze
Feature: Agregar productos al carrito
  Como cliente de Demoblaze
  Quiero agregar productos al carrito
  Para poder comprarlos más adelante

  Scenario: Agregar un producto al carrito
    Given que estoy en la página principal de Demoblaze
    When selecciono el producto "Samsung galaxy s6"
    And agrego el producto al carrito
    Then el producto "Samsung galaxy s6" debe aparecer en el carrito

  Scenario: Agregar otro producto al carrito
    Given que estoy en la página principal de Demoblaze
    When selecciono el producto "Sony vaio i5"
    And agrego el producto al carrito
    Then el producto "Sony vaio i5" debe aparecer en el carrito
