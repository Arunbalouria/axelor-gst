package com.axelor.gst.module;

import com.axelor.app.AxelorModule;
import com.axelor.gst.invoice.service.InvoiceService;
import com.axelor.gst.invoice.service.InvoiceServiceimp;
import com.axelor.gst.invoiceline.service.InvoiceLineService;
import com.axelor.gst.invoiceline.service.InvoiceLineServiceimp;
import com.axelor.gst.product.services.ProductService;
import com.axelor.gst.product.services.ProductServiceImp;

public class gstModule extends AxelorModule {

  @Override
  protected void configure() {
    bind(InvoiceService.class).to(InvoiceServiceimp.class);
    bind(InvoiceLineService.class).to(InvoiceLineServiceimp.class);
    bind(ProductService.class).to(ProductServiceImp.class);
  }
}
