# Cómo Agregar un Nuevo Caso de Prueba

Esta guía explica paso a paso cómo agregar un nuevo escenario de prueba al proyecto, siguiendo el modelo BDD con Page Object Model.

---

## Flujo General

```
1. Escribe el escenario en el .feature (Gherkin)
        ↓
2. Crea o actualiza el Page Object si hay nuevas páginas
        ↓
3. Implementa los Step Definitions que conectan Gherkin con el Page Object
        ↓
4. Ejecuta y verifica
```

---

## Ejemplo Práctico: Agregar escenario "Verificar que el carrito muestra el precio correcto"

### Paso 1 — Escribe el escenario en el `.feature`

Abre el archivo:
```
src/test/resources/features/agregar_articulo_carrito.feature
```

Agrega el nuevo `Scenario` al final del archivo:

```gherkin
  Scenario: Verificar precio del producto en el carrito
    When agrego el producto al carrito
    Then el precio en el carrito debe ser "£55.00"
```

**Reglas del Gherkin:**
- `Given` → precondición (estado inicial)
- `When` → acción del usuario
- `Then` → resultado esperado (aserción)
- `And` → continúa el paso anterior del mismo tipo
- El `Background` se ejecuta antes de **cada** escenario automáticamente
- Usa comillas dobles `"valor"` para parámetros que el step recibirá como argumento

---

### Paso 2 — Identifica si necesitas un nuevo Page Object

Pregúntate: **¿el nuevo paso interactúa con una página que ya tiene Page Object?**

| Página                                    | Page Object existente |
|-------------------------------------------|-----------------------|
| `/products/grey-jacket`                   | `ProductPage.java`    |
| `/cart`                                   | `CartPage.java`       |
| Nueva página (ej: `/checkout`, `/account`)| Debes crear uno nuevo |

**Si necesitas un nuevo Page Object**, crea el archivo en:
```
src/test/java/com/saucedemo/pages/NuevaPagina.java
```

Estructura base de un Page Object:
```java
package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class NuevaPagina {

    private WebDriver driver;
    private WebDriverWait wait;

    // 1. Declara los localizadores CSS/XPath de los elementos
    private By miElemento = By.cssSelector(".mi-selector");

    public NuevaPagina(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // 2. Crea métodos que representen acciones o lecturas de la página
    public String obtenerTexto() {
        WebElement el = wait.until(
            ExpectedConditions.visibilityOfElementLocated(miElemento)
        );
        return el.getText().trim();
    }
}
```

**Cómo encontrar los selectores CSS:**
1. Abre Chrome y navega a la página
2. Presiona `F12` → DevTools
3. Clic en el ícono de inspector (cursor) → selecciona el elemento
4. Clic derecho sobre el elemento en el panel HTML → **Copy → Copy selector**

---

### Paso 3 — Implementa el Step Definition

Abre el archivo:
```
src/test/java/com/saucedemo/steps/AddToCartSteps.java
```

Agrega el nuevo método con la anotación que coincida **exactamente** con el texto del `.feature`:

```java
@Then("el precio en el carrito debe ser {string}")
public void elPrecioEnElCarritoDebeSer(String precioEsperado) {
    // Usa el Page Object correspondiente
    String precioActual = cartPage.getPrecioProducto();
    assertEquals(precioEsperado, precioActual,
        "El precio en el carrito no coincide");
}
```

**Reglas de los Step Definitions:**
- La anotación (`@Given`, `@When`, `@Then`, `@And`) debe coincidir con la palabra clave del `.feature`
- El texto entre comillas debe ser **idéntico** al texto del paso en el `.feature`
- `{string}` captura el valor entre comillas dobles del Gherkin como parámetro `String`
- `{int}` captura un número entero
- Si el paso ya existe en otro escenario, **no lo dupliques** — Cucumber lo reutiliza automáticamente

---

### Paso 4 — Agrega el método al Page Object (si es necesario)

Si el step necesita una acción que el Page Object aún no tiene, agrégala.

Ejemplo — agregar `getPrecioProducto()` a `CartPage.java`:

```java
// Nuevo localizador
private By precioProducto = By.cssSelector(".row .price.desktop");

// Nuevo método
public String getPrecioProducto() {
    WebElement el = wait.until(
        ExpectedConditions.visibilityOfElementLocated(precioProducto)
    );
    return el.getText().trim();
}
```

**Principios del Page Object:**
- Un método por acción o lectura
- Los selectores CSS/XPath van como campos `By` privados, nunca dentro de los métodos
- Nunca pongas aserciones (`assertEquals`, `assertTrue`) en el Page Object — eso va en el Step Definition
- Usa siempre `WebDriverWait` — nunca `Thread.sleep()`

---

### Paso 5 — Ejecuta y verifica

```cmd
gradlew.bat test
```

Si el paso no está implementado, Cucumber mostrará:
```
You can implement missing steps with the snippets below:

@Then("el precio en el carrito debe ser {string}")
public void elPrecioEnElCarritoDebeSer(String string) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
}
```

Copia ese snippet como base para tu implementación.

---

### Paso 6 — Ver evidencias en Allure

```cmd
gradlew.bat allureServe
```

El nuevo escenario aparecerá en el reporte con sus pasos detallados.

---

## Resumen de Archivos a Modificar/Crear

| Qué hacer                          | Archivo                                      |
|------------------------------------|----------------------------------------------|
| Escribir el escenario              | `src/test/resources/features/*.feature`      |
| Agregar acción/lectura de página   | `src/test/java/.../pages/XxxPage.java`       |
| Crear nueva página                 | `src/test/java/.../pages/NuevaPagina.java`   |
| Conectar Gherkin con Page Object   | `src/test/java/.../steps/XxxSteps.java`      |

---

## Buenas Prácticas

| ✅ Hacer                                              | ❌ Evitar                                        |
|------------------------------------------------------|--------------------------------------------------|
| Un Page Object por página del sitio                  | Poner selectores CSS en los Step Definitions     |
| Esperas explícitas con `WebDriverWait`               | Usar `Thread.sleep()`                            |
| Aserciones solo en Step Definitions                  | Poner `assertEquals` en Page Objects            |
| Pasos reutilizables entre escenarios                 | Duplicar steps con texto diferente para lo mismo |
| Nombres de métodos descriptivos en español o inglés  | Métodos genéricos como `click1()`, `getText2()`  |
| Un `.feature` por funcionalidad del sistema          | Mezclar funcionalidades en un mismo `.feature`   |
