package edu.codespring.profinet.repository.search;

import java.util.List;

import edu.codespring.profinet.domain.ExpertField;

public interface ExpertFieldSearchRepositoryCustom {

	public List<ExpertField> findInActiveExperts(String query);
}
