# Week 9

## Table of Contents

1. [Peticiones POST](#1-peticiones-post)
    - [¿Para qué se utiliza POST?](#para-qué-se-utiliza-post)
    - [Ejemplo con código](#ejemplo-con-código)
    - [Diferencias entre `@ResponseStatus` y `ResponseEntity`](#diferencias-entre-responsestatus-y-responseentity)
    - [Validación con Jakarta Beans](#validación-con-jakarta-beans) 
    - [Ejercicio: Practicar el método POST con validación de datos en una API REST](#ejercicio-practicar-el-método-post-con-validación-de-datos-en-una-api-rest)
       - [Contexto](#contexto)
       - [Requerimientos](#requerimientos)
2. [Peticiones PUT](#2-peticiones-put)
    - [¿Para qué se utiliza PUT?](#para-qué-se-utiliza-put)
    - [Ejemplo con código](#ejemplo-con-código-1)
3. [Peticiones PATCH](#3-peticiones-patch)
    - [¿Para qué se utiliza PATCH?](#para-qué-se-utiliza-patch)
    - [Ejemplo con código](#ejemplo-con-código-2)
4. [Peticiones DELETE](#4-peticiones-delete)
    - [¿Para qué se utiliza DELETE?](#para-qué-se-utiliza-delete)
    - [Ejemplo con código](#ejemplo-con-código-3)
5. [Ejercicio: Implementar PUT, PATCH y DELETE](#ejercicio-implementar-métodos-put-patch-y-delete-en-una-api-rest)
    - [Objetivos](#objetivos)
    - [Requerimientos](#requerimientos-1)
    - [Validaciones](#validaciones)

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

```
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


---

## **2. Peticiones PUT**

### ¿Para qué se utiliza PUT?
El método **PUT** se utiliza para **actualizar un recurso existente** en su totalidad. En una operación PUT, el cliente envía un objeto completo para reemplazar el recurso actual.

### Ejemplo con código:

```java
@PutMapping("/{id}")
public ResponseEntity<TableBooking> updateBooking(@PathVariable Long id, @Valid @RequestBody TableBookingDTO tableBookingDTO) {
    return tableBookingRepository.findById(id)
            .map(existingBooking -> {
                existingBooking.setCustomerName(tableBookingDTO.getCustomerName());
                existingBooking.setReservationDate(tableBookingDTO.getReservationDate());
                existingBooking.setNumberOfGuests(tableBookingDTO.getNumberOfGuests());
                tableBookingRepository.save(existingBooking);
                return ResponseEntity.ok(existingBooking);
            })
            .orElse(ResponseEntity.notFound().build());
}
```

#### Explicación:

- **`@PutMapping("/{id}")`**: Mapea este método a una solicitud PUT que incluye una **Path Variable** (`id`).
- **`@Valid @RequestBody`**: El cuerpo de la solicitud contiene el objeto `TableBookingDTO`, que es validado antes de la actualización.
- Devuelve **`200 OK`** si la actualización es exitosa y **`404 Not Found`** si el recurso no existe.

---

## **3. Peticiones PATCH**

### ¿Para qué se utiliza PATCH?
El método **PATCH** se usa para **aplicar actualizaciones parciales** a un recurso. A diferencia de PUT, PATCH permite enviar solo los campos que deben actualizarse.

### Ejemplo con código:

```java
@PatchMapping("/{id}/reservation-date")
public ResponseEntity<TableBooking> updateReservationDate(
        @PathVariable Long id,
        @RequestBody String reservationDate) {

  return tableBookingRepository.findById(id)
          .map(existingBooking -> {
            try {
              LocalDate newDate = LocalDate.parse(reservationDate);
              existingBooking.setReservationDate(newDate);
              tableBookingRepository.save(existingBooking);
              return ResponseEntity.ok(existingBooking);
            } catch (DateTimeParseException e) {
              return ResponseEntity.badRequest().build(); // Devuelve 400 si la fecha no tiene un formato válido
            }
          })
          .orElse(ResponseEntity.notFound().build()); // Devuelve 404 si la reserva no existe
}
```

```java
@PatchMapping("/{id}/number-of-guests")
public ResponseEntity<TableBooking> updateNumberOfGuests(
        @PathVariable Long id,
        @RequestBody Integer numberOfGuests) {

  if (numberOfGuests == null || numberOfGuests < 1) {
    return ResponseEntity.badRequest().build(); // Devuelve 400 si el número de invitados es menor a 1 o es nulo
  }

  return tableBookingRepository.findById(id)
          .map(existingBooking -> {
            existingBooking.setNumberOfGuests(numberOfGuests);
            tableBookingRepository.save(existingBooking);
            return ResponseEntity.ok(existingBooking);
          })
          .orElse(ResponseEntity.notFound().build()); // Devuelve 404 si la reserva no existe
}

```
#### Explicación:

- **`@PatchMapping("/{id}/...")`**: Define un endpoint PATCH para actualizaciones parciales.
- **`@DynamicUpdate`**: Anotación en la entidad que permite que solo los campos modificados sean persistidos en la base de datos.

---

## **4. Peticiones DELETE**

### ¿Para qué se utiliza DELETE?
El método **DELETE** se utiliza para **eliminar un recurso existente**.

### Ejemplo con código:

```java
@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
    return tableBookingRepository.findById(id)
            .map(existingBooking -> {
                tableBookingRepository.delete(existingBooking);
                return ResponseEntity.noContent().build();  // Devuelve 204 No Content
            })
            .orElse(ResponseEntity.notFound().build());
}
```

#### Explicación:

- **`@DeleteMapping("/{id}")`**: Mapea una solicitud DELETE a un endpoint que recibe un ID.
- **`204 No Content`**: Indica que el recurso se eliminó correctamente.
- **`404 Not Found`**: Si el recurso no existe.

---

### Ejercicio: Implementar métodos PUT, PATCH y DELETE en una API REST

#### Objetivos:

1. **PUT**: Actualizar completamente los datos de un curso existente.
    - Debes implementar un endpoint que permita actualizar todos los campos de un curso.
    - Si el curso con el ID especificado no existe, devuelve un **404 Not Found**.
    - Si la actualización es exitosa, devuelve un **200 OK** con los datos del curso actualizados.

2. **PATCH**: Actualizar parcialmente los datos de un curso.
    - Implementa dos endpoints **PATCH**: uno para actualizar el nombre del curso y otro para actualizar la fecha de inicio.
    - Valida adecuadamente los datos recibidos en la solicitud.
    - Si los datos no son válidos o el curso no existe, devuelve un **400 Bad Request** o **404 Not Found** según corresponda.

3. **DELETE**: Eliminar un curso.
    - Implementa un endpoint que permita eliminar un curso por su ID.
    - Si el curso no existe, devuelve un **404 Not Found**.
    - Si la eliminación es exitosa, devuelve un **204 No Content**.

#### Requerimientos:

1. **PUT**:
    - Endpoint: `/api/courses/{id}`
    - Método: `PUT`
    - Debes recibir un `CourseRequestDTO` con todos los campos requeridos (nombre del curso, nombre del instructor, fecha de inicio y duración).

2. **PATCH**:
    - Endpoint 1: `/api/courses/{id}/course-name`
        - Método: `PATCH`
        - Recibe solo el nuevo nombre del curso como un **String** en el cuerpo de la solicitud  o si lo prefieres un DTO que lo contenga..
    - Endpoint 2: `/api/courses/{id}/start-date`
        - Método: `PATCH`
        - Recibe solo la nueva fecha de inicio del curso como un **String** (valida el formato y que la fecha sea presente o futura)  o si lo prefieres un DTO que lo contenga..

3. **DELETE**:
    - Endpoint: `/api/courses/{id}`
    - Método: `DELETE`
    - Si el curso es eliminado exitosamente, devuelve un código **204 No Content**.

#### Validaciones:

- Asegúrate de que los datos enviados en las solicitudes sean válidos utilizando **Jakarta Bean Validation**.
- Si algún campo es inválido, devuelve un código **400 Bad Request** con un mensaje de error apropiado.
