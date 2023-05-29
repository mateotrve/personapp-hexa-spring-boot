package co.edu.javeriana.as.personapp.mariadb.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntityPK;
import co.edu.javeriana.as.personapp.mariadb.mapper.EstudiosMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.EstudiosRepository;
import co.edu.javeriana.as.personapp.mariadb.repository.PersonaRepositoryMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.ProfesionRepositoryMaria;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("studyOutputAdapterMaria")
public class StudyOutputAdapterMaria implements StudyOutputPort{

    @Autowired
    private PersonaRepositoryMaria personaRepositoryMaria;

    @Autowired
    private ProfesionRepositoryMaria profesionRepositoryMaria;

    @Autowired
    private EstudiosMapperMaria estudiosMapperMaria;

    @Autowired
    private EstudiosRepository estudiosRepository;
    
    @Override
    public Study save(Study study) {
        log.debug("Into save on Adapter MariaDB");
		EstudiosEntity persistedEstudios = estudiosRepository.save(estudiosMapperMaria.fromDomainToAdapter(study));
        persistedEstudios.setPersona(personaRepositoryMaria.findById(persistedEstudios.getEstudiosPK().getCcPer()).orElseThrow());
        persistedEstudios.setProfesion(profesionRepositoryMaria.findById(persistedEstudios.getEstudiosPK().getIdProf()).orElseThrow());
		return estudiosMapperMaria.fromAdapterToDomain(persistedEstudios);
    }

    @Override
    public Boolean delete(Integer professionID, Integer personID) {
        log.debug("Into delete on Adapter MariaDB");
        EstudiosEntityPK estudiosEntityPK = new EstudiosEntityPK(professionID, personID);
		estudiosRepository.deleteById(estudiosEntityPK);
		return estudiosRepository.findById(estudiosEntityPK).isEmpty();
    }

    @Override
    public List<Study> find() {
        log.debug("Into find on Adapter MariaDB");
		return estudiosRepository.findAll().stream().map(estudiosMapperMaria::fromAdapterToDomain)
				.collect(Collectors.toList());
    }

    @Override
    public Study findById(Integer proffesionID, Integer personID) {
        log.debug("Into findById on Adapter MariaDB");
        EstudiosEntityPK estudiosEntityPK = new EstudiosEntityPK(proffesionID, personID);
		if (estudiosRepository.findById(estudiosEntityPK).isEmpty()) {
			return null;
		} else {
			return estudiosMapperMaria.fromAdapterToDomain(estudiosRepository.findById(estudiosEntityPK).get());
		}
    }
    
}
