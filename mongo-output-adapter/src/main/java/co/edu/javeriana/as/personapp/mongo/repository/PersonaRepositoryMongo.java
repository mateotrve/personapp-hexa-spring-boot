package co.edu.javeriana.as.personapp.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import co.edu.javeriana.as.personapp.mongo.document.PersonaDocument;

@Repository
public interface PersonaRepositoryMongo extends MongoRepository<PersonaDocument, Integer> {

}
