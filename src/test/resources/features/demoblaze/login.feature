@demoblaze
Feature: Login de usuario
  Como cliente de Demoblaze
  Quiero iniciar sesión
  Para poder comprar productos
  
  Scenario: Login exitoso
    Given que estoy en la página principal de Demoblaze
    When ingreso usuario "testuser" y contraseña "12345"
    Then debo ver el mensaje "Welcome testuser"

  Scenario: Login fallido
    Given que estoy en la página principal de Demoblaze
    When ingreso usuario "testuser" y contraseña "wrongpass"
    Then debo ver el mensaje de error "User does not exist."