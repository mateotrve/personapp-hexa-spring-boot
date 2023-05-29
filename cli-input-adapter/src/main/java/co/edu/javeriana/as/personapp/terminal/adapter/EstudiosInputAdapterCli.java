package co.edu.javeriana.as.personapp.terminal.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.application.usecase.StudyUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.terminal.mapper.EstudiosMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.EstudiosModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class EstudiosInputAdapterCli {
    
    @Autowired
    @Qualifier("personOutputAdapterMaria")
    private PersonOutputPort personOutputPortMaria;

    @Autowired
    @Qualifier("professionOutputAdapterMaria")
    private ProfessionOutputPort professionOutputPortMaria;

    @Autowired
    @Qualifier("studyOutputAdapterMaria")
    private StudyOutputPort studyOutputPortMaria;

    @Autowired
    @Qualifier("personOutputAdapterMongo")
    private PersonOutputPort personOutputPortMongo;

    @Autowired
    @Qualifier("professionOutputAdapterMongo")
    private ProfessionOutputPort professionOutputPortMongo;

    @Autowired
    @Qualifier("studyOutputAdapterMongo")
    private StudyOutputPort studyOutputPortMongo;

    @Autowired
    private EstudiosMapperCli estudiosMapperCli;

    PersonInputPort personInputPort;

    ProfessionInputPort professionInputPort;

    StudyInputPort studyInputPort;

    public void setStudyOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            personInputPort = new PersonUseCase(personOutputPortMaria);
            professionInputPort = new ProfessionUseCase(professionOutputPortMaria);
            studyInputPort = new StudyUseCase(studyOutputPortMaria);
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            personInputPort = new PersonUseCase(personOutputPortMongo);
            professionInputPort = new ProfessionUseCase(professionOutputPortMongo);
            studyInputPort = new StudyUseCase(studyOutputPortMongo);
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public void historial() {
        log.info("Into historial EstudiosModelCli in Input Adapter");
        studyInputPort.findAll().stream()
                .map(estudiosMapperCli::fromDomainToAdapterCli)
                .forEach(System.out::println);
    }

    public void crearEstudios(EstudiosModelCli estudiosModelCli) {
        try {
            Person person = personInputPort.findOne(estudiosModelCli.getDuenioId());
            Profession profession = professionInputPort.findOne(estudiosModelCli.getProfesionId());
            Study study = studyInputPort
                    .create(estudiosMapperCli.fromAdapterToDomain(estudiosModelCli, person, profession));
            System.out.println("Carrera creada exitosamente");
            System.out.println(study);
        } catch (Exception e) {
            System.out.println("La carrera no ha podido ser creada");
        }
    }

    public void obtenerEstudios(Integer idProf, Integer ccPer) {
        try {
            System.out.println(studyInputPort.findOne(idProf, ccPer));
        } catch (Exception e) {
            System.out.println("La carrera de la persona con cédula " + ccPer + " y el id de profesión " + idProf
                    + "no existe en el sistema");
        }
    }

    public void editarEstudios(EstudiosModelCli estudiosModelCli) {
        try {
            Person person = personInputPort.findOne(estudiosModelCli.getDuenioId());
            Profession profession = professionInputPort.findOne(estudiosModelCli.getProfesionId());
            Study study = studyInputPort.edit(estudiosModelCli.getProfesionId(), estudiosModelCli.getDuenioId(),
                    estudiosMapperCli.fromAdapterToDomain(estudiosModelCli, person, profession));
            System.out.println("Carrera editada exitosamente");
            System.out.println(study);
        } catch (Exception e) {
            System.out.println("La carrera no ha podido ser editada");
        }
    }

    public void eliminarEstudios(Integer idProf, Integer ccPer) {
        try {
            studyInputPort.drop(idProf, ccPer);
            System.out.println("Carrera de la persona con cédula " + ccPer + " y el id de profesión " + idProf
                    + " ha sido eliminada");
        } catch (Exception e) {
            System.out.println("La carrera no ha podido ser eliminada");
        }
    }
}
