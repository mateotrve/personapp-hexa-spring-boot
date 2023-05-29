package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.Scanner;
import java.util.InputMismatchException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.terminal.adapter.EstudiosInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.model.EstudiosModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EstudiosMenu {
    private static final int OPCION_REGRESAR_MODULOS = 0;
    private static final int PERSISTENCIA_MARIADB = 1;
    private static final int PERSISTENCIA_MONGODB = 2;

    private static final int OPCION_REGRESAR_MOTOR_PERSISTENCIA = 0;
    private static final int OPCION_VER_TODO = 1;
    private static final int OPCION_VER_UNO = 2;
    private static final int OPCION_CREAR = 3;
    private static final int OPCION_EDITAR = 4;
    private static final int OPCION_ELIMINAR = 5;

    public void iniciarMenu(EstudiosInputAdapterCli estudiosInputAdapterCli, Scanner keyboard) {
        boolean isValid = false;
        do {
            try {
                mostrarMenuMotorPersistencia();
                int opcion = leerOpcion(keyboard);
                switch (opcion) {
                    case OPCION_REGRESAR_MODULOS:
                        isValid = true;
                        break;
                    case PERSISTENCIA_MARIADB:
                        estudiosInputAdapterCli.setStudyOutputPortInjection("MARIA");
                        menuOpciones(estudiosInputAdapterCli, keyboard);
                        break;
                    case PERSISTENCIA_MONGODB:
                        estudiosInputAdapterCli.setStudyOutputPortInjection("MONGO");
                        menuOpciones(estudiosInputAdapterCli, keyboard);
                        break;
                    default:
                        log.warn("La opción elegida no es válida.");
                }
            } catch (InvalidOptionException e) {
                log.warn(e.getMessage());
            }
        } while (!isValid);
    }

    private void menuOpciones(EstudiosInputAdapterCli estudiosInputAdapterCli, Scanner keyboard) {
        boolean isValid = false;
        do {
            try {
                mostrarMenuOpciones();
                int opcion = leerOpcion(keyboard);
                switch (opcion) {
                    case OPCION_REGRESAR_MOTOR_PERSISTENCIA:
                        isValid = true;
                        break;
                    case OPCION_VER_TODO:
                        estudiosInputAdapterCli.historial();
                        break;
                    case OPCION_VER_UNO:
                        Integer cc = leerCC(keyboard);
                        Integer id = leerID(keyboard);
                        if (cc != -1 && id != -1) {
                            estudiosInputAdapterCli.obtenerEstudios(id, cc);
                        } else {
                            System.out.println("Dato incorrecto, intente nuevamente. ");
                        }
                        break;
                    case OPCION_CREAR:
                        EstudiosModelCli estudiosModelCli = leerEstudios(keyboard);
                        estudiosInputAdapterCli.crearEstudios(estudiosModelCli);
                        break;
                    case OPCION_EDITAR:
                        EstudiosModelCli estudiosModelCli2 = leerEstudios(keyboard);
                        estudiosInputAdapterCli.editarEstudios(estudiosModelCli2);
                        break;
                    case OPCION_ELIMINAR:
                        Integer cc2 = leerCC(keyboard);
                        Integer id2 = leerID(keyboard);
                        if (cc2 != -1 && id2 != -1) {
                            estudiosInputAdapterCli.eliminarEstudios(id2, cc2);
                        } else {
                            System.out.println("Dato incorrecto, intente nuevamente. ");
                        }
                        break;
                    default:
                        log.warn("La opción elegida no es válida.");
                }
            } catch (InputMismatchException e) {
                log.warn("Solo se permiten números.");
            }
        } while (!isValid);
    }

    private void mostrarMenuOpciones() {
        System.out.println("----------------------");
        System.out.println(OPCION_VER_TODO + " para ver todas las carreras");
        System.out.println(OPCION_VER_UNO + " para ver una carrera");
        System.out.println(OPCION_CREAR + " para crear una carrera");
        System.out.println(OPCION_EDITAR + " para editar una carrera");
        System.out.println(OPCION_ELIMINAR + " para eliminar una carrera");
        System.out.println(OPCION_REGRESAR_MOTOR_PERSISTENCIA + " para regresar");
    }

    private void mostrarMenuMotorPersistencia() {
        System.out.println("----------------------");
        System.out.println(PERSISTENCIA_MARIADB + " para MariaDB");
        System.out.println(PERSISTENCIA_MONGODB + " para MongoDB");
        System.out.println(OPCION_REGRESAR_MODULOS + " para regresar");
    }

    private int leerOpcion(Scanner keyboard) {
        try {
            System.out.print("Ingrese una opción: ");
            return keyboard.nextInt();
        } catch (InputMismatchException e) {
            log.warn("Solo se permiten números.");
            return leerOpcion(keyboard);
        }
    }

    private int leerCC(Scanner keyboard) {
        try {
            System.out.print("Ingrese la identificación de la persona: ");
            return keyboard.nextInt();
        } catch (InputMismatchException e) {
            log.warn("Solo se permiten cadenas de texto.");
            return -1;
        }
    }

    private int leerID(Scanner keyboard) {
        try {
            System.out.print("Ingrese el ID de la profesión: ");
            return keyboard.nextInt();
        } catch (InputMismatchException e) {
            log.warn("Solo se permiten cadenas de texto.");
            return -1;
        }
    }

    private EstudiosModelCli leerEstudios(Scanner keyboard) {
        try {
            System.out.print("Ingrese la identificacion de la persona: ");
            Integer cc = keyboard.nextInt();
            System.out.print("Ingrese el ID de la profesión: ");
            Integer id = keyboard.nextInt();
            System.out.print("Ingrese la fecha de graduación (dd-MM-yyyy): ");
            keyboard.nextLine();
            String fechacad = keyboard.nextLine();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate fecha = LocalDate.parse(fechacad, formatter);
            System.out.print("Ingrese el nombre de la universidad: ");
            String university = keyboard.nextLine();
            return new EstudiosModelCli(cc, id, fecha, university);
        } catch (InputMismatchException e) {
            log.warn("Datos ingresados incorrectos. Intente nuevamente");
            return new EstudiosModelCli(0, 0, null, "");
        }
    }
}
