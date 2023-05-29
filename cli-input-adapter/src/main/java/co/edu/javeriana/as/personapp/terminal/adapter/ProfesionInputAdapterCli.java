package co.edu.javeriana.as.personapp.terminal.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.terminal.mapper.ProfesionMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.ProfesionModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class ProfesionInputAdapterCli {

    @Autowired
    @Qualifier("professionOutputAdapterMaria")
    private ProfessionOutputPort professionOutputPortMaria;

    @Autowired
    @Qualifier("professionOutputAdapterMongo")
    private ProfessionOutputPort professionOutputPortMongo;

    @Autowired
    private ProfesionMapperCli profesionMapperCli;

    ProfessionInputPort professionInputPort;

    public void setProfessionOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            professionInputPort = new ProfessionUseCase(professionOutputPortMaria);
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            professionInputPort = new ProfessionUseCase(professionOutputPortMongo);
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public void historial() {
        log.info("Into historial ProfesionModelCli in Input Adapter");
        professionInputPort.findAll().stream()
                .map(profesionMapperCli::fromDomainToAdapterCli)
                .forEach(System.out::println);
    }

    public void crearProfesion(ProfesionModelCli profesionModelCli) {
        try {
            Profession profession = professionInputPort
                    .create(profesionMapperCli.fromAdapterToDomain(profesionModelCli));
            System.out.println("Profesión creada exitosamente");
            System.out.println(profession);
        } catch (Exception e) {
            System.out.println("La profesión no ha podido ser creada");
        }
    }

    public void obtenerProfesion(Integer id) {
        try {
            System.out.println(professionInputPort.findOne(id));
        } catch (Exception e) {
            System.out.println("La profesión con id " + id + " no existe en el sistema");
        }
    }

    public void editarProfesion(ProfesionModelCli profesionModelCli) {
        try {
            Profession profession = professionInputPort.edit(profesionModelCli.getIdentificacion(),
                    profesionMapperCli.fromAdapterToDomain(profesionModelCli));
            System.out.println("Profesión editada exitosamente");
            System.out.println(profession);
        } catch (Exception e) {
            System.out.println("La profesión no ha podido ser editada");
        }
    }

    public void eliminarProfesion(Integer id) {
        try {
            professionInputPort.drop(id);
            System.out.println("Profesión con la identificación " + id + " ha sido eliminada");
        } catch (Exception e) {
            System.out.println("La profesión no ha podido ser eliminada");
        }
    }

}
