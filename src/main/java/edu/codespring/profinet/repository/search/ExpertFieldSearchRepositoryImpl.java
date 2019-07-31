package edu.codespring.profinet.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryString;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.elasticsearch.index.query.FilterBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import edu.codespring.profinet.domain.ExpertField;

public class ExpertFieldSearchRepositoryImpl implements ExpertFieldSearchRepositoryCustom {
	@Autowired
	ExpertFieldSearchRepository expertFieldSearchRepository;
	
	@Override
	public List<ExpertField> findInActiveExperts(String query) {
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
    		    .withQuery(queryString(query))
    		    .withFilter(FilterBuilders.boolFilter().must(FilterBuilders.termFilter("active", true)))
    		    .build();
    	return StreamSupport
	            .stream(expertFieldSearchRepository.search(searchQuery).spliterator(), false)
	            .collect(Collectors.toList());
	}

}
