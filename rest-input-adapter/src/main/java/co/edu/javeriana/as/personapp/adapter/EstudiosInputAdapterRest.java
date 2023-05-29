package co.edu.javeriana.as.personapp.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mapper.EstudiosMapperRest;
import co.edu.javeriana.as.personapp.model.request.EstudiosRequest;
import co.edu.javeriana.as.personapp.model.response.EstudiosResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class EstudiosInputAdapterRest {
    
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
    private EstudiosMapperRest estudiosMapperRest;

    PersonInputPort personInputPort;

    ProfessionInputPort professionInputPort;

    StudyInputPort studyInputPort;

    private String setStudyOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            personInputPort = new PersonUseCase(personOutputPortMaria);
            professionInputPort = new ProfessionUseCase(professionOutputPortMaria);
            studyInputPort = new StudyUseCase(studyOutputPortMaria);
            return DatabaseOption.MARIA.toString();
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            personInputPort = new PersonUseCase(personOutputPortMongo);
            professionInputPort = new ProfessionUseCase(professionOutputPortMongo);
            studyInputPort = new StudyUseCase(studyOutputPortMongo);
            return DatabaseOption.MONGO.toString();
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public List<EstudiosResponse> historial(String database) {
        log.info("Into historial EstudiosEntity in Input Adapter");
        try {
            if (setStudyOutputPortInjection(database).equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return studyInputPort.findAll().stream().map(estudiosMapperRest::fromDomainToAdapterRestMaria)
                        .collect(Collectors.toList());
            } else {
                return studyInputPort.findAll().stream().map(estudiosMapperRest::fromDomainToAdapterRestMongo)
                        .collect(Collectors.toList());
            }

        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return new ArrayList<EstudiosResponse>();
        }
    }

    public EstudiosResponse crearEstudios(EstudiosRequest request) {
        try {
            String database = setStudyOutputPortInjection(request.getDatabase());
            Person person = personInputPort.findOne(Integer.valueOf(request.getPersonCC()));
            Profession profession = professionInputPort.findOne(Integer.valueOf(request.getProfessionId()));
            Study study = studyInputPort.create(estudiosMapperRest.fromAdapterToDomain(request, person, profession));
            if (database.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return estudiosMapperRest.fromDomainToAdapterRestMaria(study);
            } else {
                return estudiosMapperRest.fromDomainToAdapterRestMongo(study);
            }
        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return new EstudiosResponse(null, null, "", "", "", "");
        } catch (NumberFormatException e) {
            log.warn(e.getMessage());
            return new EstudiosResponse(null, null, "", "", "", "");
        } catch (NoExistException e) {
            log.warn(e.getMessage());
            return new EstudiosResponse(null, null, "", "", "", "");
        }
    }

    public EstudiosResponse obtenerEstudios(String database, String idProf, String ccPer) {
        log.info("Into obtenerEstudios EstudiosEntity in Input Adapter");
        try {
            if (setStudyOutputPortInjection(database).equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return estudiosMapperRest.fromDomainToAdapterRestMaria(
                        studyInputPort.findOne(Integer.parseInt(idProf), Integer.parseInt(ccPer)));
            } else {
                return estudiosMapperRest.fromDomainToAdapterRestMongo(
                        studyInputPort.findOne(Integer.parseInt(idProf), Integer.parseInt(ccPer)));
            }
        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return new EstudiosResponse(null, null, "", "", "", "");
        } catch (NoExistException e) {
            log.warn(e.getMessage());
            return new EstudiosResponse(null, null, "", "", "", "");
        }
    }

    public EstudiosResponse editarEstudios(EstudiosRequest request) {
        log.info("Into editarEstudios EstudiosEntity in Input Adapter");
        try {
            String database = setStudyOutputPortInjection(request.getDatabase());
            Person person = personInputPort.findOne(Integer.valueOf(request.getPersonCC()));
            Profession profession = professionInputPort.findOne(Integer.valueOf(request.getProfessionId()));
            Study phone = studyInputPort.edit(Integer.parseInt(request.getProfessionId()),
                    Integer.parseInt(request.getPersonCC()),
                    estudiosMapperRest.fromAdapterToDomain(request, person, profession));
            if (database.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return estudiosMapperRest.fromDomainToAdapterRestMaria(phone);
            } else {
                return estudiosMapperRest.fromDomainToAdapterRestMongo(phone);
            }
        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return new EstudiosResponse(null, null, "", "", "", "");
        } catch (NumberFormatException e) {
            log.warn(e.getMessage());
            return new EstudiosResponse(null, null, "", "", "", "");
        } catch (NoExistException e) {
            log.warn(e.getMessage());
            return new EstudiosResponse(null, null, "", "", "", "");
        }
    }

    public Boolean eliminarEstudios(String database, String idProf, String ccPer) {
        log.info("Into eliminarEstudios EstudiosEntity in Input Adapter");
        try {
            setStudyOutputPortInjection(database);
            return studyInputPort.drop(Integer.parseInt(idProf), Integer.parseInt(ccPer));
        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return false;
        } catch (NoExistException e) {
            log.warn(e.getMessage());
            return false;
        }
    }
    
}
