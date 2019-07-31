package edu.codespring.profinet.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryString;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.elasticsearch.index.query.FilterBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import edu.codespring.profinet.domain.Appointment;

public class AppointmentSearchRepositoryImpl implements AppointmentSearchRepositoryCustom {
	@Autowired
	AppointmentSearchRepository appointmentSearchRepository;
	
//	@Autowired
	
	@Override
	public List<Appointment> findInUsersAppointment(String query) {
		String login = "";
		
		SecurityContext securityContext = SecurityContextHolder.getContext();
	    Authentication authentication = securityContext.getAuthentication();
	    if(authentication != null) {
	    	login = authentication.getName();
	    }
		
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
    		    .withQuery(queryString(query))
    		    .withFilter(FilterBuilders.boolFilter().must(FilterBuilders.termFilter("userappointment.login", login)))
    		    .build();
    	return StreamSupport
	            .stream(appointmentSearchRepository.search(searchQuery).spliterator(), false)
	            .collect(Collectors.toList());
	}
	
	@Override
	public List<Appointment> findInExpertsAppointment(String query) {
		String login = "";
		
		SecurityContext securityContext = SecurityContextHolder.getContext();
	    Authentication authentication = securityContext.getAuthentication();
	    if(authentication != null) {
	    	login = authentication.getName();
	    }
		
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
    		    .withQuery(queryString(query))
    		    .withFilter(FilterBuilders.boolFilter().must(FilterBuilders.termFilter("expertField.expert.expertuser.login", login)))
    		    .build();
		
    	return StreamSupport
	            .stream(appointmentSearchRepository.search(searchQuery).spliterator(), false)
	            .collect(Collectors.toList());
	}

}
