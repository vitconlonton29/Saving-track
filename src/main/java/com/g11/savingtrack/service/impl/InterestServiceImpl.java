package com.g11.savingtrack.service.impl;

import com.g11.savingtrack.dto.request.InterestRequest;
import com.g11.savingtrack.dto.response.InterestResponse;
import com.g11.savingtrack.entity.Passbook;
import com.g11.savingtrack.repository.SavingProductRepository;
import com.g11.savingtrack.service.InterestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class InterestServiceImpl implements InterestService {
  private final SavingProductRepository savingProductRepository;

  public InterestResponse calculator(InterestRequest request) {
    log.info("(calculator) request:{}", request);

    int month = request.getTerm();
    double rate = request.getInterestRate();
    long amount = request.getAmount();
    long interest = 0;
    int paymentMethod = request.getPaymentMethod();

    Date startDate = request.getStartDate();
    Date endDate = request.getEndDate();

    //lai suat khong ky han
    double defaultRate = savingProductRepository.findByTerm(0).getInterestRate();


    //neu month = 0 => tinh lai theo lai suat khong ky han
    if (month == 0) {
      long numberOfDays = calculateDayDifference(startDate, endDate);
      interest = (long) Math.ceil((amount * numberOfDays * defaultRate / 100) / 365);
    } else {

      //tinh so thang
      int diffMonth = calculateMonthDifference(startDate, endDate);
      log.info("so Thang: {}", diffMonth);

      while (diffMonth >= month) {
        //tinh so ngay trong tung ky han
        int days = countDaysFromDate(startDate, month);
        log.info("so ngay trong tung ky han: {}", days);
        //tinh lai
        interest = (long) Math.ceil((amount * rate / 100 / 365) * days);
        log.info("lai trong 1 ky han: {}", interest);
        //neu phuong thuc tra la la cong goc
        if (paymentMethod == 1) {
          amount += interest;
        }
        log.info("goc sau cong: {}", amount);
        //ngay tinh = ngay bat dau ky han truoc + so thang cua ky han
        startDate = addMonthsToDate(startDate, month);
        //so thang tru di so thang cua ky han
        diffMonth -= month;
      }

      //so ngay du
      long numberOfDays = calculateDayDifference(startDate, endDate);
      log.info("so ngay du:{}", numberOfDays);
      interest = (long) Math.ceil((amount * defaultRate / 100 / 365 * numberOfDays));

    }
    amount += interest;

    return new InterestResponse(request.getAmount(), amount - request.getAmount(), amount);
  }

  //Tinh lai suat dua tren passbook
  public Long calculator(Passbook passbook) {
    log.info("(calculator) passbook:{}", passbook);
    int month = passbook.getSavingProduct().getTerm();

    double rate = passbook.getSavingProduct().getInterestRate();
    long amount = passbook.getAmount();
    long interest = 0;
    Date startDate = passbook.getCreatedAt();

    //neu month = 0 => tinh lai theo lai suat khong ky han
    if (month == 0) {
      interest = 0;
    } else {
      //tinh so ngay trong tung ky han
      int days = countDaysFromDate(startDate, month);
      log.info("so ngay trong tung ky han: {}", days);
      //tinh lai
      interest = (long) Math.ceil((amount * rate / 100 / 365) * days);
      log.info("lai trong 1 ky han tiep theo: {}", interest);

    }
    return interest;

  }


  private int calculateMonthDifference(Date startDate, Date endDate) {


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

    return TimeUnit.DAYS.convert(endDate.getTime() - startDate.getTime(), TimeUnit.MILLISECONDS);
  }

  private int countDaysFromDate(Date startDate, int months) {

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

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(startDate);
    calendar.add(Calendar.MONTH, months);
    return calendar.getTime();
  }
}
