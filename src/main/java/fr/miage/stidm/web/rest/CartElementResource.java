package fr.miage.stidm.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.miage.stidm.domain.CartElement;
import fr.miage.stidm.service.CartElementService;
import fr.miage.stidm.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing CartElement.
 */
@RestController
@RequestMapping("/api")
public class CartElementResource {

    private final Logger log = LoggerFactory.getLogger(CartElementResource.class);

    private static final String ENTITY_NAME = "cartElement";
        
    private final CartElementService cartElementService;

    public CartElementResource(CartElementService cartElementService) {
        this.cartElementService = cartElementService;
    }

    /**
     * POST  /cart-elements : Create a new cartElement.
     *
     * @param cartElement the cartElement to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cartElement, or with status 400 (Bad Request) if the cartElement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cart-elements")
    @Timed
    public ResponseEntity<CartElement> createCartElement(@Valid @RequestBody CartElement cartElement) throws URISyntaxException {
        log.debug("REST request to save CartElement : {}", cartElement);
        if (cartElement.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new cartElement cannot already have an ID")).body(null);
        }
        CartElement result = cartElementService.save(cartElement);
        return ResponseEntity.created(new URI("/api/cart-elements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cart-elements : Updates an existing cartElement.
     *
     * @param cartElement the cartElement to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cartElement,
     * or with status 400 (Bad Request) if the cartElement is not valid,
     * or with status 500 (Internal Server Error) if the cartElement couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cart-elements")
    @Timed
    public ResponseEntity<CartElement> updateCartElement(@Valid @RequestBody CartElement cartElement) throws URISyntaxException {
        log.debug("REST request to update CartElement : {}", cartElement);
        if (cartElement.getId() == null) {
            return createCartElement(cartElement);
        }
        CartElement result = cartElementService.save(cartElement);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cartElement.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cart-elements : get all the cartElements.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cartElements in body
     */
    @GetMapping("/cart-elements")
    @Timed
    public List<CartElement> getAllCartElements() {
        log.debug("REST request to get all CartElements");
        return cartElementService.findAll();
    }

    /**
     * GET  /cart-elements/:id : get the "id" cartElement.
     *
     * @param id the id of the cartElement to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cartElement, or with status 404 (Not Found)
     */
    @GetMapping("/cart-elements/{id}")
    @Timed
    public ResponseEntity<CartElement> getCartElement(@PathVariable String id) {
        log.debug("REST request to get CartElement : {}", id);
        CartElement cartElement = cartElementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cartElement));
    }

    /**
     * DELETE  /cart-elements/:id : delete the "id" cartElement.
     *
     * @param id the id of the cartElement to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cart-elements/{id}")
    @Timed
    public ResponseEntity<Void> deleteCartElement(@PathVariable String id) {
        log.debug("REST request to delete CartElement : {}", id);
        cartElementService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
