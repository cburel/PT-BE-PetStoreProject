package pet.store.service;

import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreData.PetStoreCustomer;
import pet.store.controller.model.PetStoreData.PetStoreEmployee;
import pet.store.dao.CustomerDao;
import pet.store.dao.EmployeeDao;
import pet.store.dao.PetStoreDao;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

@Service
public class PetStoreService {
	@Autowired
	private PetStoreDao petStoreDao;
	@Autowired
	private EmployeeDao employeeDao;
	@Autowired
	private CustomerDao customerDao;

	public PetStoreData savePetStore(PetStoreData petStoreData) {
		Long petStoreId = petStoreData.getPetStoreId();
		PetStore petStore = findOrCreatePetStore(petStoreId);

		copyPetStoreFields(petStore, petStoreData);
		return new PetStoreData(petStoreDao.save(petStore));
	}

	private void copyPetStoreFields(PetStore petStore, PetStoreData petStoreData) {
		petStore.setPetStoreAddress(petStoreData.getPetStoreAddress());
		petStore.setPetStoreCity(petStoreData.getPetStoreCity());
		petStore.setPetStoreId(petStoreData.getPetStoreId());
		petStore.setPetStoreName(petStoreData.getPetStoreName());
		petStore.setPetStorePhone(petStoreData.getPetStorePhone());
		petStore.setPetStoreState(petStoreData.getPetStoreState());
		petStore.setPetStoreZip(petStoreData.getPetStoreZip());
	}

	private PetStore findOrCreatePetStore(Long petStoreId) {
		if(Objects.isNull(petStoreId)) {
			return new PetStore();
		}
		else {
			return findPetStoreById(petStoreId);
		}
	}

	private PetStore findPetStoreById(Long petStoreId) {
		return petStoreDao.findById(petStoreId).orElseThrow(
				() -> new NoSuchElementException("Pet store with ID=" + petStoreId + " was not found.")
				);
	}

	@Transactional (readOnly = false)
	public PetStoreEmployee saveEmployee(Long petStoreId, PetStoreEmployee petStoreEmployee) {
		PetStore store = findPetStoreById(petStoreId);
		Long empId = petStoreEmployee.getEmployeeId();
		Employee emp = findOrCreateEmployee(petStoreId, empId);
		
		copyEmployeeFields(emp, petStoreEmployee);
		emp.setPetStore(store);
		
		Employee dbEmployee = employeeDao.save(emp);
		
		return new PetStoreEmployee(dbEmployee);
	}
	
	private Employee findEmployeeById(Long petStoreId, Long employeeId) {
		Employee employee = employeeDao.findById(employeeId).orElseThrow(
				() -> new NoSuchElementException("Employee with ID=" + employeeId + " was not found.")
				);
		
		if (employee.getPetStore().getPetStoreId() != petStoreId) {
			throw new IllegalArgumentException("Employee ID=" + employeeId + " does not work at pet store with ID=" + petStoreId + ".");
		}
		
		return employee;
	}
	
	private Employee findOrCreateEmployee(Long petStoreId, Long employeeId) {
		if (Objects.isNull(employeeId)) {
			return new Employee();
		}
		return findEmployeeById(petStoreId, employeeId);
	}
	
	private void copyEmployeeFields(Employee emp, PetStoreEmployee petStoreEmp) {
		emp.setEmployeeFirstName(petStoreEmp.getEmployeeFirstName());
		emp.setEmployeeId(petStoreEmp.getEmployeeId());
		emp.setEmployeeJobTitle(petStoreEmp.getEmployeeJobTitle());
		emp.setEmployeeLastName(petStoreEmp.getEmployeeLastName());
		emp.setEmployeePhone(petStoreEmp.getEmployeePhone());
	}
	
	
	//
	
	@Transactional (readOnly = false)
	public PetStoreCustomer saveCustomer(Long petStoreId, PetStoreCustomer petStoreCust) {
		PetStore store = findPetStoreById(petStoreId);
		Long custId = petStoreCust.getCustomerId();
		Customer cust = findOrCreateCustomer(petStoreId, custId);
		
		copyCustomerFields(cust, petStoreCust);
		
		cust.getPetStores().add(store);
		store.getCustomers().add(cust);
		
		Customer dbCustomer = customerDao.save(cust);
		
		return new PetStoreCustomer(dbCustomer);
	}
	
	private Customer findCustomerById(Long petStoreId, Long customerId) {
		Customer cust = customerDao.findById(customerId).orElseThrow(
				() -> new NoSuchElementException("Customer with ID=" + customerId + " was not found.")
				);
		
		boolean found = false;
		
		for (PetStore petStore : cust.getPetStores()) {
			if (petStore.getPetStoreId() == petStoreId) {
				found = true;
				break;
			}
		}
		
		if (!found) {
			throw new IllegalArgumentException("Customer ID=" + customerId + " does not have a membership at pet store with ID=" + petStoreId + ".");
		}
		
		return cust;
	}
	
	private Customer findOrCreateCustomer(Long petStoreId, Long customerId) {
		if (Objects.isNull(customerId)) {
			return new Customer();
		}
		return findCustomerById(petStoreId, customerId);
	}
	
	private void copyCustomerFields(Customer cust, PetStoreCustomer petStoreCust) {
		cust.setCustomerEmail(petStoreCust.getCustomerEmail());
		cust.setCustomerFirstName(petStoreCust.getCustomerFirstName());
		cust.setCustomerId(petStoreCust.getCustomerId());
		cust.setCustomerLastName(petStoreCust.getCustomerLastName());
	}
}
