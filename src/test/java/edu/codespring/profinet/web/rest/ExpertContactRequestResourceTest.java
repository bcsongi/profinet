package edu.codespring.profinet.web.rest;

import edu.codespring.profinet.Application;
import edu.codespring.profinet.domain.ExpertContactRequest;
import edu.codespring.profinet.repository.ExpertContactRequestRepository;
//import edu.codespring.profinet.repository.search.ExpertContactRequestSearchRepository;
import edu.codespring.profinet.service.ExpertContactRequestService;
import edu.codespring.profinet.web.rest.dto.ExpertContactRequestDTO;
import edu.codespring.profinet.web.rest.mapper.ExpertContactRequestMapper;

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
 * Test class for the ExpertContactRequestResource REST controller.
 *
 * @see ExpertContactRequestResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ExpertContactRequestResourceTest {


    private static final Boolean DEFAULT_APPROVED = false;
    private static final Boolean UPDATED_APPROVED = true;

    @Inject
    private ExpertContactRequestService expertContactRequestService;
    
    @Inject
    private ExpertContactRequestRepository expertContactRequestRepository;

    @Inject
    private ExpertContactRequestMapper expertContactRequestMapper;
    
    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restExpertContactRequestMockMvc;

    private ExpertContactRequest expertContactRequest;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ExpertContactRequestResource expertContactRequestResource = new ExpertContactRequestResource();
        ReflectionTestUtils.setField(expertContactRequestResource, "expertContactRequestService", expertContactRequestService);
        ReflectionTestUtils.setField(expertContactRequestResource, "expertContactRequestMapper", expertContactRequestMapper);
        this.restExpertContactRequestMockMvc = MockMvcBuilders.standaloneSetup(expertContactRequestResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        expertContactRequest = new ExpertContactRequest();
        expertContactRequest.setApproved(DEFAULT_APPROVED);
    }

    @Test
    @Transactional
    public void createExpertContactRequest() throws Exception {
        int databaseSizeBeforeCreate = expertContactRequestRepository.findAll().size();

        // Create the ExpertContactRequest
        ExpertContactRequestDTO expertContactRequestDTO = expertContactRequestMapper.expertContactRequestToExpertContactRequestDTO(expertContactRequest);

        restExpertContactRequestMockMvc.perform(post("/api/expertContactRequests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(expertContactRequestDTO)))
                .andExpect(status().isCreated());

        // Validate the ExpertContactRequest in the database
        List<ExpertContactRequest> expertContactRequests = expertContactRequestRepository.findAll();
        assertThat(expertContactRequests).hasSize(databaseSizeBeforeCreate + 1);
        ExpertContactRequest testExpertContactRequest = expertContactRequests.get(expertContactRequests.size() - 1);
        assertThat(testExpertContactRequest.getApproved()).isEqualTo(DEFAULT_APPROVED);
    }

    @Test
    @Transactional
    public void getAllExpertContactRequests() throws Exception {
        // Initialize the database
        expertContactRequestRepository.saveAndFlush(expertContactRequest);

        // Get all the expertContactRequests
        restExpertContactRequestMockMvc.perform(get("/api/expertContactRequests"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(expertContactRequest.getId().intValue())))
                .andExpect(jsonPath("$.[*].approved").value(hasItem(DEFAULT_APPROVED.booleanValue())));
    }

    @Test
    @Transactional
    public void getExpertContactRequest() throws Exception {
        // Initialize the database
        expertContactRequestRepository.saveAndFlush(expertContactRequest);

        // Get the expertContactRequest
        restExpertContactRequestMockMvc.perform(get("/api/expertContactRequests/{id}", expertContactRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(expertContactRequest.getId().intValue()))
            .andExpect(jsonPath("$.approved").value(DEFAULT_APPROVED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingExpertContactRequest() throws Exception {
        // Get the expertContactRequest
        restExpertContactRequestMockMvc.perform(get("/api/expertContactRequests/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExpertContactRequest() throws Exception {
        // Initialize the database
        expertContactRequestRepository.saveAndFlush(expertContactRequest);

		int databaseSizeBeforeUpdate = expertContactRequestRepository.findAll().size();

        // Update the expertContactRequest
        expertContactRequest.setApproved(UPDATED_APPROVED);
        ExpertContactRequestDTO expertContactRequestDTO = expertContactRequestMapper.expertContactRequestToExpertContactRequestDTO(expertContactRequest);

        restExpertContactRequestMockMvc.perform(put("/api/expertContactRequests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(expertContactRequestDTO)))
                .andExpect(status().isOk());

        // Validate the ExpertContactRequest in the database
        List<ExpertContactRequest> expertContactRequests = expertContactRequestRepository.findAll();
        assertThat(expertContactRequests).hasSize(databaseSizeBeforeUpdate);
        ExpertContactRequest testExpertContactRequest = expertContactRequests.get(expertContactRequests.size() - 1);
        assertThat(testExpertContactRequest.getApproved()).isEqualTo(UPDATED_APPROVED);
    }

    @Test
    @Transactional
    public void deleteExpertContactRequest() throws Exception {
        // Initialize the database
        expertContactRequestRepository.saveAndFlush(expertContactRequest);

		int databaseSizeBeforeDelete = expertContactRequestRepository.findAll().size();

        // Get the expertContactRequest
        restExpertContactRequestMockMvc.perform(delete("/api/expertContactRequests/{id}", expertContactRequest.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ExpertContactRequest> expertContactRequests = expertContactRequestRepository.findAll();
        assertThat(expertContactRequests).hasSize(databaseSizeBeforeDelete - 1);
    }
}
