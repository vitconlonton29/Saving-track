package com.g11.savingtrack.service.impl;

import com.g11.savingtrack.dto.request.PassbookRequest;
import com.g11.savingtrack.dto.response.PassbookResponse;
import com.g11.savingtrack.entity.Customer;
import com.g11.savingtrack.entity.Passbook;
import com.g11.savingtrack.entity.SavingProduct;
import com.g11.savingtrack.exception.customer.CustomerNotFoundException;
import com.g11.savingtrack.exception.passbook.PassbookNotFoundException;
import com.g11.savingtrack.exception.savingproduct.SavingProductNotFoundException;
import com.g11.savingtrack.repository.CustomerRepository;
import com.g11.savingtrack.repository.PassbookRepository;
import com.g11.savingtrack.repository.SavingProductRepository;
import com.g11.savingtrack.service.PassbookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class PassbookServiceImpl implements PassbookService {
  private final PassbookRepository passbookRepository;
  private final CustomerRepository customerRepository;
  private final SavingProductRepository savingProductRepository;

  @Override
  public PassbookResponse create(PassbookRequest request) {
    log.info("(create) request:{}", request);

    Passbook passbook = Passbook.from(request);

    Optional<Customer> customer = customerRepository.findById(request.getCustomerId());
    if (customer.isEmpty()) throw new CustomerNotFoundException();
    passbook.setCustomer(customer.get());

    Optional<SavingProduct> savingProduct = savingProductRepository.findById(request.getSavingProductId());
    if (savingProduct.isEmpty()) throw new SavingProductNotFoundException();
    passbook.setSavingProduct(savingProduct.get());

    return PassbookResponse.from(passbookRepository.save(passbook));

  }

  @Override
  public List<PassbookResponse> list(int id) {
    log.info("(list)");

    List<Passbook> passbooks = passbookRepository.findAllByCustomerId(id);
    List<PassbookResponse> passbookResponses = new ArrayList<>();
    for (Passbook p : passbooks) {
      passbookResponses.add(PassbookResponse.from(p));
    }

    return passbookResponses;
  }

  @Override
  public PassbookResponse detail(int passbookId){
    log.info("detail");

    Passbook passbook = passbookRepository.findById(passbookId).orElseThrow(PassbookNotFoundException::new);

    return PassbookResponse.from(passbook);
  }
}
