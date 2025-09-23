// package com.examly.springapp;

// import com.examly.springapp.model.Customer;
// import com.examly.springapp.repository.CustomerRepository;
// import com.examly.springapp.service.CustomerService;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import org.junit.jupiter.api.*;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;

// import java.util.Arrays;
// import java.util.List;
// import java.util.Optional;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @SpringBootTest
// @AutoConfigureMockMvc
// class SpringappApplicationTests {

//     @Autowired
//     private MockMvc mockMvc;

//     @Autowired
//     private ObjectMapper objectMapper;

//     @MockBean
//     private CustomerRepository customerRepository;

//     @Autowired
//     private CustomerService customerService;

//     @Mock
//     private CustomerRepository mockRepo;

//     @InjectMocks
//     private CustomerService mockService;

//     private Customer sampleCustomer;

//     @BeforeEach
//     void setUp() {
//         MockitoAnnotations.openMocks(this);
//         sampleCustomer = new Customer("John Doe", "john@example.com", "1234567890", "ABC Corp");
//         sampleCustomer.setId(1L);
//     }

  

//     // 4. Service - updateCustomer not found
//     @Test
//     void SpringBoot_ProjectAnalysisAndUMLDiagram_testUpdateCustomerNotFound() {
//         when(mockRepo.findById(2L)).thenReturn(Optional.empty());
//         Assertions.assertThrows(RuntimeException.class, () -> {
//             mockService.updateCustomer(2L, sampleCustomer);
//         });
//     }

   

//     // 6. Controller - GET all customers
//     @Test
//     void SpringBoot_DevelopCoreAPIsAndBusinessLogic_testGetAllCustomersController() throws Exception {
//         when(customerRepository.findAll()).thenReturn(List.of(sampleCustomer));
//         mockMvc.perform(get("/api/customers"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$[0].name").value("John Doe"));
//     }

//     // 7. Controller - POST add customer
//     @Test
//     void SpringBoot_DevelopCoreAPIsAndBusinessLogic_testAddCustomerController() throws Exception {
//         when(customerRepository.save(any(Customer.class))).thenReturn(sampleCustomer);
//         mockMvc.perform(post("/api/customers")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(sampleCustomer)))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.name").value("John Doe"));
//     }

//     // 8. Controller - PUT update customer
//     @Test
//     void SpringBoot_DevelopCoreAPIsAndBusinessLogic_testUpdateCustomerController() throws Exception {
//         when(customerRepository.findById(1L)).thenReturn(Optional.of(sampleCustomer));
//         when(customerRepository.save(any(Customer.class))).thenReturn(sampleCustomer);

//         mockMvc.perform(put("/api/customers/1")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(sampleCustomer)))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.name").value("John Doe"));
//     }

//     // 9. Controller - DELETE customer
//     @Test
//     void SpringBoot_DevelopCoreAPIsAndBusinessLogic_testDeleteCustomerController() throws Exception {
//         doNothing().when(customerRepository).deleteById(1L);
//         mockMvc.perform(delete("/api/customers/1"))
//                 .andExpect(status().isOk());
//     }

//     // 10. Repository - save
//     @Test
//     void SpringBoot_DatabaseAndSchemaSetup_testRepositorySave() {
//         when(customerRepository.save(sampleCustomer)).thenReturn(sampleCustomer);
//         Customer saved = customerRepository.save(sampleCustomer);
//         Assertions.assertEquals("John Doe", saved.getName());
//     }

//     // 11. Repository - findById
//     @Test
//     void SpringBoot_DatabaseAndSchemaSetup_testRepositoryFindById() {
//         when(customerRepository.findById(1L)).thenReturn(Optional.of(sampleCustomer));
//         Optional<Customer> found = customerRepository.findById(1L);
//         Assertions.assertTrue(found.isPresent());
//     }

//     // 12. Repository - findAll
//     @Test
//     void SpringBoot_DatabaseAndSchemaSetup_testRepositoryFindAll() {
//         when(customerRepository.findAll()).thenReturn(List.of(sampleCustomer));
//         List<Customer> customers = customerRepository.findAll();
//         Assertions.assertEquals(1, customers.size());
//     }

//     // 13. Repository - deleteById
//     @Test
//     void SpringBoot_DatabaseAndSchemaSetup_testRepositoryDeleteById() {
//         doNothing().when(customerRepository).deleteById(1L);
//         customerRepository.deleteById(1L);
//         verify(customerRepository, times(1)).deleteById(1L);
//     }

//     // 14. Controller - empty list
//     @Test
//     void SpringBoot_DevelopCoreAPIsAndBusinessLogic_testEmptyCustomerList() throws Exception {
//         when(customerRepository.findAll()).thenReturn(List.of());
//         mockMvc.perform(get("/api/customers"))
//                 .andExpect(status().isOk())
//                 .andExpect(content().json("[]"));
//     }

 
// }
