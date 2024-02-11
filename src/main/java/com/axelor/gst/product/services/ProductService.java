package com.axelor.gst.product.services;

import com.axelor.apps.account.db.Invoice;
import java.util.List;

public interface ProductService {
  public Invoice saveInvoice(List<Integer> productIds) throws Exception;
}
