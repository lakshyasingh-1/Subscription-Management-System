
package com.subscriptiq.subscriptiq.service;

import com.subscriptiq.subscriptiq.dto.CustomerRequest;
import com.subscriptiq.subscriptiq.entity.Customer;
import com.subscriptiq.subscriptiq.repository.CustomerRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // Create Customer
    public Customer createCustomer(CustomerRequest request) {

        String email = request.getEmail().trim().toLowerCase();

        if (customerRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Customer with this email already exists"
            );
        }

        Customer customer = new Customer();

        customer.setFullName(request.getFullName().trim());
        customer.setEmail(email);
        customer.setPhone(request.getPhone());
        customer.setCompanyName(request.getCompanyName());
        customer.setBillingAddress(request.getBillingAddress());
        customer.setGstin(request.getGstin());

        return customerRepository.save(customer);
    }

    // Get All Customers
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // Get Customer By ID
    public Customer getCustomerById(Long id) {

        return customerRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Customer not found")
                );
    }

    // Update Customer
    public Customer updateCustomer(Long id, CustomerRequest request) {

        Customer customer = getCustomerById(id);

        String email = request.getEmail().trim().toLowerCase();

        if (!customer.getEmail().equals(email)
                && customerRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Email is already used by another customer"
            );
        }

        customer.setFullName(request.getFullName().trim());
        customer.setEmail(email);
        customer.setPhone(request.getPhone());
        customer.setCompanyName(request.getCompanyName());
        customer.setBillingAddress(request.getBillingAddress());
        customer.setGstin(request.getGstin());

        return customerRepository.save(customer);
    }

    // Delete Customer
    public void deleteCustomer(Long id) {

        Customer customer = getCustomerById(id);

        customerRepository.delete(customer);
    }
}
