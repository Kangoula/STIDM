package fr.miage.stidm.repository;

import fr.miage.stidm.domain.CartElement;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the CartElement entity.
 */
@SuppressWarnings("unused")
public interface CartElementRepository extends MongoRepository<CartElement,String> {

}
