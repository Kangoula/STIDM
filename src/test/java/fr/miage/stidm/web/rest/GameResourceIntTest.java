package fr.miage.stidm.web.rest;

import fr.miage.stidm.StidmApp;

import fr.miage.stidm.domain.Game;
import fr.miage.stidm.repository.GameRepository;
import fr.miage.stidm.service.GameService;

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
import org.springframework.util.Base64Utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the GameResource REST controller.
 *
 * @see GameResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StidmApp.class)
public class GameResourceIntTest {

    private static final String DEFAULT_REF = "AAAAAAAAAA";
    private static final String UPDATED_REF = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_RELEASE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RELEASE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_COVER = "AAAAAAAAAA";
    private static final String UPDATED_COVER = "BBBBBBBBBB";

    private static final String DEFAULT_VIDEO = "AAAAAAAAAA";
    private static final String UPDATED_VIDEO = "BBBBBBBBBB";

    private static final String DEFAULT_KEY_REF = "AAAAAAAAAA";
    private static final String UPDATED_KEY_REF = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE = 0D;
    private static final Double UPDATED_PRICE = 1D;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameService gameService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restGameMockMvc;

    private Game game;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GameResource gameResource = new GameResource(gameService);
        this.restGameMockMvc = MockMvcBuilders.standaloneSetup(gameResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Game createEntity() {
        Game game = new Game()
                .ref(DEFAULT_REF)
                .name(DEFAULT_NAME)
                .type(DEFAULT_TYPE)
                .release(DEFAULT_RELEASE)
                .description(DEFAULT_DESCRIPTION)
                .cover(DEFAULT_COVER)
                .video(DEFAULT_VIDEO)
                .keyRef(DEFAULT_KEY_REF)
                .price(DEFAULT_PRICE);
        return game;
    }

    @Before
    public void initTest() {
        gameRepository.deleteAll();
        game = createEntity();
    }

    @Test
    public void createGame() throws Exception {
        int databaseSizeBeforeCreate = gameRepository.findAll().size();

        // Create the Game

        restGameMockMvc.perform(post("/api/games")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(game)))
            .andExpect(status().isCreated());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeCreate + 1);
        Game testGame = gameList.get(gameList.size() - 1);
        assertThat(testGame.getRef()).isEqualTo(DEFAULT_REF);
        assertThat(testGame.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGame.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testGame.getRelease()).isEqualTo(DEFAULT_RELEASE);
        assertThat(testGame.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testGame.getCover()).isEqualTo(DEFAULT_COVER);
        assertThat(testGame.getVideo()).isEqualTo(DEFAULT_VIDEO);
        assertThat(testGame.getKeyRef()).isEqualTo(DEFAULT_KEY_REF);
        assertThat(testGame.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    public void createGameWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = gameRepository.findAll().size();

        // Create the Game with an existing ID
        Game existingGame = new Game();
        existingGame.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restGameMockMvc.perform(post("/api/games")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingGame)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllGames() throws Exception {
        // Initialize the database
        gameRepository.save(game);

        // Get all the gameList
        restGameMockMvc.perform(get("/api/games?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(game.getId())))
            .andExpect(jsonPath("$.[*].ref").value(hasItem(DEFAULT_REF.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].release").value(hasItem(DEFAULT_RELEASE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].cover").value(hasItem(DEFAULT_COVER.toString())))
            .andExpect(jsonPath("$.[*].video").value(hasItem(DEFAULT_VIDEO.toString())))
            .andExpect(jsonPath("$.[*].keyRef").value(hasItem(DEFAULT_KEY_REF.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }

    @Test
    public void getGame() throws Exception {
        // Initialize the database
        gameRepository.save(game);

        // Get the game
        restGameMockMvc.perform(get("/api/games/{id}", game.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(game.getId()))
            .andExpect(jsonPath("$.ref").value(DEFAULT_REF.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.release").value(DEFAULT_RELEASE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.cover").value(DEFAULT_COVER.toString()))
            .andExpect(jsonPath("$.video").value(DEFAULT_VIDEO.toString()))
            .andExpect(jsonPath("$.keyRef").value(DEFAULT_KEY_REF.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()));
    }

    @Test
    public void getNonExistingGame() throws Exception {
        // Get the game
        restGameMockMvc.perform(get("/api/games/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateGame() throws Exception {
        // Initialize the database
        gameService.save(game);

        int databaseSizeBeforeUpdate = gameRepository.findAll().size();

        // Update the game
        Game updatedGame = gameRepository.findOne(game.getId());
        updatedGame
                .ref(UPDATED_REF)
                .name(UPDATED_NAME)
                .type(UPDATED_TYPE)
                .release(UPDATED_RELEASE)
                .description(UPDATED_DESCRIPTION)
                .cover(UPDATED_COVER)
                .video(UPDATED_VIDEO)
                .keyRef(UPDATED_KEY_REF)
                .price(UPDATED_PRICE);

        restGameMockMvc.perform(put("/api/games")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGame)))
            .andExpect(status().isOk());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeUpdate);
        Game testGame = gameList.get(gameList.size() - 1);
        assertThat(testGame.getRef()).isEqualTo(UPDATED_REF);
        assertThat(testGame.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGame.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testGame.getRelease()).isEqualTo(UPDATED_RELEASE);
        assertThat(testGame.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testGame.getCover()).isEqualTo(UPDATED_COVER);
        assertThat(testGame.getVideo()).isEqualTo(UPDATED_VIDEO);
        assertThat(testGame.getKeyRef()).isEqualTo(UPDATED_KEY_REF);
        assertThat(testGame.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    public void updateNonExistingGame() throws Exception {
        int databaseSizeBeforeUpdate = gameRepository.findAll().size();

        // Create the Game

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restGameMockMvc.perform(put("/api/games")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(game)))
            .andExpect(status().isCreated());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteGame() throws Exception {
        // Initialize the database
        gameService.save(game);

        int databaseSizeBeforeDelete = gameRepository.findAll().size();

        // Get the game
        restGameMockMvc.perform(delete("/api/games/{id}", game.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Game.class);
    }
}
