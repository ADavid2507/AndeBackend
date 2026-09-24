# AndeBackend — EduAndes

Backend REST para la gestión de carreras, cursos, estudiantes y matrículas académicas del caso EduAndes. Proyecto del examen parcial de Lenguaje de Programación II, Unidad 1, de la Universidad Peruana Unión.

Repositorio: [ADavid2507/AndeBackend](https://github.com/ADavid2507/AndeBackend).

## Estado del proyecto

Proyecto en desarrollo. Este documento describe la estructura revisada el 24 de septiembre de 2026 y distingue el contrato solicitado de las funcionalidades pendientes. La presencia de código no acredita que los casos de prueba hayan pasado; no se ha verificado una ejecución completa de la API al preparar este README.

## Tecnologías

- Java 21 y Maven.
- Spring Boot 4.1.1.
- Spring Web MVC y Spring Data JPA con Hibernate.
- Oracle Database.
- Jakarta Bean Validation.
- Lombok y SLF4J.
- springdoc OpenAPI 3.1.0 para documentación con Swagger UI.

## Estructura de paquetes

```text
src/main/java/pe/edu/upeu/AndeBackend/
├── AndeBackendApplication.java
├── controller/         Endpoints HTTP
├── dto/                Objetos de entrada, salida y proyección del reporte
├── entity/             Carrera, Curso, Estudiante, Matricula y DetalleMatricula
├── enums/              EstadoMatricula: REGISTRADA y ANULADA
├── exception/          Excepciones y GlobalExceptionHandler
│   └── dto/            ErrorResponseDTO
├── mapper/             Conversión entre entidades y DTO
├── repository/         Acceso a datos y consultas JPA
└── service/
    ├── generic/        CrudService
    ├── service/        Interfaces de los servicios
    └── impl/           Implementaciones y reglas de negocio

src/test/java/pe/edu/upeu/AndeBackend/
└── AndeBackendApplicationTests.java
```

## Arquitectura y decisiones de diseño

El flujo principal de una solicitud es:

```text
Cliente HTTP → Controller → Service → Repository → Oracle
```

- **Controller:** recibe solicitudes, aplica validaciones de entrada y devuelve DTO mediante ResponseEntity.
- **Service:** coordina las operaciones y aplica las reglas de negocio. Las interfaces separan el contrato de su implementación; CrudService agrupa las operaciones CRUD comunes.
- **Repository:** consulta y persiste entidades mediante Spring Data JPA. Las decisiones de negocio corresponden al servicio.
- **DTO y Mapper:** separan el contrato público del modelo de persistencia y evitan exponer directamente las relaciones de las entidades.
- **Inyección por constructor:** hace explícitas las dependencias entre componentes.
- **Transacciones:** el registro y la anulación de matrículas se ejecutan en servicios transaccionales para mantener consistencia entre cabecera, detalles y vacantes.
- **Importes:** se utiliza BigDecimal con escala de dos decimales y redondeo HALF_UP.
- **Historial de cobro:** cada detalle conserva los créditos y el costo del curso al matricularse, de modo que posteriores cambios del curso no alteren los importes registrados.
- **Reportes:** el reporte de matriculados por curso utiliza una consulta JPQL con GROUP BY, COUNT y SUM, y una proyección de salida.
- **Errores:** las excepciones se centralizan mediante GlobalExceptionHandler y ErrorResponseDTO.

## Modelo de datos

| Entidad | Tabla | Responsabilidad |
|---|---|---|
| Carrera | carreras | Información de las carreras académicas |
| Curso | cursos | Código, créditos, ciclo, vacantes y carrera del curso |
| Estudiante | estudiantes | Identificación, estado y carrera del estudiante |
| Matricula | matriculas | Estudiante, periodo, estado, créditos e importe total |
| DetalleMatricula | detalle_matriculas | Curso matriculado, créditos y costo histórico |

Una carrera tiene cursos y estudiantes. Un estudiante puede tener matrículas; cada matrícula contiene detalles asociados a cursos. Las entidades incluyen fechas de creación y modificación.

## Reglas de matrícula

El servicio de matrícula contiene comprobaciones para las siguientes reglas, cuya ejecución debe demostrarse con las pruebas del examen:

| Regla | Condición |
|---|---|
| RN-01 | El estudiante y los cursos deben estar activos; los cursos deben pertenecer a la carrera del estudiante. |
| RN-02 | Cada curso debe tener vacantes. Registrar descuenta una y anular la devuelve. |
| RN-03 | Solo puede existir una matrícula REGISTRADA por estudiante y periodo. |
| RN-04 | El total no puede superar 20 créditos. El costo por curso es créditos × costo por crédito, con valor predeterminado de S/ 120.00. |

También se comprueba que la solicitud contenga cursos, que no incluya cursos repetidos y que una matrícula no se anule dos veces. El reporte considera únicamente matrículas REGISTRADA.

## Contrato HTTP solicitado

La siguiente tabla corresponde al contrato de la guía; no representa una certificación de disponibilidad de todos los endpoints.

| Método | Ruta | Operación |
|---|---|---|
| GET | /api/v1/health | Comprobar el estado de la API y la conexión real a Oracle |
| GET / POST | /api/v1/carreras | Listar y registrar carreras |
| GET / PUT / DELETE | /api/v1/carreras/{id} | Consultar, actualizar y eliminar una carrera |
| GET | /api/v1/carreras/{id}/cursos | Listar cursos de una carrera |
| GET / POST | /api/v1/cursos | Listar y registrar cursos |
| GET / PUT / DELETE | /api/v1/cursos/{id} | Consultar, actualizar y eliminar un curso |
| GET | /api/v1/cursos/buscar | Filtrar por nombre, carreraId, ciclo y conVacantes; ordenar sin paginación |
| GET / POST | /api/v1/estudiantes | Listar y registrar estudiantes |
| GET / PUT | /api/v1/estudiantes/{id} | Consultar y actualizar un estudiante |
| GET / POST | /api/v1/matriculas | Listar y registrar matrículas |
| GET | /api/v1/matriculas/{id} | Consultar una matrícula con sus detalles |
| PATCH | /api/v1/matriculas/{id}/anular | Anular y devolver vacantes |
| GET | /api/v1/reportes/matriculados-por-curso | Reporte con filtros periodo y carreraId |

Respuestas esperadas: 201 al crear, 200 al consultar o actualizar, 204 al eliminar, 400 por validación, 404 por recurso o referencia inexistente, 409 por conflictos de negocio y 500 genérico sin exponer trazas. Salud debe devolver 503 si Oracle no responde.

### Diferencias y pendientes detectados

- Los controladores de carreras y cursos utilizan actualmente las rutas singulares `/api/v1/carrera` y `/api/v1/curso`; deben alinearse con el contrato plural.
- Las consultas por ID de esos dos controladores requieren vincular el parámetro de ruta mediante `@PathVariable`.
- Falta implementar la consulta de cursos por carrera y la búsqueda combinada con orden validado.
- HealthController todavía no está registrado como controlador REST ni comprueba la conexión a Oracle.
- Falta completar la comparación de nombres de carrera sin distinguir espacios y verificar la normalización al guardar.
- Falta comprobar en los servicios las asociaciones antes de eliminar carreras con cursos o cursos con matrículas.
- El servicio de cursos rechaza nombres duplicados; la guía exige unicidad del código, no del nombre del curso.
- Debe verificarse el contrato completo de errores, la documentación OpenAPI y los niveles de logs mediante los casos de demostración.

## Datos semilla

El conjunto requerido contiene:

- Tres carreras: Ingeniería de Sistemas, Ingeniería Civil y Arquitectura.
- Doce cursos, con al menos dos sin vacantes y uno inactivo.
- Más de 20 créditos entre los cursos con vacantes de Ingeniería de Sistemas.
- Seis estudiantes de al menos dos carreras, con al menos uno inactivo.

Las matrículas y sus detalles se generan mediante la API durante la demostración. Los identificadores deben consultarse antes de las pruebas y no asumirse a partir del orden de inserción.

El archivo `datos_semilla.sql` debe incorporarse al repositorio; no se encontró en la revisión de archivos realizada para este documento.

## Pruebas y evidencias

El proyecto contiene una prueba de carga del contexto, `AndeBackendApplicationTests.contextLoads`. No se ha confirmado que pase ni se han encontrado las dos pruebas funcionales automatizadas necesarias para completar la entrega.

Para ejecutar las pruebas con Maven:

```shell
mvn test
```

La demostración debe cubrir los casos CP-01 a CP-13 de la guía: salud, duplicados, validaciones, referencias inexistentes, filtros, matrícula correcta, reglas RN-01 a RN-04, rollback, reporte y anulación. Debe conservarse evidencia de las respuestas HTTP y de las vacantes en la base de datos.

La colección de Postman debe exportarse en `/postman`. La colección y el PDF de evidencias están pendientes de incorporarse o verificarse en el repositorio.

## Organización del equipo

| Integrante | Responsabilidad acordada |
|---|---|
| Asmat | Carreras y cursos; componentes compartidos de errores y CRUD; búsqueda, salud, reporte y evidencias de sus módulos |
| Segundo integrante — completar nombre | Estudiantes y matrículas; reglas RN-01 a RN-04; transacciones, anulación y evidencias de sus módulos |

La integración, las consultas que dependen de varios módulos y la entrega final son responsabilidad conjunta. Esta distribución expresa el acuerdo de trabajo, no acredita la autoría de cada archivo.

## Flujo Git y entrega

El flujo solicitado es `feature/<funcionalidad>-<apellido>` hacia `develop`, mediante pull request revisado por el compañero, y posteriormente integración en `main`. Las correcciones utilizan `fix/<descripcion>-<apellido>` y la solicitud individual del examen utiliza `sc-<letra>-<apellido>`.

Los mensajes de commit deben describir el cambio en español y utilizar los prefijos `feat`, `fix`, `refactor`, `test` o `docs`.

Antes de entregar, comprobar:

- [ ] Contrato HTTP y reglas del examen completos y demostrados.
- [ ] Datos semilla completos en `datos_semilla.sql`.
- [ ] Colección exportada en `/postman` y al menos dos pruebas automatizadas aprobadas.
- [ ] PDF con evidencias de Swagger, casos de prueba, gráfico de ramas y contribuciones por autor.
- [ ] Nombres completos de ambos integrantes y enlaces a sus aportes.
- [ ] Al menos dos pull requests cerrados por integrante, con revisión del compañero.
- [ ] Etiqueta `v1.0-unidad1` sobre el commit evaluado.
- [ ] Rama individual de la solicitud de cambio con al menos tres commits distribuidos durante el examen.
- [ ] README actualizado al estado final del proyecto.

## Referencias

- Guía del examen parcial de Lenguaje de Programación II, Unidad 1, versión A: caso EduAndes.
- [PharmaDB](https://github.com/ADavid2507/PharmaDB), referencia de estructura e implementación por capas.
