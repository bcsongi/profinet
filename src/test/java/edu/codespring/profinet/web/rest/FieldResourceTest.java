package edu.codespring.profinet.web.rest;

import edu.codespring.profinet.Application;
import edu.codespring.profinet.domain.Field;
import edu.codespring.profinet.repository.FieldRepository;
import edu.codespring.profinet.service.FieldService;
import edu.codespring.profinet.web.rest.dto.FieldDTO;
import edu.codespring.profinet.web.rest.mapper.FieldMapper;

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
 * Test class for the FieldResource REST controller.
 *
 * @see FieldResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class FieldResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";

    @Inject
    private FieldRepository fieldRepository;

    @Inject
    private FieldMapper fieldMapper;

    @Inject
    private FieldService fieldService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restFieldMockMvc;

    private Field field;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FieldResource fieldResource = new FieldResource();
        ReflectionTestUtils.setField(fieldResource, "fieldMapper", fieldMapper);
        ReflectionTestUtils.setField(fieldResource, "fieldService", fieldService);
        this.restFieldMockMvc = MockMvcBuilders.standaloneSetup(fieldResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        field = new Field();
        field.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createField() throws Exception {
        int databaseSizeBeforeCreate = fieldRepository.findAll().size();

        // Create the Field
        FieldDTO fieldDTO = fieldMapper.fieldToFieldDTO(field);

        restFieldMockMvc.perform(post("/api/fields")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fieldDTO)))
                .andExpect(status().isCreated());

        // Validate the Field in the database
        List<Field> fields = fieldRepository.findAll();
        assertThat(fields).hasSize(databaseSizeBeforeCreate + 1);
        Field testField = fields.get(fields.size() - 1);
        assertThat(testField.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = fieldRepository.findAll().size();
        // set the field null
        field.setName(null);

        // Create the Field, which fails.
        FieldDTO fieldDTO = fieldMapper.fieldToFieldDTO(field);

        restFieldMockMvc.perform(post("/api/fields")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fieldDTO)))
                .andExpect(status().isBadRequest());

        List<Field> fields = fieldRepository.findAll();
        assertThat(fields).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFields() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        // Get all the fields
        restFieldMockMvc.perform(get("/api/fields"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(field.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getField() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        // Get the field
        restFieldMockMvc.perform(get("/api/fields/{id}", field.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(field.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingField() throws Exception {
        // Get the field
        restFieldMockMvc.perform(get("/api/fields/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateField() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

		int databaseSizeBeforeUpdate = fieldRepository.findAll().size();

        // Update the field
        field.setName(UPDATED_NAME);
        
        FieldDTO fieldDTO = fieldMapper.fieldToFieldDTO(field);

        restFieldMockMvc.perform(put("/api/fields")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fieldDTO)))
                .andExpect(status().isOk());

        // Validate the Field in the database
        List<Field> fields = fieldRepository.findAll();
        assertThat(fields).hasSize(databaseSizeBeforeUpdate);
        Field testField = fields.get(fields.size() - 1);
        assertThat(testField.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteField() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

		int databaseSizeBeforeDelete = fieldRepository.findAll().size();

        // Get the field
        restFieldMockMvc.perform(delete("/api/fields/{id}", field.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Field> fields = fieldRepository.findAll();
        assertThat(fields).hasSize(databaseSizeBeforeDelete - 1);
    }
}
