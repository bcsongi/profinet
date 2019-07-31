package edu.codespring.profinet.web.rest;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import edu.codespring.profinet.domain.Appointment;
import edu.codespring.profinet.domain.Expert;
import edu.codespring.profinet.domain.ExpertContactRequest;
import edu.codespring.profinet.domain.ExpertField;
import edu.codespring.profinet.domain.Field;
import edu.codespring.profinet.domain.Keyword;
import edu.codespring.profinet.domain.Language;
import edu.codespring.profinet.domain.Message;
import edu.codespring.profinet.domain.User;
import edu.codespring.profinet.domain.UserFeedback;
import edu.codespring.profinet.domain.enumeration.AppointmentStatus;
import edu.codespring.profinet.repository.AppointmentRepository;
import edu.codespring.profinet.repository.ExpertContactRequestRepository;
import edu.codespring.profinet.repository.ExpertFieldRepository;
import edu.codespring.profinet.repository.ExpertRepository;
import edu.codespring.profinet.repository.FieldRepository;
import edu.codespring.profinet.repository.KeywordRepository;
import edu.codespring.profinet.repository.LanguageRepository;
import edu.codespring.profinet.repository.MessageRepository;
import edu.codespring.profinet.repository.UserFeedbackRepository;
import edu.codespring.profinet.repository.search.AppointmentSearchRepository;
import edu.codespring.profinet.repository.search.ExpertContactRequestSearchRepository;
import edu.codespring.profinet.repository.search.ExpertFieldSearchRepository;
import edu.codespring.profinet.repository.search.ExpertSearchRepository;
import edu.codespring.profinet.repository.search.FieldSearchRepository;
import edu.codespring.profinet.repository.search.KeywordSearchRepository;
import edu.codespring.profinet.repository.search.LanguageSearchRepository;
import edu.codespring.profinet.repository.search.MessageSearchRepository;
import edu.codespring.profinet.repository.search.UserFeedbackSearchRepository;
import edu.codespring.profinet.service.UserService;

@RestController
@Transactional
@RequestMapping("/api")
public class CreateDb {

	
    @Inject
    private UserService userService;
	
	@Inject
	private FieldRepository fieldRepository;
	@Inject
	private FieldSearchRepository fieldSearchRepository;

	@Inject
	private KeywordRepository keywordRepository;
	@Inject
	private KeywordSearchRepository keywordSearchRepository;

	@Inject
	private LanguageRepository languageRepository;
	@Inject
	private LanguageSearchRepository languageSearchRepository;

	@Inject
	private ExpertRepository expertRepository;
	@Inject
	private ExpertSearchRepository expertSearchRepository;

	@Inject
	private ExpertFieldRepository expertFieldRepository;
	@Inject
	private ExpertFieldSearchRepository expertFieldSearchRepository;
	
    @Inject
    private ExpertContactRequestRepository expertContactRequestRepository;
    @Inject
    private ExpertContactRequestSearchRepository expertContactRequestSearchRepository;

	@Inject
	private AppointmentRepository appointmentRepository;
	@Inject
	private AppointmentSearchRepository appointmentSearchRepository;

    @Inject
    private UserFeedbackRepository userFeedbackRepository;
    @Inject
    private UserFeedbackSearchRepository userFeedbackSearchRepository;

    @Inject
    private MessageRepository messageRepository;
    @Inject
    private MessageSearchRepository messageSearchRepository;
    
