package co.edu.javeriana.as.personapp.application.port.in;

import java.util.List;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Port;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;

@Port
public interface StudyInputPort {
    
    public void setPersistence(StudyOutputPort studyPersistence);

    public Study create(Study study);

    public Study edit(Integer professionID, Integer personID, Study study) throws NoExistException;

    public Boolean drop(Integer professionID, Integer personID) throws NoExistException;

    public List<Study> findAll();

    public Study findOne(Integer professionID, Integer personID) throws NoExistException;

    public Integer count();

    public Person getPerson(Integer professionID, Integer personID) throws NoExistException;

    public Profession getProfession(Integer professionID, Integer personID) throws NoExistException;
    
}
