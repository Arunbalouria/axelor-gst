package com.axelor.gst.wab;

import com.axelor.apps.account.db.Invoice;
import com.axelor.db.EntityHelper;
import com.axelor.exception.service.TraceBackService;
import com.axelor.gst.invoice.service.InvoiceService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import javax.inject.Inject;

public class InvoiceController {

  @Inject InvoiceService invoiceService;

  // its for all the calculation
  public void setAllAmount(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);
    if (invoice == null) {
      response.setError("Invoice Not Formed");
    }
    try {
      invoice = EntityHelper.getEntity(invoice);
      response.setValue("netAmount", invoiceService.setNetAmount(invoice));
      response.setValue("netIgst", invoiceService.setNetIGST(invoice));
      response.setValue("netCgst", invoiceService.setNetCGST(invoice));
      response.setValue("netSgst", invoiceService.setNetSGST(invoice));
      response.setValue("grossAmount", invoiceService.setGrossAmount(invoice));
    } catch (Exception e) {
      TraceBackService.trace(response, e);
    }
  }
}
