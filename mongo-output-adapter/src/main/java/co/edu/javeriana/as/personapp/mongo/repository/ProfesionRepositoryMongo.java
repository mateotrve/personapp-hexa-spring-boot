package co.edu.javeriana.as.personapp.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument;

@Repository
public interface ProfesionRepositoryMongo extends MongoRepository<ProfesionDocument, Integer>{
    
}
