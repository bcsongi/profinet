package edu.codespring.profinet.web.rest;

import edu.codespring.profinet.Application;
import edu.codespring.profinet.domain.Expert;
import edu.codespring.profinet.domain.User;
import edu.codespring.profinet.repository.ExpertRepository;
import edu.codespring.profinet.repository.UserRepository;
import edu.codespring.profinet.service.ExpertService;
import edu.codespring.profinet.service.UserService;
import edu.codespring.profinet.service.util.RandomUtil;
import edu.codespring.profinet.web.rest.dto.ExpertDTO;
import edu.codespring.profinet.web.rest.mapper.ExpertMapper;

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
 * Test class for the ExpertResource REST controller.
 *
 * @see ExpertResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@Ignore
public class ExpertResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_TIMETABLE = "SAMPLE_TEXT";
    private static final String UPDATED_TIMETABLE = "UPDATED_TEXT";
    private static final String DEFAULT_PHONE_NUMBER = "SAMPLE_TEXT";
    private static final String UPDATED_PHONE_NUMBER = "UPDATED_TEXT";

    private static final DateTime DEFAULT_LAST_ACTIVE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_LAST_ACTIVE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_LAST_ACTIVE_STR = dateTimeFormatter.print(DEFAULT_LAST_ACTIVE);

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;

    @Inject
    private ExpertRepository expertRepository;
    
    @Inject
    private ExpertService expertService;

    @Inject
    private ExpertMapper expertMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restExpertMockMvc;

    private Expert expert;

    
    @Inject
    private UserRepository userRepository;

    @Inject
    private UserService userService;

    User user;
    
    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ExpertResource expertResource = new ExpertResource();
        ReflectionTestUtils.setField(expertResource, "expertRepository", expertRepository);
        ReflectionTestUtils.setField(expertResource, "expertMapper", expertMapper);
        ReflectionTestUtils.setField(expertResource, "expertService", expertService);
        this.restExpertMockMvc = MockMvcBuilders.standaloneSetup(expertResource).setMessageConverters(jacksonMessageConverter).build();
        
		user = userService.createUserInformation("johndoe123", "johndoe123", "John123", "Doe132", "johnd.doe@localhost", "en-US");

        DateTime daysAgo = DateTime.now().minusHours(25);
        String resetKey = RandomUtil.generateResetKey();
        user.setActivated(true);
        user.setResetDate(daysAgo);
        user.setResetKey(resetKey);

        user = userRepository.save(user);
    }

    @Before
    public void initTest() {
   

    	
        expert = new Expert();
        expert.setExpertuser(user);
        expert.setTimetable(DEFAULT_TIMETABLE);
        expert.setPhoneNumber(DEFAULT_PHONE_NUMBER);
        expert.setLastActive(DEFAULT_LAST_ACTIVE);
        expert.setLatitude(DEFAULT_LATITUDE);
        expert.setLongitude(DEFAULT_LONGITUDE);
    }

    @Test
    @Transactional
    public void createExpert() throws Exception {
        int databaseSizeBeforeCreate = expertRepository.findAll().size();

        // Create the Expert
        ExpertDTO expertDTO = expertMapper.expertToExpertDTO(expert);

        restExpertMockMvc.perform(post("/api/experts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(expertDTO)))
                .andExpect(status().isCreated());

        // Validate the Expert in the database
        List<Expert> experts = expertRepository.findAll();
        assertThat(experts).hasSize(databaseSizeBeforeCreate + 1);
        Expert testExpert = experts.get(experts.size() - 1);
        assertThat(testExpert.getTimetable()).isEqualTo(DEFAULT_TIMETABLE);
        assertThat(testExpert.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testExpert.getLastActive().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_LAST_ACTIVE);
        assertThat(testExpert.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testExpert.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
    }

    @Test
    @Transactional
    public void checkTimetableIsRequired() throws Exception {
        int databaseSizeBeforeTest = expertRepository.findAll().size();
        // set the field null
        expert.setTimetable(null);

        // Create the Expert, which fails.
        ExpertDTO expertDTO = expertMapper.expertToExpertDTO(expert);

        restExpertMockMvc.perform(post("/api/experts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(expertDTO)))
                .andExpect(status().isBadRequest());

        List<Expert> experts = expertRepository.findAll();
        assertThat(experts).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = expertRepository.findAll().size();
        // set the field null
        expert.setPhoneNumber(null);

        // Create the Expert, which fails.
        ExpertDTO expertDTO = expertMapper.expertToExpertDTO(expert);

        restExpertMockMvc.perform(post("/api/experts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(expertDTO)))
                .andExpect(status().isBadRequest());

        List<Expert> experts = expertRepository.findAll();
        assertThat(experts).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLatitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = expertRepository.findAll().size();
        // set the field null
        expert.setLatitude(null);

        // Create the Expert, which fails.
        ExpertDTO expertDTO = expertMapper.expertToExpertDTO(expert);

        restExpertMockMvc.perform(post("/api/experts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(expertDTO)))
                .andExpect(status().isBadRequest());

        List<Expert> experts = expertRepository.findAll();
        assertThat(experts).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLongitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = expertRepository.findAll().size();
        // set the field null
        expert.setLongitude(null);

        // Create the Expert, which fails.
        ExpertDTO expertDTO = expertMapper.expertToExpertDTO(expert);

        restExpertMockMvc.perform(post("/api/experts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(expertDTO)))
                .andExpect(status().isBadRequest());

        List<Expert> experts = expertRepository.findAll();
        assertThat(experts).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExperts() throws Exception {
        // Initialize the database
        expertRepository.saveAndFlush(expert);

        // Get all the experts
        restExpertMockMvc.perform(get("/api/experts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(expert.getId().intValue())))
                .andExpect(jsonPath("$.[*].timetable").value(hasItem(DEFAULT_TIMETABLE.toString())))
                .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].lastActive").value(hasItem(DEFAULT_LAST_ACTIVE_STR)))
                .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
                .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())));
    }

    @Test
    @Transactional
    public void getExpert() throws Exception {
        // Initialize the database
        expertRepository.saveAndFlush(expert);

        // Get the expert
        restExpertMockMvc.perform(get("/api/experts/{id}", expert.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(expert.getId().intValue()))
            .andExpect(jsonPath("$.timetable").value(DEFAULT_TIMETABLE.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.toString()))
            .andExpect(jsonPath("$.lastActive").value(DEFAULT_LAST_ACTIVE_STR))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingExpert() throws Exception {
        // Get the expert
        restExpertMockMvc.perform(get("/api/experts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExpert() throws Exception {
        // Initialize the database
        expertRepository.saveAndFlush(expert);

		int databaseSizeBeforeUpdate = expertRepository.findAll().size();

        // Update the expert
        expert.setTimetable(UPDATED_TIMETABLE);
        expert.setPhoneNumber(UPDATED_PHONE_NUMBER);
        expert.setLastActive(UPDATED_LAST_ACTIVE);
        expert.setLatitude(UPDATED_LATITUDE);
        expert.setLongitude(UPDATED_LONGITUDE);
        
        ExpertDTO expertDTO = expertMapper.expertToExpertDTO(expert);

        restExpertMockMvc.perform(put("/api/experts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(expertDTO)))
                .andExpect(status().isOk());

        // Validate the Expert in the database
        List<Expert> experts = expertRepository.findAll();
        assertThat(experts).hasSize(databaseSizeBeforeUpdate);
        Expert testExpert = experts.get(experts.size() - 1);
        assertThat(testExpert.getTimetable()).isEqualTo(UPDATED_TIMETABLE);
        assertThat(testExpert.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testExpert.getLastActive().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_LAST_ACTIVE);
        assertThat(testExpert.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testExpert.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void deleteExpert() throws Exception {
        // Initialize the database
        expertRepository.saveAndFlush(expert);

		int databaseSizeBeforeDelete = expertRepository.findAll().size();

        // Get the expert
        restExpertMockMvc.perform(delete("/api/experts/{id}", expert.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Expert> experts = expertRepository.findAll();
        assertThat(experts).hasSize(databaseSizeBeforeDelete - 1);
    }
}
