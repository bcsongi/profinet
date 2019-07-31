package edu.codespring.profinet.web.rest;

import edu.codespring.profinet.Application;
import edu.codespring.profinet.domain.Language;
import edu.codespring.profinet.repository.LanguageRepository;
import edu.codespring.profinet.service.LanguageService;
import edu.codespring.profinet.web.rest.dto.LanguageDTO;
import edu.codespring.profinet.web.rest.mapper.LanguageMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the LanguageResource REST controller.
 *
 * @see LanguageResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class LanguageResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";

    @Inject
    private LanguageRepository languageRepository;

    @Inject
    private LanguageMapper languageMapper;

    @Inject
    private LanguageService languageService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restLanguageMockMvc;

    private Language language;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LanguageResource languageResource = new LanguageResource();
        ReflectionTestUtils.setField(languageResource, "languageMapper", languageMapper);
        ReflectionTestUtils.setField(languageResource, "languageService", languageService);
        this.restLanguageMockMvc = MockMvcBuilders.standaloneSetup(languageResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        language = new Language();
        language.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createLanguage() throws Exception {
        int databaseSizeBeforeCreate = languageRepository.findAll().size();

        // Create the Language
        LanguageDTO languageDTO = languageMapper.languageToLanguageDTO(language);

        restLanguageMockMvc.perform(post("/api/languages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(languageDTO)))
                .andExpect(status().isCreated());

        // Validate the Language in the database
        List<Language> languages = languageRepository.findAll();
        assertThat(languages).hasSize(databaseSizeBeforeCreate + 1);
        Language testLanguage = languages.get(languages.size() - 1);
        assertThat(testLanguage.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = languageRepository.findAll().size();
        // set the field null
        language.setName(null);

        // Create the Language, which fails.
        LanguageDTO languageDTO = languageMapper.languageToLanguageDTO(language);

        restLanguageMockMvc.perform(post("/api/languages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(languageDTO)))
                .andExpect(status().isBadRequest());

        List<Language> languages = languageRepository.findAll();
        assertThat(languages).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLanguages() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languages
        restLanguageMockMvc.perform(get("/api/languages"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(language.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getLanguage() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get the language
        restLanguageMockMvc.perform(get("/api/languages/{id}", language.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(language.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLanguage() throws Exception {
        // Get the language
        restLanguageMockMvc.perform(get("/api/languages/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLanguage() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

		int databaseSizeBeforeUpdate = languageRepository.findAll().size();

        // Update the language
        language.setName(UPDATED_NAME);
        
        LanguageDTO languageDTO = languageMapper.languageToLanguageDTO(language);

        restLanguageMockMvc.perform(put("/api/languages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(languageDTO)))
                .andExpect(status().isOk());

        // Validate the Language in the database
        List<Language> languages = languageRepository.findAll();
        assertThat(languages).hasSize(databaseSizeBeforeUpdate);
        Language testLanguage = languages.get(languages.size() - 1);
        assertThat(testLanguage.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteLanguage() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

		int databaseSizeBeforeDelete = languageRepository.findAll().size();

        // Get the language
        restLanguageMockMvc.perform(delete("/api/languages/{id}", language.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Language> languages = languageRepository.findAll();
        assertThat(languages).hasSize(databaseSizeBeforeDelete - 1);
    }
}
