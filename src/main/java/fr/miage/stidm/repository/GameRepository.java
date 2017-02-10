package fr.miage.stidm.repository;

import fr.miage.stidm.domain.Game;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Game entity.
 */
@SuppressWarnings("unused")
public interface GameRepository extends MongoRepository<Game,String> {

}
