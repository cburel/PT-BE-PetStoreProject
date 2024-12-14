package pet.store.controller.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

@Data
@NoArgsConstructor
public class PetStoreData {
	private Long petStoreId;
	private String petStoreName;
	private String petStoreAddress;
	private String petStoreCity;
	private String petStoreState;
	private String petStoreZip;
	private String petStorePhone;
	Set<PetStoreCustomer> customers = new HashSet<>();	
	Set<PetStoreEmployee> employees = new HashSet<>();
	
	public PetStoreData(PetStore petStore) {
		petStoreId = petStore.getPetStoreId();
		petStoreAddress = petStore.getPetStoreAddress();
		petStoreCity = petStore.getPetStoreCity();
		petStoreState = petStore.getPetStoreState();
		petStoreZip = petStore.getPetStoreZip();
		petStorePhone = petStore.getPetStorePhone();
		
		for(Customer cust : petStore.getCustomers()) {
			customers.add(new PetStoreCustomer(cust));
		}
		
		for (Employee emp : petStore.getEmployees()) {
			employees.add(new PetStoreEmployee(emp));
		}
	}
	
	@Data
	@NoArgsConstructor
	public static class PetStoreCustomer {
		private Long customerId;
		private String customerFirstName;
		private String customerLastName;
		private String customerEmail;
		
		public PetStoreCustomer(Customer cust) {
			customerId = cust.getCustomerId();
			customerFirstName = cust.getCustomerFirstName();
			customerLastName = cust.getCustomerLastName();
			customerEmail = cust.getCustomerEmail();			
		}
	}
	
	@Data
	@NoArgsConstructor
	public static class PetStoreEmployee {
		private Long employeeId;
		private String employeeFirstName;
		private String employeeLastName;
		private String employeePhone;
		private String employeeJobTitle;
		
		public PetStoreEmployee(Employee emp) {
			employeeId = emp.getEmployeeId();
			employeeFirstName = emp.getEmployeeFirstName();
			employeeLastName = emp.getEmployeeLastName();
			employeePhone = emp.getEmployeePhone();
			employeeJobTitle = emp.getEmployeeJobTitle();
		}
	}
}
