package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.terminal.adapter.TelefonoInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.model.TelefonoModelCli;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TelefonoMenu {
    
    private static final int OPCION_REGRESAR_MODULOS = 0;
    private static final int PERSISTENCIA_MARIADB = 1;
    private static final int PERSISTENCIA_MONGODB = 2;

    private static final int OPCION_REGRESAR_MOTOR_PERSISTENCIA = 0;
    private static final int OPCION_VER_TODO = 1;
    private static final int OPCION_VER_UNO = 2;
    private static final int OPCION_CREAR = 3;
    private static final int OPCION_EDITAR = 4;
    private static final int OPCION_ELIMINAR = 5;

    public void iniciarMenu(TelefonoInputAdapterCli telefonoInputAdapterCli, Scanner keyboard) {
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
                        telefonoInputAdapterCli.setPhoneOutputPortInjection("MARIA");
                        menuOpciones(telefonoInputAdapterCli, keyboard);
                        break;
                    case PERSISTENCIA_MONGODB:
                        telefonoInputAdapterCli.setPhoneOutputPortInjection("MONGO");
                        menuOpciones(telefonoInputAdapterCli, keyboard);
                        break;
                    default:
                        log.warn("La opción elegida no es válida.");
                }
            } catch (InvalidOptionException e) {
                log.warn(e.getMessage());
            }
        } while (!isValid);
    }

    private void menuOpciones(TelefonoInputAdapterCli telefonoInputAdapterCli, Scanner keyboard) {
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
                        telefonoInputAdapterCli.historial();
                        break;
                    case OPCION_VER_UNO:
                        String number = leerNumero(keyboard);
                        if (number != "") {
                            telefonoInputAdapterCli.obtenerTelefono(number);
                        } else {
                            System.out.println("Dato incorrecto, intenta de nuevo");
                        }
                        break;
                    case OPCION_CREAR:
                        TelefonoModelCli telefonoModelCli = leerTelefono(keyboard);
                        telefonoInputAdapterCli.crearTelefono(telefonoModelCli);
                        break;
                    case OPCION_EDITAR:
                        TelefonoModelCli telefonoModelCli2 = leerTelefono(keyboard);
                        telefonoInputAdapterCli.editarTelefono(telefonoModelCli2);
                        break;
                    case OPCION_ELIMINAR:
                        String number2 = leerNumero(keyboard);
                        if (number2 != "") {
                            telefonoInputAdapterCli.eliminarTelefono(number2);
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
        System.out.println(OPCION_VER_TODO + " para ver todas los telefonos");
        System.out.println(OPCION_VER_UNO + " para ver un teléfono");
        System.out.println(OPCION_CREAR + " para crear un teléfono");
        System.out.println(OPCION_EDITAR + " para editar un teléfono");
        System.out.println(OPCION_ELIMINAR + " para eliminar un teléfono");
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

    private String leerNumero(Scanner keyboard) {
        try {
            System.out.print("Ingrese el número de teléfono: ");
            return keyboard.next();
        } catch (InputMismatchException e) {
            log.warn("Solo se permiten cadenas.");
            return "";
        }
    }

    private TelefonoModelCli leerTelefono(Scanner keyboard) {
        try {
            System.out.print("Ingrese el número de teléfono: ");
            keyboard.nextLine();
            String num = keyboard.nextLine();
            System.out.print("Ingrese la compañía del teléfono: ");
            String company = keyboard.nextLine();
            System.out.print("Ingrese la cedula de la persona: ");
            int cc = keyboard.nextInt();
            return new TelefonoModelCli(num, company, cc);
        } catch (InputMismatchException e) {
            log.warn("Algún dato ingresado fue incorrecto. Intenta de nuevo");
            return new TelefonoModelCli("", "", 0);
        }
    }

}
