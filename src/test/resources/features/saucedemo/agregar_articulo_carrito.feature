@saucedemo
Feature: Agregar articulo al carrito
  Como cliente de Sauce Demo
  Quiero agregar un articulo al carrito
  Para poder comprarlo

  Background:
    Given que estoy en la pagina del producto "Grey jacket"

  Scenario: Verificar titulo y precio del producto
    Then el titulo del producto debe ser "Grey jacket"
    And el precio del producto debe ser "£55.00"

  Scenario: Agregar un articulo al carrito y verificarlo
    When agrego el producto al carrito
    Then el producto "Grey jacket" debe aparecer en el carrito

  Scenario: Agregar Bronze sandals desde el catalogo al carrito
    Given que estoy en el catalogo de productos
    When hago clic en el producto "Bronze sandals"
    And agrego el producto al carrito
    Then el producto "Bronze sandals" debe aparecer en el carrito