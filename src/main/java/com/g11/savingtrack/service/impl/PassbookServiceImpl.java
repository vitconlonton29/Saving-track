package com.g11.savingtrack.service.impl;

import com.g11.savingtrack.dto.request.PassbookRequest;
import com.g11.savingtrack.dto.response.PassbookResponse;
import com.g11.savingtrack.entity.Customer;
import com.g11.savingtrack.entity.Employee;
import com.g11.savingtrack.entity.Passbook;
import com.g11.savingtrack.entity.Period;
import com.g11.savingtrack.exception.customer.CustomerNotFoundException;
import com.g11.savingtrack.exception.employee.EmployeeNotFoundException;
import com.g11.savingtrack.exception.period.PeriodNotFoundException;
import com.g11.savingtrack.repository.*;
import com.g11.savingtrack.service.PassbookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.g11.savingtrack.utils.MapperUtils.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PassbookServiceImpl implements PassbookService {
  private final PassbookRepository passbookRepository;
  private final CustomerRepository customerRepository;
  private final EmployeeRepository employeeRepository;
  private final ExpireRepository expireRepository;
  private final PeriodRepository periodRepository;

  public PassbookResponse create(PassbookRequest request) {
    log.info("request:{}", request);

    Passbook passbook = Passbook.from(request);

    Optional<Customer> customer = customerRepository.findById(request.getCustomerId());
    if (customer.isEmpty()) throw new CustomerNotFoundException();
    passbook.setCustomer(customer.get());

    Optional<Employee> employee = employeeRepository.findById(request.getEmployeeId());
    if (employee.isEmpty()) throw new EmployeeNotFoundException();
    passbook.setEmployee(employee.get());

    Period period = periodRepository.findByMonth(request.getMonth());
    if (period == null) throw new PeriodNotFoundException();
    passbook.setPeriod(period);


    return PassbookResponse.from(passbookRepository.save(passbook));

  }
}
