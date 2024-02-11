package com.axelor.gst.wab;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import com.axelor.db.EntityHelper;
import com.axelor.gst.invoiceline.service.InvoiceLineService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import java.math.BigDecimal;
import javax.inject.Inject;

public class InvoiceLineController {

  @Inject InvoiceLineService invoiceLineService;

  public void controller(ActionRequest request, ActionResponse response) {
    InvoiceLine invoiceLine = request.getContext().asType(InvoiceLine.class);
    Invoice invoice = request.getContext().getParent().asType(Invoice.class);
    invoice = EntityHelper.getEntity(invoice);
    invoiceLine = EntityHelper.getEntity(invoiceLine);
    response.setValue("invoice", invoice);
    BigDecimal netAmount = invoiceLine.getNetAmount();
    try {
      BigDecimal setIGST = invoiceLineService.setIgst(invoiceLine, invoice);
      BigDecimal setCGST = invoiceLineService.setCgst(invoiceLine, invoice);
      BigDecimal setSGST = invoiceLineService.setSgst(invoiceLine, invoice);
      BigDecimal grossAmount = BigDecimal.ZERO;
      grossAmount = grossAmount.add(netAmount).add(setCGST).add(setSGST).add(setIGST);
      response.setValue("IGST", setIGST);
      response.setValue("CGST", setCGST);
      response.setValue("SGST", setSGST);
      response.setValue("grossAmount", grossAmount);
    } catch (Exception e) {
      response.setError(e.getMessage());
    }
  }
}
