//package net.boardrank.backend.service;
//
//import net.boardrank.backend.entity.Contact;
//import net.boardrank.backend.repository.CompanyRepository;
//import net.boardrank.backend.repository.ContactRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//@Service
//public class ContactService {
//    private static final Logger LOGGER = Logger.getLogger(ContactService.class.getName());
//    private ContactRepository contactRepository;
//    private CompanyRepository companyRepository;
//
//    public ContactService(ContactRepository contactRepository,
//                          CompanyRepository companyRepository) {
//        this.contactRepository = contactRepository;
//        this.companyRepository = companyRepository;
//    }
//
//    public List<Contact> findAll() {
//        return contactRepository.findAll();
//    }
//
//    public long count() {
//        return contactRepository.count();
//    }
//
//    public void delete(Contact contact) {
//        contactRepository.delete(contact);
//    }
//
//    public void save(Contact contact) {
//        if (contact == null) {
//            LOGGER.log(Level.SEVERE,
//                    "Contact is null. Are you sure you have connected your form to the application?");
//            return;
//        }
//        contactRepository.save(contact);
//    }
//
//    public List<Contact> findAll(String stringFilter) {
//        if (stringFilter == null || stringFilter.isEmpty()) {
//            return contactRepository.findAll();
//        } else {
////            return contactRepository.search(stringFilter);
//            return  contactRepository.findByFirstNameContainsOrLastNameContains(stringFilter, stringFilter);
//        }
//    }
//
//}
