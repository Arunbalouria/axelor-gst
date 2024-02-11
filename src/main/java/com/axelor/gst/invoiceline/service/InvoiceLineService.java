package com.axelor.gst.invoiceline.service;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import java.math.BigDecimal;

public interface InvoiceLineService {

  public BigDecimal setIgst(InvoiceLine invoiceline, Invoice invoice) throws Exception;

  public BigDecimal setSgst(InvoiceLine invoiceline, Invoice invoice) throws Exception;

  public BigDecimal setCgst(InvoiceLine invoiceline, Invoice invoice) throws Exception;

  public BigDecimal setNetAmount(InvoiceLine invoiceLine) throws Exception;

  public BigDecimal setGrossAmount(InvoiceLine invoiceLine) throws Exception;
}
