package co.edu.javeriana.as.personapp.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.model.request.EstudiosRequest;
import co.edu.javeriana.as.personapp.model.response.EstudiosResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Mapper
public class EstudiosMapperRest {
    
    public EstudiosResponse fromDomainToAdapterRestMaria(Study study) {
        return fromDomainToAdapterRest(study, "MariaDB");
    }

    public EstudiosResponse fromDomainToAdapterRestMongo(Study study) {
        return fromDomainToAdapterRest(study, "MongoDB");
    }

    public EstudiosResponse fromDomainToAdapterRest(Study study, String database) {
        return new EstudiosResponse(
                study.getPerson().getIdentification() + "",
                study.getProfession().getIdentification() + "",
                study.getGraduationDate() + "",
                study.getUniversityName(),
                database,
                "OK");
    }

    public Study fromAdapterToDomain(EstudiosRequest request, Person person, Profession profession) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return new Study(person,
                profession,
                LocalDate.parse(request.getGraduationDate(), formatter), request.getUniversityName());
    }
    
}
