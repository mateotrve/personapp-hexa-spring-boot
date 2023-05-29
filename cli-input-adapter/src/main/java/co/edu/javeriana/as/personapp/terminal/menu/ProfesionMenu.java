package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.terminal.adapter.ProfesionInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.model.ProfesionModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProfesionMenu {
    
    private static final int OPCION_REGRESAR_MODULOS = 0;
    private static final int PERSISTENCIA_MARIADB = 1;
    private static final int PERSISTENCIA_MONGODB = 2;

    private static final int OPCION_REGRESAR_MOTOR_PERSISTENCIA = 0;
    private static final int OPCION_VER_TODO = 1;
    private static final int OPCION_VER_UNO = 2;
    private static final int OPCION_CREAR = 3;
    private static final int OPCION_EDITAR = 4;
    private static final int OPCION_ELIMINAR = 5;

    public void iniciarMenu(ProfesionInputAdapterCli profesionInputAdapterCli, Scanner keyboard) {
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
                        profesionInputAdapterCli.setProfessionOutputPortInjection("MARIA");
                        menuOpciones(profesionInputAdapterCli, keyboard);
                        break;
                    case PERSISTENCIA_MONGODB:
                        profesionInputAdapterCli.setProfessionOutputPortInjection("MONGO");
                        menuOpciones(profesionInputAdapterCli, keyboard);
                        break;
                    default:
                        log.warn("La opción elegida no es válida.");
                }
            } catch (InvalidOptionException e) {
                log.warn(e.getMessage());
            }
        } while (!isValid);
    }

    private void menuOpciones(ProfesionInputAdapterCli profesionInputAdapterCli, Scanner keyboard) {
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
                        profesionInputAdapterCli.historial();
                        break;
                    case OPCION_VER_UNO:
                        int id = leerID(keyboard);
                        if (id != -1) {
                            profesionInputAdapterCli.obtenerProfesion(id);
                        } else {
                            System.out.println("Dato incorrecto, intenta de nuevo");
                        }
                        break;
                    case OPCION_CREAR:
                        ProfesionModelCli profesionModelCli = leerPersona(keyboard);
                        profesionInputAdapterCli.crearProfesion(profesionModelCli);
                        break;
                    case OPCION_EDITAR:
                        ProfesionModelCli personaModelCliEdit = leerPersona(keyboard);
                        profesionInputAdapterCli.editarProfesion(personaModelCliEdit);
                        break;
                    case OPCION_ELIMINAR:
                        int idDelete = leerID(keyboard);
                        if (idDelete != -1) {
                            profesionInputAdapterCli.eliminarProfesion(idDelete);
                        } else {
                            System.out.println("Dato incorrecto, intenta de nuevo");
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
        System.out.println(OPCION_VER_TODO + " para ver todas las profesiones");
        System.out.println(OPCION_VER_UNO + " para ver una profesion");
        System.out.println(OPCION_CREAR + " para crear una profesion");
        System.out.println(OPCION_EDITAR + " para editar una profesion");
        System.out.println(OPCION_ELIMINAR + " para eliminar una profesion");
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

    private int leerID(Scanner keyboard) {
        try {
            System.out.print("Ingrese el ID de la profesion: ");
            return keyboard.nextInt();
        } catch (InputMismatchException e) {
            log.warn("Solo se permiten números.");
            return -1;
        }
    }

    private ProfesionModelCli leerPersona(Scanner keyboard) {
        try {
            System.out.print("Ingrese el id de la profesion: ");
            int id = keyboard.nextInt();
            System.out.print("Ingrese el nombre de la profesion: ");
            keyboard.nextLine();
            String name = keyboard.nextLine();
            System.out.print("Ingrese la descripcion de la persona: ");
            String description = keyboard.nextLine();
            return new ProfesionModelCli(id, name, description);
        } catch (InputMismatchException e) {
            log.warn("Algún dato ingresado fue incorrecto. Intenta de nuevo");
            return new ProfesionModelCli(0, "", "");

        }
    }
    
}
