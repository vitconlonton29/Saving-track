package com.g11.savingtrack.service.impl;

import com.g11.savingtrack.dto.request.PassbookRequest;
import com.g11.savingtrack.dto.response.InterestResponse;
import com.g11.savingtrack.dto.response.PassbookResponse;
import com.g11.savingtrack.entity.Customer;
import com.g11.savingtrack.entity.Employee;
import com.g11.savingtrack.entity.Passbook;
import com.g11.savingtrack.entity.Period;
import com.g11.savingtrack.exception.customer.CustomerNotFoundException;
import com.g11.savingtrack.exception.employee.EmployeeNotFoundException;
import com.g11.savingtrack.exception.passbook.PassbookAlreadyWithdaw;
import com.g11.savingtrack.exception.passbook.PassbookNotFoundException;
import com.g11.savingtrack.exception.period.PeriodNotFoundException;
import com.g11.savingtrack.repository.*;
import com.g11.savingtrack.service.PassbookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.g11.savingtrack.utils.MapperUtils.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PassbookServiceImpl implements PassbookService {
  private final PassbookRepository passbookRepository;
  private final CustomerRepository customerRepository;
  private final EmployeeRepository employeeRepository;
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

  public PassbookResponse getPassbookById(int id) {
    Passbook passbook = passbookRepository.findById(id).orElseThrow(() -> new PassbookNotFoundException());
    return PassbookResponse.from(passbook);
  }

  public List<Passbook> getPassbooksByCccd(String cccd) {
    List<Passbook> list = passbookRepository.getPassbooksByCCCD(cccd);
    if (list.isEmpty()) throw new PassbookNotFoundException();
    else return list;
  }

  public Passbook withdrawPassbook(int id) {
    Passbook passbook = passbookRepository.findById(id).orElseThrow(() -> new PassbookNotFoundException());
    if (passbook.getStatus().equals("0")) throw new PassbookAlreadyWithdaw();
    else {
      passbook.setStatus("0");
      passbook.setWithdrawAt(new Date());
      return passbookRepository.save(passbook);
    }
  }

  @Override
  public InterestResponse getInterest(int id) {
    log.info("getInterest) id:{}", id);

    Passbook passbook = passbookRepository.findById(id)
          .orElseThrow(PassbookNotFoundException::new);


    int month = passbook.getPeriod().getMonth();
    double rate = passbook.getPeriod().getInterestRate();
    long amount = passbook.getAmount();
    long interest = 0;

    Date startDate = passbook.getCreatedAt();
    Date endDate = new Date();
    double defaultRate = 6;

    long numberOfDays = calculateDayDifference(startDate, endDate);

    if (month == 0) {
      interest = (long) Math.ceil((amount * numberOfDays * defaultRate / 100) / 365);
    } else {
      int diffMonth = calculateMonthDifference(startDate, endDate);
      while (diffMonth >= month) {
        int days = countDaysFromDate(startDate, month);
        interest = (long) Math.ceil((amount * rate / 100 / 365 * days));
        amount += interest;
        diffMonth -= month;
        startDate = addMonthsToDate(startDate, month);
      }

      numberOfDays = calculateDayDifference(startDate, endDate);
      interest = (long) Math.ceil((amount * defaultRate / 100 / 365 * numberOfDays));


    }
    amount += interest;

    return new InterestResponse(
          id,
          passbook.getCustomer().getFullName(),
          passbook.getAmount(),
          passbook.getPeriod().getMonth(),
          passbook.getPeriod().getInterestRate(),
          startDate,
          endDate,
          amount);

  }


  private int calculateMonthDifference(Date startDate, Date endDate) {
    log.info("(calculateMonthDifference) startDate: {} end:{}", startDate, endDate);

    Calendar startCalendar = Calendar.getInstance();
    startCalendar.setTime(startDate);

    Calendar endCalendar = Calendar.getInstance();
    endCalendar.setTime(endDate);

    int startYear = startCalendar.get(Calendar.YEAR);
    int startMonth = startCalendar.get(Calendar.MONTH);
    int endYear = endCalendar.get(Calendar.YEAR);
    int endMonth = endCalendar.get(Calendar.MONTH);

    return (endYear - startYear) * 12 + (endMonth - startMonth);
  }

  private long calculateDayDifference(Date startDate, Date endDate) {
    log.info("(calculateDayDifference) start:{} end:{}", startDate, endDate);

    return TimeUnit.DAYS.convert(endDate.getTime() - startDate.getTime(), TimeUnit.MILLISECONDS);
  }

  private int countDaysFromDate(Date startDate, int months) {
    log.info("(countDaysFromDate) start:{} month:{}", startDate, months);

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(startDate);

    int totalDays = 0;
    for (int i = 0; i < months; i++) {
      int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
      totalDays += daysInMonth;
      calendar.add(Calendar.MONTH, 1);
    }

    return totalDays;
  }

  private Date addMonthsToDate(Date startDate, int months) {
    log.info("(addMonthsToDate) start:{} months:{}", startDate, months);

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(startDate);
    calendar.add(Calendar.MONTH, months);
    return calendar.getTime();
  }


}

