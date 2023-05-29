package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.terminal.adapter.PersonaInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.model.PersonaModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PersonaMenu {

	private static final int OPCION_REGRESAR_MODULOS = 0;
	private static final int PERSISTENCIA_MARIADB = 1;
	private static final int PERSISTENCIA_MONGODB = 2;

	private static final int OPCION_REGRESAR_MOTOR_PERSISTENCIA = 0;
	private static final int OPCION_VER_TODO = 1;
	private static final int OPCION_VER_UNO = 2;
	private static final int OPCION_CREAR = 3;
	private static final int OPCION_EDITAR = 4;
	private static final int OPCION_ELIMINAR = 5;

	public void iniciarMenu(PersonaInputAdapterCli personaInputAdapterCli, Scanner keyboard) {
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
						personaInputAdapterCli.setPersonOutputPortInjection("MARIA");
						menuOpciones(personaInputAdapterCli, keyboard);
						break;
					case PERSISTENCIA_MONGODB:
						personaInputAdapterCli.setPersonOutputPortInjection("MONGO");
						menuOpciones(personaInputAdapterCli, keyboard);
						break;
					default:
						log.warn("La opción elegida no es válida.");
				}
			} catch (InvalidOptionException e) {
				log.warn(e.getMessage());
			}
		} while (!isValid);
	}

	private void menuOpciones(PersonaInputAdapterCli personaInputAdapterCli, Scanner keyboard) {
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
						personaInputAdapterCli.historial();
						break;
					case OPCION_VER_UNO:
						int cc = leerCC(keyboard);
						if (cc != -1) {
							personaInputAdapterCli.obtenerPersona(cc);
						} else {
							System.out.println("Dato incorrecto, intenta de nuevo");
						}
						break;
					case OPCION_CREAR:
						PersonaModelCli personaModelCli = leerPersona(keyboard);
						personaInputAdapterCli.crearPersona(personaModelCli);
						break;
					case OPCION_EDITAR:
						PersonaModelCli personaModelCliEdit = leerPersona(keyboard);
						personaInputAdapterCli.editarPersona(personaModelCliEdit);
						break;
					case OPCION_ELIMINAR:
						int ccDelete = leerCC(keyboard);
						if (ccDelete != -1) {
							personaInputAdapterCli.eliminarPersona(ccDelete);
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
		System.out.println(OPCION_VER_TODO + " para ver todas las personas");
		System.out.println(OPCION_VER_UNO + " para ver una persona");
		System.out.println(OPCION_CREAR + " para crear una persona");
		System.out.println(OPCION_EDITAR + " para editar una persona");
		System.out.println(OPCION_ELIMINAR + " para eliminar una persona");
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
			System.out.print("Ingrese la cédula de la persona: ");
			return keyboard.nextInt();
		} catch (InputMismatchException e) {
			log.warn("Solo se permiten números.");
			return -1;
		}
	}

	private PersonaModelCli leerPersona(Scanner keyboard) {
		try {
			System.out.print("Ingrese la cédula de la persona: ");
			int cc = keyboard.nextInt();
			System.out.print("Ingrese el nombre de la persona: ");
			keyboard.nextLine();
			String name = keyboard.nextLine();
			System.out.print("Ingrese el apellido de la persona: ");
			String lastName = keyboard.nextLine();
			System.out.print("Ingrese el género de la persona (MALE, FEMALE, OTHER): ");
			String gender = keyboard.next();
			System.out.print("Ingrese la edad de la persona: ");
			int age = keyboard.nextInt();
			return new PersonaModelCli(cc, name, lastName, gender, age);
		} catch (InputMismatchException e) {
			log.warn("Algún dato ingresado fue incorrecto. Intenta de nuevo");
			return new PersonaModelCli(0, "", "", "", 0);
		}
	}

}
