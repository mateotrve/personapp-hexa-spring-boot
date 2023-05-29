package co.edu.javeriana.as.personapp.terminal.adapter;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.terminal.mapper.PersonaMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.PersonaModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class PersonaInputAdapterCli {

	@Autowired
	@Qualifier("personOutputAdapterMaria")
	private PersonOutputPort personOutputPortMaria;

	@Autowired
	@Qualifier("personOutputAdapterMongo")
	private PersonOutputPort personOutputPortMongo;

	@Autowired
	private PersonaMapperCli personaMapperCli;

	PersonInputPort personInputPort;

	public void setPersonOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMaria);
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMongo);
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public void historial() {
	    log.info("Into historial PersonaEntity in Input Adapter");
	    personInputPort.findAll().stream()
	        .map(personaMapperCli::fromDomainToAdapterCli)
	        .forEach(System.out::println);
	}

	public void crearPersona(PersonaModelCli personaModelCli) {
		try {
			Person person = personInputPort.create(personaMapperCli.fromAdapterToDomain(personaModelCli));
			System.out.println("Persona creada exitosamente");
			System.out.println(person);
		} catch (Exception e) {
			System.out.println("La persona no ha podido ser creada");
		}
	}

	public void obtenerPersona(Integer id) {
		try {
			System.out.println(personInputPort.findOne(id));
		} catch (Exception e) {
			System.out.println("La persona con id " + id + " no existe en el sistema");
		}
	}

	public void editarPersona(PersonaModelCli personaModelCli) {
		try {
			Person person = personInputPort.edit(personaModelCli.getCc(),
					personaMapperCli.fromAdapterToDomain(personaModelCli));
			System.out.println("Persona editada exitosamente");
			System.out.println(person);
		} catch (Exception e) {
			System.out.println("La persona no ha podido ser editada");
		}
	}

	public void eliminarPersona(Integer id) {
		try {
			personInputPort.drop(id);
			System.out.println("Persona con la cédula " + id + " ha sido eliminada");
		} catch (Exception e) {
			System.out.println("La persona no ha podido ser eliminada");
		}
	}

}
