@demoblaze
Feature: Navegación por categorías
  Como cliente de Demoblaze
  Quiero navegar por las categorías de productos
  Para visualizar los artículos disponibles

  Scenario: Navegar a la categoría Phones
    Given que estoy en la página principal de Demoblaze
    When selecciono la categoría "Phones"
    Then debo ver productos relacionados con "Phones"

  Scenario: Navegar a la categoría Laptops
    Given que estoy en la página principal de Demoblaze
    When selecciono la categoría "Laptops"
    Then debo ver productos relacionados con "Laptops"

  Scenario: Navegar a la categoría Monitors
    Given que estoy en la página principal de Demoblaze
    When selecciono la categoría "Monitors"
    Then debo ver productos relacionados con "Monitors"
