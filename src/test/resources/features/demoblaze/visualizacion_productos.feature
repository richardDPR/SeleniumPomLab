@demoblaze
Feature: Visualización de productos
  Como cliente de Demoblaze
  Quiero visualizar la información de un producto
  Para conocer sus detalles antes de comprarlo

  Scenario: Verificar título, precio y descripción de un producto
    Given que estoy en la página principal de Demoblaze
    When selecciono el producto "Samsung galaxy s6"
    Then el título del producto debe ser "Samsung galaxy s6"
    And el precio del producto debe ser "$360"
    And la descripción del producto debe contener "Super AMOLED capacitive touchscreen"
