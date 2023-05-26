package co.edu.javeriana.as.personapp.mariadb.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mariadb.entity.ProfesionEntity;
import co.edu.javeriana.as.personapp.mariadb.mapper.ProfesionMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.ProfesionRepositoryMaria;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("professionOutputAdapterMaria")
public class ProfessionOutputAdapterMaria implements ProfessionOutputPort {

    @Autowired
    private ProfesionMapperMaria profesionMapperMaria;

    @Autowired
    private ProfesionRepositoryMaria profesionRepository;

    @Override
    public Profession save(Profession profession) {
        log.debug("Into save on Adapter MariaDB");
		ProfesionEntity persistedProfesion = profesionRepository.save(profesionMapperMaria.fromDomainToAdapter(profession));
		return profesionMapperMaria.fromAdapterToDomain(persistedProfesion);
    }

    @Override
    public boolean delete(Integer identification) {
        log.debug("Into delete on Adapter MariaDB");
		profesionRepository.deleteById(identification);
		return profesionRepository.findById(identification).isEmpty();
    }

    @Override
    public List<Profession> find() {
        log.debug("Into find on Adapter MariaDB");
		return profesionRepository.findAll().stream().map(profesionMapperMaria::fromAdapterToDomain)
				.collect(Collectors.toList());
    }

    @Override
    public Profession findById(Integer identification) {
        log.debug("Into findById on Adapter MariaDB");
		if (profesionRepository.findById(identification).isEmpty()) {
			return null;
		} else {
			return profesionMapperMaria.fromAdapterToDomain(profesionRepository.findById(identification).get());
		}
    }
    
}
