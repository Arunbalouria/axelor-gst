package com.axelor.gst.invoice.service;

import com.axelor.apps.account.db.Invoice;
import java.math.BigDecimal;

public interface InvoiceService {
  public BigDecimal setNetAmount(Invoice invoice);

  public BigDecimal setNetIGST(Invoice invoice);

  public BigDecimal setNetCGST(Invoice invoice);

  public BigDecimal setNetSGST(Invoice invoice);

  public BigDecimal setGrossAmount(Invoice invoice);
}
