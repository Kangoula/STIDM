package fr.miage.stidm.web.rest;

import fr.miage.stidm.StidmApp;

import fr.miage.stidm.domain.CartElement;
import fr.miage.stidm.repository.CartElementRepository;
import fr.miage.stidm.service.CartElementService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CartElementResource REST controller.
 *
 * @see CartElementResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StidmApp.class)
public class CartElementResourceIntTest {

    private static final String DEFAULT_ID_GAME = "AAAAAAAAAA";
    private static final String UPDATED_ID_GAME = "BBBBBBBBBB";

    private static final String DEFAULT_REF_GAME = "AAAAAAAAAA";
    private static final String UPDATED_REF_GAME = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_GAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME_GAME = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE = 0D;
    private static final Double UPDATED_PRICE = 1D;

    private static final String DEFAULT_ID_USER = "AAAAAAAAAA";
    private static final String UPDATED_ID_USER = "BBBBBBBBBB";

    @Autowired
    private CartElementRepository cartElementRepository;

    @Autowired
    private CartElementService cartElementService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCartElementMockMvc;

    private CartElement cartElement;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CartElementResource cartElementResource = new CartElementResource(cartElementService);
        this.restCartElementMockMvc = MockMvcBuilders.standaloneSetup(cartElementResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CartElement createEntity() {
        CartElement cartElement = new CartElement()
                .idGame(DEFAULT_ID_GAME)
                .refGame(DEFAULT_REF_GAME)
                .nameGame(DEFAULT_NAME_GAME)
                .price(DEFAULT_PRICE)
                .idUser(DEFAULT_ID_USER);
        return cartElement;
    }

    @Before
    public void initTest() {
        cartElementRepository.deleteAll();
        cartElement = createEntity();
    }

    @Test
    public void createCartElement() throws Exception {
        int databaseSizeBeforeCreate = cartElementRepository.findAll().size();

        // Create the CartElement

        restCartElementMockMvc.perform(post("/api/cart-elements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartElement)))
            .andExpect(status().isCreated());

        // Validate the CartElement in the database
        List<CartElement> cartElementList = cartElementRepository.findAll();
        assertThat(cartElementList).hasSize(databaseSizeBeforeCreate + 1);
        CartElement testCartElement = cartElementList.get(cartElementList.size() - 1);
        assertThat(testCartElement.getIdGame()).isEqualTo(DEFAULT_ID_GAME);
        assertThat(testCartElement.getRefGame()).isEqualTo(DEFAULT_REF_GAME);
        assertThat(testCartElement.getNameGame()).isEqualTo(DEFAULT_NAME_GAME);
        assertThat(testCartElement.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testCartElement.getIdUser()).isEqualTo(DEFAULT_ID_USER);
    }

    @Test
    public void createCartElementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cartElementRepository.findAll().size();

        // Create the CartElement with an existing ID
        CartElement existingCartElement = new CartElement();
        existingCartElement.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restCartElementMockMvc.perform(post("/api/cart-elements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingCartElement)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<CartElement> cartElementList = cartElementRepository.findAll();
        assertThat(cartElementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllCartElements() throws Exception {
        // Initialize the database
        cartElementRepository.save(cartElement);

        // Get all the cartElementList
        restCartElementMockMvc.perform(get("/api/cart-elements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cartElement.getId())))
            .andExpect(jsonPath("$.[*].idGame").value(hasItem(DEFAULT_ID_GAME.toString())))
            .andExpect(jsonPath("$.[*].refGame").value(hasItem(DEFAULT_REF_GAME.toString())))
            .andExpect(jsonPath("$.[*].nameGame").value(hasItem(DEFAULT_NAME_GAME.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].idUser").value(hasItem(DEFAULT_ID_USER.toString())));
    }

    @Test
    public void getCartElement() throws Exception {
        // Initialize the database
        cartElementRepository.save(cartElement);

        // Get the cartElement
        restCartElementMockMvc.perform(get("/api/cart-elements/{id}", cartElement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cartElement.getId()))
            .andExpect(jsonPath("$.idGame").value(DEFAULT_ID_GAME.toString()))
            .andExpect(jsonPath("$.refGame").value(DEFAULT_REF_GAME.toString()))
            .andExpect(jsonPath("$.nameGame").value(DEFAULT_NAME_GAME.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.idUser").value(DEFAULT_ID_USER.toString()));
    }

    @Test
    public void getNonExistingCartElement() throws Exception {
        // Get the cartElement
        restCartElementMockMvc.perform(get("/api/cart-elements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateCartElement() throws Exception {
        // Initialize the database
        cartElementService.save(cartElement);

        int databaseSizeBeforeUpdate = cartElementRepository.findAll().size();

        // Update the cartElement
        CartElement updatedCartElement = cartElementRepository.findOne(cartElement.getId());
        updatedCartElement
                .idGame(UPDATED_ID_GAME)
                .refGame(UPDATED_REF_GAME)
                .nameGame(UPDATED_NAME_GAME)
                .price(UPDATED_PRICE)
                .idUser(UPDATED_ID_USER);

        restCartElementMockMvc.perform(put("/api/cart-elements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCartElement)))
            .andExpect(status().isOk());

        // Validate the CartElement in the database
        List<CartElement> cartElementList = cartElementRepository.findAll();
        assertThat(cartElementList).hasSize(databaseSizeBeforeUpdate);
        CartElement testCartElement = cartElementList.get(cartElementList.size() - 1);
        assertThat(testCartElement.getIdGame()).isEqualTo(UPDATED_ID_GAME);
        assertThat(testCartElement.getRefGame()).isEqualTo(UPDATED_REF_GAME);
        assertThat(testCartElement.getNameGame()).isEqualTo(UPDATED_NAME_GAME);
        assertThat(testCartElement.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testCartElement.getIdUser()).isEqualTo(UPDATED_ID_USER);
    }

    @Test
    public void updateNonExistingCartElement() throws Exception {
        int databaseSizeBeforeUpdate = cartElementRepository.findAll().size();

        // Create the CartElement

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCartElementMockMvc.perform(put("/api/cart-elements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartElement)))
            .andExpect(status().isCreated());

        // Validate the CartElement in the database
        List<CartElement> cartElementList = cartElementRepository.findAll();
        assertThat(cartElementList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteCartElement() throws Exception {
        // Initialize the database
        cartElementService.save(cartElement);

        int databaseSizeBeforeDelete = cartElementRepository.findAll().size();

        // Get the cartElement
        restCartElementMockMvc.perform(delete("/api/cart-elements/{id}", cartElement.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CartElement> cartElementList = cartElementRepository.findAll();
        assertThat(cartElementList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CartElement.class);
    }
}
