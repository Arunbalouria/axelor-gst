package com.axelor.gst.invoice.service;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import java.math.BigDecimal;
import java.util.List;

public class InvoiceServiceimp implements InvoiceService {

  @Override
  public BigDecimal setNetAmount(Invoice invoice) {
    List<InvoiceLine> invoiceLine = invoice.getInvoiceLineList();
    BigDecimal setNetAmount = BigDecimal.ZERO;

    for (InvoiceLine invl : invoiceLine) {
      setNetAmount = setNetAmount.add(invl.getNetAmount());
    }
    return setNetAmount;
  }

  @Override
  public BigDecimal setNetIGST(Invoice invoice) {
    List<InvoiceLine> invoiceLine = invoice.getInvoiceLineList();
    BigDecimal setNetIGST = BigDecimal.ZERO;

    for (InvoiceLine invl : invoiceLine) {
      setNetIGST = setNetIGST.add(invl.getIGST());
    }
    return setNetIGST;
  }

  @Override
  public BigDecimal setNetCGST(Invoice invoice) {
    List<InvoiceLine> invoiceLine = invoice.getInvoiceLineList();
    BigDecimal setNetCGST = BigDecimal.ZERO;

    for (InvoiceLine invl : invoiceLine) {
      setNetCGST = setNetCGST.add(invl.getCGST());
    }
    return setNetCGST;
  }

  @Override
  public BigDecimal setNetSGST(Invoice invoice) {

    List<InvoiceLine> invoiceLine = invoice.getInvoiceLineList();
    BigDecimal setNetSGST = BigDecimal.ZERO;

    for (InvoiceLine invl : invoiceLine) {
      setNetSGST = setNetSGST.add(invl.getSGST());
    }
    return setNetSGST;
  }

  @Override
  public BigDecimal setGrossAmount(Invoice invoice) {
    List<InvoiceLine> invoiceLine = invoice.getInvoiceLineList();
    BigDecimal GrossAmount = BigDecimal.ZERO;

    for (InvoiceLine invl : invoiceLine) {
      GrossAmount = GrossAmount.add(invl.getGrossAmount());
    }
    return GrossAmount;
  }
}
