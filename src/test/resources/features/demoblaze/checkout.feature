@demoblaze
Feature: Proceso de compra
  Como cliente de Demoblaze
  Quiero completar el proceso de compra
  Para adquirir los productos seleccionados

  Scenario: Compra exitosa
    Given que estoy en la página principal de Demoblaze
    And selecciono el producto "Samsung galaxy s6"
    And agrego el producto al carrito
    When procedo al checkout con nombre "Richard" y tarjeta "1234 5678 9012 3456"
    Then debo ver el mensaje de confirmación "Thank you for your purchase!"

  Scenario: Compra fallida (carrito vacío)
    Given que estoy en la página principal de Demoblaze
    When procedo al checkout con nombre "Richard" y tarjeta "1234 5678 9012 3456"
    Then debo ver el mensaje de error "Cart is empty"
