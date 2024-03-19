package com.g11.savingtrack.service.impl;

import com.g11.savingtrack.dto.request.CustomerRequest;
import com.g11.savingtrack.dto.response.CustomerResponse;
import com.g11.savingtrack.entity.Customer;
import com.g11.savingtrack.exception.customer.CustomerAlreadyExistException;
import com.g11.savingtrack.repository.CustomerRepository;
import com.g11.savingtrack.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.g11.savingtrack.utils.MapperUtils.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
  private final CustomerRepository repository;

  public CustomerResponse create(CustomerRequest request) {
    log.info("(create) request:{}", request);

    if (repository.findByIdentityCardNumber(request.getIdentityCardNumber()) != null)
      throw new CustomerAlreadyExistException();

    Customer customer = toEntity(request, Customer.class);
    
    return toDTO(repository.save(customer), CustomerResponse.class);
  }

}
