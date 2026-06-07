@demoblaze
Feature: Registro de usuario
  Como cliente de Demoblaze
  Quiero registrarme en la plataforma
  Para poder iniciar sesión y comprar productos

  Scenario: Registro exitoso
    Given que estoy en la página principal de Demoblaze
    When me registro con usuario "nuevoUser" y contraseña "12345"
    Then debo ver el mensaje de éxito "Sign up successful."

  Scenario: Registro fallido (usuario existente)
    Given que estoy en la página principal de Demoblaze
    When me registro con usuario "testuser" y contraseña "12345"
    Then debo ver el mensaje de error "This user already exist."
