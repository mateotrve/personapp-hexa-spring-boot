# personapp-hexa-spring-boot
Laboratorio Arquitectura Limpia - Arquitectura Hexagonal

Este proyecto es un sistema que permite gestionar la información de personas como sus datos personales, sus profesiones, los estudios asociados a estas profesiones y sus telefonos.

El sistema da la posibilidad de guardar esta información en dos bases de datos; Maria DB Y Mongo DB.

Para interactuar con el sistema se puede hacer mediante una API REST con Swagger o por un menu en consola de comandos (CLI).

## Requisitos
Para ejecutar este proyecto se necesita:
1. Instalar MariaDB en puerto 3307
2. Instalar MongoDB en puerto 27017

## Ejecución
1. Ejecutar los scripts en las bases de datos que se encuentran en la carpeta "scripts"
2. Para interactuar con el sistema se tiene dos formas:
- Por medio del API REST, ejecutar el proyecto relacionado con "rest-input-adapter" y acceder desde un navegador a la dirección http://localhost:3000/swagger-ui.html
- Por medio de la consola de comandos CLI, ejecutar el proyecto relacionado con "cli-input-adapter" y acceder a la terminal de comandos para interactuar con el sistema.

## Colaboradores
- Mateo Andres Arenas Angel (arenas.mandres@javeriana.edu.co)

