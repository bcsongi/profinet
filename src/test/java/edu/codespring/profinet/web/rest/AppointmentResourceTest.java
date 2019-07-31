package edu.codespring.profinet.web.rest;

import edu.codespring.profinet.Application;
import edu.codespring.profinet.domain.Appointment;
import edu.codespring.profinet.repository.AppointmentRepository;
import edu.codespring.profinet.service.AppointmentService;
import edu.codespring.profinet.web.rest.dto.AppointmentDTO;
import edu.codespring.profinet.web.rest.mapper.AppointmentMapper;

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
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import edu.codespring.profinet.domain.enumeration.AppointmentStatus;

/**
 * Test class for the AppointmentResource REST controller.
 *
 * @see AppointmentResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AppointmentResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");


    private static final DateTime DEFAULT_STARTING_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_STARTING_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_STARTING_DATE_STR = dateTimeFormatter.print(DEFAULT_STARTING_DATE);

    private static final DateTime DEFAULT_ENDING_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_ENDING_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_ENDING_DATE_STR = dateTimeFormatter.print(DEFAULT_ENDING_DATE);
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    private static final AppointmentStatus DEFAULT_STATUS = AppointmentStatus.CREATED;
    private static final AppointmentStatus UPDATED_STATUS = AppointmentStatus.ACCEPTED;

    @Inject
    private AppointmentRepository appointmentRepository;

    @Inject
    private AppointmentService appointmentService;
    
    @Inject
    private AppointmentMapper appointmentMapper;
    
    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restAppointmentMockMvc;

    private Appointment appointment;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AppointmentResource appointmentResource = new AppointmentResource();
        ReflectionTestUtils.setField(appointmentResource, "appointmentService", appointmentService);
        ReflectionTestUtils.setField(appointmentResource, "appointmentMapper", appointmentMapper);
        this.restAppointmentMockMvc = MockMvcBuilders.standaloneSetup(appointmentResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        appointment = new Appointment();
        appointment.setStartingDate(DEFAULT_STARTING_DATE);
        appointment.setEndingDate(DEFAULT_ENDING_DATE);
        appointment.setDescription(DEFAULT_DESCRIPTION);
        appointment.setStatus(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createAppointment() throws Exception {
        int databaseSizeBeforeCreate = appointmentRepository.findAll().size();

        // Create the Appointment
        AppointmentDTO appointmentDTO = appointmentMapper.appointmentToAppointmentDTO(appointment);

        restAppointmentMockMvc.perform(post("/api/appointments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(appointmentDTO)))
                .andExpect(status().isCreated());

        // Validate the Appointment in the database
        List<Appointment> appointments = appointmentRepository.findAll();
        assertThat(appointments).hasSize(databaseSizeBeforeCreate + 1);
        Appointment testAppointment = appointments.get(appointments.size() - 1);
        assertThat(testAppointment.getStartingDate().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_STARTING_DATE);
        assertThat(testAppointment.getEndingDate().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_ENDING_DATE);
        assertThat(testAppointment.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAppointment.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = appointmentRepository.findAll().size();
        // set the field null
        appointment.setStatus(null);

        // Create the Appointment, which fails.
        AppointmentDTO appointmentDTO = appointmentMapper.appointmentToAppointmentDTO(appointment);
        
        restAppointmentMockMvc.perform(post("/api/appointments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(appointmentDTO)))
                .andExpect(status().isBadRequest());

        List<Appointment> appointments = appointmentRepository.findAll();
        assertThat(appointments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAppointments() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointments
        restAppointmentMockMvc.perform(get("/api/appointments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(appointment.getId().intValue())))
                .andExpect(jsonPath("$.[*].startingDate").value(hasItem(DEFAULT_STARTING_DATE_STR)))
                .andExpect(jsonPath("$.[*].endingDate").value(hasItem(DEFAULT_ENDING_DATE_STR)))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getAppointment() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get the appointment
        restAppointmentMockMvc.perform(get("/api/appointments/{id}", appointment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(appointment.getId().intValue()))
            .andExpect(jsonPath("$.startingDate").value(DEFAULT_STARTING_DATE_STR))
            .andExpect(jsonPath("$.endingDate").value(DEFAULT_ENDING_DATE_STR))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAppointment() throws Exception {
        // Get the appointment
        restAppointmentMockMvc.perform(get("/api/appointments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAppointment() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

		int databaseSizeBeforeUpdate = appointmentRepository.findAll().size();

        // Update the appointment
        appointment.setStartingDate(UPDATED_STARTING_DATE);
        appointment.setEndingDate(UPDATED_ENDING_DATE);
        appointment.setDescription(UPDATED_DESCRIPTION);
        appointment.setStatus(UPDATED_STATUS);
        
        AppointmentDTO appointmentDTO = appointmentMapper.appointmentToAppointmentDTO(appointment);

        restAppointmentMockMvc.perform(put("/api/appointments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(appointmentDTO)))
                .andExpect(status().isOk());

        // Validate the Appointment in the database
        List<Appointment> appointments = appointmentRepository.findAll();
        assertThat(appointments).hasSize(databaseSizeBeforeUpdate);
        Appointment testAppointment = appointments.get(appointments.size() - 1);
        assertThat(testAppointment.getStartingDate().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_STARTING_DATE);
        assertThat(testAppointment.getEndingDate().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_ENDING_DATE);
        assertThat(testAppointment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAppointment.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void deleteAppointment() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

		int databaseSizeBeforeDelete = appointmentRepository.findAll().size();

        // Get the appointment
        restAppointmentMockMvc.perform(delete("/api/appointments/{id}", appointment.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Appointment> appointments = appointmentRepository.findAll();
        assertThat(appointments).hasSize(databaseSizeBeforeDelete - 1);
    }
}
