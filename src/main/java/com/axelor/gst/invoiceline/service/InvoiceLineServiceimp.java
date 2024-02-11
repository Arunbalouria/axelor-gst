package com.axelor.gst.invoiceline.service;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import com.axelor.exception.AxelorException;
import com.axelor.exception.db.repo.TraceBackRepository;
import java.math.BigDecimal;

public class InvoiceLineServiceimp implements InvoiceLineService {

  @Override
  public BigDecimal setIgst(InvoiceLine invoiceline, Invoice invoice) throws Exception {
    if (invoiceline.getProduct() == null) {
      throw new AxelorException(
          TraceBackRepository.CATEGORY_CONFIGURATION_ERROR, "Product Not Set");
    }
    BigDecimal igst = BigDecimal.ZERO;
    BigDecimal gstReat = BigDecimal.ZERO;
    gstReat = invoiceline.getGstRate();

    BigDecimal netAmount = invoiceline.getNetAmount();
    try {
      String invoiceState = invoice.getAddress().getState().getName();
      String companyState = invoice.getCompany().getAddress().getState().getName();
      if (!invoiceState.equalsIgnoreCase(companyState)) {
        BigDecimal igstvalur = netAmount.multiply(gstReat).divide(new BigDecimal(100));
        return igstvalur;
      }
    } catch (Exception e) {
      throw new AxelorException(
          TraceBackRepository.CATEGORY_CONFIGURATION_ERROR, "Address Not Set properly");
    }

    return igst;
  }

  @Override
  public BigDecimal setSgst(InvoiceLine invoiceline, Invoice invoice) throws Exception {
    try {
      String invoiceState = invoice.getAddress().getState().getName();
      String companyState = invoice.getCompany().getAddress().getState().getName();
      BigDecimal gstReat = invoiceline.getGstRate();
      BigDecimal netAmount = invoiceline.getNetAmount();
      if (invoiceState.equalsIgnoreCase(companyState)) {
        BigDecimal sgstValue = netAmount.multiply(gstReat).divide(new BigDecimal(100));
        BigDecimal setFinalsgstVlaue = sgstValue.divide(new BigDecimal(2));
        return setFinalsgstVlaue;
      }
    } catch (Exception e) {
      throw new AxelorException(
          TraceBackRepository.CATEGORY_CONFIGURATION_ERROR, "Address Not Set properly");
    }
    return new BigDecimal(0);
  }

  @Override
  public BigDecimal setCgst(InvoiceLine invoiceline, Invoice invoice) throws Exception {
    try {
      BigDecimal gstReat = invoiceline.getGstRate();
      BigDecimal netAmount = invoiceline.getNetAmount();
      String invoiceState = invoice.getAddress().getState().getName();
      String companyState = invoice.getCompany().getAddress().getState().getName();
      if (invoiceState.equalsIgnoreCase(companyState)) {
        BigDecimal cgstValue = netAmount.multiply(gstReat).divide(new BigDecimal(100));
        BigDecimal setFinalsgstVlaue = cgstValue.divide(new BigDecimal(2));
        return setFinalsgstVlaue;
      }
    } catch (Exception e) {

      throw new AxelorException(
          TraceBackRepository.CATEGORY_CONFIGURATION_ERROR, "Address Not Set properly");
    }
    return new BigDecimal(0);
  }

  @Override
  public BigDecimal setNetAmount(InvoiceLine invoiceLine) throws Exception {
    BigDecimal netAmount = BigDecimal.ONE;
    netAmount = netAmount.multiply(invoiceLine.getQty().multiply(invoiceLine.getPrice()));
    return netAmount;
  }

  @Override
  public BigDecimal setGrossAmount(InvoiceLine invoiceLine) throws Exception {
    BigDecimal grossAmount = BigDecimal.ZERO;
    grossAmount =
        grossAmount
            .add(invoiceLine.getCGST())
            .add(invoiceLine.getIGST())
            .add(invoiceLine.getSGST())
            .add(invoiceLine.getNetAmount());
    return grossAmount;
  }
}
