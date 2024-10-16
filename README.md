# Week 9

## Table of Contents

1. [Peticiones POST](#1-peticiones-post)
    - [¿Para qué se utiliza POST?](#para-qué-se-utiliza-post)
    - [Ejemplo con código](#ejemplo-con-código)
    - [Diferencias entre `@ResponseStatus` y `ResponseEntity`](#diferencias-entre-responsestatus-y-responseentity)
    - [Validación con Jakarta Beans](#validación-con-jakarta-beans)

2. [Ejercicio: Practicar el método POST con validación de datos en una API REST](#ejercicio-practicar-el-método-post-con-validación-de-datos-en-una-api-rest)
    - [Contexto](#contexto)
    - [Requerimientos](#requerimientos)

## **1. Peticiones POST**

### ¿Para qué se utiliza POST?

La petición **POST** se usa principalmente para **crear un nuevo recurso** en el servidor. En una API REST, cuando el
cliente envía datos a través de una solicitud POST, el servidor procesa la solicitud, crea el nuevo recurso, y devuelve
una respuesta que confirma la creación del recurso.

### Ejemplo con código:

```java

@RestController
@RequestMapping("/api/bookings")
public class TableBookingController {

    @Autowired
    private TableBookingRepository tableBookingRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)  // Indica que se ha creado un nuevo recurso
    public ResponseEntity<TableBooking> createBooking(@Valid @RequestBody TableBookingDTO tableBookingDTO) {
        TableBooking booking = new TableBooking(
                tableBookingDTO.getCustomerName(),
                tableBookingDTO.getReservationDate(),
                tableBookingDTO.getNumberOfGuests()
        );
        tableBookingRepository.save(booking);
        return ResponseEntity.status(HttpStatus.CREATED).body(booking); // Devuelve 201 Created
    }
}
```

### Explicación:

- **`@PostMapping`**: Define un endpoint que maneja solicitudes HTTP POST.
- **`@ResponseStatus(HttpStatus.CREATED)`**: Especifica que la respuesta HTTP debe tener un código de estado
  `201 Created` cuando la creación sea exitosa.
- **`ResponseEntity`**: Permite devolver tanto el cuerpo de la respuesta como el código de estado HTTP.
- **`@RequestBody`**: Indica que los datos del cuerpo de la solicitud serán mapeados al objeto `TableBookingDTO`. Esto
  se utiliza cuando el cliente envía datos en formato JSON.
- **`@Valid`**: Se usa para activar la validación de **Jakarta Beans** en el objeto recibido.
- **`TableBookingDTO`** Un DTO es un patrón de diseño utilizado para transportar datos entre diferentes partes de una
  aplicación, como entre el controlador y el servicio, o entre la aplicación y el cliente

#### Diferencias entre `@ResponseStatus` y `ResponseEntity`:

- **`@ResponseStatus`**:
    - Devuelve un **código de estado fijo**.
    - Simplifica el código cuando no necesitas personalizar el cuerpo de la respuesta.
    - **No** permite controlar el cuerpo de la respuesta directamente.
    - Útil cuando el código de estado es **siempre el mismo**.

- **`ResponseEntity`**:
    - Ofrece **control total** sobre el código de estado, cuerpo, y cabeceras HTTP.
    - Más **flexible**, permite devolver códigos de estado dinámicos.
    - Ideal para devolver **diferentes respuestas** según la lógica del controlador.

### Validación con Jakarta Beans:

Dependencia:

```java
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

Para asegurar que los datos enviados sean correctos, podemos aplicar validaciones sencillas usando anotaciones de *
*Jakarta Beans**.

```java
public class TableBookingDTO {

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotNull(message = "Reservation date is required")
    private LocalDate reservationDate;

    @Min(value = 1, message = "At least one guest is required")
    private Integer numberOfGuests;

    // Getters y Setters
}
```

- **`@NotBlank`**: Asegura que el nombre del cliente no sea nulo ni vacío.
- **`@NotNull`**: Indica que la fecha de reserva es obligatoria.
- **`@Min`**: Define un valor mínimo para el número de invitados.

### Ejercicio: Practicar el método POST con validación de datos en una API REST

#### Contexto:

Vas a implementar un endpoint **POST** en una API REST que permita crear un nuevo **curso** en una plataforma educativa.
El objetivo es que los datos enviados sean validados correctamente utilizando **Jakarta Bean Validation**, y se manejen
los errores de validación devolviendo un código de estado HTTP adecuado. Además, trabajarás con **RequestDTO** y *
*ResponseDTO** para manejar las entradas y salidas de datos.

#### Requerimientos:

1. **Entidades**:

- Debes crear la entidad `Course` que represente un curso en la plataforma.
- Debes crear un **RequestDTO** para recibir los datos de la solicitud con validaciones.
- Debes crear un **ResponseDTO** para devolver la información del recurso creado sin exponer toda la entidad.

2. **Propiedades de la entidad `Course`**:

- **`id`** (`Long`): ID único generado automáticamente.
- **`courseName`** (`String`): Nombre del curso (requerido).
- **`instructorName`** (`String`): Nombre del instructor que imparte el curso (requerido).
- **`startDate`** (`LocalDate`): Fecha de inicio del curso (requerido).
- **`durationInWeeks`** (`Integer`): Duración del curso en semanas (debe ser mayor a 0).

3. **Validaciones**:

- El nombre del curso (`courseName`) no debe estar vacío ni en blanco.
- El nombre del instructor (`instructorName`) no debe estar vacío ni en blanco.
- La fecha de inicio del curso (`startDate`) debe ser proporcionada y no puede ser en el pasado.
- La duración del curso en semanas (`durationInWeeks`) debe ser un valor positivo (mayor que 0).

4. **Endpoint a implementar**:

- **URL del endpoint**: `/api/courses`
- **Método HTTP**: POST
- **Descripción**: El endpoint debe recibir los datos de un nuevo curso, validarlos y guardarlos en la base de datos.

5. **Comportamiento esperado**:

- Si los datos no pasan la validación, debes devolver un código **400 Bad Request**.
- Si el curso se crea exitosamente, debes devolver un código **201 Created** junto con los datos del recurso recién
  creado en el formato de **ResponseDTO**.

---