	@RequestMapping(value = "/createdb", method = RequestMethod.GET)
	@Transactional
	@Timed
	public void createdb() {
		
		// ********************* FIELD ***********************
		
		Field field1 = new Field();
		field1.setName("Asztalos");
		
		Field field2 = new Field();
		field2.setName("Felszolgalo");

		Field field3 = new Field();
		field3.setName("Pek");

		Field field4 = new Field();
		field4.setName("Varro");

		Field field5 = new Field();
		field5.setName("Kovacs");

		Field field6 = new Field();
		field6.setName("Acs");

		Field field7 = new Field();
		field7.setName("Cipesz");

		Field field8 = new Field();
		field8.setName("Vizszerelo");

		Field field10 = new Field();
		field10.setName("Festo");
		
		Field field9 = new Field();
		field9.setName("Egyeb");

		field1 = fieldRepository.save(field1);
		fieldSearchRepository.save(field1);
		
		field2 = fieldRepository.save(field2);
		fieldSearchRepository.save(field2);
		
		field3 = fieldRepository.save(field3);
		fieldSearchRepository.save(field3);
		
		field4 = fieldRepository.save(field4);
		fieldSearchRepository.save(field4);
		
		field5 = fieldRepository.save(field5);
		fieldSearchRepository.save(field5);
		
		field6 = fieldRepository.save(field6);
		fieldSearchRepository.save(field6);
		
		field7 = fieldRepository.save(field7);
		fieldSearchRepository.save(field7);
		
		field8 = fieldRepository.save(field8);
		fieldSearchRepository.save(field8);
		
		field9 = fieldRepository.save(field9);
		fieldSearchRepository.save(field9);
		
		field10 = fieldRepository.save(field10);
		fieldSearchRepository.save(field10);
		
		fieldRepository.flush();
		
		// KEYWORDS
		Keyword keyword1 = new Keyword();
		keyword1.setName("jo festo");

		Keyword keyword2 = new Keyword();
		keyword2.setName("tulzsufolt");

		Keyword keyword3 = new Keyword();
		keyword3.setName("kedvezmenyes");

		Keyword keyword4 = new Keyword();
		keyword4.setName("tapasztalt");

		Keyword keyword5 = new Keyword();
		keyword5.setName("kezdo");
		
		keyword1 = keywordRepository.save(keyword1);
		keywordSearchRepository.save(keyword1);
		
		keyword2 = keywordRepository.save(keyword2);
		keywordSearchRepository.save(keyword2);
		
		keyword3 = keywordRepository.save(keyword3);
		keywordSearchRepository.save(keyword3);
		
		keyword4 = keywordRepository.save(keyword4);
		keywordSearchRepository.save(keyword4);
		
		keyword5 = keywordRepository.save(keyword5);
		keywordSearchRepository.save(keyword5);
		
		keywordRepository.flush();		

		// ********************* LANGUAGES *********************

		Language language1 = new Language();
		language1.setName("magyar");

		Language language2 = new Language();
		language2.setName("roman");

		Language language3 = new Language();
		language3.setName("angol");

		Language language4 = new Language();
		language4.setName("nemet");

		
		language1 = languageRepository.save(language1);
		languageSearchRepository.save(language1);
		
		language2 = languageRepository.save(language2);
		languageSearchRepository.save(language2);
		
		language3 = languageRepository.save(language3);
		languageSearchRepository.save(language3);
		
		language4 = languageRepository.save(language4);
		languageSearchRepository.save(language4);

		languageRepository.flush();
		
		// ********************* USER **************************
		
		User user1 = userService.createUserInformation("vizespista", "12345", "Kiss", "Istvan", "kisistvan@yahoo.com", "en");
		User user2 = userService.createUserInformation("asztalosjoska", "12345", "Borbely", "Jozsef", "borbelyjozsef@gmail.com", "en");
		User user3 = userService.createUserInformation("festojancsi", "12345", "Vass", "Janos", "vassjanos@yahoo.com", "en");
		User user4 = userService.createUserInformation("konyhatunder", "12345", "Balog", "Andrea", "balogandrea@yahoo.com", "en");
		User user5 = userService.createUserInformation("csovesdani", "12345", "Komaromi", "Daniel", "komaromidaniel@gmail.com", "en");
		User user6 = userService.createUserInformation("rozsdaskoma", "12345", "Rozs", "Daniel", "rozsdaniel@gmail.com", "en");
		User user7 = userService.createUserInformation("bcsongi", "12345", "Balog", "Csongor", "balogcsongor@gmail.com", "en");
		
		// ********************* EXPERT ************************

		Expert expert1 = new Expert();
		expert1.setTimetable("Hetfo-Pentek: 8-17 ora kozott");
		expert1.setPhoneNumber("0739213323");
		expert1.setLastActive(new DateTime("2015-10-04"));
		expert1.setLatitude(46.771232);
		expert1.setLongitude(23.56812);
		expert1.setExpertuser(user1);
		expert1.setActive(true);
		expert1.setCountry("Romania");

		Expert expert2 = new Expert();
		expert2.setTimetable("Hetfo-Pentek: 8-17 ora kozott");
		expert2.setPhoneNumber("0739213444");
		expert2.setLastActive(new DateTime("2015-10-04"));
		expert2.setLatitude(46.763123);
		expert2.setLongitude(23.56812);
		expert2.setExpertuser(user2);
		expert2.setActive(true);
		expert2.setCountry("Romania");
		
		Expert expert3 = new Expert();
		expert3.setTimetable("Hetfo-Pentek: 8-17 ora kozott");
		expert3.setPhoneNumber("0723999634");
		expert3.setLastActive(new DateTime("2015-10-04"));
		expert3.setLatitude(46.771232);
		expert3.setLongitude(23.56111);
		expert3.setExpertuser(user3);
		expert3.setActive(true);
		expert3.setCountry("Romania");
		
		Expert expert4 = new Expert();
		expert4.setTimetable("Hetfo-Pentek: 8-17 ora kozott");
		expert4.setPhoneNumber("0765425634");
		expert4.setLastActive(new DateTime("2015-10-04"));
		expert4.setLatitude(46.0);
		expert4.setLongitude(23.0);
		expert4.setExpertuser(user5);
		expert4.setActive(true);
		expert4.setCountry("Romania");
		
		Expert expert5 = new Expert();
		expert5.setTimetable("Hetfo-Pentek: 8-17 ora kozott");
		expert5.setPhoneNumber("0723325634");
		expert5.setLastActive(new DateTime("2015-10-04"));
		expert5.setLatitude(46.571232);
		expert5.setLongitude(23.96812);
		expert5.setExpertuser(user6);
		expert5.setActive(true);
		expert5.setCountry("Romania");
		
		expert1 = expertRepository.save(expert1);
		expertSearchRepository.save(expert1);
		
		expert2 = expertRepository.save(expert2);
		expertSearchRepository.save(expert2);
		
		expert3 = expertRepository.save(expert3);
		expertSearchRepository.save(expert3);
		
		expert4 = expertRepository.save(expert4);
		expertSearchRepository.save(expert4);
		
		expert5 = expertRepository.save(expert5);
		expertSearchRepository.save(expert5);
		
		expertRepository.flush();
		
		//EXPERT_FIELD
		ExpertField expertField1 = new ExpertField();
		expertField1.setExpert(expert1);
		expertField1.setField(field8);
		expertField1.setDescription("Gyors, pontos, szakszerű munkavégzéssel vállaljuk a ránk bízott vízszerelési munkákat!");
		expertField1.setRating(8.0);
		
		ExpertField expertField2 = new ExpertField();
		expertField2.setExpert(expert2);
		expertField2.setField(field1);
		expertField2.setDescription("Asztalos munkat vallalok. Javitok, szerelek, asztalbol csinalok kepkeretet.");
		expertField2.setRating(4.0);
		
		ExpertField expertField3 = new ExpertField();
		expertField3.setExpert(expert3);
		expertField3.setField(field10);
		expertField3.setDescription("Falfestet vallalok kedvezo arakkal Kolozsvar kornyeken.");
		expertField3.setRating(5.0);
		
		ExpertField expertField4 = new ExpertField();
		expertField4.setExpert(expert4);
		expertField4.setField(field8);
		expertField4.setDescription("A vízszerelési munka során az alábbi tevékenységeket vállalom: beázások, csőszivárgások, csőtörések");
		expertField4.setRating(9.0);

		ExpertField expertField5 = new ExpertField();
		expertField5.setExpert(expert5);
		expertField5.setField(field8);
		expertField5.setDescription("Olcsó vízszerelő munka.");
		expertField5.setRating(1.0);
		
		expertField1 = expertFieldRepository.save(expertField1);
		expertFieldSearchRepository.save(expertField1);
		
		expertField2 = expertFieldRepository.save(expertField2);
		expertFieldSearchRepository.save(expertField2);
		
		expertField3 = expertFieldRepository.save(expertField3);
		expertFieldSearchRepository.save(expertField3);
		
		expertField4 = expertFieldRepository.save(expertField4);
		expertFieldSearchRepository.save(expertField4);
		
		expertField5 = expertFieldRepository.save(expertField5);
		expertFieldSearchRepository.save(expertField5);
		
		expertFieldRepository.flush();
		
		// EXPERT_CONTACT_REQUEST
		
		ExpertContactRequest expertContactRequest1 = new ExpertContactRequest();
		expertContactRequest1.setApproved(true);
		expertContactRequest1.setExpertrequest(expert3);
		expertContactRequest1.setUserRequest(user1);
		
		ExpertContactRequest expertContactRequest2 = new ExpertContactRequest();
		expertContactRequest2.setApproved(true);
		expertContactRequest2.setExpertrequest(expert1);
		expertContactRequest2.setUserRequest(user2);

		ExpertContactRequest expertContactRequest3 = new ExpertContactRequest();
		expertContactRequest3.setApproved(true);
		expertContactRequest3.setExpertrequest(expert2);
		expertContactRequest3.setUserRequest(user3);
		
		ExpertContactRequest expertContactRequest4 = new ExpertContactRequest();
		expertContactRequest4.setApproved(true);
		expertContactRequest4.setExpertrequest(expert2);
		expertContactRequest4.setUserRequest(user4);
		
		expertContactRequest1 = expertContactRequestRepository.save(expertContactRequest1);
	    expertContactRequestSearchRepository.save(expertContactRequest1);
		
	    expertContactRequest2 = expertContactRequestRepository.save(expertContactRequest2);
	    expertContactRequestSearchRepository.save(expertContactRequest2);
		
	    expertContactRequest3 = expertContactRequestRepository.save(expertContactRequest3);
	    expertContactRequestSearchRepository.save(expertContactRequest3);
		
	    expertContactRequest4 = expertContactRequestRepository.save(expertContactRequest4);
	    expertContactRequestSearchRepository.save(expertContactRequest4);
		
	    expertContactRequestRepository.flush();
	    
		// MESSAGESEARCHREPOSITORY

		Message message1 = new Message();
		message1.setUserfrom(user1);
		message1.setUserto(user2);
		message1.setText("message text1");
		message1.setDate(new DateTime("2015-10-04"));
	
		Message message2 = new Message();
		message2.setUserfrom(user2);
		message2.setUserto(user1);
		message2.setText("message text2");
		message2.setDate(new DateTime("2015-10-04"));
		
		Message message3 = new Message();
		message3.setUserfrom(user3);
		message3.setUserto(user2);
		message3.setText("message text3");
		message3.setDate(new DateTime("2015-10-04"));
		
		Message message4 = new Message();
		message4.setUserfrom(user2);
		message4.setUserto(user3);
		message4.setText("message text4");
		message4.setDate(new DateTime("2015-10-04"));
		
		message1 = messageRepository.save(message1);
		messageSearchRepository.save(message1);
		
		message2 = messageRepository.save(message2);
		messageSearchRepository.save(message2);
		
		message3 = messageRepository.save(message3);
		messageSearchRepository.save(message3);
		
		message4 = messageRepository.save(message4);
		messageSearchRepository.save(message4);
		
		messageRepository.flush();
		
		// APPOINTMENT
		
		Appointment appointment1 = new Appointment();
		appointment1.setStartingDate(new DateTime("2015-10-04"));
		appointment1.setEndingDate(new DateTime("2015-10-04"));
		appointment1.setDescription("appointment description1");
		appointment1.setStatus(AppointmentStatus.ACCEPTED);
		appointment1.setExpertField(expertField1);
		appointment1.setUserappointment(user2);
		appointment1.setRated(false);

		Appointment appointment2 = new Appointment();
		appointment2.setStartingDate(new DateTime("2015-10-04"));
		appointment2.setEndingDate(new DateTime("2015-10-04"));
		appointment2.setDescription("appointment description2");
		appointment2.setStatus(AppointmentStatus.ACCEPTED);
		appointment2.setExpertField(expertField2);
		appointment2.setUserappointment(user2);
		appointment2.setRated(true);
		
		Appointment appointment3 = new Appointment();
		appointment3.setStartingDate(new DateTime("2015-10-04"));
		appointment3.setEndingDate(new DateTime("2015-10-04"));
		appointment3.setDescription("appointment description3");
		appointment3.setStatus(AppointmentStatus.ACCEPTED);
		appointment3.setExpertField(expertField4);
		appointment3.setUserappointment(user5);
		appointment3.setRated(false);

		Appointment appointment4 = new Appointment();
		appointment4.setStartingDate(new DateTime("2015-10-04"));
		appointment4.setEndingDate(new DateTime("2015-10-04"));
		appointment4.setDescription("appointment description4");
		appointment4.setStatus(AppointmentStatus.ACCEPTED);
		appointment4.setExpertField(expertField5);
		appointment4.setUserappointment(user6);
		appointment4.setRated(false);

		appointment1 = appointmentRepository.save(appointment1);
		appointmentSearchRepository.save(appointment1);
		
		appointment2 = appointmentRepository.save(appointment2);
		appointmentSearchRepository.save(appointment2);
		
		appointment3 = appointmentRepository.save(appointment3);
		appointmentSearchRepository.save(appointment3);
		
		appointment4 = appointmentRepository.save(appointment4);
		appointmentSearchRepository.save(appointment4);
		
		appointmentRepository.flush();
		
		// USERFEEDBACK
		
		UserFeedback userFeedback1 = new UserFeedback();
		userFeedback1.setComment("userfeedback comment1");
		userFeedback1.setDate(new DateTime("1998-12-12"));
		userFeedback1.setRating(8);	
		userFeedback1.setAppointmentuserfeedback(appointment1);
		
		UserFeedback userFeedback2 = new UserFeedback();
		userFeedback2.setComment("userfeedback comment2");
		userFeedback2.setDate(new DateTime("2015-10-04"));
		userFeedback2.setRating(4);	
		userFeedback2.setAppointmentuserfeedback(appointment2);
		
		UserFeedback userFeedback3 = new UserFeedback();
		userFeedback3.setComment("userfeedback comment3");
		userFeedback3.setDate(new DateTime("2015-10-04"));
		userFeedback3.setRating(8);	
		userFeedback3.setAppointmentuserfeedback(appointment3);
		
		UserFeedback userFeedback4 = new UserFeedback();
		userFeedback4.setComment("userfeedback comment4");
		userFeedback4.setDate(new DateTime("2015-10-04"));
		userFeedback4.setRating(10);	
		userFeedback4.setAppointmentuserfeedback(appointment3);

		UserFeedback userFeedback5 = new UserFeedback();
		userFeedback5.setComment("userfeedback comment5");
		userFeedback5.setDate(new DateTime("2015-10-04"));
		userFeedback5.setRating(1);	
		userFeedback5.setAppointmentuserfeedback(appointment4);

		userFeedback1 = userFeedbackRepository.save(userFeedback1);
		userFeedbackSearchRepository.save(userFeedback1);
		
		userFeedback2 = userFeedbackRepository.save(userFeedback2);
		userFeedbackSearchRepository.save(userFeedback2);
		
		userFeedback3 = userFeedbackRepository.save(userFeedback3);
		userFeedbackSearchRepository.save(userFeedback3);
		
		userFeedback4 = userFeedbackRepository.save(userFeedback4);
		userFeedbackSearchRepository.save(userFeedback4);
		
		userFeedback5 = userFeedbackRepository.save(userFeedback5);
		userFeedbackSearchRepository.save(userFeedback5);

		userFeedbackRepository.flush();
		
	}
}
