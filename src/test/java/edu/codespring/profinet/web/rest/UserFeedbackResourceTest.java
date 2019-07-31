package edu.codespring.profinet.web.rest;

import edu.codespring.profinet.Application;
import edu.codespring.profinet.domain.UserFeedback;
import edu.codespring.profinet.repository.UserFeedbackRepository;
import edu.codespring.profinet.service.UserFeedbackService;
import edu.codespring.profinet.web.rest.dto.UserFeedbackDTO;
import edu.codespring.profinet.web.rest.mapper.UserFeedbackMapper;

import org.junit.Before;
import org.junit.Ignore;
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
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the UserFeedbackResource REST controller.
 *
 * @see UserFeedbackResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@Ignore
public class UserFeedbackResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_COMMENT = "SAMPLE_TEXT";
    private static final String UPDATED_COMMENT = "UPDATED_TEXT";

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;

    private static final DateTime DEFAULT_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_DATE_STR = dateTimeFormatter.print(DEFAULT_DATE);

    @Inject
    private UserFeedbackRepository userFeedbackRepository;

    @Inject
    private UserFeedbackService userFeedbackService;

    @Inject
    private UserFeedbackMapper userFeedbackMapper;
    
    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restUserFeedbackMockMvc;

    private UserFeedback userFeedback;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserFeedbackResource userFeedbackResource = new UserFeedbackResource();
        ReflectionTestUtils.setField(userFeedbackResource, "userFeedbackService", userFeedbackService);
        ReflectionTestUtils.setField(userFeedbackResource, "userFeedbackMapper", userFeedbackMapper);
        this.restUserFeedbackMockMvc = MockMvcBuilders.standaloneSetup(userFeedbackResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        userFeedback = new UserFeedback();
        userFeedback.setComment(DEFAULT_COMMENT);
        userFeedback.setRating(DEFAULT_RATING);
        userFeedback.setDate(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createUserFeedback() throws Exception {
        int databaseSizeBeforeCreate = userFeedbackRepository.findAll().size();

        // Create the UserFeedback
        UserFeedbackDTO userFeedbackDTO = userFeedbackMapper.userFeedbackToUserFeedbackDTO(userFeedback);
        
        restUserFeedbackMockMvc.perform(post("/api/userFeedbacks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userFeedbackDTO)))
                .andExpect(status().isCreated());

        // Validate the UserFeedback in the database
        List<UserFeedback> userFeedbacks = userFeedbackRepository.findAll();
        assertThat(userFeedbacks).hasSize(databaseSizeBeforeCreate + 1);
        UserFeedback testUserFeedback = userFeedbacks.get(userFeedbacks.size() - 1);
        assertThat(testUserFeedback.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testUserFeedback.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testUserFeedback.getDate().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void getAllUserFeedbacks() throws Exception {
        // Initialize the database
        userFeedbackRepository.saveAndFlush(userFeedback);

        // Get all the userFeedbacks
        restUserFeedbackMockMvc.perform(get("/api/userFeedbacks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(userFeedback.getId().intValue())))
                .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
                .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE_STR)));
    }

    @Test
    @Transactional
    public void getUserFeedback() throws Exception {
        // Initialize the database
        userFeedbackRepository.saveAndFlush(userFeedback);

        // Get the userFeedback
        restUserFeedbackMockMvc.perform(get("/api/userFeedbacks/{id}", userFeedback.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(userFeedback.getId().intValue()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingUserFeedback() throws Exception {
        // Get the userFeedback
        restUserFeedbackMockMvc.perform(get("/api/userFeedbacks/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserFeedback() throws Exception {
        // Initialize the database
        userFeedbackRepository.saveAndFlush(userFeedback);

		int databaseSizeBeforeUpdate = userFeedbackRepository.findAll().size();

        // Update the userFeedback
        userFeedback.setComment(UPDATED_COMMENT);
        userFeedback.setRating(UPDATED_RATING);
        userFeedback.setDate(UPDATED_DATE);
        
        UserFeedbackDTO userFeedbackDTO = userFeedbackMapper.userFeedbackToUserFeedbackDTO(userFeedback);

        restUserFeedbackMockMvc.perform(put("/api/userFeedbacks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userFeedbackDTO)))
                .andExpect(status().isOk());

        // Validate the UserFeedback in the database
        List<UserFeedback> userFeedbacks = userFeedbackRepository.findAll();
        assertThat(userFeedbacks).hasSize(databaseSizeBeforeUpdate);
        UserFeedback testUserFeedback = userFeedbacks.get(userFeedbacks.size() - 1);
        assertThat(testUserFeedback.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testUserFeedback.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testUserFeedback.getDate().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void deleteUserFeedback() throws Exception {
        // Initialize the database
        userFeedbackRepository.saveAndFlush(userFeedback);

		int databaseSizeBeforeDelete = userFeedbackRepository.findAll().size();

        // Get the userFeedback
        restUserFeedbackMockMvc.perform(delete("/api/userFeedbacks/{id}", userFeedback.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<UserFeedback> userFeedbacks = userFeedbackRepository.findAll();
        assertThat(userFeedbacks).hasSize(databaseSizeBeforeDelete - 1);
    }
}
