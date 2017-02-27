package fr.miage.stidm.service;

import fr.miage.stidm.domain.CartElement;
import fr.miage.stidm.repository.CartElementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing CartElement.
 */
@Service
public class CartElementService {

    private final Logger log = LoggerFactory.getLogger(CartElementService.class);
    
    private final CartElementRepository cartElementRepository;

    public CartElementService(CartElementRepository cartElementRepository) {
        this.cartElementRepository = cartElementRepository;
    }

    /**
     * Save a cartElement.
     *
     * @param cartElement the entity to save
     * @return the persisted entity
     */
    public CartElement save(CartElement cartElement) {
        log.debug("Request to save CartElement : {}", cartElement);
        CartElement result = cartElementRepository.save(cartElement);
        return result;
    }

    /**
     *  Get all the cartElements.
     *  
     *  @return the list of entities
     */
    public List<CartElement> findAll() {
        log.debug("Request to get all CartElements");
        List<CartElement> result = cartElementRepository.findAll();

        return result;
    }

    /**
     *  Get one cartElement by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public CartElement findOne(String id) {
        log.debug("Request to get CartElement : {}", id);
        CartElement cartElement = cartElementRepository.findOne(id);
        return cartElement;
    }

    /**
     *  Delete the  cartElement by id.
     *
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete CartElement : {}", id);
        cartElementRepository.delete(id);
    }
}
