package com.axelor.gst.wab;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.repo.InvoiceRepository;
import com.axelor.db.EntityHelper;
import com.axelor.gst.product.services.ProductService;
import com.axelor.meta.schema.actions.ActionView;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.persist.Transactional;
import java.util.List;
import javax.inject.Inject;

public class ProductController {

  @Inject ProductService productServices;

  @Inject InvoiceRepository invoiceRepository;

  @Transactional
  @SuppressWarnings("unchecked")
  public void productOnSelectController(ActionRequest request, ActionResponse response) {

    response.setView(
        ActionView.define("Invoice")
            .model(Invoice.class.getName())
            .add("form", "invoice-form")
            .add("grid", "invoice-grid")
            .context("_showRecord", 8)
            .map());

    List<Integer> ids = (List<Integer>) request.getContext().get("_ids");

    Invoice invoice = new Invoice();

    if (ids == null) {
      response.setError("Select the Product first");
    } else {
      try {
        invoice = productServices.saveInvoice(ids);
        invoice = EntityHelper.getEntity(invoice);
        invoiceRepository.save(invoice);

        response.setView(
            ActionView.define("Invoice")
                .model(Invoice.class.getName())
                .add("form", "invoice-form")
                .add("grid", "invoice-grid")
                .context("_showRecord", invoice.getId())
                .map());
      } catch (Exception e) {
        response.setError(e.getMessage());
      }
    }
  }
}
